package com.example.studely;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.studely.misc.DatabaseErrorHandler;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;


public class OrderEnterAddress extends BottomNavBar {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    ImageButton mNextBtn;
    FrameLayout loadingOverlay;
    Geocoder geocoder;
    AutocompleteSupportFragment autocompleteSupportFragment;
    String add;
    private ResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_enter_address);
        navBar(this.getApplicationContext());
        resultReceiver = new AddressResultReceiver(new Handler());
        mNextBtn = findViewById(R.id.NextBtn);
        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingOverlay.bringToFront();
        loadingOverlay.getParent().requestLayout();
        ((View) loadingOverlay.getParent()).invalidate();
        geocoder = new Geocoder(OrderEnterAddress.this);
        PlacesClient placesClient;
        Context context = getApplicationContext();
        final String API_KEY = context.getString(R.string.my_api_key);

        if (!Places.isInitialized()) {
            Places.initialize(OrderEnterAddress.this, API_KEY);
        }
        placesClient = Places.createClient(this);


        autocompleteSupportFragment =
                (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setCountry("SG");

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                add = place.getName();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("placeInfoError", "An error occurred " + status);
            }
        });


        findViewById(R.id.currentLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OrderEnterAddress.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION);
                } else {
                    getCurrentLocation();
                }
            }
        });

        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = database.getReference().child("users").child(currentUser);

        loadingOverlay.setVisibility(View.VISIBLE);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String priAdd = dataSnapshot.child("primary_address").getValue(String.class);
                findViewById(R.id.primaryAddress).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add = priAdd;
                        autocompleteSupportFragment.setText(priAdd);
                    }
                });
                loadingOverlay.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String error = DatabaseErrorHandler.handleError(databaseError);
                Toast.makeText(OrderEnterAddress.this, error, Toast.LENGTH_LONG).show();
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add == null) {
                    String error = "Address has not been entered";
                    Toast.makeText(OrderEnterAddress.this, error, Toast.LENGTH_LONG).show();
                    return;
                }
                Intent newIntent = new Intent(getApplicationContext(), OrderCanteenSelect.class);
                System.out.println(add);
                newIntent.putExtra("orderDestination", add);
                startActivity(newIntent);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getCurrentLocation() {

        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(OrderEnterAddress.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(OrderEnterAddress.this)
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
                            fetchAddressFromLatLong(location);
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void fetchAddressFromLatLong(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                add = resultData.getString(Constants.RESULT_DATA_KEY);
                autocompleteSupportFragment.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            } else {
                Toast.makeText(OrderEnterAddress.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_LONG).show();
            }
        }
    }
}
