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

import com.example.studely.DeliverConfirm;
import com.example.studely.R;

import java.util.List;

public class OrderRecAdapter extends RecyclerView.Adapter<OrderRecAdapter.OrderViewHolder> {

    Context context;
    List<String[]> orderPostingsData;
    List<String> orderIDList;

    public OrderRecAdapter(Context context, List<String[]> orderPostingsData, List<String> orderIDList) {
        this.context = context;
        this.orderPostingsData = orderPostingsData;
        this.orderIDList = orderIDList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_posting_row, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        final String[] data = orderPostingsData.get(position);
        holder.destText.setText(data[0]);
        holder.deliTimeText.setText(data[1]);
        String items = data[4];
        String cost = data[5];
        String itemCostText = "Items: " + items + ", Cost: $" + cost;
        holder.itemCost.setText(itemCostText);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(context, DeliverConfirm.class);
                newIntent.putExtra("orderPostingID", data[2]);
                newIntent.putExtra("canteenID", data[3]);
                context.startActivity(newIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.orderPostingsData.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView destText;
        TextView deliTimeText;
        TextView itemCost;
        ConstraintLayout mainLayout;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            destText = itemView.findViewById(R.id.locText);
            deliTimeText = itemView.findViewById(R.id.deliTimeText);
            itemCost = itemView.findViewById(R.id.itemCostText);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
