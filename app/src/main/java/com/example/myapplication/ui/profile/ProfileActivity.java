package com.example.myapplication.ui.profile;

import static com.example.myapplication.MainActivity.fromMain;
import static com.example.myapplication.ui.detention.DetentionActivity.fromDetention;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.detention.DetentionActivity;
import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button back = findViewById(R.id.backButton);

        back.setOnClickListener(v -> {
            if (fromMain == true) {
                fromMain = false;
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
            if (fromDetention == true) {
                fromDetention = false;
                startActivity(new Intent(ProfileActivity.this, DetentionActivity.class));
            }
        });
    }
}
