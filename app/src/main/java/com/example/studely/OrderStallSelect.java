package com.example.studely;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderStallSelect extends BottomNavBar {


    ArrayAdapter<String> stallAdapter;
    ListView stallList;
    List<String> stalls;
    Button popup;
    PopupWindow popupWindow;
    ConstraintLayout mConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_stall_select);

        stallList = (ListView) findViewById(R.id.stallListView);
        popup = findViewById(R.id.popup);
        final String canteenID = getIntent().getExtras().getString("canteenID");
        final String destination = getIntent().getExtras().getString("orderDestination");
        navBar(this.getApplicationContext());
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference stallRef = database.getReference().child("canteens")
                .child(canteenID).child("StallList");

        stallRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stalls = new ArrayList<String>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String stallName = snapshot.getKey();
                    if (stallName != null) {
                        stalls.add(stallName);
                    }
                }

                stallAdapter = new ArrayAdapter<>(OrderStallSelect.this,
                        android.R.layout.simple_list_item_1, stalls);
                stallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stallList.setAdapter(stallAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        stallList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(getApplicationContext(), OrderFoodSelect.class);
                String choice = stalls.get(position);
                newIntent.putExtra("canteenID", canteenID);
                newIntent.putExtra("stallID", choice);
                newIntent.putExtra("orderDestination", destination);
                startActivity(newIntent);
                Toast.makeText(OrderStallSelect.this, stalls.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        mConstraintLayout =  findViewById(R.id.relativeLayout);

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.review_pop_up,null);

                /*
                    public PopupWindow (View contentView, int width, int height)
                        Create a new non focusable popup window which can display the contentView.
                        The dimension of the window must be passed to this constructor.

                        The popup does not provide any background. This should be handled by
                        the content view.

                    Parameters
                        contentView : the popup's content
                        width : the popup's width
                        height : the popup's height
                */
                // Initialize a new instance of popup window
                popupWindow = new PopupWindow(
                        customView,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    popupWindow.setElevation(5.0f);
                }
                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);
                RatingBar mRatingBar = (RatingBar) customView.findViewById(R.id.ratingBar);
                Button mReviewBtn = (Button) customView.findViewById(R.id.reviewBtn);

                mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        //TODO
                    }
                });


                mReviewBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO
                        popupWindow.dismiss();
                    }
                });

                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        popupWindow.dismiss();
                    }
                });
                popupWindow.showAtLocation(mConstraintLayout, Gravity.CENTER,0,0);

            }
        });

    }
}
