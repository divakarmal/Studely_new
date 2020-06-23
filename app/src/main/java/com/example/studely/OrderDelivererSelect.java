package com.example.studely;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.DelivererRecAdapter;
import com.example.studely.adapters.OrderRecAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDelivererSelect extends AppCompatActivity {

    RecyclerView delivererRecView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_deliverer_select);

        delivererRecView = findViewById(R.id.delivererRecView);

        final String canteenID = getIntent().getExtras().getString("canteenID");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference orderPostingsRef = database.getReference().child("DeliverPostings")
                .child(canteenID);

        final List<String> nameList = new ArrayList<>();
        final List<String> timeList = new ArrayList<>();
        orderPostingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    nameList.add(snapshot.child("Name").getValue(String.class));
                    timeList.add(snapshot.child("DeliveryTime").getValue(String.class));
                }

                final DelivererRecAdapter delivererRecAdapter = new DelivererRecAdapter(OrderDelivererSelect.this,
                        nameList, timeList);
                delivererRecView.setAdapter(delivererRecAdapter);
                delivererRecView.setLayoutManager(new LinearLayoutManager(OrderDelivererSelect.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
