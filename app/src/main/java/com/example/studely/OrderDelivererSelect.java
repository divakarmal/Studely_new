package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.DelivererRecAdapter;
import com.example.studely.classes.Order;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_deliverer_select);

        delivererRecView = findViewById(R.id.delivererRecView);
        mPostBtn = findViewById(R.id.postOrderBtn);
        navBar(this.getApplicationContext());

        Bundle bundle = this.getIntent().getExtras();
        final Order order = (Order) bundle.getSerializable("orderObj");

        final String canteenID = order.getCanteen();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference orderPostingsRef = database.getReference().child("DeliveryPostings")
                .child(canteenID);

        final List<String> nameList = new ArrayList<>();
        final List<String> timeList = new ArrayList<>();
        final List<String> delivererIDList = new ArrayList<>();
        final List<String> deliveryPostingIDList = new ArrayList<>();
        orderPostingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    nameList.add(snapshot.child("Name").getValue(String.class));
                    timeList.add(snapshot.child("DeliveryTime").getValue(String.class));
                    delivererIDList.add(snapshot.child("Deliverer").getValue(String.class));
                    deliveryPostingIDList.add(snapshot.getKey());
                }

                final DelivererRecAdapter delivererRecAdapter = new DelivererRecAdapter(OrderDelivererSelect.this,
                        nameList, timeList, delivererIDList, order, deliveryPostingIDList);
                delivererRecView.setAdapter(delivererRecAdapter);
                delivererRecView.setLayoutManager(new LinearLayoutManager(OrderDelivererSelect.this));
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
}
