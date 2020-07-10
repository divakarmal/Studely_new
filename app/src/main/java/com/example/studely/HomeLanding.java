package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeLanding extends AppCompatActivity {
    Button mOrderButton, mDeliverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_landing);

        mOrderButton = findViewById(R.id.orderBtn);
        mDeliverButton = findViewById(R.id.deliverBtn);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_profile:
                        Intent profile = new Intent(HomeLanding.this, UserDetailsForm.class);
                        startActivity(profile);
                        break;
                    case R.id.ic_myOrderList:
                        Intent myOrders = new Intent(HomeLanding.this, MyOrders.class);
                        startActivity(myOrders);
                        break;
                    case R.id.ic_myListings:
                        Intent listings = new Intent(HomeLanding.this, MyListings.class);
                        startActivity(listings);
                        break;
                }
                return false;
            }
        });


        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseMessaging.getInstance().subscribeToTopic("user_" + userID);


        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OrderEnterAddress.class));
            }
        });
        mDeliverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeliverCanteenSelect.class));
            }
        });

    }
}
