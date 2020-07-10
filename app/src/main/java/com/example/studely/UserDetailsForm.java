package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsForm extends BottomNavBar {

    EditText phoneNum, priAdd, pinCode, name;
    Button mSubmitBtn, mLogOutBtn;
    FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_form);
        name = findViewById(R.id.nameField);
        phoneNum = findViewById(R.id.phoneNumField);
        priAdd = findViewById(R.id.priAddField);
        pinCode = findViewById(R.id.pinCodeField);
        mSubmitBtn = findViewById(R.id.submitBtn);
        loadingOverlay = findViewById(R.id.loading_overlay);
        mLogOutBtn = findViewById(R.id.logoutBtn);
        navBar(this.getApplicationContext());

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference userRef = dbRef.child("users");
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadingOverlay.setVisibility(View.VISIBLE);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.child(currentUser).child("phone_number").getValue(String.class));
                name.setText(dataSnapshot.child(currentUser).child("name").getValue(String.class));
                phoneNum.setText(dataSnapshot.child(currentUser).child("phone_number").getValue(String.class));
                priAdd.setText(dataSnapshot.child(currentUser).child("primary_address").getValue(String.class));
                pinCode.setText(dataSnapshot.child(currentUser).child("pin_code").getValue(String.class));
                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNum.getText().toString();
                String primaryAddress = priAdd.getText().toString();
                String pinCode = UserDetailsForm.this.pinCode.getText().toString();
                String mName = name.getText().toString();
                dbRef.child("users").child(currentUser).child("name").setValue(mName);
                dbRef.child("users").child(currentUser).child("phone_number").setValue(phoneNumber);
                dbRef.child("users").child(currentUser).child("primary_address").setValue(primaryAddress);
                dbRef.child("users").child(currentUser).child("pin_code").setValue(pinCode);
                startActivity(new Intent(getApplicationContext(), HomeLanding.class));
            }
        });

        mLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }
}
