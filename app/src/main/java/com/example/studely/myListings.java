package com.example.studely;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.studely.adapters.myOrderListingsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyListings extends BottomNavBar {
    RecyclerView listingRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);
        listingRecView = findViewById(R.id.myOrderListings);
        navBar(this.getApplicationContext());

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userPostingRef = dbRef.child("users").child(currentUser).child("OrderPostings");
        final DatabaseReference orderPostingsRef = dbRef.child("OrderPostings");

        userPostingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> timeList = new ArrayList<>();
                final List<String> destList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String postingID = snapshot.getKey();
                    final String canteenID = snapshot.getValue(String.class);


                    orderPostingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String destination = dataSnapshot.child(canteenID).child(postingID).child("Destination").getValue(String.class);
                            System.out.println(destination);
                            String time = dataSnapshot.child(canteenID).child(postingID).child("DeliveryTime").getValue(String.class);
                            timeList.add(time);
                            destList.add(destination);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                final myOrderListingsAdapter myListingAdapter = new myOrderListingsAdapter(MyListings.this,
                        timeList, destList);

                listingRecView.setAdapter(myListingAdapter);
                listingRecView.setLayoutManager(new LinearLayoutManager(MyListings.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
