package com.example.studely;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.CanteenAdapter;
import com.example.studely.misc.DatabaseErrorHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderCanteenSelect extends BottomNavBar {

    RecyclerView canteenRecView;
    FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_canteen_select);
        navBar(this.getApplicationContext());

        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();
        canteenRecView = findViewById(R.id.canteenRecView);

        final String destination = getIntent().getExtras().getString("orderDestination");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference fCanteenRef = database.getReference().child("canteens");

        loadingOverlay.setVisibility(View.VISIBLE);
        fCanteenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> canteenList = new ArrayList<String>();
                final List<Integer> noOfPostings = new ArrayList<Integer>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String canteenID = snapshot.getKey();
                    if (canteenID != null) {
                        int postings = (int) snapshot.child("DeliveryPostings").getChildrenCount();
                        noOfPostings.add(postings);
                        canteenList.add(canteenID);
                    }
                }

                CanteenAdapter canteenAdapter = new CanteenAdapter(OrderCanteenSelect.this, canteenList, destination, noOfPostings);
                canteenRecView.setAdapter(canteenAdapter);
                canteenRecView.setLayoutManager(new LinearLayoutManager(OrderCanteenSelect.this));
                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String error = DatabaseErrorHandler.handleError(databaseError);
                Toast.makeText(OrderCanteenSelect.this, error, Toast.LENGTH_LONG).show();
            }
        });

    }
}
