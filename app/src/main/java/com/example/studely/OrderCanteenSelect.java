package com.example.studely;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderCanteenSelect extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("canteens");

        DataSnapshot fDatabaseRoot;
        mDatabase.child("canteens").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> canteens = new ArrayList<String>();

                for (DataSnapshot canteenSnapshot: dataSnapshot.getChildren()) {
                    String canteenName = canteenSnapshot.child("Value").getValue(String.class);
                    canteens.add(canteenName);
                }

                Spinner areaSpinner = (Spinner) findViewById(R.id.canteenSpinner);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(OrderCanteenSelect.this,
                        android.R.layout.simple_spinner_item, canteens);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


/*
        setContentView(R.layout.activity_order_canteen_select);
        Spinner canteenSelect = findViewById(R.id.canteenSpinner);
        ArrayAdapter<String> canteenAdaptor = new ArrayAdapter<>(OrderCanteenSelect.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.CanteenList));
        canteenAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        canteenSelect.setAdapter(canteenAdaptor);

 */
    }
}
