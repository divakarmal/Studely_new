package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.studely.notifications.NotifServer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeliverConfirm extends BottomNavBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_order_confirm);
        final String orderPostingID = getIntent().getExtras().getString("orderPostingID");
        final String canteenID = getIntent().getExtras().getString("canteenID");
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference orderRef = dbRef.child("OrderPostings").child(canteenID).child(orderPostingID);
        final DatabaseReference confirmedOrdersRef = dbRef.child("ConfirmedOrders");
        final DatabaseReference userRef = dbRef.child("users");
        navBar(this.getApplicationContext());

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String destination = dataSnapshot.child("Destination").getValue(String.class);
                String receiver = dataSnapshot.child("Receiver").getValue(String.class);
                if(dataSnapshot.hasChild("Destination")) {

                    String pushID = confirmedOrdersRef.push().getKey();
                    DatabaseReference pushRef = confirmedOrdersRef.child(pushID);
                    pushRef.child("DeliveryTime").setValue(dataSnapshot.child("DeliveryTime").getValue());
                    pushRef.child("Destination").setValue(destination);
                    pushRef.child("OrderCost").setValue(dataSnapshot.child("OrderCost").getValue(String.class));
                    pushRef.child("Receiver").setValue(receiver);
                    pushRef.child("Deliverer").setValue(currentUser);
                    pushRef.child("Canteen").setValue(canteenID);
                    pushRef.child("Reached").setValue(false);
                    pushRef.child("Completed").setValue(false);
                    pushRef.child("Time").setValue("0000"); // <-------------------------------------------------------------------

                    DatabaseReference itemListRef = pushRef.child("ItemList");
                    for (DataSnapshot snapshot : dataSnapshot.child("ItemList").getChildren()) {
                        String price = snapshot.child("Price").getValue(String.class);
                        String quantity = snapshot.child("Quantity").getValue(String.class);
                        itemListRef.child(snapshot.getKey()).child("Price").setValue(price);
                        itemListRef.child(snapshot.getKey()).child("Quantity").setValue(quantity);
                    }

                    userRef.child(currentUser).child("ConfirmedOrders").child(pushID).child("destination").setValue(destination);
                    userRef.child(currentUser).child("ConfirmedOrders").child(pushID).child("isOrderer").setValue("0");
                    userRef.child(receiver).child("ConfirmedOrders").child(pushID).child("destination").setValue(destination);
                    userRef.child(receiver).child("ConfirmedOrders").child(pushID).child("isOrderer").setValue("1");

                    dbRef.child("OrderPostings").child(canteenID).child(orderPostingID).removeValue();
                    dbRef.child("canteens").child(canteenID).child("OrderPostings").child(orderPostingID).removeValue();
                    dbRef.child("users").child(receiver).child("OrderPostings").child(orderPostingID).removeValue();

                    NotifServer.sendMessage(receiver, "Your order has been accepted for delivery!");
                }
                else {
                    Toast.makeText(DeliverConfirm.this, "This order posting has been deleted/picked by someone else", Toast.LENGTH_SHORT).show();
                    Intent newIntent = new Intent(getApplicationContext(), DeliverOrderSelect.class);
                    newIntent.putExtra("canteenID", canteenID);
                    startActivity(newIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
}
