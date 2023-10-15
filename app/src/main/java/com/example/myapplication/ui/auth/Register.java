package com.example.myapplication.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://authentication-9c17d-default-rtdb.firebaseio.com");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(emailTxt)) {
                                Toast.makeText(Register.this, "Email is already registered.", Toast.LENGTH_SHORT).show();
                            } else if (!passTxt.equals(conpassTxt)) {
                                Toast.makeText(Register.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("users").child(emailTxt).child(emailTxt).setValue(emailTxt);
                                databaseReference.child("users").child(passTxt).child(passTxt).setValue(passTxt);

                                Toast.makeText(Register.this, "User Registered Successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }

            public void checkUser() {
                String userfullname = fullname.getText().toString().trim();
                String userpassword = password.getText().toString().trim();
                String useremail = email.getText().toString().trim();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                Query checkUserDatabase = reference.orderByChild("email").equalTo("useremail");

                checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });


    }
}