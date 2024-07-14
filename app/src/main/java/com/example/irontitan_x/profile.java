package com.example.irontitan_x;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class profile extends AppCompatActivity {

    private ImageButton homeButton;
    private ImageButton fitnessButton;
    private ImageButton foodButton;
    private ImageButton moreButton;
    private TextView profileButton;
    private TextView goalsButton;
    private TextView premiumButton;
    private TextView settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        homeButton = findViewById(R.id.home_button);
        fitnessButton = findViewById(R.id.fitness_button);
        foodButton = findViewById(R.id.food_button);
        moreButton = findViewById(R.id.more_button);
        moreButton.setBackgroundResource(R.drawable.bg_button);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreButton.setBackgroundResource(R.drawable.icon_bg_deafult);
                Intent intent = new Intent(profile.this, Home.class);
                startActivity(intent);
            }
        });

        fitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreButton.setBackgroundResource(R.drawable.icon_bg_deafult);
                Intent intent = new Intent(profile.this, workoutPlan.class);
                startActivity(intent);
            }
        });

        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreButton.setBackgroundResource(R.drawable.icon_bg_deafult);
                Intent intent = new Intent(profile.this, FoodActivity.class);
                startActivity(intent);
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        // Initialize TextViews
        profileButton = findViewById(R.id.profileButton);
        goalsButton = findViewById(R.id.goalsButton);
        premiumButton = findViewById(R.id.premiumButton);
        settingsButton = findViewById(R.id.settingsButton);

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(profile.this, profile_info.class);
            startActivity(intent);
        });

        goalsButton.setOnClickListener(v -> {
//            Intent intent = new Intent(profile.this, GoalsActivity.class);
//            startActivity(intent);
        });

        premiumButton.setOnClickListener(v -> {
            Toast.makeText(profile.this, "Upgrade to Premium clicked", Toast.LENGTH_SHORT).show();
        });

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(profile.this, settings.class);
            startActivity(intent);
        });

    }
}