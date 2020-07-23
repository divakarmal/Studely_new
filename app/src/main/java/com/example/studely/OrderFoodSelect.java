package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.FoodRecAdapter;
import com.example.studely.misc.DatabaseErrorHandler;
import com.example.studely.misc.Food;
import com.example.studely.misc.Order;
import com.example.studely.misc.QtyTextChanged;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderFoodSelect extends BottomNavBar {

    RecyclerView foodList;
    ImageButton mNextBtn;
    FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food_select);
        navBar(this.getApplicationContext());

        foodList = findViewById(R.id.foodRecView);
        mNextBtn = findViewById(R.id.nextBtn);
        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();

        final String canteenID = getIntent().getExtras().getString("canteenID");
        final String stallID = getIntent().getExtras().getString("stallID");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String destination = getIntent().getExtras().getString("orderDestination");
        DatabaseReference fFoodRef = database.getReference().child("canteens")
                .child(canteenID).child("StallList").child(stallID);

        final List<String> foodItems = new ArrayList<>();
        final List<Integer> foodQty = new ArrayList<>();
        final List<Integer> foodPrice = new ArrayList<>();
        loadingOverlay.setVisibility(View.VISIBLE);
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
                        }, foodPrice.toArray(new Integer[0]));

                foodList.setAdapter(foodAdapter);
                foodList.setLayoutManager(new LinearLayoutManager(OrderFoodSelect.this));
                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String error = DatabaseErrorHandler.handleError(databaseError);
                Toast.makeText(OrderFoodSelect.this, error, Toast.LENGTH_LONG).show();
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = new Order();
                order.setCanteen(canteenID);
                order.setDestination(destination);
                order.setReceiver(FirebaseAuth.getInstance().getCurrentUser().getUid());
                int totalItems = 0;
                for (int i = 0; i < foodQty.size(); i++) {
                    int qty = foodQty.get(i);
                    totalItems += qty;
                    if (qty != 0) {
                        order.addFood(new Food(foodItems.get(i), foodPrice.get(i), qty));
                    }
                }
                int cost = order.calcOrderCost();
                order.setItems(totalItems);

                if (cost == 0) {
                    Toast.makeText(OrderFoodSelect.this, "At least one item must be ordered", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (totalItems > 10) {
                    Toast.makeText(OrderFoodSelect.this, "Bruh fat", Toast.LENGTH_SHORT).show(); // idk display what?
                    return;
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
