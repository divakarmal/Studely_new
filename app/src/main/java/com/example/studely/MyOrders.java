package com.example.studely;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        navBar(this.getApplicationContext());

        myOrderRecView = findViewById(R.id.myOrderRecView);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference userOrdersRef = database.getReference().child("users")
                            .child(currentUser).child("ConfirmedOrders");

        final List<String> orderIDs = new ArrayList<>();
        final List<String> destinations = new ArrayList<>();

        userOrdersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    orderIDs.add(snapshot.getKey());
                    destinations.add((String) snapshot.getValue());
                }

                final MyOrderAdapter myOrderAdapter = new MyOrderAdapter(MyOrders.this, orderIDs, destinations);
                myOrderRecView.setAdapter(myOrderAdapter);
                myOrderRecView.setLayoutManager(new LinearLayoutManager(MyOrders.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}