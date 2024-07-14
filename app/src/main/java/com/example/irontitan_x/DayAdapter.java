package com.example.irontitan_x;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    private final List<Day> days;
    private final OnAddExerciseClickListener onAddExerciseClickListener;

    public DayAdapter(List<Day> days, OnAddExerciseClickListener onAddExerciseClickListener) {
        this.days = days;
        this.onAddExerciseClickListener = onAddExerciseClickListener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Day day = days.get(position);
        holder.dayNameTextView.setText(day.getName());
        holder.addExerciseButton.setOnClickListener(v -> onAddExerciseClickListener.onAddExerciseClick(day));

        holder.exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        ExerciseAdapter exercisesAdapter = new ExerciseAdapter(day.getExercises(), exercise -> {
            day.removeExercise(exercise);
            notifyItemChanged(position);
        });
        holder.exercisesRecyclerView.setAdapter(exercisesAdapter);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView dayNameTextView;
        RecyclerView exercisesRecyclerView;
        ImageButton addExerciseButton;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            dayNameTextView = itemView.findViewById(R.id.dayNameTextView);
            exercisesRecyclerView = itemView.findViewById(R.id.exercisesRecyclerView);
            addExerciseButton = itemView.findViewById(R.id.addExerciseButton);
        }
    }

    public interface OnAddExerciseClickListener {
        void onAddExerciseClick(Day day);
    }
}
