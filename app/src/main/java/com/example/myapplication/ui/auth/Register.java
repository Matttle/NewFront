package com.example.myapplication.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;
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

        MaterialButton registerbtn = findViewById(R.id.to_register);


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTxt = fullname.getText().toString();
                String emailTxt = email.getText().toString();
                String passTxt = password.getText().toString();
                String conpassTxt = conpassword.getText().toString();

                if (nameTxt.isEmpty() || emailTxt.isEmpty() || passTxt.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    if (passTxt.equals(conpassTxt))
                    {
                        signUp(emailTxt, passTxt, nameTxt);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }
    public void signUp(String email, String password, String fullname)
    {
        if (!runTextTests(email))
        {
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Account Creation Successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullname).build();

                            user.updateProfile(profileUpdates);


                            user.getDisplayName();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            user.sendEmailVerification();
                        } else {
                            Toast.makeText(getApplicationContext(), "Account Creation Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
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