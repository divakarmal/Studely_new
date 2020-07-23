package com.example.studely;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.List;

public class UserDetailsForm extends BottomNavBar {

    EditText phoneNum, priAdd, name;
    Button mSubmitBtn, mLogOutBtn;
    FrameLayout loadingOverlay;
    Geocoder geocoder;


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
        geocoder = new Geocoder(UserDetailsForm.this);

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
                loadingOverlay.setVisibility(View.VISIBLE);

                final String mName = name.getText().toString().trim();
                final String mPhoneNum = phoneNum.getText().toString().trim();
                final String mPriAdd = priAdd.getText().toString();

                if (TextUtils.isEmpty(mName)) {
                    name.setError("Name is required");
                    loadingOverlay.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(mPhoneNum)) {
                    phoneNum.setError("Phone number is Required.");
                    loadingOverlay.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(mPriAdd)) {
                    priAdd.setError("Primary address is Required.");
                    loadingOverlay.setVisibility(View.GONE);
                    return;
                } else {
                    List<Address> addresses;
                    try {
                        addresses = geocoder.getFromLocationName(mPriAdd, 1);
                        if (addresses.size() == 0) {
                            priAdd.setError("Invalid address entered");
                            loadingOverlay.setVisibility(View.GONE);
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (mPhoneNum.length() != 8) {
                    phoneNum.setError("Phone number must be 8 digits");
                    loadingOverlay.setVisibility(View.GONE);
                    return;
                }

                String phoneNumber = phoneNum.getText().toString();
                String primaryAddress = priAdd.getText().toString();
                String myName = name.getText().toString();
                dbRef.child("users").child(currentUser).child("name").setValue(myName);
                dbRef.child("users").child(currentUser).child("phone_number").setValue(phoneNumber);
                dbRef.child("users").child(currentUser).child("primary_address").setValue(primaryAddress);
                loadingOverlay.setVisibility(View.GONE);
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
