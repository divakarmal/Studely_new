package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.example.studely.misc.DatabaseErrorHandler;
import com.example.studely.misc.Food;
import com.example.studely.misc.Order;
import com.example.studely.notifications.NotifServer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderConfirm extends BottomNavBar {

    Order order;
    String deliveryPostingID;
    FrameLayout loadingOverlay;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    boolean confirmed = false;
    long startTime = System.currentTimeMillis();

    Handler timerHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_post_confirm);
        navBar(this.getApplicationContext());

        deliveryPostingID = getIntent().getExtras().getString("deliveryPostingID");
        Bundle bundle = this.getIntent().getExtras();
        order = (Order) bundle.getSerializable("orderObj");

        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();

        getName();
    }

    private void getName() {
        final String currentUser = order.getReceiver();

        DatabaseReference userRef = dbRef.child("users").child(currentUser);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("Name").getValue(String.class);
                String contact = snapshot.child("Contact").getValue(String.class);
                awaitConfirm(currentUser, name, contact);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String error = DatabaseErrorHandler.handleError(databaseError);
                Toast.makeText(OrderConfirm.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void awaitConfirm(String currentUser, String name, String contact) {
        DatabaseReference awaitRef = dbRef.child("AwaitingConfirmation");
        final DatabaseReference pushRef = awaitRef.push();
        final String pushID = pushRef.getKey();

        pushRef.child("Name").setValue(name);
        pushRef.child("Contact").setValue(contact);
        pushRef.child("Time").setValue(String.valueOf(startTime)); // <--------------- TIME
        pushRef.child("Location").setValue(order.getDestination());
        pushRef.child("OrdererID").setValue(currentUser);

        DatabaseReference itemListRef = pushRef.child("ItemList");
        for (Food food : order.getList()) {
            itemListRef.child(food.name).child("Price")
                    .setValue(String.valueOf(food.price));
            itemListRef.child(food.name).child("Quantity")
                    .setValue(String.valueOf(food.quantity));
        }

        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dbRef.child("AwaitingConfirmation").child(pushID).child("Accepted").setValue("-1");
            }
        }, 600000);

        NotifServer.sendAwait(order.getDeliverer(), pushID);
        loadingOverlay.setVisibility(View.GONE);

        pushRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String accepted = snapshot.child("Accepted").getValue(String.class);
                if (accepted.equals("0")) {
                    pushRef.removeValue();
                    Intent intent = new Intent(OrderConfirm.this, OrderDelivererSelect.class);
                    intent.putExtra("Toast", "Order expired");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderObj", order);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (accepted.equals("1")) {
                    confirmed = true;
                    pushRef.removeValue();
                    confirmOrder();
                } else if (accepted.equals("-1")) {
                    pushRef.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String error = DatabaseErrorHandler.handleError(databaseError);
                Toast.makeText(OrderConfirm.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void confirmOrder() {
        String currentUser = order.getReceiver();
        DatabaseReference confirmedOrdersRef = dbRef.child("ConfirmedOrders");
        DatabaseReference userRef = dbRef.child("users");

        String pushID = confirmedOrdersRef.push().getKey();
        DatabaseReference pushRef = confirmedOrdersRef.child(pushID);

        pushRef.child("DeliveryTime").setValue(order.getDeliveryTime());
        pushRef.child("Destination").setValue(order.getDestination());
        pushRef.child("OrderCost").setValue(Integer.toString(order.calcOrderCost()));
        pushRef.child("Receiver").setValue(currentUser);
        pushRef.child("Deliverer").setValue(order.getDeliverer());
        pushRef.child("Canteen").setValue(order.getCanteen());
        pushRef.child("Reached").setValue(false);
        pushRef.child("Completed").setValue(false);
        pushRef.child("Time").setValue("0000"); // <------------------------ How this again

        DatabaseReference itemListRef = pushRef.child("ItemList");
        for (Food food : order.getList()) {
            itemListRef.child(food.name).child("Price")
                    .setValue(String.valueOf(food.price));
            itemListRef.child(food.name).child("Quantity")
                    .setValue(String.valueOf(food.quantity));
        }

        userRef.child(currentUser).child("ConfirmedOrders").child(pushID).child("destination").setValue(order.getDestination());
        userRef.child(currentUser).child("ConfirmedOrders").child(pushID).child("isOrderer").setValue("1");
        userRef.child(order.getDeliverer()).child("ConfirmedOrders").child(pushID).child("destination").setValue(order.getDestination());
        userRef.child(order.getDeliverer()).child("ConfirmedOrders").child(pushID).child("isOrderer").setValue("0");

        dbRef.child("DeliveryPostings").child(deliveryPostingID).removeValue();
        dbRef.child("users").child(order.getDeliverer()).child("DeliveryPostings").child(deliveryPostingID).removeValue();
        dbRef.child("canteens").child(order.getCanteen()).child("DeliveryPostings").child(deliveryPostingID).removeValue();

        NotifServer.sendMessage(order.getDeliverer(), "You have received an order for delivery!");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OrderConfirm.this, HomeLanding.class);
        startActivity(intent);
    }
}
