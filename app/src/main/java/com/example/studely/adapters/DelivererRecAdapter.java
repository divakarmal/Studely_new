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
    List<String[]> deliveryPostingsData;
    List<String> deliveryIDList;
    Order order;
    List<String> ratingList;

    public DelivererRecAdapter(Context context, List<String[]> deliveryPostingsData,
                               List<String> deliveryIDList, Order order, List<String> ratingList) {
        this.context = context;
        this.deliveryPostingsData = deliveryPostingsData;
        this.deliveryIDList = deliveryIDList;
        this.order = order;
        this.ratingList = ratingList;
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
        final String[] data = deliveryPostingsData.get(position);
        holder.nameText.setText(data[0]);
        holder.deliTimeText.setText(data[1]);
        holder.rating.setText(ratingList.get(position));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setDeliverer(data[2]);
                order.setDeliveryTime(data[1]);

                Intent newIntent = new Intent(context, OrderConfirm.class);
                newIntent.putExtra("deliveryPostingID", deliveryIDList.get(position));
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderObj", order);
                newIntent.putExtras(bundle);
                context.startActivity(newIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.deliveryIDList.size();
    }

    public class DelivererViewHolder extends RecyclerView.ViewHolder {

        TextView nameText;
        TextView deliTimeText;
        TextView rating;
        ConstraintLayout mainLayout;

        public DelivererViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            deliTimeText = itemView.findViewById(R.id.deliTimeText);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            rating = itemView.findViewById(R.id.ratingText);
        }
    }
}
