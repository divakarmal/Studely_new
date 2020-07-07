package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderCanteenSelect extends BottomNavBar {

    Spinner canteenSpinner;
    ArrayAdapter<String> canteenAdapter;
    ImageButton mNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_canteen_select);

        mNextBtn = findViewById(R.id.nextBtn);
        canteenSpinner = (Spinner) findViewById(R.id.canteenSpinner);
        navBar(this.getApplicationContext());

        final String destination = getIntent().getExtras().getString("orderDestination");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference fCanteenRef = database.getReference().child("canteens");

        fCanteenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> canteenList = new ArrayList<String>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String canteenName = snapshot.getKey();
                    if (canteenName != null) {
                        canteenList.add(canteenName);
                    }
                }

                canteenAdapter = new ArrayAdapter<String>(OrderCanteenSelect.this,
                        android.R.layout.simple_list_item_1, canteenList);
                canteenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                canteenSpinner.setAdapter(canteenAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), OrderStallSelect.class);
                String choice = canteenSpinner.getSelectedItem().toString();
                newIntent.putExtra("canteenID", choice);
                newIntent.putExtra("orderDestination", destination);
                startActivity(newIntent);
            }
        });
    }
}
