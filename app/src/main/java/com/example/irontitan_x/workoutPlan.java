package com.example.irontitan_x;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class workoutPlan extends AppCompatActivity {

    private Button addButton;
    private Button createPlanButton;
    private RecyclerView recyclerView;
    private RadioGroup toggleGroup;
    private RadioButton readyPlanButton;
    private RadioButton customizeButton;
    private ImageButton homeButton;
    private ImageButton fitnessButton;
    private ImageButton foodButton;
    private ImageButton moreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_plans);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addButton = findViewById(R.id.add_Button);
        createPlanButton = findViewById(R.id.createPlan);
        recyclerView = findViewById(R.id.recyclerView);
        toggleGroup = findViewById(R.id.toggle);
        readyPlanButton = findViewById(R.id.readyPlan);
        customizeButton = findViewById(R.id.customize);
        homeButton = findViewById(R.id.home_button);
        fitnessButton = findViewById(R.id.fitness_button);
        foodButton = findViewById(R.id.food_button);
        moreButton = findViewById(R.id.more_button);
        fitnessButton.setBackgroundResource(R.drawable.bg_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the exercise selection activity
                Intent intent = new Intent(workoutPlan.this, add_workout.class);
                startActivity(intent);
            }
        });

        createPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle create plan button click
                // TODO: Implement create plan logic
            }
        });

        toggleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.readyPlan) {
                    // Navigate to the Ready Plans activity
                    Intent intent = new Intent(workoutPlan.this, ReadyPlansActivity.class);
                    startActivity(intent);
                } else if (checkedId == R.id.customize) {
                    // Navigate to the Customize activity (if different from current)
                    // Intent intent = new Intent(WorkoutPlanActivity.this, CustomizeActivity.class);
                    // startActivity(intent);
                }
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Home activity
                fitnessButton.setBackgroundResource(R.drawable.icon_bg_deafult);
                Intent intent = new Intent(workoutPlan.this, Home.class);
                startActivity(intent);
            }
        });

        fitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Fitness activity
            }
        });

        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Food activity
                fitnessButton.setBackgroundResource(R.drawable.bg_button);
                Intent intent = new Intent(workoutPlan.this, FoodActivity.class);
                startActivity(intent);
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the More activity
                fitnessButton.setBackgroundResource(R.drawable.bg_button);
                Intent intent = new Intent(workoutPlan.this, profile.class);
                startActivity(intent);
            }
        });

        // TODO: Setup RecyclerView adapter and data
    }
}
