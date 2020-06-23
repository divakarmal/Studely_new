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

public class DelivererRecAdapter extends RecyclerView.Adapter<DelivererRecAdapter.DelivererViewHolder> {

    Context context;
    List<String> nameList;
    List<String> timeList;

    public DelivererRecAdapter(Context context, List<String> nameList, List<String> timeList) {
        this.context = context;
        this.nameList = nameList;
        this.timeList = timeList;

    }

    @NonNull
    @Override
    public DelivererViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.deliverer_row, parent, false);
        return new DelivererRecAdapter.DelivererViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DelivererViewHolder holder, int position) {
        holder.nameText.setText(nameList.get(position));
        holder.deliTimeText.setText(timeList.get(position));

    }

    @Override
    public int getItemCount() {
        return this.nameList.size();
    }

    public class DelivererViewHolder extends RecyclerView.ViewHolder {

        TextView nameText;
        TextView deliTimeText;

        public DelivererViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            deliTimeText = itemView.findViewById(R.id.deliTimeText);
        }
    }
}
