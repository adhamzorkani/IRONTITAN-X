package com.example.irontitan_x;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;



public class settings extends AppCompatActivity {

    private static final String PRIVACY_POLICY_TITLE = "Privacy Policy";
    private static final String PRIVACY_POLICY_CONTENT = "At IronTitanX, we are committed to protecting your privacy and ensuring the security of your personal information. When you use our app, we may collect data such as your name, email address, fitness goals, and activity logs to provide a personalized fitness experience. This information is used solely to customize workout plans, track habits, and offer tailored nutritional advice. We do not share your personal information with third parties without your explicit consent, except as required by law. By using IronTitanX, you agree to our privacy policy and the responsible use of your information to enhance your fitness journey.";
    private static final String ABOUT_US_TITLE = "About Us";
    private static final String ABOUT_US_CONTENT = "IronTitanX is a comprehensive mobile application designed to optimize your fitness journey by personalizing workout and nutrition plans. Our mission is to empower individuals to achieve their fitness goals through tailored exercise routines, habit tracking, and nutritional guidance. Upon starting the app, users answer a series of questions to determine their specific fitness objectives. Based on these responses, IronTitanX crafts customized workout plans with detailed exercise demonstrations. Additionally, our app supports habit tracking, allowing users to monitor daily calorie and water intake, along with other health habits. Our unique chatbot feature creates meal schedules and provides recipes based on available ingredients, calculating total calorie counts to ensure users stay on track with their nutritional goals. Join us at IronTitanX and take the first step towards a healthier, fitter you!";
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();

        RecyclerView recyclerView = findViewById(R.id.settingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SettingsItem> items = Arrays.asList(
                new SettingsItem("Privacy Policy"),
                new SettingsItem("About Us"),
                new SettingsItem("Logout")
        );

        SettingsAdapter adapter = new SettingsAdapter(items, item -> {
            switch (item.getTitle()) {
                case "Privacy Policy":
                    showInfoDialog(PRIVACY_POLICY_TITLE, PRIVACY_POLICY_CONTENT);
                    break;
                case "About Us":
                    showInfoDialog(ABOUT_US_TITLE, ABOUT_US_CONTENT);
                    break;
                case "Logout":
                    logout();
                    break;
                default:
                    Toast.makeText(settings.this, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        recyclerView.setAdapter(adapter);

        findViewById(R.id.backButton).setOnClickListener(v -> onBackPressed());
    }

    private void showInfoDialog(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));
    }
    private void logout() {
        auth.signOut();
        Intent intent = new Intent(settings.this, login.class);
        startActivity(intent);
        finish();
    }
}

