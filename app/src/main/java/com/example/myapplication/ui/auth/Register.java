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
                    Toast.makeText(Register.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    if (passTxt.equals(conpassTxt))
                    {
                        signUp(emailTxt, passTxt, nameTxt);
                    }
                    else
                    {
                        Toast.makeText(Register.this, "Passwords dont match", Toast.LENGTH_SHORT).show();
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
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            Toast.makeText(Register.this, "createUserWithEmail:success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullname).build();

                            user.updateProfile(profileUpdates);


                            user.getDisplayName();
                            startActivity(new Intent(Register.this, LoginActivity.class));
                            user.sendEmailVerification();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            // TODO : look into why it failed to give the user a better response
                            Toast.makeText(Register.this, "createUserWithEmail:failure", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public Boolean runTextTests()
    {
        // TODO WIP : this function is to run tests on the text boxes to assure firebase runs smoothly
       return true;
    }

}