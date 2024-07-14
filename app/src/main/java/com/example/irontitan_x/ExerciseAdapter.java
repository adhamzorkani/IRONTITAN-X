package com.example.irontitan_x;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private final List<Exercise> exercises;
    private final OnRemoveExerciseClickListener onRemoveExerciseClickListener;

    public ExerciseAdapter(List<Exercise> exercises, OnRemoveExerciseClickListener onRemoveExerciseClickListener) {
        this.exercises = exercises;
        this.onRemoveExerciseClickListener = onRemoveExerciseClickListener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.exerciseNameTextView.setText(exercise.getName());
        holder.removeExerciseButton.setOnClickListener(v -> onRemoveExerciseClickListener.onRemoveExerciseClick(exercise));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseNameTextView;
        ImageButton removeExerciseButton;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseNameTextView = itemView.findViewById(R.id.exerciseNameTextView);
            removeExerciseButton = itemView.findViewById(R.id.removeExerciseButton);
        }
    }

    public interface OnRemoveExerciseClickListener {
        void onRemoveExerciseClick(Exercise exercise);
    }
}
