package com.example.studely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class form extends AppCompatActivity {

    EditText number, address, pinCode;
    Button mSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        number = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.PrimaryAddress);
        pinCode = findViewById(R.id.PinCode);
        mSubmitBtn = findViewById(R.id.SubmitBtn);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeLanding.class));
            }
        });
    }
}
