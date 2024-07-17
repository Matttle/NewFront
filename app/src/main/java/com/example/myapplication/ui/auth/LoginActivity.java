package com.example.myapplication.ui.auth;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
import android.widget.Button;
//import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

//import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.SignInMethodQueryResult;

//import org.w3c.dom.Text;

//import java.util.List;
//import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public static String displayName;
    public static String mEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        FirebaseUser currentUser = mAuth.getCurrentUser();
      
        if(currentUser != null && currentUser.isEmailVerified()){
            displayName = currentUser.getDisplayName();
            mEmail = currentUser.getEmail();
            reload();
        }


       TextView user = findViewById(R.id.email);
       TextView pass = findViewById(R.id.password);
       MaterialButton signinbtn = findViewById(R.id.sign_in);
       MaterialButton registerbtn = findViewById(R.id.to_register);

        Button to_reset = findViewById(R.id.to_reset);

        to_reset.setOnClickListener(view -> resetPassword(user.getText().toString(), user));

        signinbtn.setOnClickListener(view -> {
            final String emailTxt = user.getText().toString();
            final String passTxt = pass.getText().toString();

            if (user.getText().toString().isEmpty()|| pass.getText().toString().isEmpty()){
                if (emailTxt.isEmpty()) {
                    user.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    user.getBackground().clearColorFilter();
                }

                if (passTxt.isEmpty()) {
                    pass.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    pass.getBackground().clearColorFilter();
                }

                Toast.makeText(getApplicationContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            } else {
                signIn(emailTxt, passTxt);
            }
        }
        );


        registerbtn.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, Register.class)));
    }

    private void reload() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        LoginActivity.this.finish();
    }

    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        if (!(currentUser != null && currentUser.isEmailVerified())){

                            assert currentUser != null;
                            currentUser.sendEmailVerification();
                            Toast.makeText(getApplicationContext(), "email not verified, sending verification email to " + currentUser.getEmail(), Toast.LENGTH_LONG).show();

                            return;
                        }


                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Toast.makeText(getApplicationContext(), "Login success.", Toast.LENGTH_SHORT).show();
                        LoginActivity.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Login failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resetPassword(String emailAddress, TextView user){
        user.getBackground().clearColorFilter();

        if (emailAddress.isEmpty())
        {
            user.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();

            return;
        }
        mAuth.sendPasswordResetEmail(emailAddress);

        Toast.makeText(getApplicationContext(), "password recovery email sent to: " + emailAddress, Toast.LENGTH_SHORT).show();
    }
}