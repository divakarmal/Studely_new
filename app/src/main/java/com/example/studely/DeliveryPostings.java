package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.MyListingsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryPostings extends BottomNavBar {

    FrameLayout loadingOverlay;
    RecyclerView deliveryListingsRecView;
    TextView orderListingsText;

    final List<String> deliveryLocList = new ArrayList<>();
    final List<String> deliveryTimeList = new ArrayList<>();
    final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_postings);
        navBar(this.getApplicationContext());

        loadingOverlay = findViewById(R.id.loading_overlay);
        deliveryListingsRecView = findViewById(R.id.myDeliveryListings);
        orderListingsText = findViewById(R.id.orderListings);
        loadingOverlay.bringToFront();

        loadingOverlay.setVisibility(View.VISIBLE);
        fetchFromDB();
        orderListingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), MyListings.class);
                startActivity(newIntent);
            }
        });
    }

    private void fetchFromDB() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = dbRef.child("users").child(currentUser);

        final List<String> deliveryPostingsList = new ArrayList<>();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readSnapshot(deliveryPostingsList, snapshot.child("DeliveryPostings"));

                readDeliveryPostings(deliveryPostingsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readSnapshot(List<String> list, DataSnapshot snapshot) {
        for (DataSnapshot snap : snapshot.getChildren()) {
            list.add(snap.getKey());
        }
    }

    private void readDeliveryPostings(final List<String> deliveryPostList) {
        DatabaseReference deliveryPostingsRef = dbRef.child("DeliveryPostings");

        deliveryPostingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (String pushID : deliveryPostList) {
                    deliveryLocList.add((String) snapshot.child(pushID).child("Canteen").getValue());
                    deliveryTimeList.add((String) snapshot.child(pushID).child("DeliveryTime").getValue());
                }

                MyListingsAdapter deliveryListingsAdapter = new MyListingsAdapter(DeliveryPostings.this, deliveryTimeList, deliveryLocList);
                deliveryListingsRecView.setAdapter(deliveryListingsAdapter);
                deliveryListingsRecView.setLayoutManager(new LinearLayoutManager(DeliveryPostings.this));

                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
