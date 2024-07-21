package com.example.irontitan_x;

import android.os.Bundle;
import android.widget.Button;
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
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyWorkoutPlans extends AppCompatActivity {

    private Button backButton;
    private RecyclerView profileRecyclerView;
    private ReadyPlansAdapter planAdapter;
    private List<ReadyPlans> plans;

    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_workout_plans);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        backButton = findViewById(R.id.backButton);
        profileRecyclerView = findViewById(R.id.profileRecyclerView);

        backButton.setOnClickListener(v -> finish());

        // Setup RecyclerView
        profileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        plans = new ArrayList<>();
        planAdapter = new ReadyPlansAdapter(plans, true, false, true, this::onPlanRemove);  // Show remove buttons
        profileRecyclerView.setAdapter(planAdapter);

        // Fetch user's plans from Firestore
        fetchUserPlansFromFirestore();
    }

    private void fetchUserPlansFromFirestore() {
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            parseUserPlans(documentSnapshot);
                        } else {
                            Toast.makeText(this, "No plans found.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch plans.", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseUserPlans(DocumentSnapshot documentSnapshot) {
        Map<String, Object> data = documentSnapshot.getData();
        if (data != null) {
            // The root map should contain the "workout_plans" key
            Map<String, Object> plansData = (Map<String, Object>) data.get("workout_plans");
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

    private void onPlanRemove(String planName, int position) {
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .update("workout_plans." + planName, FieldValue.delete())
                    .addOnSuccessListener(aVoid -> {
                        plans.remove(position);
                        planAdapter.notifyItemRemoved(position);
                        planAdapter.notifyItemRangeChanged(position, plans.size());
                        Toast.makeText(MyWorkoutPlans.this, "Plan removed successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(MyWorkoutPlans.this, "Failed to remove plan", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}
