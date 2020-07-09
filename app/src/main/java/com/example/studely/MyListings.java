package com.example.studely;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MyListings extends BottomNavBar {

    FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();

        Log.d("ASYNC", "Starting async");
        new MyListingsAsync().execute();
    }

    public CompletableFuture<List<String>> readDB(DatabaseReference dbRef) {
        final List<String> arr = new ArrayList<>();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    arr.add(snap.getKey());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return null;
    }

    private class MyListingsAsync extends AsyncTask<Void, Void, Void> {

        private List<String> deliveryPushIDs = new ArrayList<>();
        private List<String> orderPushIDs = new ArrayList<>();

        public MyListingsAsync() {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingOverlay.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

            final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = dbRef.child("users").child(currentUser);

            userRef.child("OrderPostings").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        orderPushIDs.add(snap.getKey());
                    }

                    DatabaseReference orderPostingsRef = dbRef.child("OrderPostings");

                    orderPostingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (String pushID : orderPushIDs) {
                                if (snapshot.hasChild(pushID)) {
                                    Log.d("ORDER", pushID);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            userRef.child("DeliveryPostings").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        deliveryPushIDs.add(snap.getKey());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            DatabaseReference deliveryPostingsRef = dbRef.child("DeliveryPostings");

            deliveryPostingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (String pushID : deliveryPushIDs) {
                        if (snapshot.hasChild(pushID)) {
                            Log.d("DELIVERY", pushID);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadingOverlay.setVisibility(View.GONE);
        }
    }
}
