package com.example.irontitan_x;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class profile_info extends AppCompatActivity {

    private RecyclerView recyclerView;
    private editableAdapter adapter;
    private List<editableItem> userProfiles = new ArrayList<>();
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private Button saveChangesButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.profileRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new editableAdapter(userProfiles);
        recyclerView.setAdapter(adapter);

        saveChangesButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.backButton);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        loadUserData();

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Go back to the previous activity without saving changes
            }
        });
    }

    private void loadUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            firestore.collection("users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                DocumentSnapshot document = task.getResult();
                                userProfiles.clear();  // Clear the list before adding new data
                                userProfiles.add(new editableItem("Name", document.getString("name")));
                                userProfiles.add(new editableItem("Email", document.getString("email")));
                                userProfiles.add(new editableItem("Age", document.getString("age")));  // Convert to String
                                userProfiles.add(new editableItem("Height", document.getString("height")));  // Convert to String
                                userProfiles.add(new editableItem("Gender", document.getString("gender")));
                                userProfiles.add(new editableItem("Weight", document.getString("weight")));  // Convert to String
                                userProfiles.add(new editableItem("Activity Level", document.getString("activity_level")));
                                userProfiles.add(new editableItem("Weight Goal", document.getString("weight_goal")));
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(profile_info.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void saveUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Map<String, Object> updates = new HashMap<>();
            String gender = null;
            String age = null;
            String height = null;
            String weight = null;
            String activity = null;
            String goal = null;
            for (editableItem profile : userProfiles) {
                String key = profile.getTitle().toLowerCase();
                String value = profile.getValue();
                if (key.equals("gender")) {
                    gender = value;
                } else if (key.equals("age")) {
                    age = value;
                } else if (key.equals("height")) {
                    height = value;
                } else if (key.equals("weight")) {
                    weight = value;
                } else if (key.equals("activity level")) {
                    activity = value;
                } else if (key.equals("weight goal")) {
                    goal = value;
                }
                updates.put(key, value);
            }

            if (gender != null && age != null && height != null && weight != null && activity != null && goal != null) {
                Map<String, Object> calculatedValues = calculateCalorieIntakeAndWaterGoal(gender, age, height, weight, activity, goal);
                updates.putAll(calculatedValues);
            }

            firestore.collection("users").document(user.getUid())
                    .update(updates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(profile_info.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
                                finish();  // Go back to the previous activity after saving changes
                            } else {
                                Toast.makeText(profile_info.this, "Failed to save changes", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private Map<String, Object> calculateCalorieIntakeAndWaterGoal(String gender, String age, String height, String weight, String activity, String goal) {
        Map<String, Object> result = new HashMap<>();
        double BMR, TDEE;
        double weightDouble = Double.parseDouble(weight);
        double heightDouble = Double.parseDouble(height);
        double ageDouble = Double.parseDouble(age);

        if (Objects.equals(gender, "Male")) {
            BMR = 66 + (13.7 * weightDouble) + (5 * heightDouble) - (6.8 * ageDouble);
        } else {
            BMR = 655 + (9.6 * weightDouble) + (1.8 * heightDouble) - (4.7 * ageDouble);
        }

        switch (activity) {
            case "No activity":
                TDEE = BMR * 1.2;
                break;
            case "Light activity":
                TDEE = BMR * 1.375;
                break;
            case "Active":
                TDEE = BMR * 1.55;
                break;
            case "Highly active":
                TDEE = BMR * 1.725;
                break;
            default:
                TDEE = BMR;
                break;
        }

        if (Objects.equals(goal, "Lose weight")) {
            TDEE -= 750;
        } else if (Objects.equals(goal, "Gain weight")) {
            TDEE += 750;
        }

        int calorieGoal = (int) TDEE;
        int waterGoal = Objects.equals(gender, "Male") ? 4 : 3;

        result.put("calories_goal", calorieGoal);
        result.put("water_goal", waterGoal);

        return result;
    }
}
