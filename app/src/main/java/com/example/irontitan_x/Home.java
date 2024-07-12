package com.example.irontitan_x;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
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
    ImageButton chatbotBtn;

    User user;
    double calorieGoal;
    TextView streakTV, waterIntakeTV, remainingCaloriesTV;

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

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            firestore.collection("users").document(firebaseUser.getUid()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                user = document.toObject(User.class);
                            } else {
                                Toast toast = Toast.makeText(Home.this, "There is no document for the current user", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        } else {
                            Toast toast = Toast.makeText(Home.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_LONG);
                            toast.show();
                            Intent intent = new Intent(Home.this, login.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }

        calculateCalorieIntake();

        streakTV = findViewById(R.id.streakText);
        waterIntakeTV = findViewById(R.id.waterText);
        remainingCaloriesTV = findViewById(R.id.caloriesRemaining);



        if (Objects.equals(user.getGender(), "male")){

        }


        homeButton = findViewById(R.id.home_button);
        fitnessButton = findViewById(R.id.fitness_button);
        foodButton = findViewById(R.id.food_button);
        moreButton = findViewById(R.id.more_button);
        homeButton.setBackgroundResource(R.drawable.bg_button);
        chatbotBtn = findViewById(R.id.chatBotButton);
        chatbotBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Assistant.class);
            startActivity(intent);
        });
        homeButton.setOnClickListener(v -> {
            // Navigate to the Home activity
        });

        fitnessButton.setOnClickListener(v -> {
            // Navigate to the Fitness activity
            homeButton.setBackgroundResource(R.drawable.icon_bg_deafult);
            Intent intent = new Intent(Home.this, workoutPlan.class);
            startActivity(intent);
        });

        foodButton.setOnClickListener(v -> {
            // Navigate to the Food activity
            homeButton.setBackgroundResource(R.drawable.icon_bg_deafult);
            Intent intent = new Intent(Home.this, FoodActivity.class);
            startActivity(intent);
        });

        moreButton.setOnClickListener(v -> {
            // Navigate to the More activity
            homeButton.setBackgroundResource(R.drawable.icon_bg_deafult);
            Intent intent = new Intent(Home.this, profile.class);
            startActivity(intent);
        });
    }

    private void calculateCalorieIntake(){
        double BMR, TDEE;
        if (Objects.equals(user.getGender(), "male")) {
            BMR = 66 + (13.7 * Double.parseDouble(user.getWeight())) + (5 * Double.parseDouble(user.getHeight())) - (6.8 * Double.parseDouble(user.getAge()));
        } else {
            BMR = 655 + (9.6 * Double.parseDouble(user.getWeight())) + (1.8 * Double.parseDouble(user.getHeight())) - (4.7 * Double.parseDouble(user.getAge()));
        }

        if (Objects.equals(user.getActivity_level(), "No activity")){
            TDEE = BMR * 1.2;
        } else if (Objects.equals(user.getActivity_level(), "Light activity")) {
            TDEE = BMR * 1.375;
        } else if (Objects.equals(user.getActivity_level(), "Active")) {
            TDEE = BMR * 1.55;
        } else if (Objects.equals(user.getActivity_level(), "Highly active")) {
            TDEE = BMR * 1.725;
        } else {
            TDEE = BMR;
        }

        if (Objects.equals(user.getWeight_goal(), "Lose weight")){
            TDEE -= 750;
        } else  if (Objects.equals(user.getWeight_goal(), "Gain weight")){
            TDEE += 750;
        }

        calorieGoal = TDEE;
    }
}