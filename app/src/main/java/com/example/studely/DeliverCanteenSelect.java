package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliverCanteenSelect extends BottomNavBar {

    Spinner canteenSpinner;
    ArrayAdapter<String> canteenAdapter;
    ImageButton mNextBtn;
    FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_canteen_select);
        loadingOverlay = findViewById(R.id.loading_overlay);
        canteenSpinner = (Spinner) findViewById(R.id.canteenSpinner);
        mNextBtn = findViewById(R.id.nextBtn);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fCanteenRef = database.getReference().child("canteens");
        loadingOverlay.setVisibility(View.VISIBLE);

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

                canteenAdapter = new ArrayAdapter<String>(DeliverCanteenSelect.this,
                        android.R.layout.simple_list_item_1, canteenList);
                canteenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                canteenSpinner.setAdapter(canteenAdapter);

                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        navBar(this.getApplicationContext());

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
