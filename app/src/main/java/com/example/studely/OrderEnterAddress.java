package com.example.studely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class OrderEnterAddress extends AppCompatActivity {
    EditText address;
    CheckBox priAddBool;
    Button mNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_enter_address);

        address = findViewById(R.id.addressField);
        mNextBtn = findViewById(R.id.NextBtn);
        priAddBool = findViewById(R.id.priAddCheck);



        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),OrderCanteenSelect.class));
            }
        });

    }
}
