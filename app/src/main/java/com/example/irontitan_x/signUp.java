package com.example.irontitan_x;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class signUp extends AppCompatActivity {

    Button signUpBtn;
    TextView orLoginTV;
    FirebaseAuth  auth;
    EditText nameET, emailET, passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();

        nameET = findViewById(R.id.nameField);
        emailET = findViewById(R.id.emailAddress);
        passwordET = findViewById(R.id.password);

        signUpBtn=findViewById(R.id.signUpButton);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email, password;

                name = nameET.getText().toString();
                email = emailET.getText().toString();
                password = passwordET.getText().toString();

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(signUp.this, "Please enter name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(signUp.this, "Please enter email" , Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(signUp.this, "Please enter password" , Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                                    Map<String, Object> userObj = new HashMap<>();

                                    userObj.put("name", name);
                                    userObj.put("email", email);
                                    userObj.put("weight_goal", "");
                                    userObj.put("activity_level", "");
                                    userObj.put("gender", "");
                                    userObj.put("age", 0);
                                    userObj.put("height", 0);
                                    userObj.put("weight", 0);
                                    userObj.put("plan", "standard");
                                    userObj.put("streak", 0);
                                    userObj.put("longest_streak", 0);
                                    userObj.put("workout_plans", new HashMap<>());
                                    userObj.put("calories_goal", 0);
                                    userObj.put("calories_input", 0);
                                    userObj.put("water_goal",0);
                                    userObj.put("water_input", 0);

                                    FirebaseUser user = auth.getCurrentUser();

                                    if (user != null){
                                        firestore.collection("users").document(user.getUid())
                                                .set(userObj);
                                        Intent intent = new Intent(signUp.this, userInfo.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(signUp.this, Objects.requireNonNull(task.getException()).toString(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        orLoginTV=findViewById(R.id.orLoginButton);
        orLoginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUp.this, login.class);
                startActivity(intent);
                finish();
            }
        });
    }

}