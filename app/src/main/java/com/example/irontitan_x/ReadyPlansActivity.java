package com.example.irontitan_x;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadyPlansActivity extends AppCompatActivity {

    private Button addPlanButton;
    private RecyclerView plansRecyclerView;
    private RadioGroup toggleGroup;
    private RadioButton readyPlanButton;
    private RadioButton customizeButton;
    private ImageButton homeButton;
    private ImageButton fitnessButton;
    private ImageButton foodButton;
    private ImageButton moreButton;
    private ReadyPlansAdapter planAdapter;
    private List<ReadyPlans> plans;

    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_plans);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        addPlanButton = findViewById(R.id.addPlan);
        plansRecyclerView = findViewById(R.id.plansRecyclerView);
        toggleGroup = findViewById(R.id.toggle);
        readyPlanButton = findViewById(R.id.readyPlan);
        customizeButton = findViewById(R.id.customize);
        homeButton = findViewById(R.id.home_button);
        fitnessButton = findViewById(R.id.fitness_button);
        foodButton = findViewById(R.id.food_button);
        moreButton = findViewById(R.id.more_button);
        fitnessButton.setBackgroundResource(R.drawable.bg_button);

        addPlanButton.setOnClickListener(v -> {
            ReadyPlans selectedPlan = planAdapter.getSelectedPlan();
            if (selectedPlan != null) {
                addSelectedPlanToUserPlans(selectedPlan);
            } else {
                Toast.makeText(ReadyPlansActivity.this, "Please select a plan to add", Toast.LENGTH_SHORT).show();
            }
        });

        toggleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.readyPlan) {
                // This is the current activity, no action needed
            } else if (checkedId == R.id.customize) {
                Intent intent = new Intent(ReadyPlansActivity.this, workoutPlan.class);
                startActivity(intent);
            }
        });

        homeButton.setOnClickListener(v -> {
            // Navigate to the Home activity
            fitnessButton.setBackgroundResource(R.drawable.icon_bg_deafult);
            Intent intent = new Intent(ReadyPlansActivity.this, Home.class);
            startActivity(intent);
        });

        fitnessButton.setOnClickListener(v -> {
            // Stay in the current activity
        });

        foodButton.setOnClickListener(v -> {
            // Navigate to the Food activity
            fitnessButton.setBackgroundResource(R.drawable.icon_bg_deafult);
            Intent intent = new Intent(ReadyPlansActivity.this, FoodActivity.class);
            startActivity(intent);
        });

        moreButton.setOnClickListener(v -> {
            // Navigate to the More activity
            fitnessButton.setBackgroundResource(R.drawable.icon_bg_deafult);
            Intent intent = new Intent(ReadyPlansActivity.this, profile.class);
            startActivity(intent);
        });

        // Setup RecyclerView
        plansRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        plans = new ArrayList<>();
        planAdapter = new ReadyPlansAdapter(plans, false, true, false, null);  // Pass false to hide add/remove buttons
        plansRecyclerView.setAdapter(planAdapter);

        // Fetch ready plans from Firestore
        fetchReadyPlansFromFirestore();
    }

    private void fetchReadyPlansFromFirestore() {
        db.collection("ReadyWorkoutPlans").document("readyPlans")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        parseReadyPlans(documentSnapshot);
                    } else {
                        Toast.makeText(this, "No ready plans found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch ready plans.", Toast.LENGTH_SHORT).show());
    }

    private void parseReadyPlans(DocumentSnapshot documentSnapshot) {
        Map<String, Object> data = documentSnapshot.getData();
        if (data != null) {
            // The root map should contain the "plans" key
            Map<String, Object> plansData = (Map<String, Object>) data.get("plans");
            if (plansData != null) {
                for (Map.Entry<String, Object> planEntry : plansData.entrySet()) {
                    String planName = planEntry.getKey();
                    Map<String, Object> planDetails = (Map<String, Object>) planEntry.getValue();
                    List<Day> days = new ArrayList<>();

                    for (Map.Entry<String, Object> dayEntry : planDetails.entrySet()) {
                        String dayName = dayEntry.getKey();
                        List<Map<String, String>> exercisesData = (List<Map<String, String>>) dayEntry.getValue();
                        List<Exercise> exercises = new ArrayList<>();

                        for (Map<String, String> exerciseData : exercisesData) {
                            String exerciseName = exerciseData.get("name");
                            String exerciseUrl = exerciseData.get("url");
                            exercises.add(new Exercise(exerciseName, exerciseUrl));
                        }

                        days.add(new Day(dayName, exercises));
                    }

                    plans.add(new ReadyPlans(planName, days));
                }

                planAdapter.notifyDataSetChanged();
            }
        }
    }

    private void addSelectedPlanToUserPlans(ReadyPlans selectedPlan) {
        if (user != null) {
            db.collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Map<String, Object> existingPlans = (Map<String, Object>) documentSnapshot.get("workout_plans");
                        if (existingPlans == null) {
                            existingPlans = new HashMap<>();
                        }

                        // Check if the plan name already exists
                        if (existingPlans.containsKey(selectedPlan.getName())) {
                            Toast.makeText(this, "A plan with this name already exists. Please choose a different plan.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Prepare the new plan to be added
                        Map<String, Object> planMap = new HashMap<>();
                        Map<String, Object> validDaysMap = new HashMap<>();
                        int dayCount = 1;

                        for (Day day : selectedPlan.getDays()) {
                            if (!day.getExercises().isEmpty()) {
                                List<Map<String, String>> exercises = new ArrayList<>();
                                for (Exercise exercise : day.getExercises()) {
                                    Map<String, String> exerciseMap = new HashMap<>();
                                    exerciseMap.put("name", exercise.getName());
                                    exerciseMap.put("url", exercise.getUrl());
                                    exercises.add(exerciseMap);
                                }
                                validDaysMap.put("Day " + dayCount, exercises);
                                dayCount++;
                            }
                        }

                        if (validDaysMap.isEmpty()) {
                            Toast.makeText(this, "Selected plan has no exercises.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        planMap.put(selectedPlan.getName(), validDaysMap);
                        existingPlans.putAll(planMap);

                        db.collection("users")
                                .document(user.getUid())
                                .update("workout_plans", existingPlans)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(ReadyPlansActivity.this, "Plan added successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ReadyPlansActivity.this, ReadyPlansActivity.class);
                                    startActivity(intent);
                                })
                                .addOnFailureListener(e -> Toast.makeText(ReadyPlansActivity.this, "Failed to add plan", Toast.LENGTH_SHORT).show());
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to retrieve existing plans", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}
