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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodActivity extends AppCompatActivity {

    private Button addButton;
    private EditText searchBar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_tracking);


        addButton = findViewById(R.id.addButton);
        searchBar = findViewById(R.id.searchBar);
        recyclerView = findViewById(R.id.recyclerView);
        foodLogRecyclerView = findViewById(R.id.foodLogRecyclerView);
        homeButton = findViewById(R.id.home_button);
        fitnessButton = findViewById(R.id.fitness_button);
        foodButton = findViewById(R.id.food_button);
        moreButton = findViewById(R.id.more_button);
        foodButton.setBackgroundResource(R.drawable.bg_button);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodLogRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        foodAdapter = new FoodAdapter(foodList, foodLogList, this);
        foodLogAdapter = new FoodLogAdapter(foodLogList);

        recyclerView.setAdapter(foodAdapter);
        foodLogRecyclerView.setAdapter(foodLogAdapter);

//        if (currentUser != null) {
//            fetchFoodData();
//        } else {
//            Log.d(TAG, "User is not authenticated");
//            // Handle user not being authenticated (e.g., redirect to login)
//        }
        fetchFoodData();

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

        // Set up navigation buttons (home, fitness, food, more)
        setupNavigationButtons();
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
                                    long energy = (long) entry.getValue();
                                    foodList.add(new FoodItem(name, (int) energy));
                                    Log.d(TAG, "Added: " + name + " with energy: " + energy);
                                }
                                foodAdapter.notifyDataSetChanged();
                            }
                            else {
                                Log.d(TAG, "No such document");
                            }
                        }
                        else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                }) .addOnFailureListener(e -> Log.e(TAG, "Error fetching data", e));
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
        foodLogList.add(new FoodLogItem(foodItem.getName(), grams, foodItem.getEnergy()));
        foodLogAdapter.notifyDataSetChanged();
    }
}
