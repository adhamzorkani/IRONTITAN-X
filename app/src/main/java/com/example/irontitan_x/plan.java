package com.example.irontitan_x;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class plan extends AppCompatActivity {
    Button nextBtn;
    TextView backTV;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;

    String gender;
    String goal;
    String activity;
    String weight;
    String height;
    String age;
    double calorieGoal;
    String waterGoal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gender = getIntent().getStringExtra("gender");
        goal = getIntent().getStringExtra("goal");
        activity = getIntent().getStringExtra("activity");
        weight = getIntent().getStringExtra("weight");
        height = getIntent().getStringExtra("height");
        age = getIntent().getStringExtra("age");


        nextBtn = findViewById(R.id.nextButton);
        nextBtn.setOnClickListener(view -> {
            radioGroup = findViewById(R.id.toggle);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            selectedRadioButton = findViewById(selectedId);
            String plan = selectedRadioButton.getText().toString();

            calculateCalorieIntake();

            if (Objects.equals(gender, "Male")){
                waterGoal = "4";
            } else {
                waterGoal = "3";
            }

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();


            if (user != null){
                Map<String, Object> userObj = new HashMap<>();
                userObj.put("gender", gender);
                userObj.put("weight_goal", goal);
                userObj.put("activity_level", activity);
                userObj.put("weight", weight);
                userObj.put("height", height);
                userObj.put("age", age);
                userObj.put("plan", plan);
                userObj.put("calories_goal", (int)calorieGoal);
                userObj.put("water_goal", Integer.parseInt(waterGoal) );

                firestore.collection("users").document(user.getUid())
                        .update(userObj)
                        .addOnSuccessListener(aVoid -> {
                            Intent intent = new Intent(plan.this, Home.class);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> Toast.makeText(plan.this, "Couldn't add user data",
                                Toast.LENGTH_SHORT).show());
            }
        });

        backTV = findViewById(R.id.backText);
        backTV.setOnClickListener(v -> finish());
    }

    private void calculateCalorieIntake(){
        double BMR, TDEE;
        if (Objects.equals(gender, "male")) {
            BMR = 66 + (13.7 * Double.parseDouble(weight)) + (5 * Double.parseDouble(height)) - (6.8 * Double.parseDouble(age));
        } else {
            BMR = 655 + (9.6 * Double.parseDouble(weight)) + (1.8 * Double.parseDouble(height)) - (4.7 * Double.parseDouble(age));
        }

        if (Objects.equals(activity, "No activity")){
            TDEE = BMR * 1.2;
        } else if (Objects.equals(activity, "Light activity")) {
            TDEE = BMR * 1.375;
        } else if (Objects.equals(activity, "Active")) {
            TDEE = BMR * 1.55;
        } else if (Objects.equals(activity, "Highly active")) {
            TDEE = BMR * 1.725;
        } else {
            TDEE = BMR;
        }

        if (Objects.equals(goal, "Lose weight")){
            TDEE -= 750;
        } else  if (Objects.equals(goal, "Gain weight")){
            TDEE += 750;
        }

        calorieGoal = TDEE;
    }
}