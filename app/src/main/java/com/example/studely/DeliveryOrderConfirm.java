package com.example.studely;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.studely.classes.Food;
import com.example.studely.classes.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryOrderConfirm extends BottomNavBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order_confirm);
        final String orderPostingID = getIntent().getExtras().getString("orderPostingID");
        final String canteenID = getIntent().getExtras().getString("canteenID");
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference orderRef = dbRef.child("OrderPostings").child(canteenID).child(orderPostingID);
        final DatabaseReference confirmedOrdersRef = dbRef.child("ConfirmedOrders");
        final DatabaseReference userRef = dbRef.child("users").child(currentUser);
        navBar(this.getApplicationContext());

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pushID = confirmedOrdersRef.push().getKey();
                DatabaseReference pushRef = confirmedOrdersRef.child(pushID);
                String destination = dataSnapshot.child("Destination").getValue(String.class);
                String receiver = dataSnapshot.child("Receiver").getValue(String.class);
                pushRef.child("DeliveryTime").setValue(dataSnapshot.child("DeliveryTime").getValue());
                pushRef.child("Destination").setValue(destination);
                pushRef.child("OrderCost").setValue(dataSnapshot.child("OrderCost").getValue(String.class));
                pushRef.child("Receiver").setValue(dataSnapshot.child("Receiver").getValue());
                pushRef.child("Deliverer").setValue(currentUser);
                pushRef.child("Canteen").setValue(canteenID);
                pushRef.child("Reached").setValue(false);
                pushRef.child("Completed").setValue(false);
                DatabaseReference itemListRef = pushRef.child("ItemList");
                for (DataSnapshot snapshot: dataSnapshot.child("ItemList").getChildren()) {
                    String price = snapshot.child("Price").getValue(String.class);
                    String quantity = snapshot.child("Quantity").getValue(String.class);
                    itemListRef.child(snapshot.getKey()).child("Price").setValue(price);
                    itemListRef.child(snapshot.getKey()).child("Quantity").setValue(quantity);
                }
                pushRef.child("Time").setValue("0000");


                dbRef.child("users").child(receiver).child("ConfirmedOrders").child(pushID).setValue(destination);
                userRef.child("ConfirmedOrders").child(pushID).setValue(destination);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });



    }
}
