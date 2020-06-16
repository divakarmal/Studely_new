package com.example.studely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.studely.classes.FoodRecAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderFoodSelect extends AppCompatActivity {

    RecyclerView foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food_select);

        foodList = findViewById(R.id.foodRecView);

        final String canteenID = getIntent().getExtras().getString("canteenID");
        final String stallID = getIntent().getExtras().getString("stallID");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fFoodRef = database.getReference().child("canteens")
                .child(canteenID).child("StallList").child(stallID);

        final List<String> foodItems = new ArrayList<>();
        fFoodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    foodItems.add(snapshot.getKey());
                }

                FoodRecAdapter foodAdapter = new FoodRecAdapter(OrderFoodSelect.this,
                                            foodItems.toArray(new String[0]));
                foodList.setAdapter(foodAdapter);
                foodList.setLayoutManager(new LinearLayoutManager(OrderFoodSelect.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
