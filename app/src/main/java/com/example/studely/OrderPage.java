package com.example.studely;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.studely.adapters.SummaryRecAdapter;
import com.example.studely.classes.Food;
import com.example.studely.classes.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderPage extends BottomNavBar {
    RecyclerView summaryList;
    TextView mOrderID, mTimeStamp, mDeliveryTime, mOrderTotal, mOrderReached, mNothereYet, mComplete;
    Button mReachedBtn, mReceivedBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        navBar(this.getApplicationContext());

        final String orderID = getIntent().getExtras().getString("orderID");
        summaryList = findViewById(R.id.recyclerView);
        mOrderID = findViewById(R.id.OrderID);
        mTimeStamp = findViewById(R.id.TimeStamp);
        mDeliveryTime = findViewById(R.id.DeliveryTime);
        mOrderTotal = findViewById(R.id.orderCost);
        mReachedBtn = findViewById(R.id.reachedBtn);
        mReceivedBtn = findViewById(R.id.receivedBtn);
        mOrderReached = findViewById(R.id.orderReached);
        mNothereYet = findViewById(R.id.notReachedText);
        mComplete = findViewById(R.id.CompletedOrder);


        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final List<Food> orderList = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mOrderID.setText(orderID);
        final DatabaseReference dbRef = database.getReference().child("ConfirmedOrders").child(orderID);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTimeStamp.setText((String) dataSnapshot.child("Time").getValue());
                mDeliveryTime.setText((String) dataSnapshot.child("DeliveryTime").getValue());
                mOrderTotal.setText((String) dataSnapshot.child("OrderCost").getValue());
                String delivererID = (String) dataSnapshot.child("Deliverer").getValue();
                String receiverID = (String) dataSnapshot.child("Receiver").getValue();
                boolean reached = dataSnapshot.child("Reached").getValue(boolean.class);
                boolean completed = dataSnapshot.child("Completed").getValue(boolean.class);


                if(completed){
                    mComplete.setVisibility(View.VISIBLE);
                } else if(delivererID.equals(currentUser)){
                    mReachedBtn.setVisibility(View.VISIBLE);
                } else if (receiverID.equals(currentUser)){
                    if(reached) {
                        mReceivedBtn.setVisibility(View.VISIBLE);
                    } else {
                        mNothereYet.setVisibility(View.VISIBLE);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference fOrderRef = database.getReference().child("ConfirmedOrders")
                .child(orderID).child("ItemList");
        fOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String foodName = snapshot.getKey();
                    int price = Integer.parseInt((String) snapshot.child("Price").getValue());
                    int quantity = Integer.parseInt((String) snapshot.child("Quantity").getValue());
                    orderList.add(new Food(foodName, price, quantity));
                }
                SummaryRecAdapter summaryRecAdapter = new SummaryRecAdapter(OrderPage.this, orderList);
                summaryList.setAdapter(summaryRecAdapter);
                summaryList.setLayoutManager(new LinearLayoutManager(OrderPage.this));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mReachedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedOrders").child(orderID);
                dbRef.child("Reached").setValue(true);
            }
        });

        mReceivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedOrders").child(orderID);
                dbRef.child("Completed").setValue(true);
                mReceivedBtn.setVisibility(View.INVISIBLE);
            }
        });


    }
}
