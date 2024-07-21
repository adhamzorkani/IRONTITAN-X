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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profile extends AppCompatActivity {

    private ImageButton homeButton;
    private ImageButton fitnessButton;
    private ImageButton foodButton;
    private ImageButton moreButton;
    private TextView profileButton;
    private TextView plansButton;
    private TextView premiumButton;
    private TextView settingsButton;

    private FirebaseFirestore db;
    private FirebaseUser user;

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

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

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
        plansButton = findViewById(R.id.plansButton);
        premiumButton = findViewById(R.id.premiumButton);
        settingsButton = findViewById(R.id.settingsButton);

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(profile.this, profile_info.class);
            startActivity(intent);
        });

        plansButton.setOnClickListener(v -> {
            Intent intent = new Intent(profile.this, MyWorkoutPlans.class);
            startActivity(intent);
        });

        premiumButton.setOnClickListener(v -> upgradeToPremium());

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(profile.this, settings.class);
            startActivity(intent);
        });
    }

    private void upgradeToPremium() {
        if (user != null) {
            DocumentReference userDocRef = db.collection("users").document(user.getUid());
            userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String currentPlan = documentSnapshot.getString("plan");
                    if ("premium".equals(currentPlan)) {
                        Toast.makeText(profile.this, "You are already a premium member", Toast.LENGTH_SHORT).show();
                    } else {
                        userDocRef.update("plan", "premium").addOnSuccessListener(aVoid -> {
                            Toast.makeText(profile.this, "Successfully upgraded to premium", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(profile.this, "Failed to upgrade to premium", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    Toast.makeText(profile.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(profile.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(profile.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}
