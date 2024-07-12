package com.example.irontitan_x;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class userInfo extends AppCompatActivity {

    Button nextBtn, ageBtn;
    Calendar calendar;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;
    Spinner goalSpinner, activitySpinner;
    EditText weightEditText,heightEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calendar = Calendar.getInstance();

        goalSpinner = findViewById(R.id.spinnerGoal);
        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(this,
                R.array.goal_options, android.R.layout.simple_spinner_item);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalSpinner.setAdapter(goalAdapter);

        activitySpinner = findViewById(R.id.spinnerActivity);
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(this,
                R.array.activity_options, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(activityAdapter);

        nextBtn=findViewById(R.id.nextButton);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup = findViewById(R.id.toggle);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                selectedRadioButton = findViewById(selectedId);
                String gender = selectedRadioButton.getText().toString();

                goalSpinner = findViewById(R.id.spinnerGoal);
                String goal = goalSpinner.getSelectedItem().toString();

                activitySpinner = findViewById(R.id.spinnerActivity);
                String activity = activitySpinner.getSelectedItem().toString();

                weightEditText = findViewById(R.id.weight);
                String weight = weightEditText.getText().toString();



                heightEditText = findViewById(R.id.height);
                String height = heightEditText.getText().toString();

                if (TextUtils.isEmpty(weight)) {
                    Toast.makeText(userInfo.this, "Please enter weight", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(height)) {
                    Toast.makeText(userInfo.this, "Please enter height", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(userInfo.this, plan.class);
                intent.putExtra("gender", gender);
                intent.putExtra("goal", goal);
                intent.putExtra("activity", activity);
                intent.putExtra("weight", weight);
                intent.putExtra("height", height);
                intent.putExtra("age", ageBtn.getText());

                startActivity(intent);
            }
        });

        ageBtn = findViewById(R.id.age_button);
        ageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(userInfo.this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            selectedMonth++;
                            calculateAge(selectedYear, selectedMonth, selectedDay);
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void calculateAge(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - year;

        if (today.get(Calendar.MONTH) + 1 < month ||
                (today.get(Calendar.MONTH) + 1 == month && today.get(Calendar.DAY_OF_MONTH) < day)) {
            age--;
        }
        ageBtn.setText( String.valueOf(age) );
    }
}