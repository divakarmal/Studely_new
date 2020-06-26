package com.example.studely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class OrderEnterAddress extends BottomNavBar {
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

        navBar(this.getApplicationContext());
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = database.getReference().child("users").child(currentUser);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String priAdd = dataSnapshot.child("primary_address").getValue(String.class);
                mNextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent newIntent = new Intent(getApplicationContext(), OrderCanteenSelect.class);
                        if(priAddBool.isChecked()){
                            newIntent.putExtra("orderDestination", priAdd);
                        } else {
                            String add = address.getText().toString();
                            newIntent.putExtra("orderDestination", add);
                        }
                        startActivity(newIntent);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });




    }
}
