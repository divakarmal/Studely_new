package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.adapters.SummaryRecAdapter;
import com.example.studely.misc.Order;

public class OrderSummary extends BottomNavBar {

    RecyclerView summaryList;
    Button mConfirmButton;
    TextView orderTotal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        navBar(this.getApplicationContext());

        orderTotal = findViewById(R.id.totalOrderCost);
        summaryList = findViewById(R.id.summaryRecView);
        Bundle bundle = this.getIntent().getExtras();
        final Order order = (Order) bundle.getSerializable("orderObj");
        String cost = Integer.toString(order.calcOrderCost());
        orderTotal.setText("Total   $" + cost);

        SummaryRecAdapter summaryRecAdapter = new SummaryRecAdapter(this, order.getList());
        summaryList.setAdapter(summaryRecAdapter);
        summaryList.setLayoutManager(new LinearLayoutManager(this));

        mConfirmButton = findViewById(R.id.acceptBtn);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), OrderDelivererSelect.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderObj", order);
                newIntent.putExtras(bundle);
                startActivity(newIntent);
            }
        });
    }
}
