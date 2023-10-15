package com.example.myapplication.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       EditText email = findViewById(R.id.email);
       EditText password = findViewById(R.id.password);
       MaterialButton register = findViewById(R.id.to_register);

        TextView user = (TextView) findViewById(R.id.email);
        TextView pass = (TextView) findViewById(R.id.password);

        MaterialButton signinbtn = (MaterialButton) findViewById(R.id.sign_in);
        MaterialButton registerbtn = (MaterialButton) findViewById(R.id.to_register);

        signinbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                final String emailTxt = user.getText().toString();
                final String passTxt = pass.getText().toString();

                // Add login failed and login successful code - Matt ***

                if (user.getText().toString().isEmpty()|| pass.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, Register.class));
            }
        });
    }
}