package com.example.studely;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class OrderCanteenSelect extends AppCompatActivity {

    Spinner canteenSpinner;
    ArrayAdapter<String> canteenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_canteen_select);

        canteenSpinner = (Spinner) findViewById(R.id.canteenSpinner);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fDatabaseRoot = database.getReference();



        fDatabaseRoot.child("canteens").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> canteenList = new ArrayList<String>();

                for (DataSnapshot addressSnapshot: dataSnapshot.getChildren()) {
                    String canteenName = addressSnapshot.child("CanteenName").getValue(String.class);
                    if (canteenName!=null){
                        canteenList.add(canteenName);
                    }
                }


                canteenAdapter = new ArrayAdapter<String>(OrderCanteenSelect.this,
                        android.R.layout.simple_list_item_1, canteenList);
                canteenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                canteenSpinner.setAdapter(canteenAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
