package com.example.irontitan_x;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class add_workout extends AppCompatActivity {

    private Button backButton;
    private Button addButton;
    private RecyclerView videoRecyclerView;
    private VideoAdapter videoAdapter;
    private List<VideoItem> videoItems;
    private AutoCompleteTextView muscleGroupSpinner;
    private EditText searchEditText;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButton = findViewById(R.id.backButton);
        addButton = findViewById(R.id.addButton);
        videoRecyclerView = findViewById(R.id.videoRecyclerView);
        muscleGroupSpinner = findViewById(R.id.muscle_selector);
        searchEditText = findViewById(R.id.searchBar);

        db = FirebaseFirestore.getInstance();

        // Setup RecyclerView
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoItems = new ArrayList<>();
        videoAdapter = new VideoAdapter(videoItems, this::onPlayButtonClicked, this::onCheckBoxClicked);
        videoRecyclerView.setAdapter(videoAdapter);

        backButton.setOnClickListener(v -> finish());

        addButton.setOnClickListener(v -> {
            ArrayList<VideoItem> selectedExercises = new ArrayList<>();
            for (VideoItem item : videoItems) {
                if (item.isSelected()) {
                    selectedExercises.add(item);
                }
            }
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("selected_exercises", selectedExercises);
            setResult(RESULT_OK, intent);
            finish();
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchExercises();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });

        muscleGroupSpinner.setOnItemClickListener((parent, view, position, id) -> searchExercises());

        loadMuscleGroups();
        loadAllExercises();
    }

    private void loadMuscleGroups() {
        CollectionReference muscleGroupsRef = db.collection("muscleGroups");
        muscleGroupsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> muscleGroups = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    muscleGroups.add(document.getId());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, muscleGroups);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                muscleGroupSpinner.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Failed to load muscle groups", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllExercises() {
        CollectionReference exercisesRef = db.collection("muscleGroups");
        exercisesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                videoItems.clear();
                for (QueryDocumentSnapshot muscleDoc : task.getResult()) {
                    CollectionReference muscleExercisesRef = muscleDoc.getReference().collection("exercises");
                    muscleExercisesRef.get().addOnCompleteListener(exerciseTask -> {
                        if (exerciseTask.isSuccessful()) {
                            for (QueryDocumentSnapshot exerciseDoc : exerciseTask.getResult()) {
                                String title = exerciseDoc.getString("name");
                                String url = exerciseDoc.getString("videoUrl");
                                String thumbnailUrl = exerciseDoc.getString("thumbnailUrl");
                                videoItems.add(new VideoItem(thumbnailUrl, title, url));
                            }
                            videoAdapter.notifyDataSetChanged();
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Failed to load exercises", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchExercises() {
        String query = searchEditText.getText().toString().trim();
        String muscleGroup = muscleGroupSpinner.getText().toString().trim();

        if (muscleGroup.isEmpty()) {
            loadAllExercises();
            return;
        }

        CollectionReference muscleExercisesRef = db.collection("muscleGroups").document(muscleGroup).collection("exercises");
        muscleExercisesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                videoItems.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("name");
                    if (title.toLowerCase().contains(query.toLowerCase())) {
                        String url = document.getString("videoUrl");
                        String thumbnailUrl = document.getString("thumbnailUrl");
                        videoItems.add(new VideoItem(thumbnailUrl, title, url));
                    }
                }
                videoAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to search exercises", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onPlayButtonClicked(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void onCheckBoxClicked(VideoItem videoItem, boolean isChecked) {
        videoItem.setSelected(isChecked);
    }
}
