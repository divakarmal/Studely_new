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

import com.example.studely.DeliverOrderSelect;
import com.example.studely.ListingPage;
import com.example.studely.OrderStallSelect;
import com.example.studely.R;

import java.util.List;

public class MyListingsAdapter extends RecyclerView.Adapter<MyListingsAdapter.MyListingsViewHolder> {

    Context context;
    List<String> locationList;
    List<String> timeList;
    List<String> postingIDList;
    Boolean isOrder;

    public MyListingsAdapter(Context context, List<String> timeList, List<String> locationList, List<String> postingIDList, Boolean order) {
        this.timeList = timeList;
        this.locationList = locationList;
        this.context = context;
        this.postingIDList = postingIDList;
        this.isOrder = order;
    }

    @NonNull
    @Override
    public MyListingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listing_row, parent, false);
        return new MyListingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyListingsViewHolder holder, final int position) {
        String destination = locationList.get(position);
        String time = timeList.get(position);
        holder.locTest.setText(destination);
        holder.deliTimeText.setText(time);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(context, ListingPage.class);
                newIntent.putExtra("postingID", postingIDList.get(position));
                newIntent.putExtra("isOrder", isOrder);
                context.startActivity(newIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.locationList.size();
    }


    public class MyListingsViewHolder extends RecyclerView.ViewHolder {

        TextView locTest, deliTimeText;
        ConstraintLayout mainLayout;

        public MyListingsViewHolder(@NonNull View itemView) {
            super(itemView);
            locTest = itemView.findViewById(R.id.locText);
            deliTimeText = itemView.findViewById(R.id.deliTimeText);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
