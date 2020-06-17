package com.example.studely;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studely.classes.Food;
import com.example.studely.classes.Order;

public class OrderSummary extends AppCompatActivity {

    ListView itemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemList = findViewById(R.id.itemListView);
        Bundle bundle = this.getIntent().getExtras();
        Order order = (Order) bundle.getSerializable("orderObj");

        ArrayAdapter<Food> itemAdapter = new ArrayAdapter<>(OrderSummary.this,
                android.R.layout.simple_list_item_1, order.getList());
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemList.setAdapter(itemAdapter);
    }
}
