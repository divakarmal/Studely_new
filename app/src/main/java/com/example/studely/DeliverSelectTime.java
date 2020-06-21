package com.example.studely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeliverSelectTime extends BottomNavBar {
    Button mConfirmBtn;
    TimePicker mTimePicker;
    EditText mNoOfOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_select_time);
        mConfirmBtn = findViewById(R.id.confirmBtn);
        mTimePicker = findViewById(R.id.timePicker1);
        mNoOfOrders = findViewById(R.id.numOfOrders);
        mTimePicker.setIs24HourView(true);
        final String canteenID = getIntent().getExtras().getString("canteenID");
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        navBar(this.getApplicationContext());

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), DeliverPostConfirm.class);
                String pushID = dbRef.push().getKey();
                int clockTime = mTimePicker.getHour() * 100 + mTimePicker.getMinute();
                String numOfOrders;
                numOfOrders = mNoOfOrders.getText().toString();
                String deliveryTime = Integer.toString(clockTime);
                dbRef.child("DeliveryPostings").child(canteenID).child(pushID).child("Deliverer").setValue(currentUser);
                dbRef.child("DeliveryPostings").child(canteenID).child(pushID).child("DeliveryTime").setValue(deliveryTime);
                dbRef.child("DeliveryPostings").child(canteenID).child(pushID).child("NoOfOrders").setValue(numOfOrders);

                startActivity(newIntent);
            }
        });
    }
}
