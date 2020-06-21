package com.example.studely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DeliverOrderSelect extends BottomNavBar {
    Button mNewPostingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_order_select);
        mNewPostingBtn = findViewById(R.id.newPosting);
        final String canteenID = getIntent().getExtras().getString("canteenID");


        mNewPostingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), DeliverSelectTime.class);
                newIntent.putExtra("canteenID", canteenID);
                startActivity(newIntent);
            }
        });

        navBar(this.getApplicationContext());

    }

}
