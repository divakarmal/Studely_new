package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.studely.misc.DatabaseErrorHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsForm extends BottomNavBar {

    EditText phoneNum, priAdd, name;
    Button mSubmitBtn, mLogOutBtn;
    FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_form);
        navBar(this.getApplicationContext());

        name = findViewById(R.id.nameField);
        phoneNum = findViewById(R.id.phoneNumField);
        priAdd = findViewById(R.id.priAddField);
        mSubmitBtn = findViewById(R.id.submitBtn);
        mLogOutBtn = findViewById(R.id.logoutBtn);
        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();

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
                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String error = DatabaseErrorHandler.handleError(databaseError);
                Toast.makeText(UserDetailsForm.this, error, Toast.LENGTH_LONG).show();
            }
        });


        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String mName = name.getText().toString().trim();
                final String mPhoneNum = phoneNum.getText().toString().trim();
                final String mPriAdd = priAdd.getText().toString();

                if (TextUtils.isEmpty(mName)) {
                    name.setError("Name is required");
                    return;
                }

                if (TextUtils.isEmpty(mPhoneNum)) {
                    phoneNum.setError("Phone number is Required.");
                    return;
                }

                if (TextUtils.isEmpty(mPriAdd)) {
                    priAdd.setError("Primary address is Required.");
                    return;
                }

                if (mPhoneNum.length() != 8) {
                    phoneNum.setError("Phone number must be 8 digits");
                    return;
                }


                String phoneNumber = phoneNum.getText().toString();
                String primaryAddress = priAdd.getText().toString();
                String myName = name.getText().toString();
                dbRef.child("users").child(currentUser).child("name").setValue(myName);
                dbRef.child("users").child(currentUser).child("phone_number").setValue(phoneNumber);
                dbRef.child("users").child(currentUser).child("primary_address").setValue(primaryAddress);
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
