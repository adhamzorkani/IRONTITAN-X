package com.example.irontitan_x;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Home extends AppCompatActivity {
    ImageButton homeButton;
    ImageButton fitnessButton;
    ImageButton foodButton;
    ImageButton moreButton;
    ImageButton nearByGymsBtn;

    TextView streakTV, waterIntakeTV, remainingCaloriesTV;
    ProgressBar progressBar;
    TableLayout caloriesContainer;

    User userObj;

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

        streakTV = findViewById(R.id.streakText);
        waterIntakeTV = findViewById(R.id.waterText);
        remainingCaloriesTV = findViewById(R.id.text_remaining);
        homeButton = findViewById(R.id.home_button);
        fitnessButton = findViewById(R.id.fitness_button);
        foodButton = findViewById(R.id.food_button);
        moreButton = findViewById(R.id.more_button);
        homeButton.setBackgroundResource(R.drawable.bg_button);
        progressBar = findViewById(R.id.progressBar);
        caloriesContainer = findViewById(R.id.caloriesContainer);

        nearByGymsBtn = findViewById(R.id.locationButton);
        nearByGymsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, NearbyGymsActivity.class);
            startActivity(intent);
        });

        fitnessButton.setOnClickListener(v -> {
            homeButton.setBackgroundResource(R.drawable.icon_bg_deafult);
            Intent intent = new Intent(Home.this, workoutPlan.class);
            startActivity(intent);
        });

        foodButton.setOnClickListener(v -> {
            homeButton.setBackgroundResource(R.drawable.icon_bg_deafult);
            Intent intent = new Intent(Home.this, FoodActivity.class);
            startActivity(intent);
        });

        moreButton.setOnClickListener(v -> {
            homeButton.setBackgroundResource(R.drawable.icon_bg_deafult);
            Intent intent = new Intent(Home.this, profile.class);
            startActivity(intent);
        });

        caloriesContainer.setOnClickListener(v -> {
            homeButton.setBackgroundResource(R.drawable.icon_bg_deafult);
            Intent intent = new Intent(Home.this, FoodActivity.class);
            startActivity(intent);
        });

        fetchUserData();
    }

    private void fetchUserData() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            firestore.collection("users").document(firebaseUser.getUid()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                if (user != null) {
                                    userObj = user;
                                    updateUIWithUserData(user);
                                } else {
                                    showToast("Failed to parse user data.");
                                }
                            } else {
                                showToast("There is no document for the current user.");
                            }
                        } else {
                            showToast(Objects.requireNonNull(task.getException()).toString());
                            redirectToLogin();
                        }
                    });
        } else {
            redirectToLogin();
        }
    }

    private void updateUIWithUserData(User user) {
        streakTV.setText(String.valueOf(user.getStreak()));

        String waterIntake = user.getWater_input()/1000 + "/" + user.getWater_goal() + "L";
        waterIntakeTV.setText(waterIntake);

        if (user.getCalories_goal()  < user.getCalories_input())
        {
            progressBar.setMax(user.getCalories_goal());
            progressBar.setProgress(user.getCalories_goal());

            double remainingCalories = user.getCalories_goal() - user.getCalories_input();
            String remainingCaloriesText = (int)Math.abs(remainingCalories) + "\nexcess";
            remainingCaloriesTV.setText(remainingCaloriesText);
        } else {
            progressBar.setMax(user.getCalories_goal());
            progressBar.setProgress((int)user.getCalories_input());

            double remainingCalories = user.getCalories_goal() - user.getCalories_input();
            String remainingCaloriesText = (int)remainingCalories + "\nremaining";
            remainingCaloriesTV.setText(remainingCaloriesText);
        }



    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(Home.this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(Home.this, login.class);
        startActivity(intent);
        finish();
    }
}
