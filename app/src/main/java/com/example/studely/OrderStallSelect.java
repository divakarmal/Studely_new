package com.example.studely;

import android.os.Bundle;

import android.view.View;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.StallRecAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderStallSelect extends BottomNavBar {

    RecyclerView stallRecView;
    List<String> stallList;
    FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_stall_select);
        navBar(this.getApplicationContext());

        stallRecView = findViewById(R.id.stallRecView);
        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();

        final String canteenID = getIntent().getExtras().getString("canteenID");
        final String destination = getIntent().getExtras().getString("orderDestination");
        System.out.println(destination);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference stallRef = database.getReference().child("canteens")
                .child(canteenID).child("StallList");

        loadingOverlay.setVisibility(View.VISIBLE);
        stallRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stallList = new ArrayList<String>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String stallName = snapshot.getKey();
                    if (stallName != null) {
                        stallList.add(stallName);
                    }
                }

                StallRecAdapter stallRecAdapter = new StallRecAdapter(OrderStallSelect.this, stallList, canteenID, destination);
                stallRecView.setAdapter(stallRecAdapter);
                stallRecView.setLayoutManager(new GridLayoutManager(OrderStallSelect.this, 2));
                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
