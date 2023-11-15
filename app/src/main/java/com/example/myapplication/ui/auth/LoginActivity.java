package com.example.myapplication.ui.auth;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.SignInMethodQueryResult;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

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


       EditText email = findViewById(R.id.email);
       EditText password = findViewById(R.id.password);
       MaterialButton register = findViewById(R.id.to_register);


        TextView user = (TextView) findViewById(R.id.email);
        TextView pass = (TextView) findViewById(R.id.password);

        MaterialButton signinbtn = (MaterialButton) findViewById(R.id.sign_in);
        MaterialButton registerbtn = (MaterialButton) findViewById(R.id.to_register);


        Button to_reset = (Button) findViewById(R.id.to_reset);


        to_reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                resetPassword(email.getText().toString());
             }
         }
        );

        signinbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                final String emailTxt = user.getText().toString();
                final String passTxt = pass.getText().toString();

                if (user.getText().toString().isEmpty()|| pass.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
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
        LoginActivity.this.finish();
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (!(currentUser != null && currentUser.isEmailVerified())){
                                Toast.makeText(getApplicationContext(), "email not verified, sending verification email to " + currentUser.getEmail(), Toast.LENGTH_LONG).show();
                                currentUser.sendEmailVerification();
                                return;
                            }
                            Toast.makeText(getApplicationContext(), "Login success.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            LoginActivity.this.finish();
                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Login failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void resetPassword(String emailAddress){
        if (!runTextTests(emailAddress))
        {
            return;
        }

        Toast.makeText(getApplicationContext(), "password recovery email sent to: " + emailAddress, Toast.LENGTH_SHORT).show();
        mAuth.sendPasswordResetEmail(emailAddress);

    }

    public Boolean runTextTests(String email)
    {
        if (email.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.matches(String.valueOf(R.string.emailPattern)))
        {
            return true;
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}