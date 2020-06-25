package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeLanding extends BottomNavBar {
    Button mOrderButton, mDeliverButton, myAccuntBtn, mlogoutBtn, listings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_landing);
        mOrderButton = findViewById(R.id.orderBtn);
        mDeliverButton = findViewById(R.id.deliverBtn);
        myAccuntBtn = findViewById(R.id.myProfileBtn);
        mlogoutBtn = findViewById(R.id.logoutBtn);
        listings = findViewById(R.id.listings);


        navBar(this.getApplicationContext());

        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),OrderEnterAddress.class));
            }
        });
        mDeliverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DeliverCanteenSelect.class));
            }
        });
        myAccuntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UserDetailsForm.class));
            }
        });
        mlogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });
        listings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyListings.class));
            }
        });

    }
}
