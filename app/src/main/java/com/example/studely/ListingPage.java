package com.example.studely;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.studely.adapters.SummaryRecAdapter;
import com.example.studely.misc.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListingPage extends BottomNavBar {

    TextView time, canteen;
    Button back, delete;
    RecyclerView listingRecView;
    PopupWindow mPopupWindow;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_page);

        time = findViewById(R.id.time);
        canteen = findViewById(R.id.canteen);
        back = findViewById(R.id.BackButton);
        delete = findViewById(R.id.DeleteButton);
        listingRecView = findViewById(R.id.summary);
        constraintLayout = (ConstraintLayout) findViewById(R.id.cl);
        constraintLayout.getForeground().setAlpha(0);
        navBar(this.getApplicationContext());

        final String postingID = getIntent().getExtras().getString("postingID");
        final Boolean isOrder = getIntent().getExtras().getBoolean("isOrder");

        if(isOrder) {
            final List<Food> orderList = new ArrayList<>();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference postingRef = database.getReference().child("OrderPostings")
                    .child(postingID);
            postingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    time.setText(dataSnapshot.child("DeliveryTime").getValue(String.class));
                    canteen.setText(dataSnapshot.child("Canteen").getValue(String.class));


                    for (DataSnapshot snapshot : dataSnapshot.child("ItemList").getChildren()) {
                        String foodName = snapshot.getKey();
                        int price = Integer.parseInt((String) snapshot.child("Price").getValue());
                        int quantity = Integer.parseInt((String) snapshot.child("Quantity").getValue());
                        orderList.add(new Food(foodName, price, quantity));
                    }
                    SummaryRecAdapter summaryRecAdapter = new SummaryRecAdapter(ListingPage.this, orderList);
                    listingRecView.setAdapter(summaryRecAdapter);
                    listingRecView.setLayoutManager(new LinearLayoutManager(ListingPage.this));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference postingRef = database.getReference().child("DeliveryPostings")
                    .child(postingID);
            postingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    time.setText(dataSnapshot.child("DeliveryTime").getValue(String.class));
                    canteen.setText(dataSnapshot.child("Canteen").getValue(String.class));
                    listingRecView.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), MyListings.class);
                startActivity(newIntent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View customView = inflater.inflate(R.layout.confirmation_popup,null);

                mPopupWindow = new PopupWindow(
                        customView,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT
                );

                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                Button cancel = (Button) customView.findViewById(R.id.cancelBtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPopupWindow.dismiss();
                        constraintLayout.getForeground().setAlpha(0);
                    }
                });

                Button delete = (Button) customView.findViewById(R.id.deleteBtn);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deletePosting(postingID, isOrder);
                        Intent newIntent = new Intent(getApplicationContext(), MyListings.class);
                        startActivity(newIntent);
                    }
                });


                mPopupWindow.showAtLocation(constraintLayout, Gravity.CENTER,0,0);
                constraintLayout.getForeground().setAlpha(220);
            }
        });


    }

    public void deletePosting(String postingID, Boolean isOrder){
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference orderPostingsRef = dbRef.child("OrderPostings");
        final DatabaseReference deliverPostingsRef = dbRef.child("DeliveryPostings");
        final DatabaseReference userRef = dbRef.child("users").child(currentUser);
        final DatabaseReference canteenRef = dbRef.child("canteens").child(canteen.getText().toString());

        if(isOrder){
            orderPostingsRef.child(postingID).removeValue();
            userRef.child("OrderPostings").child(postingID).removeValue();
            canteenRef.child("OrderPostings").child(postingID).removeValue();
        } else {
            deliverPostingsRef.child(postingID).removeValue();
            userRef.child("DeliveryPostings").child(postingID).removeValue();
            canteenRef.child("DeliveryPostings").child(postingID).removeValue();
        }


    }
}