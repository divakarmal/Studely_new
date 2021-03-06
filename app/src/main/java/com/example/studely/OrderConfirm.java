package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.studely.misc.DatabaseErrorHandler;
import com.example.studely.misc.Food;
import com.example.studely.misc.Order;
import com.example.studely.notifications.NotifServer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static android.view.View.GONE;

public class OrderConfirm extends BottomNavBar {

    Order order;
    String deliveryPostingID;
    FrameLayout loadingOverlay;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    boolean confirmed = false;
    long startTime = System.currentTimeMillis();
    TextView confirmText, viewText, waitText;

    Handler timerHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_post_confirm);
        navBar(this.getApplicationContext());

        deliveryPostingID = getIntent().getExtras().getString("deliveryPostingID");
        Bundle bundle = this.getIntent().getExtras();
        order = (Order) bundle.getSerializable("orderObj");
        confirmText = findViewById(R.id.confirmText);
        viewText = findViewById(R.id.viewOrderText);
        waitText = findViewById(R.id.waitingText);

        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();

        loadingOverlay.setVisibility(View.VISIBLE);

        getName();
    }

    private void getName() {
        final String currentUser = order.getReceiver();

        DatabaseReference userRef = dbRef.child("users").child(currentUser);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String contact = snapshot.child("phone_number").getValue(String.class);
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
        pushRef.child("pushTime").setValue(String.valueOf(startTime));
        pushRef.child("Location").setValue(order.getDestination());
        pushRef.child("OrdererID").setValue(currentUser);
        pushRef.child("Accepted").setValue("2");
        pushRef.child("DeliveryTime").setValue(order.getDeliveryTime());

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

        pushRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String accepted = snapshot.child("Accepted").getValue(String.class);
                System.out.println(accepted);
                if (accepted == null) {
                    return;
                } else if (accepted.equals("0")) {
                    pushRef.removeValue();
                    Intent intent = new Intent(OrderConfirm.this, OrderDelivererSelect.class);
                    intent.putExtra("Toast", "Deliverer declined, pick another or make a new posting");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderObj", order);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (accepted.equals("1")) {
                    loadingOverlay.setVisibility(GONE);
                    waitText.setVisibility(GONE);
                    confirmText.setVisibility(View.VISIBLE);
                    viewText.setVisibility(View.VISIBLE);
                    confirmOrder();
                    pushRef.removeValue();
                } else if (accepted.equals("-1")) {
                    pushRef.removeValue();
                    Intent intent = new Intent(OrderConfirm.this, OrderDelivererSelect.class);
                    intent.putExtra("Toast", "Order expired");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderObj", order);
                    intent.putExtras(bundle);
                    startActivity(intent);
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
        confirmed = true;

        String currentUser = order.getReceiver();
        DatabaseReference confirmedOrdersRef = dbRef.child("ConfirmedOrders");
        DatabaseReference userRef = dbRef.child("users");
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int min = rightNow.get(Calendar.MINUTE);
        int currentTime = hour * 100 + min;
        String confirmationTime;
        if (currentTime < 100) {
            confirmationTime = "00" + currentTime;
        } else if (currentTime < 1000) {
            confirmationTime = "0" + currentTime;
        } else {
            confirmationTime = Integer.toString(currentTime);
        }

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
        pushRef.child("Time").setValue(confirmationTime);

        DatabaseReference itemListRef = pushRef.child("ItemList");
        for (Food food : order.getList()) {
            itemListRef.child(food.name).child("Price")
                    .setValue(String.valueOf(food.price));
            itemListRef.child(food.name).child("Quantity")
                    .setValue(String.valueOf(food.quantity));
        }

        userRef.child(currentUser).child("ConfirmedOrders").child(pushID).child("destination").setValue(order.getDestination());
        userRef.child(currentUser).child("ConfirmedOrders").child(pushID).child("isOrderer").setValue(true);
        userRef.child(currentUser).child("ConfirmedOrders").child(pushID).child("isComplete").setValue(false);
        userRef.child(order.getDeliverer()).child("ConfirmedOrders").child(pushID).child("destination").setValue(order.getDestination());
        userRef.child(order.getDeliverer()).child("ConfirmedOrders").child(pushID).child("isOrderer").setValue(false);
        userRef.child(order.getDeliverer()).child("ConfirmedOrders").child(pushID).child("isComplete").setValue(false);

        dbRef.child("DeliveryPostings").child(deliveryPostingID).removeValue();
        dbRef.child("users").child(order.getDeliverer()).child("DeliveryPostings").child(deliveryPostingID).removeValue();
        dbRef.child("canteens").child(order.getCanteen()).child("DeliveryPostings").child(deliveryPostingID).removeValue();

        NotifServer.sendMessage(order.getDeliverer(), "You have received an order for delivery!");
        loadingOverlay.setVisibility(GONE);
    }

    @Override
    public void onBackPressed() {
        if (confirmed) {
            Intent intent = new Intent(OrderConfirm.this, HomeLanding.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(OrderConfirm.this, OrderDelivererSelect.class);
            intent.putExtra("Toast", "Deliverer declined, pick another or make a new posting");
            Bundle bundle = new Bundle();
            bundle.putSerializable("orderObj", order);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
