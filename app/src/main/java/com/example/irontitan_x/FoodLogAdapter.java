package com.example.irontitan_x;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodLogAdapter extends RecyclerView.Adapter<FoodLogAdapter.FoodLogViewHolder> {

    private List<FoodLogItem> foodLogList;

    public FoodLogAdapter(List<FoodLogItem> foodLogList) {
        this.foodLogList = foodLogList;
    }

    @NonNull
    @Override
    public FoodLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_food_log, parent, false);
        return new FoodLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodLogViewHolder holder, int position) {
        FoodLogItem foodLogItem = foodLogList.get(position);
        holder.foodName.setText(foodLogItem.getName());
        holder.foodGrams.setText(String.valueOf(foodLogItem.getGrams()));
        holder.foodEnergy.setText(String.valueOf(foodLogItem.getEnergy()));
    }

    @Override
    public int getItemCount() {
        return foodLogList.size();
    }

    public static class FoodLogViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        TextView foodGrams;
        TextView foodEnergy;

        public FoodLogViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodGrams = itemView.findViewById(R.id.foodGrams);
            foodEnergy = itemView.findViewById(R.id.foodEnergy);
        }
    }
}
