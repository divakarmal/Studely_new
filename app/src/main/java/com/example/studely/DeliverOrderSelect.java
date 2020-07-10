package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.OrderRecAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliverOrderSelect extends BottomNavBar {

    Button mNewPostingBtn;
    RecyclerView orderRecView;
    FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_order_select);
        navBar(this.getApplicationContext());

        orderRecView = findViewById(R.id.orderRecView);
        mNewPostingBtn = findViewById(R.id.newPosting);
        loadingOverlay = findViewById(R.id.loading_overlay);

        final String canteenID = getIntent().getExtras().getString("canteenID");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postingIDRef = database.getReference().child("canteens").child(canteenID)
                .child("OrderPostings");
        final List<String> orderIDList = new ArrayList<>();

        loadingOverlay.setVisibility(View.VISIBLE);
        postingIDRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    orderIDList.add(snapshot.getKey());
                }

                initRecView(orderIDList, canteenID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mNewPostingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), DeliverTimeSelect.class);
                newIntent.putExtra("canteenID", canteenID);
                startActivity(newIntent);
            }
        });
    }

    private void initRecView(final List<String> postingIDList, final String canteenID) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final List<String[]> orderPostingsData = new ArrayList<>();

        dbRef.child("OrderPostings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (String pushID : postingIDList) {
                    DataSnapshot snapshot = dataSnapshot.child(pushID);
                    orderPostingsData.add(new String[]{
                            snapshot.child("Destination").getValue(String.class),
                            snapshot.child("DeliveryTime").getValue(String.class),
                            snapshot.getKey(),
                            canteenID
                    });
                }

                OrderRecAdapter orderRecAdapter = new OrderRecAdapter(DeliverOrderSelect.this,
                        orderPostingsData, postingIDList);
                orderRecView.setAdapter(orderRecAdapter);
                orderRecView.setLayoutManager(new LinearLayoutManager(DeliverOrderSelect.this));

                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
