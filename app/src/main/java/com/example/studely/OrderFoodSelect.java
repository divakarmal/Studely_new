package com.example.studely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.studely.classes.Food;
import com.example.studely.adapters.FoodRecAdapter;
import com.example.studely.classes.Order;
import com.example.studely.classes.QtyTextChanged;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderFoodSelect extends AppCompatActivity {

    RecyclerView foodList;
    Button mNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food_select);

        foodList = findViewById(R.id.foodRecView);
        mNextBtn = findViewById(R.id.nextBtn);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        Intent home = new Intent(OrderFoodSelect.this,HomeLanding.class);
                        startActivity(home);
                        break;
                    case R.id.ic_myOrderList:
                        Intent orderList = new Intent(OrderFoodSelect.this,MyOrderList.class);
                        startActivity(orderList);
                        break;
                }
                return false;
            }
        });

        final String canteenID = getIntent().getExtras().getString("canteenID");
        final String stallID = getIntent().getExtras().getString("stallID");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String destination = getIntent().getExtras().getString("orderDestination");
        DatabaseReference fFoodRef = database.getReference().child("canteens")
                .child(canteenID).child("StallList").child(stallID);

        final List<String> foodItems = new ArrayList<>();
        final List<Integer> foodQty = new ArrayList<>();
        final List<Integer> foodPrice = new ArrayList<>();
        fFoodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    foodItems.add(snapshot.getKey());
                    foodQty.add(0);
                    Integer p = Integer.parseInt((String) snapshot.getValue());
                    foodPrice.add(p);
                }

                final FoodRecAdapter foodAdapter = new FoodRecAdapter(OrderFoodSelect.this,
                        foodItems.toArray(new String[0]),
                        new QtyTextChanged() {
                    @Override
                    public void OnTextChanged(int position, String charSeq) {
                        if (!charSeq.equals("")) {
                            foodQty.set(position, Integer.parseInt(charSeq));
                        }
                    }
                });
                foodList.setAdapter(foodAdapter);
                foodList.setLayoutManager(new LinearLayoutManager(OrderFoodSelect.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = new Order();
                order.setCanteen(canteenID);
                order.setDestination(destination); // <----------------------- Figure this out
                for (int i = 0; i < foodQty.size(); i++) {
                    int qty = foodQty.get(i);
                    if (qty != 0) {
                        order.addFood(new Food(foodItems.get(i), foodPrice.get(i), qty));
                    }
                }
                Intent newIntent = new Intent(getApplicationContext(), OrderSummary.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderObj", order);
                newIntent.putExtras(bundle);
                startActivity(newIntent);
            }
        });
    }
}
