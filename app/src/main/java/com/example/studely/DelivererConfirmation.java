package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.SummaryRecAdapter;
import com.example.studely.misc.DatabaseErrorHandler;
import com.example.studely.misc.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DelivererConfirmation extends BottomNavBar {

    FrameLayout loadingOverlay;
    RecyclerView summaryRecView;
    TextView orderName, orderContact, orderTime, orderDestination;
    Button acceptBtn, declineBtn;

    DatabaseReference detailsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliverer_confirmation);
        navBar(this.getApplicationContext());

        summaryRecView = findViewById(R.id.summaryRecView);
        orderName = findViewById(R.id.orderName);
        orderContact = findViewById(R.id.orderContact);
        orderTime = findViewById(R.id.orderTime);
        orderDestination = findViewById(R.id.orderDestination);
        acceptBtn = findViewById(R.id.acceptBtn);
        declineBtn = findViewById(R.id.declineBtn);
        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();

        Bundle bundle = getIntent().getExtras();

        final String pushID = bundle.getString("pushID");
        Log.d("PUSH", pushID);

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        detailsRef = dbRef.child("AwaitingConfirmation").child(pushID);
        final List<Food> orderList = new ArrayList<>();
        Log.d("DB", "Accessed");

        detailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("IN_DB", "Listening");
                long pushTime = Long.parseLong(snapshot.child("pushTime").getValue(String.class));
                if (System.currentTimeMillis() - pushTime > 600000) {
                    Intent intent = new Intent(DelivererConfirmation.this, MainActivity.class);
                    intent.putExtra("Toast", "Order no longer valid");
                    startActivity(intent);
                }

                orderName.setText("To: " + snapshot.child("Name").getValue());
                orderTime.setText("At: " + snapshot.child("DeliveryTime").getValue());
                orderContact.setText("Contact: " + snapshot.child("Contact").getValue());
                orderDestination.setText("At: " + snapshot.child("Location").getValue());

                for (DataSnapshot snap : snapshot.child("ItemList").getChildren()) {
                    String foodName = snap.getKey();
                    int price = Integer.parseInt((String) snap.child("Price").getValue());
                    int quantity = Integer.parseInt((String) snap.child("Quantity").getValue());
                    orderList.add(new Food(foodName, price, quantity));
                }
                SummaryRecAdapter summaryRecAdapter = new SummaryRecAdapter(DelivererConfirmation.this, orderList);
                summaryRecView.setAdapter(summaryRecAdapter);
                summaryRecView.setLayoutManager(new LinearLayoutManager(DelivererConfirmation.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String error = DatabaseErrorHandler.handleError(databaseError);
                Toast.makeText(DelivererConfirmation.this, error, Toast.LENGTH_LONG).show();
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsRef.child("Accepted").setValue("1");
                Toast.makeText(DelivererConfirmation.this, "Order Accepted!", Toast.LENGTH_LONG).show();
                Intent newIntent = new Intent(getApplicationContext(), HomeLanding.class);
                startActivity(newIntent);
            }
        });

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsRef.child("Accepted").setValue("0");
                Toast.makeText(DelivererConfirmation.this, "Order Declined", Toast.LENGTH_LONG).show();
                Intent newIntent = new Intent(getApplicationContext(), HomeLanding.class);
                startActivity(newIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        detailsRef.child("Accepted").setValue("0");
        Intent intent = new Intent(DelivererConfirmation.this, MainActivity.class);
        startActivity(intent);
    }
}
