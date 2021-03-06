package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studely.misc.DatabaseErrorHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewPage extends AppCompatActivity {
    Button reviewBtn;
    RatingBar ratingBar;
    String dID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_page);

        final String orderID = getIntent().getExtras().getString("orderID");
        final String delivererID = getIntent().getExtras().getString("delivererID");
        System.out.println(delivererID);
        reviewBtn = findViewById(R.id.reviewBtn);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                //TODO
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = database.getReference().child("ConfirmedOrders").child(orderID);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String delID = dataSnapshot.child("Deliverer").getValue(String.class);
                dID = delID;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String error = DatabaseErrorHandler.handleError(databaseError);
                Toast.makeText(ReviewPage.this, error, Toast.LENGTH_LONG).show();
            }
        });


        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference dbRef = database.getReference().child("users").child(dID);
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int currentRating = dataSnapshot.child("rating").getValue(int.class);
                        int totalRatings = dataSnapshot.child("totalRatings").getValue(int.class);
                        int newRating = (((int) ratingBar.getRating() * 10) + (currentRating * totalRatings)) / (totalRatings + 1);
                        dbRef.child("rating").setValue(newRating);
                        dbRef.child("totalRatings").setValue(totalRatings + 1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        String error = DatabaseErrorHandler.handleError(databaseError);
                        Toast.makeText(ReviewPage.this, error, Toast.LENGTH_LONG).show();
                    }
                });
                Intent newIntent = new Intent(getApplicationContext(), OrderPageOrderer.class);
                newIntent.putExtra("orderID", orderID);
                startActivity(newIntent);
            }
        });


    }
}