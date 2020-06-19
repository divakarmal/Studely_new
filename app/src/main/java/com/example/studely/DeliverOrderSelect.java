package com.example.studely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DeliverOrderSelect extends AppCompatActivity {
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

    }

}
