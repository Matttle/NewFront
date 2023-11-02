package com.example.myapplication.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public static String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            displayName = currentUser.getEmail(); // TODO: Change getEmail with getDisplayName() to display that instead. (when we have display names)
            reload();
        }
        else {
            displayName = "Guest";
        }


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

                if (user.getText().toString().isEmpty()|| pass.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    signIn(emailTxt, passTxt);
                }
            }
        }
        );






        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, Register.class));
            }
        });
    }

    private void reload() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    private void signIn(String email, String password) {
        Log.d("TAG", "signIn:" + email);
        Log.d("TAG", "password:" + password);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Login success.", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            // TODO : check "user.isEmailVerified()" to assure the user verified their email and if they haven't send email verification
                            /*
                            if (user.isEmailVerified())
                            {
                                Toast.makeText(LoginActivity.this, "isEmailVerified.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "isNotEmailVerified.", Toast.LENGTH_SHORT).show();
                                // user.sendEmailVerification();
                            }

                             */
                        } else {
                            // If sign in fails, display a message to the user.
                            // TODO : look into why it failed to give the user a better response
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getContext(), "Authentication failed.",
                            //        Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            //checkForMultiFactorFailure(task.getException());
                        }
                    }
                });
    }




}