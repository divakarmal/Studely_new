package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.studely.misc.Food;
import com.example.studely.misc.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

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
        mTimePicker.setIs24HourView(true);

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference orderPostingsRef = dbRef.child("OrderPostings");
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Bundle bundle = this.getIntent().getExtras();
        final Order order = (Order) bundle.getSerializable("orderObj");
        final String canteenID = order.getCanteen();

        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clockTime = mTimePicker.getHour() * 100 + mTimePicker.getMinute();
                String deliveryTime;
                if (clockTime < 100) {
                    deliveryTime = "00" + clockTime;
                } else if (clockTime < 1000) {
                    deliveryTime = "0" + clockTime;
                } else {
                    deliveryTime = Integer.toString(clockTime);
                }

                System.out.println(clockTime);


                Calendar rightNow = Calendar.getInstance();
                int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                int min = rightNow.get(Calendar.MINUTE);
                int currentTime = hour * 100 + min;
                System.out.println(currentTime);

                if (clockTime <= (currentTime + 1200) % 2400 || (clockTime <= (currentTime + 1200) && clockTime >= currentTime)) {

                    String pushID = orderPostingsRef.push().getKey();
                    dbRef.child("users").child(currentUser).child("OrderPostings").child(pushID).setValue(canteenID);
                    dbRef.child("canteens").child(canteenID).child("OrderPostings").child(pushID).setValue("POSTING");

                    DatabaseReference pushRef = orderPostingsRef.child(pushID);
                    pushRef.child("DeliveryTime").setValue(deliveryTime);
                    pushRef.child("Destination").setValue(order.getDestination());
                    pushRef.child("OrderCost").setValue(Integer.toString(order.calcOrderCost()));
                    pushRef.child("Receiver").setValue(currentUser);
                    pushRef.child("Canteen").setValue(canteenID);
                    pushRef.child("TotalItems").setValue(Integer.toString(order.getItems()));


                    DatabaseReference itemListRef = pushRef.child("ItemList");
                    for (Food food : order.getList()) {
                        itemListRef.child(food.name).child("Price")
                                .setValue(String.valueOf(food.price));
                        itemListRef.child(food.name).child("Quantity")
                                .setValue(String.valueOf(food.quantity));
                    }
                    Intent newIntent = new Intent(getApplicationContext(), OrderPostingConfirmed.class);
                    startActivity(newIntent);
                } else {
                    Toast.makeText(OrderTimeSelect.this, "Can't order for that far ahead", Toast.LENGTH_SHORT).show();
                    Intent newIntent = new Intent(getApplicationContext(), OrderTimeSelect.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderObj", order);
                    newIntent.putExtras(bundle);
                    startActivity(newIntent);
                }
            }
        });
    }
}
