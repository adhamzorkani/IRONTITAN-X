package com.example.irontitan_x;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class add_workout extends AppCompatActivity {

    private Button backButton;
    private Button addButton;
    private RecyclerView videoRecyclerView;
    private VideoAdapter videoAdapter;
    private List<VideoItem> videoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        backButton = findViewById(R.id.backButton);
        addButton = findViewById(R.id.addButton);
        videoRecyclerView = findViewById(R.id.videoRecyclerView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous activity
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the main activity (or another target activity)
                Intent intent = new Intent(add_workout.this, workoutPlan.class);
                startActivity(intent);
            }
        });

        // Setup RecyclerView
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoItems = new ArrayList<>();
        videoItems.add(new VideoItem(R.drawable.video1, "Bicep Curls (Dumbbell or Barbell)"));
        // Add more items as needed

        videoAdapter = new VideoAdapter(videoItems);
        videoRecyclerView.setAdapter(videoAdapter);
    }
}
