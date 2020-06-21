package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.SummaryRecAdapter;
import com.example.studely.classes.Food;
import com.example.studely.classes.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderSummary extends BottomNavBar {

    RecyclerView summaryList;
    Button mConfirmButton;
    TextView orderTotal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        orderTotal = findViewById(R.id.totalOrderCost);
        summaryList = findViewById(R.id.summaryRecView);
        Bundle bundle = this.getIntent().getExtras();
        final Order order = (Order) bundle.getSerializable("orderObj");
        String cost = Integer.toString(order.calcOrderCost());
        orderTotal.setText(cost);

        SummaryRecAdapter summaryRecAdapter = new SummaryRecAdapter(this, order.getList());
        summaryList.setAdapter(summaryRecAdapter);
        summaryList.setLayoutManager(new LinearLayoutManager(this));

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference orderPostingsRef = dbRef.child("OrderPostings");
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String canteenID = order.getCanteen();
        navBar(this.getApplicationContext());

        mConfirmButton = findViewById(R.id.confirmBtn);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pushID = orderPostingsRef.child(canteenID).push().getKey();

                DatabaseReference pushRef = orderPostingsRef.child(canteenID).child(pushID);
                pushRef.child("DeliveryTime").setValue("0000"); // <-------------------------- Lmao we never did this
                pushRef.child("Destination").setValue(order.getDestination());
                pushRef.child("OrderCost").setValue(order.calcOrderCost());
                pushRef.child("Receiver").setValue(currentUser);

                DatabaseReference itemListRef = pushRef.child("ItemList");
                for (Food food : order.getList()) {
                    itemListRef.child(food.name).child("Price")
                                .setValue(String.valueOf(food.price));
                    itemListRef.child(food.name).child("Quantity")
                                .setValue(String.valueOf(food.quantity));
                }

                Intent newIntent = new Intent(getApplicationContext(), OrderPostConfirm.class);
                startActivity(newIntent);
            }
        });
    }
}
