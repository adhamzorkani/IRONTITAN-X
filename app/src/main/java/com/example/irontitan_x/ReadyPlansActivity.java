package com.example.irontitan_x;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ReadyPlansActivity extends AppCompatActivity {

    private Button createPlanButton;
    private RecyclerView videoRecyclerView;
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
        setContentView(R.layout.activity_ready_plans); // Make sure this matches the layout file name

        createPlanButton = findViewById(R.id.createPlan);
        videoRecyclerView = findViewById(R.id.videoRecyclerView);
        toggleGroup = findViewById(R.id.toggle);
        readyPlanButton = findViewById(R.id.readyPlan);
        customizeButton = findViewById(R.id.customize);
        homeButton = findViewById(R.id.home_button);
        fitnessButton = findViewById(R.id.fitness_button);
        foodButton = findViewById(R.id.food_button);
        moreButton = findViewById(R.id.more_button);

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
                    // This is the current activity, no action needed
                } else if (checkedId == R.id.customize) {
                    // Navigate to the Customize activity
                    Intent intent = new Intent(ReadyPlansActivity.this, workoutPlan.class);
                    startActivity(intent);
                }
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Home activity
                Intent intent = new Intent(ReadyPlansActivity.this, Home.class);
                startActivity(intent);
            }
        });

        fitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Food activity
                Intent intent = new Intent(ReadyPlansActivity.this, FoodActivity.class);
                startActivity(intent);
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the More activity
                Intent intent = new Intent(ReadyPlansActivity.this, profile.class);
                startActivity(intent);
            }
        });

        // TODO: Setup RecyclerView adapter and data to ensure lists are clickable
    }
}
