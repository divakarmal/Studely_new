package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.studely.misc.DatabaseErrorHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class DeliverTimeSelect extends BottomNavBar {

    Button mConfirmBtn;
    TimePicker mTimePicker;
    FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_time_select);
        navBar(this.getApplicationContext());

        mConfirmBtn = findViewById(R.id.acceptBtn);
        mTimePicker = findViewById(R.id.timePicker1);
        mTimePicker.setIs24HourView(true);
        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();

        final String canteenID = getIntent().getExtras().getString("canteenID");
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference deliverPostingRef = dbRef.child("DeliveryPostings");
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference userRef = dbRef.child("users").child(currentUser);
        final DatabaseReference canteenRef = dbRef.child("canteens").child(canteenID).child("DeliveryPostings");

        loadingOverlay.setVisibility(View.VISIBLE);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                final String name = (String) snapshot.child("name").getValue();
                loadingOverlay.setVisibility(View.GONE);

                mConfirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pushID = deliverPostingRef.push().getKey();
                        int clockTime = mTimePicker.getHour() * 100 + mTimePicker.getMinute();
                        String deliveryTime;
                        if (clockTime < 100) {
                            deliveryTime = "00" + clockTime;
                        } else if (clockTime < 1000) {
                            deliveryTime = "0" + clockTime;
                        } else {
                            deliveryTime = Integer.toString(clockTime);
                        }

                        Calendar rightNow = Calendar.getInstance();
                        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                        int min = rightNow.get(Calendar.MINUTE);
                        int currentTime = hour * 100 + min;

                        if (clockTime <= (currentTime + 1200) % 2400 || (clockTime <= (currentTime + 1200) && clockTime >= currentTime)) {
                            userRef.child("DeliveryPostings").child(pushID).setValue(canteenID);
                            deliverPostingRef.child(pushID).child("Deliverer").setValue(currentUser);
                            deliverPostingRef.child(pushID).child("DeliveryTime").setValue(deliveryTime);
                            deliverPostingRef.child(pushID).child("Name").setValue(name);
                            deliverPostingRef.child(pushID).child("Canteen").setValue(canteenID);
                            canteenRef.child(pushID).setValue("POSTING");
                            Intent newIntent = new Intent(getApplicationContext(), DeliverPostingConfirmed.class);
                            startActivity(newIntent);
                        } else {
                            Toast.makeText(DeliverTimeSelect.this, "Can't deliver that far ahead", Toast.LENGTH_SHORT).show();
                            Intent newIntent = new Intent(getApplicationContext(), OrderTimeSelect.class);
                            newIntent.putExtra("canteenID", canteenID);
                            startActivity(newIntent);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String error = DatabaseErrorHandler.handleError(databaseError);
                Toast.makeText(DeliverTimeSelect.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
