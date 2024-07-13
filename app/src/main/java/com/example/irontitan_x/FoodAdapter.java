package com.example.irontitan_x;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodItem> foodList;
    private List<FoodLogItem> foodLogList;
    private FoodActivity activity;

    public FoodAdapter(List<FoodItem> foodList, List<FoodLogItem> foodLogList, FoodActivity activity) {
        this.foodList = foodList;
        this.foodLogList = foodLogList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodList.get(position);
        holder.foodName.setText(foodItem.getName());

        holder.addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String grams = holder.foodGrams.getText().toString().trim();
                if (!grams.isEmpty()) {
                    activity.addFoodToLog(foodItem, Integer.parseInt(grams));
                    holder.foodGrams.setText("");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        EditText foodGrams;
        Button addFoodButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodGrams = itemView.findViewById(R.id.foodGrams);
            addFoodButton = itemView.findViewById(R.id.addFoodButton);
        }
    }
}
