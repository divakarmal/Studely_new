package com.example.studely;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavBar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav_bar);
        BottomNavigationView navigation = findViewById(R.id.navigation);
    }

    public void navBar(final Context context) {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        Intent home = new Intent(context, HomeLanding.class);
                        startActivity(home);
                        break;
                    case R.id.ic_myOrderList:
                        Intent orderList = new Intent(context, MyOrders.class);
                        startActivity(orderList);
                        break;
                    case R.id.ic_myListings:
                        Intent listings = new Intent(context, MyListings.class);
                        startActivity(listings);
                        break;
                }
                return false;
            }
        });
    }
}
