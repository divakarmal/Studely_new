package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrderPostConfirm extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_post_confirm);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        Intent home = new Intent(OrderPostConfirm.this,HomeLanding.class);
                        startActivity(home);
                        break;
                    case R.id.ic_myOrderList:
                        Intent orderList = new Intent(OrderPostConfirm.this,MyOrderList.class);
                        startActivity(orderList);
                        break;
                }
                return false;
            }
        });
    }
}
