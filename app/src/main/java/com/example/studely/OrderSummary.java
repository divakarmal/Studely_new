package com.example.studely;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.SummaryRecAdapter;
import com.example.studely.classes.Food;
import com.example.studely.classes.Order;

public class OrderSummary extends AppCompatActivity {

    RecyclerView summaryList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        summaryList = findViewById(R.id.summaryRecView);
        Bundle bundle = this.getIntent().getExtras();
        Order order = (Order) bundle.getSerializable("orderObj");

        SummaryRecAdapter summaryRecAdapter = new SummaryRecAdapter(this, order.getList());
        summaryList.setAdapter(summaryRecAdapter);
        summaryList.setLayoutManager(new LinearLayoutManager(this));
    }
}
