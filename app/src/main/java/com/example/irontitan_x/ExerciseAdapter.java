package com.example.irontitan_x;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    private final boolean isEditable;
    private final Context context;

    public ExerciseAdapter(List<Exercise> exercises, boolean isEditable, Context context) {
        this.exercises = exercises;
        this.isEditable = isEditable;
        this.context = context;
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

        holder.itemView.setOnClickListener(v -> {
            String url = exercise.getUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        });

        if (isEditable) {
            holder.removeExerciseButton.setVisibility(View.VISIBLE);
            holder.removeExerciseButton.setOnClickListener(v -> {
                exercises.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, exercises.size());
            });
        } else {
            holder.removeExerciseButton.setVisibility(View.GONE);
        }
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
}
