package com.example.studely.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.OrderConfirm;
import com.example.studely.R;
import com.example.studely.misc.Order;

import java.util.List;

public class DelivererRecAdapter extends RecyclerView.Adapter<DelivererRecAdapter.DelivererViewHolder> {

    Context context;
    List<String> nameList;
    List<String> timeList;
    List<String> delivererIDList;
    List<String> deliveryPostingIDList;
    Order order;

    public DelivererRecAdapter(Context context, List<String> nameList, List<String> timeList,
                               List<String> delivererIDList, Order order, List<String> deliveryPostingIDList) {
        this.context = context;
        this.nameList = nameList;
        this.timeList = timeList;
        this.delivererIDList = delivererIDList;
        this.order = order;
        this.deliveryPostingIDList = deliveryPostingIDList;
    }

    @NonNull
    @Override
    public DelivererViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.deliverer_posting_row, parent, false);
        return new DelivererRecAdapter.DelivererViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DelivererViewHolder holder, final int position) {
        holder.nameText.setText(nameList.get(position));
        holder.deliTimeText.setText(timeList.get(position));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setDeliverer(delivererIDList.get(position));
                order.setDeliveryTime(timeList.get(position));

                Intent newIntent = new Intent(context, OrderConfirm.class);
                newIntent.putExtra("deliveryPostingID", deliveryPostingIDList.get(position));
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderObj", order);
                newIntent.putExtras(bundle);
                context.startActivity(newIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.nameList.size();
    }

    public class DelivererViewHolder extends RecyclerView.ViewHolder {

        TextView nameText;
        TextView deliTimeText;
        ConstraintLayout mainLayout;

        public DelivererViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            deliTimeText = itemView.findViewById(R.id.deliTimeText);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
