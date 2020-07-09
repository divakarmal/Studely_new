package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeliverSelectTime extends BottomNavBar {
    Button mConfirmBtn;
    TimePicker mTimePicker;
    EditText mNoOfOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_select_time);
        navBar(this.getApplicationContext());

        mConfirmBtn = findViewById(R.id.confirmBtn);
        mTimePicker = findViewById(R.id.timePicker1);
        mNoOfOrders = findViewById(R.id.numOfOrders);
        mTimePicker.setIs24HourView(true);

        final String canteenID = getIntent().getExtras().getString("canteenID");
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference deliverPostingRef = dbRef.child("DeliveryPostings");
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference userRef = dbRef.child("users").child(currentUser);
        final DatabaseReference canteenRef = dbRef.child("canteens").child(canteenID).child("DeliveryPostings");


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                final String name = (String) snapshot.child("name").getValue();

                mConfirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent newIntent = new Intent(getApplicationContext(), DeliverPostingConfirmed.class);
                        String pushID = deliverPostingRef.push().getKey();
                        int clockTime = mTimePicker.getHour() * 100 + mTimePicker.getMinute();
                        String numOfOrders;
                        numOfOrders = mNoOfOrders.getText().toString();
                        String deliveryTime = Integer.toString(clockTime);
                        userRef.child("DeliveryPostings").child(pushID).setValue(canteenID);
                        deliverPostingRef.child(pushID).child("Deliverer").setValue(currentUser);
                        deliverPostingRef.child(pushID).child("DeliveryTime").setValue(deliveryTime);
                        deliverPostingRef.child(pushID).child("NoOfOrders").setValue(numOfOrders);
                        deliverPostingRef.child(pushID).child("Name").setValue(name);
                        canteenRef.child(pushID).setValue("POSTING");


                        startActivity(newIntent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
