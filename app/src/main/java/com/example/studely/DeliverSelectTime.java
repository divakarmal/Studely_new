package com.example.studely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class DeliverSelectTime extends AppCompatActivity {
    Button mConfirmBtn;
    TimePicker mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_select_time);
        mConfirmBtn = findViewById(R.id.confirm);
        mTimePicker = findViewById(R.id.timePicker1);
        mTimePicker.setIs24HourView(true);

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DeliverPostConfirm.class));
            }
        });
    }
}
