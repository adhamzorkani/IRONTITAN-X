package com.example.irontitan_x;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {
    private ImageButton homeButton;
    private ImageButton fitnessButton;
    private ImageButton foodButton;
    private ImageButton moreButton;
    ImageButton chatbotBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        homeButton = findViewById(R.id.home_button);
        fitnessButton = findViewById(R.id.fitness_button);
        foodButton = findViewById(R.id.food_button);
        moreButton = findViewById(R.id.more_button);
        chatbotBtn = findViewById(R.id.chatBotButton);
        chatbotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Assistant.class);
                startActivity(intent);
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Home activity
            }
        });

        fitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Fitness activity
                Intent intent = new Intent(Home.this, workoutPlan.class);
                startActivity(intent);
            }
        });

        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Food activity
                Intent intent = new Intent(Home.this, FoodActivity.class);
                startActivity(intent);
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the More activity
                Intent intent = new Intent(Home.this, profile.class);
                startActivity(intent);
            }
        });
    }
}