package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderStallSelect extends AppCompatActivity {


    ArrayAdapter<String> stallAdapter;
    ListView stallList;
    List<String> stalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_stall_select);

        stallList = (ListView) findViewById(R.id.stallListView);
        final String canteenID = getIntent().getExtras().getString("canteenID");
        final String destination = getIntent().getExtras().getString("orderDestination");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fStallRef = database.getReference().child("canteens")
                                        .child(canteenID).child("StallList");

        fStallRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

    }
}
