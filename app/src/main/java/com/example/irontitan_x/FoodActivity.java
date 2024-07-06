package com.example.irontitan_x;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class FoodActivity extends AppCompatActivity {

    private Button addButton;
    private EditText searchBar;
    private RecyclerView recyclerView;
    private ImageButton homeButton;
    private ImageButton fitnessButton;
    private ImageButton foodButton;
    private ImageButton moreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_tracking); // Make sure this matches the layout file name

        addButton = findViewById(R.id.addButton);
        searchBar = findViewById(R.id.searchBar);
        recyclerView = findViewById(R.id.recyclerView);
        homeButton = findViewById(R.id.home_button);
        fitnessButton = findViewById(R.id.fitness_button);
        foodButton = findViewById(R.id.food_button);
        moreButton = findViewById(R.id.more_button);
        foodButton.setBackgroundResource(R.drawable.bg_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle log meal entry button click
                // TODO: Implement log meal entry logic
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Home activity
                foodButton.setBackgroundResource(R.drawable.icon_bg_deafult);
                Intent intent = new Intent(FoodActivity.this, Home.class);
                startActivity(intent);
            }
        });

        fitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Fitness activity
                foodButton.setBackgroundResource(R.drawable.icon_bg_deafult);
                Intent intent = new Intent(FoodActivity.this, workoutPlan.class);
                startActivity(intent);
            }
        });

        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Food activity
                // No need to start a new activity since we're already here
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the More activity
                foodButton.setBackgroundResource(R.drawable.icon_bg_deafult);
                Intent intent = new Intent(FoodActivity.this, profile.class);
                startActivity(intent);
            }
        });

        // TODO: Setup RecyclerView adapter and data to ensure lists are clickable
    }
}
