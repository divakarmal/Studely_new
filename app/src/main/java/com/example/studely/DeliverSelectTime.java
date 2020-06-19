package com.example.studely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeliverSelectTime extends AppCompatActivity {
    Button mConfirmBtn;
    TimePicker mTimePicker;
    EditText mNoOfOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_select_time);
        mConfirmBtn = findViewById(R.id.confirm);
        mTimePicker = findViewById(R.id.timePicker1);
        mNoOfOrders = findViewById(R.id.numOfOrders);
        mTimePicker.setIs24HourView(true);
        final String canteenID = getIntent().getExtras().getString("canteenID");
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), DeliverPostConfirm.class);
                String mPushId = dbRef.push().getKey();
                int clockTime = mTimePicker.getHour() * 100 + mTimePicker.getMinute();
                String numOfOrders = mNoOfOrders.getText().toString();
                String deliveryTime = Integer.toString(clockTime);
                dbRef.child("DeliveryPostings").child(canteenID).child(mPushId).child("Deliverer").setValue(currentuser);
                dbRef.child("DeliveryPostings").child(canteenID).child(mPushId).child("DeliveryTime").setValue(deliveryTime);
                dbRef.child("DeliveryPostings").child(canteenID).child(mPushId).child("NoOfOrders").setValue(numOfOrders);

                startActivity(newIntent);
            }
        });
    }
}
