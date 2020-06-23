package com.example.studely.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        String[] data = orderPostingsData.get(position);
        holder.destText.setText(data[0]);
        holder.deliTimeText.setText(data[1]);
    }

    @Override
    public int getItemCount() {
        return this.orderPostingsData.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView destText;
        TextView deliTimeText;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            destText = itemView.findViewById(R.id.destText);
            deliTimeText = itemView.findViewById(R.id.deliTimeText);
        }
    }
}
