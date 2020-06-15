package com.example.studely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class form extends AppCompatActivity {

    EditText field1, field2, field3;
    Button mSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        field1 = findViewById(R.id.phoneNumber);
        field2 = findViewById(R.id.PrimaryAddress);
        field3 = findViewById(R.id.PinCode);
        mSubmitBtn = findViewById(R.id.SubmitBtn);

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = field1.getText().toString();
                String primaryAddress = field2.getText().toString();
                String pinCode = field3.getText().toString();
                dbRef.child("users").child(currentuser).child("phone_number").setValue(phoneNumber);
                dbRef.child("users").child(currentuser).child("primary_address").setValue(primaryAddress);
                dbRef.child("users").child(currentuser).child("pin_code").setValue(pinCode);
                startActivity(new Intent(getApplicationContext(),HomeLanding.class));
            }
        });

    }
}
