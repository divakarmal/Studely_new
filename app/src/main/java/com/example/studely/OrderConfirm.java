package com.example.studely;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.studely.misc.Food;
import com.example.studely.misc.Order;
import com.example.studely.notifications.NotifServer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderConfirm extends BottomNavBar {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_post_confirm);
        navBar(this.getApplicationContext());
        final String deliveryPostingID = getIntent().getExtras().getString("deliveryPostingID");

        Bundle bundle = this.getIntent().getExtras();
        final Order order = (Order) bundle.getSerializable("orderObj");
        final String currentUser = order.getReceiver();

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference confirmedOrdersRef = dbRef.child("ConfirmedOrders");
        final DatabaseReference userRef = dbRef.child("users");

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

        userRef.child(currentUser).child("ConfirmedOrders").child(pushID).setValue(order.getDestination());
        userRef.child(order.getDeliverer()).child("ConfirmedOrders").child(pushID).setValue(order.getDestination());

        dbRef.child("DeliveryPostings").child(deliveryPostingID).removeValue();
        dbRef.child("users").child(order.getDeliverer()).child("DeliveryPostings").child(deliveryPostingID).removeValue();
        dbRef.child("canteens").child(order.getCanteen()).child("DeliveryPostings").child(deliveryPostingID).removeValue();

        NotifServer.sendMessage(order.getDeliverer(), "You have received an order for delivery!");
    }
}
