package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class MyListings extends BottomNavBar {

    FrameLayout loadingOverlay;
    RecyclerView orderListingsRecView;
    TextView deliveryListingsText;

    final List<String> orderLocList = new ArrayList<>();
    final List<String> orderTimeList = new ArrayList<>();
    final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);
        navBar(this.getApplicationContext());

        loadingOverlay = findViewById(R.id.loading_overlay);
        orderListingsRecView = findViewById(R.id.myOrderListings);
        loadingOverlay.bringToFront();
        deliveryListingsText = findViewById(R.id.deliveryListings);
        loadingOverlay.setVisibility(View.VISIBLE);
        fetchFromDB();

        deliveryListingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), DeliveryPostings.class);
                startActivity(newIntent);
            }
        });

    }

    private void fetchFromDB() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = dbRef.child("users").child(currentUser);

        final List<String> orderPostingsList = new ArrayList<>();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readSnapshot(orderPostingsList, snapshot.child("OrderPostings"));

                readOrderPostings(orderPostingsList);
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

    private void readOrderPostings(final List<String> orderPostList) {
        DatabaseReference orderPostingsRef = dbRef.child("OrderPostings");

        orderPostingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (String pushID : orderPostList) {
                    orderLocList.add((String) snapshot.child(pushID).child("Destination").getValue());
                    orderTimeList.add((String) snapshot.child(pushID).child("DeliveryTime").getValue());
                }

                MyListingsAdapter orderListingsAdapter = new MyListingsAdapter(MyListings.this, orderTimeList, orderLocList);
                orderListingsRecView.setAdapter(orderListingsAdapter);
                orderListingsRecView.setLayoutManager(new LinearLayoutManager(MyListings.this));
                loadingOverlay.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
