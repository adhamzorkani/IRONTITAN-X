package com.example.irontitan_x;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.SetOptions;
import com.google.protobuf.NullValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodActivity extends AppCompatActivity {

    private Button addButton;
    private Button addWaterButton;
    private EditText searchBar;
    private EditText waterIntake;
    private RecyclerView recyclerView;
    private RecyclerView foodLogRecyclerView;
    private ImageButton homeButton;
    private ImageButton fitnessButton;
    private ImageButton foodButton;
    private ImageButton moreButton;
    private FoodAdapter foodAdapter;
    private FoodLogAdapter foodLogAdapter;
    private List<FoodItem> foodList = new ArrayList<>();
    private List<FoodLogItem> foodLogList = new ArrayList<>();
    private static final String TAG = "FoodActivity";
    private int userCaloriesInput;
    private int userWaterInput;
    private int waterGoal;
    private int caloriesGoal; // Assuming you want to show calories goal.
    private TextView caloriesRemaining;
    private TextView waterRemaining;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meal_tracking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        addButton = findViewById(R.id.addButton);
        addWaterButton = findViewById(R.id.addWaterButton);
        searchBar = findViewById(R.id.searchBar);
        waterIntake = findViewById(R.id.waterIntake);
        recyclerView = findViewById(R.id.recyclerView);
        foodLogRecyclerView = findViewById(R.id.foodLogRecyclerView);
        homeButton = findViewById(R.id.home_button);
        fitnessButton = findViewById(R.id.fitness_button);
        foodButton = findViewById(R.id.food_button);
        moreButton = findViewById(R.id.more_button);
        caloriesRemaining = findViewById(R.id.caloriesRemaining);
        waterRemaining = findViewById(R.id.waterRemaining);
        progressBar = findViewById(R.id.progressBar);
        foodButton.setBackgroundResource(R.drawable.bg_button);

        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodLogRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        foodAdapter = new FoodAdapter(foodList, foodLogList, this);
        foodLogAdapter = new FoodLogAdapter(foodLogList, this);

        recyclerView.setAdapter(foodAdapter);
        foodLogRecyclerView.setAdapter(foodLogAdapter);

        fetchUserData();
        fetchFoodData();
        fetchFoodLogData();


        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        addWaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String waterInputText = waterIntake.getText().toString();
                if (!waterInputText.isEmpty()) {
                    int waterIntakeAmount = Integer.parseInt(waterInputText);
                    addWaterIntake(waterIntakeAmount);
                    waterIntake.setText("");
                }
            }
        });

        setupNavigationButtons();
    }

    private void fetchUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            userCaloriesInput = document.getLong("calories_input").intValue();
                            caloriesGoal = document.getLong("calories_goal").intValue();

                            int remainingCalories = caloriesGoal - userCaloriesInput;
                            caloriesRemaining.setText(remainingCalories + " Calories remaining");

                            int progress = (int) (((double) userCaloriesInput / caloriesGoal) * 100);
                            progressBar.setProgress(progress);

                            userWaterInput = document.getLong("water_input").intValue();
                            waterGoal = document.getLong("water_goal").intValue()*1000;

                            int remainingWater = waterGoal - userWaterInput;
                            waterRemaining.setText(remainingWater + " mL water remaining");
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching user data", e));
    }

    private void fetchFoodData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("meals").document("oLbkMmtNodzGPeZ805ld")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            foodList.clear();
                            Map<String, Object> data = document.getData();
                            if (data != null) {
                                for (Map.Entry<String, Object> entry : data.entrySet()) {
                                    String name = entry.getKey();
                                    long calories = (long) entry.getValue();
                                    foodList.add(new FoodItem(name, (int) calories));
                                    Log.d(TAG, "Added: " + name + " with calories: " + calories);
                                }
                                foodAdapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "No data found");
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching data", e));
    }

    private void fetchFoodLogData() {
        if (user == null) return;

        firestore.collection("users").document(user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<Map<String, Object>> dbFoodLog = (List<Map<String, Object>>) document.get("foodLogList");
                            if (dbFoodLog != null) {
                                foodLogList.clear();
                                for (Map<String, Object> entry : dbFoodLog) {
                                    String name = (String) entry.get("name");
                                    long grams = (long) entry.get("grams");
                                    float energy = (long) entry.get("energy");
                                    energy = (energy/grams)*100;
                                    foodLogList.add(new FoodLogItem(name, (int) grams, (int) energy));
                                }
                                foodLogAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching food log data", e));
    }

    private void filter(String text) {
        List<FoodItem> filteredList = new ArrayList<>();
        for (FoodItem item : foodList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        foodAdapter = new FoodAdapter(filteredList, foodLogList, this);
        recyclerView.setAdapter(foodAdapter);
    }

    private void setupNavigationButtons() {
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodButton.setBackgroundResource(R.drawable.icon_bg_deafult);
                Intent intent = new Intent(FoodActivity.this, Home.class);
                startActivity(intent);
            }
        });

        fitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodButton.setBackgroundResource(R.drawable.icon_bg_deafult);
                Intent intent = new Intent(FoodActivity.this, workoutPlan.class);
                startActivity(intent);
            }
        });

        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // No need to start a new activity since we're already here
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodButton.setBackgroundResource(R.drawable.icon_bg_deafult);
                Intent intent = new Intent(FoodActivity.this, profile.class);
                startActivity(intent);
            }
        });
    }

    public void addFoodToLog(FoodItem foodItem, int grams) {
        FoodLogItem foodLogItem = new FoodLogItem(foodItem.getName(), grams, foodItem.getEnergy());
        foodLogList.add(foodLogItem);
        if (user != null) {
            Map<String, Object> db_food = new HashMap<>();
            db_food.put("foodLogList", foodLogList);
            firestore.collection("users").document(user.getUid())
                    .set(db_food, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Food log successfully updated!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating food log", e));
        }
        foodLogAdapter.notifyDataSetChanged();
        updateCaloriesRemaining();
    }

    public void updateCaloriesAfterRemoval(FoodLogItem removedItem) {
        //userCaloriesInput -= removedItem.getEnergy();
        if (user != null) {
            Map<String, Object> db_food = new HashMap<>();
            db_food.put("foodLogList", foodLogList);
            firestore.collection("users").document(user.getUid())
                    .set(db_food, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Food log successfully updated after removal!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error removing food log", e));
        }
        int totalCalories = 0;
        for (FoodLogItem item : foodLogList) {
            totalCalories += item.getEnergy();
        }
        int remainingCalories = caloriesGoal - totalCalories;
        caloriesRemaining.setText(remainingCalories + " Calories remaining");

        int progress = (int) (((double) totalCalories / caloriesGoal) * 100);
        progressBar.setProgress(progress);
        if (user != null) {
            Map<String, Object> db_calories = new HashMap<>();
            db_calories.put("calories_input", totalCalories);
            firestore.collection("users").document(user.getUid())
                    .set(db_calories, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Calories input successfully updated!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating calories input", e));
        }
    }

    private void addWaterIntake(int amount) {
        userWaterInput += amount;
        updateWaterRemaining();
        if (user != null) {
            Map<String, Object> db_water = new HashMap<>();
            db_water.put("water_input", userWaterInput);
            firestore.collection("users").document(user.getUid())
                    .set(db_water, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Water intake successfully updated!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating water intake", e));
        }
    }

    private void updateCaloriesRemaining() {
        int totalCalories = userCaloriesInput;
        for (FoodLogItem item : foodLogList) {
            totalCalories += item.getEnergy();
        }
        int remainingCalories = caloriesGoal - totalCalories;
        caloriesRemaining.setText(remainingCalories + " Calories remaining");

        int progress = (int) (((double) totalCalories / caloriesGoal) * 100);
        progressBar.setProgress(progress);

        if (user != null) {
            Map<String, Object> db_calories = new HashMap<>();
            db_calories.put("calories_input", totalCalories);
            firestore.collection("users").document(user.getUid())
                    .set(db_calories, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Calories input successfully updated!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating calories input", e));
        }
    }

    private void updateWaterRemaining() {
        int remainingWater = waterGoal - userWaterInput;
        waterRemaining.setText(remainingWater + " mL water remaining");

//        if (user != null) {
//            Map<String, Object> db_water = new HashMap<>();
//            db_water.put("water_input", userWaterInput);
//            firestore.collection("users").document(user.getUid())
//                    .set(db_water, SetOptions.merge())
//                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Water intake successfully updated!"))
//                    .addOnFailureListener(e -> Log.w(TAG, "Error updating water intake", e));
//        }
    }
}
