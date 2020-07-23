package com.example.studely.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.OrderPageDeliverer;
import com.example.studely.OrderPageOrderer;
import com.example.studely.R;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderViewHolder> {

    Context context;
    List<String> orderIDs;
    List<String> destinations;
    List<Boolean> isOrderer;

    public MyOrderAdapter(Context context, List<String> orderIDs, List<String> destinations, List<Boolean> isOrderer) {
        this.context = context;
        this.orderIDs = orderIDs;
        this.destinations = destinations;
        this.isOrderer = isOrderer;
    }

    @NonNull
    @Override
    public MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_order_row, parent, false);
        return new MyOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderViewHolder holder, final int position) {
        holder.orderIDText.setText(destinations.get(position));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent;
                if (isOrderer.get(position)) {
                    newIntent = new Intent(context, OrderPageOrderer.class);
                } else {
                    newIntent = new Intent(context, OrderPageDeliverer.class);
                }
                newIntent.putExtra("orderID", orderIDs.get(position));
                context.startActivity(newIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.orderIDs.size();
    }

    public class MyOrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderIDText;
        ConstraintLayout mainLayout;

        public MyOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIDText = itemView.findViewById(R.id.orderIDText);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
