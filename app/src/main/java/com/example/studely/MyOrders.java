package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.MyOrderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOrders extends BottomNavBar {

    RecyclerView myOrderRecView;
    Button listingsBtn;
    FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        navBar(this.getApplicationContext());

        listingsBtn = findViewById(R.id.listingsBtn);
        myOrderRecView = findViewById(R.id.myOrderRecView);
        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();

        listingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyListings.class));
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference userOrdersRef = database.getReference().child("users")
                .child(currentUser).child("ConfirmedOrders");

        final List<String> orderIDs = new ArrayList<>();
        final List<String> destinations = new ArrayList<>();

        loadingOverlay.setVisibility(View.VISIBLE);
        userOrdersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    orderIDs.add(snapshot.getKey());
                    destinations.add((String) snapshot.getValue());
                }

                MyOrderAdapter myOrderAdapter = new MyOrderAdapter(MyOrders.this, orderIDs, destinations);
                myOrderRecView.setAdapter(myOrderAdapter);
                myOrderRecView.setLayoutManager(new LinearLayoutManager(MyOrders.this));

                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
