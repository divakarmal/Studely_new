package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.DelivererRecAdapter;
import com.example.studely.misc.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDelivererSelect extends BottomNavBar {

    RecyclerView delivererRecView;
    Button mPostBtn;
    FrameLayout loadingOverlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_deliverer_select);
        navBar(this.getApplicationContext());

        delivererRecView = findViewById(R.id.delivererRecView);
        mPostBtn = findViewById(R.id.postOrderBtn);
        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();

        Bundle bundle = this.getIntent().getExtras();
        final Order order = (Order) bundle.getSerializable("orderObj");

        final String canteenID = order.getCanteen();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference deliveryPostingsRef = database.getReference().child("canteens")
                .child(canteenID).child("DeliveryPostings");

        final List<String> deliveryPostingIDList = new ArrayList<>();

        loadingOverlay.setVisibility(View.VISIBLE);
        deliveryPostingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    deliveryPostingIDList.add(snapshot.getKey());
                }

                initRecView(deliveryPostingIDList, order);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), OrderTimeSelect.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderObj", order);
                newIntent.putExtras(bundle);
                startActivity(newIntent);
            }
        });
    }

    private void initRecView(final List<String> postingIDList, final Order order) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.child("DeliveryPostings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String[]> deliveryPostingsData;
                deliveryPostingsData = new ArrayList<>();
                for (String pushID : postingIDList) {
                    DataSnapshot snapshot = dataSnapshot.child(pushID);
                    deliveryPostingsData.add(new String[]{
                            snapshot.child("Name").getValue(String.class),
                            snapshot.child("DeliveryTime").getValue(String.class),
                            snapshot.child("Deliverer").getValue(String.class),
                    });
                }

                getRatings(postingIDList, order, deliveryPostingsData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getRatings(final List<String> postingIDList, final Order order, final List<String[]> deliveryPostingData) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> ratingList = new ArrayList<>();
                for (int i = 0; i < deliveryPostingData.size(); i++) {
                    String delivererID = deliveryPostingData.get(i)[2];
                    final Long rating = dataSnapshot.child(delivererID).child("rating").getValue(Long.class);
                    final String ratingText = rating / 10 + "." + rating % 10;
                    ratingList.add(ratingText);
                }


                DelivererRecAdapter delivererRecAdapter = new DelivererRecAdapter(OrderDelivererSelect.this,
                        deliveryPostingData, postingIDList, order, ratingList);
                delivererRecView.setAdapter(delivererRecAdapter);
                delivererRecView.setLayoutManager(new LinearLayoutManager(OrderDelivererSelect.this));

                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
