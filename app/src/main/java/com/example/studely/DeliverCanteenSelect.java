package com.example.studely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliverCanteenSelect extends AppCompatActivity {

    Spinner canteenSpinner;
    ArrayAdapter<String> canteenAdapter;
    Button mNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_canteen_select);
        canteenSpinner = (Spinner) findViewById(R.id.canteenSpinner);
        mNextBtn = findViewById(R.id.nextBtn);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fCanteenRef = database.getReference().child("canteens");

        fCanteenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> canteenList = new ArrayList<String>();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String canteenName = snapshot.getKey();
                    if (canteenName!=null){
                        canteenList.add(canteenName);
                    }
                }

                canteenAdapter = new ArrayAdapter<String>(DeliverCanteenSelect.this,
                        android.R.layout.simple_list_item_1, canteenList);
                canteenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                canteenSpinner.setAdapter(canteenAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        Intent home = new Intent(DeliverCanteenSelect.this,HomeLanding.class);
                        startActivity(home);
                        break;
                    case R.id.ic_myOrderList:
                        Intent orderList = new Intent(DeliverCanteenSelect.this,MyOrderList.class);
                        startActivity(orderList);
                        break;
                }
                return false;
            }
        });
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), DeliverOrderSelect.class);
                String choice = canteenSpinner.getSelectedItem().toString();
                newIntent.putExtra("canteenID", choice);
                startActivity(newIntent);
            }
        });
    }
}
