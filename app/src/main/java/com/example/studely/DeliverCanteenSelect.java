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

public class DeliverCanteenSelect extends BottomNavBar {

    RecyclerView canteenRecView;
    FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_canteen_select);
        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();
        canteenRecView = findViewById(R.id.canteenRecView);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fCanteenRef = database.getReference().child("canteens");
        loadingOverlay.setVisibility(View.VISIBLE);

        fCanteenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> canteenList = new ArrayList<String>();
                final List<Integer> noOfPostings = new ArrayList<Integer>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String canteenName = snapshot.getKey();
                    if (canteenName != null) {
                        int postings = (int) snapshot.child("OrderPostings").getChildrenCount();
                        noOfPostings.add(postings);
                        canteenList.add(canteenName);
                    }
                }

                CanteenAdapter canteenAdapter = new CanteenAdapter(DeliverCanteenSelect.this, canteenList, noOfPostings);
                canteenRecView.setAdapter(canteenAdapter);
                canteenRecView.setLayoutManager(new LinearLayoutManager(DeliverCanteenSelect.this));
                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String error = DatabaseErrorHandler.handleError(databaseError);
                Toast.makeText(DeliverCanteenSelect.this, error, Toast.LENGTH_LONG).show();
            }
        });


        navBar(this.getApplicationContext());
    }
}
