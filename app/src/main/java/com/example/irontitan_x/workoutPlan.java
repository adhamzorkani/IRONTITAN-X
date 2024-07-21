package com.example.irontitan_x;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class workoutPlan extends AppCompatActivity {

    private Button addDayButton;
    private Button createPlanButton;
    private RecyclerView daysRecyclerView;
    private RadioGroup toggleGroup;
    private RadioButton readyPlanButton;
    private RadioButton customizeButton;
    private ImageButton homeButton;
    private ImageButton fitnessButton;
    private ImageButton foodButton;
    private ImageButton moreButton;
    private EditText planNameEditText;

    private DayAdapter daysAdapter;
    private List<Day> daysList;

    private static final int ADD_EXERCISE_REQUEST_CODE = 1;
    private static final int MAX_DAYS = 6;
    private Day selectedDay;

    private FirebaseFirestore db;
    private FirebaseUser user;

    private boolean isToggleListenerEnabled = true;

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

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        addDayButton = findViewById(R.id.addDayButton);
        createPlanButton = findViewById(R.id.createPlanButton);
        daysRecyclerView = findViewById(R.id.daysRecyclerView);
        toggleGroup = findViewById(R.id.toggle);
        readyPlanButton = findViewById(R.id.readyPlan);
        customizeButton = findViewById(R.id.customize);
        homeButton = findViewById(R.id.home_button);
        fitnessButton = findViewById(R.id.fitness_button);
        foodButton = findViewById(R.id.food_button);
        moreButton = findViewById(R.id.more_button);
        planNameEditText = findViewById(R.id.planName);
        fitnessButton.setBackgroundResource(R.drawable.bg_button);

        daysList = new ArrayList<>();
        daysAdapter = new DayAdapter(daysList, this::onAddExerciseClicked, true);
        daysRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        daysRecyclerView.setAdapter(daysAdapter);

        addDayButton.setOnClickListener(v -> addDay());

        createPlanButton.setOnClickListener(v -> createPlan());

        toggleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (!isToggleListenerEnabled) {
                return;
            }
            if (checkedId == R.id.readyPlan) {
                checkIfPremiumAndNavigate();
            } else if (checkedId == R.id.customize) {
                // Already in the customize activity, no action needed
            }
        });

        homeButton.setOnClickListener(v -> {
            // Navigate to the Home activity
            fitnessButton.setBackgroundResource(R.drawable.icon_bg_deafult);
            Intent intent = new Intent(workoutPlan.this, Home.class);
            startActivity(intent);
        });

        fitnessButton.setOnClickListener(v -> {
            // Navigate to the Fitness activity
        });

        foodButton.setOnClickListener(v -> {
            // Navigate to the Food activity
            fitnessButton.setBackgroundResource(R.drawable.bg_button);
            Intent intent = new Intent(workoutPlan.this, FoodActivity.class);
            startActivity(intent);
        });

        moreButton.setOnClickListener(v -> {
            // Navigate to the More activity
            fitnessButton.setBackgroundResource(R.drawable.bg_button);
            Intent intent = new Intent(workoutPlan.this, profile.class);
            startActivity(intent);
        });
    }

    private void addDay() {
        if (daysList.size() >= MAX_DAYS) {
            Toast.makeText(this, "You can only add up to 6 days.", Toast.LENGTH_SHORT).show();
            return;
        }
        int dayNumber = daysList.size() + 1;
        Day newDay = new Day("Day " + dayNumber, new ArrayList<>());
        daysList.add(newDay);
        daysAdapter.notifyItemInserted(daysList.size() - 1);
    }

    private void createPlan() {
        String planName = planNameEditText.getText().toString().trim();
        if (planName.isEmpty()) {
            planNameEditText.setError("Please enter a plan name");
            return;
        }

        Map<String, Object> validDaysMap = new HashMap<>();
        int dayCount = 1;

        for (Day day : daysList) {
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
            Toast.makeText(this, "Please add at least one exercise to the plan.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> newPlan = new HashMap<>();
        newPlan.put(planName, validDaysMap);

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
                        if (existingPlans.containsKey(planName)) {
                            Toast.makeText(this, "A plan with this name already exists. Please choose a different name.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        existingPlans.putAll(newPlan);

                        db.collection("users")
                                .document(user.getUid())
                                .update("workout_plans", existingPlans)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Plan created successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(workoutPlan.this, workoutPlan.class);
                                    startActivity(intent);
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Failed to create plan", Toast.LENGTH_SHORT).show());
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to retrieve existing plans", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void onAddExerciseClicked(Day day) {
        selectedDay = day;
        Intent intent = new Intent(this, add_workout.class);
        startActivityForResult(intent, ADD_EXERCISE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EXERCISE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<VideoItem> selectedExercises = data.getParcelableArrayListExtra("selected_exercises");
            if (selectedExercises != null) {
                for (VideoItem item : selectedExercises) {
                    Exercise newExercise = new Exercise(item.getTitle(), item.getVideoUrl());
                    selectedDay.addExercise(newExercise);
                }
                daysAdapter.notifyDataSetChanged();
            }
        }
    }

    private void checkIfPremiumAndNavigate() {
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String currentPlan = documentSnapshot.getString("plan");
                            if ("premium".equals(currentPlan)) {
                                Intent intent = new Intent(workoutPlan.this, ReadyPlansActivity.class);
                                startActivity(intent);
                            } else {
                                showUpgradeDialog();
                            }
                        } else {
                            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUpgradeDialog() {
        isToggleListenerEnabled = false;
        new AlertDialog.Builder(this)
                .setTitle("Upgrade to Premium")
                .setMessage("You need to be a premium member to access ready plans. Would you like to upgrade?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upgradeToPremiumAndNavigate();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        toggleGroup.check(R.id.customize);
                        isToggleListenerEnabled = true;
                    }
                })
                .create()
                .show();
    }

    private void upgradeToPremiumAndNavigate() {
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .update("plan", "premium")
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(workoutPlan.this, "Successfully upgraded to premium", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(workoutPlan.this, ReadyPlansActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(workoutPlan.this, "Failed to upgrade to premium", Toast.LENGTH_SHORT).show();
                        isToggleListenerEnabled = true;
                    });
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            isToggleListenerEnabled = true;
        }
    }
}
