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
    ListView StallList;
    List<String> stallList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_stall_select);

        StallList = (ListView) findViewById(R.id.stallListView);
        final String canteenID = getIntent().getExtras().getString("canteenID");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fStallRef = database.getReference().child("canteens")
                                        .child(canteenID).child("StallList");

        fStallRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stallList = new ArrayList<String>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String stallName = snapshot.getKey();
                    if (stallName != null) {
                        stallList.add(stallName);
                    }
                }

                stallAdapter = new ArrayAdapter<>(OrderStallSelect.this,
                        android.R.layout.simple_list_item_1, stallList);
                stallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                StallList.setAdapter(stallAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        StallList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(getApplicationContext(), OrderFoodSelect.class);
                String choice = stallList.get(position);
                newIntent.putExtra("canteenID", canteenID);
                newIntent.putExtra("stallID", choice);
                startActivity(newIntent);
                Toast.makeText(OrderStallSelect.this,stallList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
