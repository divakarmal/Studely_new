package com.example.studely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.studely.classes.Food;
import com.example.studely.classes.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderTimeSelect extends BottomNavBar {
    Button mPostBtn;
    TimePicker mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_time_select);
        navBar(this.getApplicationContext());

        mPostBtn = findViewById(R.id.postOrder);
        mTimePicker = findViewById(R.id.timePicker1);

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference orderPostingsRef = dbRef.child("OrderPostings");
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Bundle bundle = this.getIntent().getExtras();
        final Order order = (Order) bundle.getSerializable("orderObj");
        final String canteenID = order.getCanteen();

        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), OrderPostConfirm.class);

                startActivity(newIntent);
                int clockTime = mTimePicker.getHour() * 100 + mTimePicker.getMinute();
                final String deliveryTime = Integer.toString(clockTime);
                String pushID = orderPostingsRef.child(canteenID).push().getKey();
                DatabaseReference pushRef = orderPostingsRef.child(canteenID).child(pushID);
                pushRef.child("DeliveryTime").setValue(deliveryTime);
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
            }
        });
    }
}
