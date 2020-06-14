package com.example.studely;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DeliverCanteenSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_canteen_select);
        setContentView(R.layout.activity_order_canteen_select);
        Spinner canteenSelect = findViewById(R.id.canteenSpinner);
        ArrayAdapter<String> canteenAdaptor = new ArrayAdapter<>(DeliverCanteenSelect.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.CanteenList));
        canteenAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        canteenSelect.setAdapter(canteenAdaptor);


    }
}
