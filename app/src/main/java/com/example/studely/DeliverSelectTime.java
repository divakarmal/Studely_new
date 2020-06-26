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
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference userRef = dbRef.child("users").child(currentUser);


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String name = (String) snapshot.child("name").getValue();

                mConfirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent newIntent = new Intent(getApplicationContext(), DeliverPostingConfirmed.class);
                        String pushID = dbRef.push().getKey();
                        int clockTime = mTimePicker.getHour() * 100 + mTimePicker.getMinute();
                        String numOfOrders;
                        numOfOrders = mNoOfOrders.getText().toString();
                        String deliveryTime = Integer.toString(clockTime);
                        dbRef.child("users").child(currentUser).child("DeliveryPostings").child(pushID).setValue(canteenID);
                        dbRef.child("DeliveryPostings").child(canteenID).child(pushID).child("Deliverer").setValue(currentUser);
                        dbRef.child("DeliveryPostings").child(canteenID).child(pushID).child("DeliveryTime").setValue(deliveryTime);
                        dbRef.child("DeliveryPostings").child(canteenID).child(pushID).child("NoOfOrders").setValue(numOfOrders);
                        dbRef.child("DeliveryPostings").child(canteenID).child(pushID).child("Name").setValue(name);

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
