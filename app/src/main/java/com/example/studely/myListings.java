package com.example.studely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.studely.adapters.OrderRecAdapter;
import com.example.studely.adapters.myOrderListingsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class myListings extends BottomNavBar {
    RecyclerView listingRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);
        listingRecView = findViewById(R.id.myOrderListings);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference orderPostingsRef = dbRef.child("users").child(currentUser).child("OrderPostings");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> timeList = new ArrayList<>();
                List<String> destList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.child("users").child(currentUser).child("OrderPostings").getChildren()) {
                    String postingID = snapshot.getKey();
                    String canteenID = snapshot.getValue(String.class);
                    String time = dataSnapshot.child("OrderPostings").child(canteenID).child(postingID).child("DeliveryTime").getValue(String.class);
                    String destination = dataSnapshot.child("OrderPostings").child(canteenID).child(postingID).child("Destination").getValue(String.class);
                    timeList.add(time);
                    destList.add(destination);
                }

                final myOrderListingsAdapter myListingAdapter = new myOrderListingsAdapter(myListings.this,
                        timeList, destList);

                listingRecView.setAdapter(myListingAdapter);
                listingRecView.setLayoutManager(new LinearLayoutManager(myListings.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
