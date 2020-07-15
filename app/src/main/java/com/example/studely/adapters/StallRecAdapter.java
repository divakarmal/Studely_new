package com.example.studely.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.OrderFoodSelect;
import com.example.studely.R;

import java.util.List;

public class StallRecAdapter extends RecyclerView.Adapter<StallRecAdapter.StallRecViewHolder> {

    final String canteenID;
    final String destination;
    Context context;
    List<String> stallList;

    public StallRecAdapter(Context context, List<String> stallList, String canteenID, String destination) {
        this.context = context;
        this.stallList = stallList;
        this.canteenID = canteenID;
        this.destination = destination;
    }

    @NonNull
    @Override
    public StallRecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.stall_grid, parent, false);
        return new StallRecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StallRecViewHolder holder, int position) {
        final String stall = stallList.get(position);
        holder.stallName.setText(stall);

        holder.stallImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(context, OrderFoodSelect.class);
                newIntent.putExtra("canteenID", canteenID);
                newIntent.putExtra("stallID", stall);
                newIntent.putExtra("orderDestination", destination);
                context.startActivity(newIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.stallList.size();
    }

    protected class StallRecViewHolder extends RecyclerView.ViewHolder {

        TextView stallName;
        ImageView stallImage;

        public StallRecViewHolder(@NonNull View itemView) {
            super(itemView);
            stallName = itemView.findViewById(R.id.stallName);
            stallImage = itemView.findViewById(R.id.stallImage);
        }
    }
}
