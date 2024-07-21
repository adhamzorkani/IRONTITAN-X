package com.example.irontitan_x;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReadyPlansAdapter extends RecyclerView.Adapter<ReadyPlansAdapter.PlanViewHolder> {

    private final List<ReadyPlans> plans;
    private final boolean isEditable;
    private final boolean isSelectable;
    private final boolean showRemoveButton;
    private final OnPlanRemoveListener onPlanRemoveListener; // Listener for removing plans
    private int selectedPosition = RecyclerView.NO_POSITION;

    public ReadyPlansAdapter(List<ReadyPlans> plans, boolean isEditable, boolean isSelectable, boolean showRemoveButton, OnPlanRemoveListener onPlanRemoveListener) {
        this.plans = plans;
        this.isEditable = isEditable;
        this.isSelectable = isSelectable;
        this.showRemoveButton = showRemoveButton;
        this.onPlanRemoveListener = onPlanRemoveListener;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ready_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        ReadyPlans plan = plans.get(position);
        holder.planNameTextView.setText(plan.getName());

        holder.daysRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        DayAdapter dayAdapter = new DayAdapter(plan.getDays(), null, isEditable);
        holder.daysRecyclerView.setAdapter(dayAdapter);

        if (isSelectable) {
            holder.itemView.setBackgroundColor(selectedPosition == position ? Color.RED : Color.TRANSPARENT);

            holder.itemView.setOnClickListener(v -> {
                notifyItemChanged(selectedPosition);
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedPosition);
            });
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            holder.itemView.setOnClickListener(null);
        }

        if (showRemoveButton) {
            holder.removePlanButton.setVisibility(View.VISIBLE);
            holder.removePlanButton.setOnClickListener(v -> onPlanRemoveListener.onPlanRemove(plan.getName(), position));
        } else {
            holder.removePlanButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public ReadyPlans getSelectedPlan() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            return plans.get(selectedPosition);
        }
        return null;
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView planNameTextView;
        RecyclerView daysRecyclerView;
        ImageButton removePlanButton;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            planNameTextView = itemView.findViewById(R.id.planNameTextView);
            daysRecyclerView = itemView.findViewById(R.id.daysRecyclerView);
            removePlanButton = itemView.findViewById(R.id.removePlanButton);
        }
    }

    // Interface for removing plans
    public interface OnPlanRemoveListener {
        void onPlanRemove(String planName, int position);
    }
}
