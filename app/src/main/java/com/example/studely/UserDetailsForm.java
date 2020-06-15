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

public class UserDetailsForm extends AppCompatActivity {

    EditText phoneNum, priAdd, pinCode;
    Button mSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_form);
        phoneNum = findViewById(R.id.phoneNumField);
        priAdd = findViewById(R.id.priAddField);
        pinCode = findViewById(R.id.pinCodeField);
        mSubmitBtn = findViewById(R.id.submitBtn);

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNum.getText().toString();
                String primaryAddress = priAdd.getText().toString();
                String pinCode = UserDetailsForm.this.pinCode.getText().toString();
                dbRef.child("users").child(currentuser).child("phone_number").setValue(phoneNumber);
                dbRef.child("users").child(currentuser).child("primary_address").setValue(primaryAddress);
                dbRef.child("users").child(currentuser).child("pin_code").setValue(pinCode);
                startActivity(new Intent(getApplicationContext(),HomeLanding.class));
            }
        });

    }
}
