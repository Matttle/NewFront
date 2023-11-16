package com.example.myapplication.ui.profile;

import static com.example.myapplication.MainActivity.fromMain;
import static com.example.myapplication.ui.auth.LoginActivity.mEmail;
import static com.example.myapplication.ui.detention.DetentionActivity.fromDetention;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.detention.DetentionActivity;
import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends AppCompatActivity {

    public static String mName = "Enter your name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText email = findViewById(R.id.editTextEmail);
        EditText name = findViewById(R.id.editTextName);

        email.setHint(mEmail);
        name.setHint(mName);

        Button back = findViewById(R.id.btnBack);
        Button apply = findViewById(R.id.btnApply);

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

        apply.setOnClickListener(v -> {
            mName = name.getText().toString();
            Toast.makeText(getApplicationContext(), "Settings applied.", Toast.LENGTH_SHORT).show();
        });

    }
}
