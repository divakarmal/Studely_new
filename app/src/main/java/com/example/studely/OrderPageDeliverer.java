package com.example.studely;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.SummaryRecAdapter;
import com.example.studely.misc.DatabaseErrorHandler;
import com.example.studely.misc.Food;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderPageDeliverer extends BottomNavBar {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    RecyclerView summaryList;
    TextView mOrderID, mTimeStamp, mDeliveryTime, mOrderTotal, mStatus;
    Button mReachedBtn;
    Location currentLoc, delLocation;
    String orderID;

    FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page_deliverer);
        navBar(this.getApplicationContext());

        orderID = getIntent().getExtras().getString("orderID");
        summaryList = findViewById(R.id.sumRecView);
        mOrderID = findViewById(R.id.OrderID);
        mTimeStamp = findViewById(R.id.TimeStamp);
        mDeliveryTime = findViewById(R.id.DeliveryTime);
        mOrderTotal = findViewById(R.id.orderCost);
        mReachedBtn = findViewById(R.id.reachedBtn);
        mStatus = findViewById(R.id.status);
        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();

        mOrderID.setText(orderID);

        loadingOverlay.setVisibility(View.VISIBLE);
        getCurrentLocation();

        final List<Food> orderList = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fOrderRef = database.getReference().child("ConfirmedOrders")
                .child(orderID).child("ItemList");
        fOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String foodName = snapshot.getKey();
                    int price = Integer.parseInt((String) snapshot.child("Price").getValue());
                    int quantity = Integer.parseInt((String) snapshot.child("Quantity").getValue());
                    orderList.add(new Food(foodName, price, quantity));
                }
                SummaryRecAdapter summaryRecAdapter = new SummaryRecAdapter(OrderPageDeliverer.this, orderList);
                summaryList.setAdapter(summaryRecAdapter);
                summaryList.setLayoutManager(new LinearLayoutManager(OrderPageDeliverer.this));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String error = DatabaseErrorHandler.handleError(databaseError);
                Toast.makeText(OrderPageDeliverer.this, error, Toast.LENGTH_LONG).show();
            }
        });

        mReachedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedOrders").child(orderID);
                Intent newIntent = new Intent(getApplicationContext(), OrderPageDeliverer.class);
                newIntent.putExtra("orderID", orderID);
                startActivity(newIntent);
                dbRef.child("Reached").setValue(true);
            }
        });
    }

    private void initFromDB() {
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedOrders").child(orderID);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTimeStamp.setText((String) dataSnapshot.child("Time").getValue());
                mDeliveryTime.setText((String) dataSnapshot.child("DeliveryTime").getValue());
                mOrderTotal.setText("$" + dataSnapshot.child("OrderCost").getValue());
                String delivererID = (String) dataSnapshot.child("Deliverer").getValue();
                String receiverID = (String) dataSnapshot.child("Receiver").getValue();
                boolean reached = dataSnapshot.child("Reached").getValue(boolean.class);
                boolean completed = dataSnapshot.child("Completed").getValue(boolean.class);
                String delAddress = dataSnapshot.child("Destination").getValue(String.class);
                Geocoder geocoder = new Geocoder(OrderPageDeliverer.this);
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocationName(delAddress, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() > 0) {
                    double latitude = addresses.get(0).getLatitude();
                    double longitude = addresses.get(0).getLongitude();
                    System.out.println(latitude + "long: " + longitude);
                    delLocation = new Location("providerNA");
                    delLocation.setLongitude(longitude);
                    delLocation.setLatitude(latitude);
                    System.out.println("del long " + longitude + "lat" + latitude);
                }


                if (completed) {
                    mStatus.setText("Order Complete");
                } else {
                    if (reached) {
                        mStatus.setText("Awaiting Confirmation");
                    } else {
                        if (delLocation.distanceTo(currentLoc) < 100) {
                            System.out.println("reached");
                            mReachedBtn.setVisibility(View.VISIBLE);
                        } else {
                            mStatus.setText("Please reach the destination before proceeding");
                        }
                    }
                }

                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String error = DatabaseErrorHandler.handleError(databaseError);
                Toast.makeText(OrderPageDeliverer.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCurrentLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.getFusedLocationProviderClient(OrderPageDeliverer.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(OrderPageDeliverer.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            currentLoc = location;


                            initFromDB();
                        }
                    }
                }, Looper.getMainLooper());
    }
}
