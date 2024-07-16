package com.example.myapplication.ui.auth;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import com.example.myapplication.ui.detention.DetentionActivity;
import com.example.myapplication.ui.profile.ProfileActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        EditText fullname = findViewById(R.id.fullname);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText conpassword = findViewById(R.id.conpassword);
        TextView guest = findViewById(R.id.guestTxt);

        MaterialButton registerbtn = findViewById(R.id.to_register);

        guest.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, MainActivity.class));
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTxt = fullname.getText().toString();
                String emailTxt = email.getText().toString();
                String passTxt = password.getText().toString();
                String conpassTxt = conpassword.getText().toString();

                if (nameTxt.isEmpty() || emailTxt.isEmpty() || passTxt.isEmpty()) {
                    if (nameTxt.isEmpty()) {
                        fullname.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    } else {
                        fullname.getBackground().clearColorFilter();
                    }


                    if (emailTxt.isEmpty()) {
                        email.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    }else {
                        email.getBackground().clearColorFilter();
                    }

                    if (passTxt.isEmpty()) {
                        password.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    }else {
                        password.getBackground().clearColorFilter();
                    }

                    if (passTxt.isEmpty()) {
                        conpassword.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    } else {
                        conpassword.getBackground().clearColorFilter();
                    }

                    Toast.makeText(getApplicationContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    email.getBackground().clearColorFilter();
                    if (passTxt.equals(conpassTxt))
                    {
                        password.getBackground().clearColorFilter();
                        conpassword.getBackground().clearColorFilter();

                        signUp(emailTxt, passTxt, nameTxt);
                    }
                    else
                    {
                        password.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                        conpassword.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

                        Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void signUp(String email, String password, String fullname)
    {
        mAuth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullname).build();

                            assert user != null;
                            user.updateProfile(profileUpdates);
                            user.getDisplayName();

                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            user.sendEmailVerification();
                            Toast.makeText(getApplicationContext(), "Account Creation Successful", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Account Creation Failed, email already has an account", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}