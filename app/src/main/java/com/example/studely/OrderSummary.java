package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.SummaryRecAdapter;
import com.example.studely.classes.Food;
import com.example.studely.classes.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderSummary extends AppCompatActivity {

    RecyclerView summaryList;
    Button mConfirmButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        summaryList = findViewById(R.id.summaryRecView);
        Bundle bundle = this.getIntent().getExtras();
        final Order order = (Order) bundle.getSerializable("orderObj");

        SummaryRecAdapter summaryRecAdapter = new SummaryRecAdapter(this, order.getList());
        summaryList.setAdapter(summaryRecAdapter);
        summaryList.setLayoutManager(new LinearLayoutManager(this));

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference orderPostingsRef = dbRef.child("OrderPostings");
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mConfirmButton = findViewById(R.id.confirmBtn);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pushID = orderPostingsRef.push().getKey();

                DatabaseReference pushRef = orderPostingsRef.child(pushID);
                pushRef.child("Canteen").setValue(order.getCanteen());
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
