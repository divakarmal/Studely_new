package com.example.studely;

import android.graphics.Typeface;
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

public class MyListings extends BottomNavBar {

    final List<String> orderLocList = new ArrayList<>();
    final List<String> orderTimeList = new ArrayList<>();
    final List<String> deliveryLocList = new ArrayList<>();
    final List<String> deliveryTimeList = new ArrayList<>();
    final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    FrameLayout loadingOverlay;
    RecyclerView orderListingsRecView, deliveryListingsRecView;
    TextView deliveryListingsText, orderListingsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);
        navBar(this.getApplicationContext());

        orderListingsRecView = findViewById(R.id.myOrderListings);
        deliveryListingsText = findViewById(R.id.deliveryListings);
        deliveryListingsRecView = findViewById(R.id.myDeliveryListings);
        orderListingsText = findViewById(R.id.orderListings);
        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();
        loadingOverlay.setVisibility(View.VISIBLE);
        fetchFromDB();

        deliveryListingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryListingsText.setTypeface(null, Typeface.BOLD);
                orderListingsText.setTypeface(null, Typeface.NORMAL);
                orderListingsRecView.setVisibility(View.GONE);
                deliveryListingsRecView.setVisibility(View.VISIBLE);
            }
        });

        orderListingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryListingsText.setTypeface(null, Typeface.NORMAL);
                orderListingsText.setTypeface(null, Typeface.BOLD);
                orderListingsRecView.setVisibility(View.VISIBLE);
                deliveryListingsRecView.setVisibility(View.GONE);
            }
        });

    }

    private void fetchFromDB() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = dbRef.child("users").child(currentUser);

        final List<String> orderPostingsList = new ArrayList<>();
        final List<String> deliveryPostingsList = new ArrayList<>();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readSnapshot(orderPostingsList, snapshot.child("OrderPostings"));
                readSnapshot(deliveryPostingsList, snapshot.child("DeliveryPostings"));

                readOrderPostings(orderPostingsList, deliveryPostingsList);
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

    private void readOrderPostings(final List<String> orderPostList, final List<String> deliveryPostingsList) {
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
                readDeliveryPostings(deliveryPostingsList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

                MyListingsAdapter deliveryListingsAdapter = new MyListingsAdapter(MyListings.this, deliveryTimeList, deliveryLocList);
                deliveryListingsRecView.setAdapter(deliveryListingsAdapter);
                deliveryListingsRecView.setLayoutManager(new LinearLayoutManager(MyListings.this));

                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
