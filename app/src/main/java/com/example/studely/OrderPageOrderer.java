package com.example.studely;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.SummaryRecAdapter;
import com.example.studely.misc.DatabaseErrorHandler;
import com.example.studely.misc.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderPageOrderer extends BottomNavBar {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    RecyclerView summaryList;
    TextView mOrderID, mTimeStamp, mDeliveryTime, mOrderTotal, mStatus;
    Button mReceivedBtn;
    Location currentLoc, delLocation;
    String orderID;
    String delivererID;
    String ordererID;

    FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page_orderer);
        navBar(this.getApplicationContext());

        orderID = getIntent().getExtras().getString("orderID");
        summaryList = findViewById(R.id.sumRecView);
        mOrderID = findViewById(R.id.OrderID);
        mTimeStamp = findViewById(R.id.TimeStamp);
        mDeliveryTime = findViewById(R.id.DeliveryTime);
        mOrderTotal = findViewById(R.id.orderCost);
        mReceivedBtn = findViewById(R.id.receivedBtn);
        mStatus = findViewById(R.id.status);
        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();


        mOrderID.setText(orderID);

        loadingOverlay.setVisibility(View.VISIBLE);
        initFromDB();

        final List<Food> orderList = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
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
                SummaryRecAdapter summaryRecAdapter = new SummaryRecAdapter(OrderPageOrderer.this, orderList);
                summaryList.setAdapter(summaryRecAdapter);
                summaryList.setLayoutManager(new LinearLayoutManager(OrderPageOrderer.this));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String error = DatabaseErrorHandler.handleError(databaseError);
                Toast.makeText(OrderPageOrderer.this, error, Toast.LENGTH_LONG).show();
            }
        });

        mReceivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedOrders").child(orderID);
                final DatabaseReference ordererRef = FirebaseDatabase.getInstance().getReference().child("users").child(ordererID).child("ConfirmedOrders").child(orderID).child("isComplete");
                final DatabaseReference delivererRef = FirebaseDatabase.getInstance().getReference().child("users").child(delivererID).child("ConfirmedOrders").child(orderID).child("isComplete");

                dbRef.child("Completed").setValue(true);
                ordererRef.setValue(true);
                delivererRef.setValue(true);
                Intent newIntent = new Intent(getApplicationContext(), ReviewPage.class);
                newIntent.putExtra("orderID", orderID);
                startActivity(newIntent);
            }
        });
    }

    private void initFromDB() {
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedOrders").child(orderID);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTimeStamp.setText((String) dataSnapshot.child("Time").getValue());
                mDeliveryTime.setText((String) dataSnapshot.child("DeliveryTime").getValue());
                mOrderTotal.setText("$" + dataSnapshot.child("OrderCost").getValue());
                delivererID = (String) dataSnapshot.child("Deliverer").getValue();
                ordererID = (String) dataSnapshot.child("Receiver").getValue();
                boolean reached = dataSnapshot.child("Reached").getValue(boolean.class);
                boolean completed = dataSnapshot.child("Completed").getValue(boolean.class);


                if (completed) {
                    mStatus.setText("Order Complete");
                } else {
                    if (reached) {
                        mStatus.setText("Order is here, press the received button once order is collected");
                        mReceivedBtn.setVisibility(View.VISIBLE);
                    } else {
                        mStatus.setText("Order is not here yet");
                    }
                }

                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String error = DatabaseErrorHandler.handleError(databaseError);
                Toast.makeText(OrderPageOrderer.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

}
