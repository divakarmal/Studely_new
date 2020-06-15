package com.example.studely;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderStallSelect extends AppCompatActivity {

    Spinner stallSpinner;
    ArrayAdapter<String> stallAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_stall_select);

        stallSpinner = (Spinner) findViewById(R.id.stallSpinner);
        String canteenID = getIntent().getExtras().getString("canteenID");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fStallRef = database.getReference().child("canteens")
                                        .child(canteenID).child("StallList");

        fStallRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> stallList = new ArrayList<String>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String stallName = snapshot.child("StallName").getValue(String.class);
                    if (stallName != null) {
                        stallList.add(stallName);
                    }
                }

                stallAdapter = new ArrayAdapter<>(OrderStallSelect.this,
                        android.R.layout.simple_list_item_1, stallList);
                stallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stallSpinner.setAdapter(stallAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
