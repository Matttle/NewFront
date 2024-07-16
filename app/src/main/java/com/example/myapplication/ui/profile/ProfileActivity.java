package com.example.myapplication.ui.profile;

import static com.example.myapplication.MainActivity.fromMain;
import static com.example.myapplication.ui.auth.LoginActivity.mEmail;
import static com.example.myapplication.ui.detention.DetentionActivity.fromDetention;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.detention.DetentionActivity;
import com.example.myapplication.ui.payment.PaymentActivity;

import com.example.myapplication.ui.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    public static String mName = "Enter your name";
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    public static boolean fromProfile;
    public static double mPay = 0.00000001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            mName = currentUser.getDisplayName();
        } catch (NullPointerException npe) {
            mName = "Guest";
        }
        try {
            mEmail = currentUser.getEmail();
        } catch (NullPointerException npe) {
            mEmail = "Guest@guest.com";
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText email = findViewById(R.id.editTextEmail);
        EditText name = findViewById(R.id.editTextName);
        EditText pay = findViewById(R.id.editTextPay);

        email.setHint(mEmail);
        name.setHint(mName);

        Button back = findViewById(R.id.btnBackProfile);
        Button apply = findViewById(R.id.btnApply);
        TextView paymentBtn = findViewById(R.id.paymentHistory);
        TextView themeBtn = findViewById(R.id.themeBtn);
        ImageView payArrowBtn = findViewById(R.id.paymentArrow);
        ImageView themeArrow = findViewById(R.id.themeArrow);

        paymentBtn.setOnClickListener(v -> {
            fromProfile = true;
            startActivity(new Intent(ProfileActivity.this, PaymentActivity.class));
        });

        payArrowBtn.setOnClickListener(v -> {
            fromProfile = true;
            startActivity(new Intent(ProfileActivity.this, PaymentActivity.class));
        });

        themeBtn.setOnClickListener(v -> {
            showThemeSelectionDialog();
        });

        themeArrow.setOnClickListener(v -> {
            showThemeSelectionDialog();
        });

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
            if (pay.getText().toString() == "")
                mPay = 0;
            else {
                try {
                    mPay = Double.parseDouble(pay.getText().toString());
                } catch (NumberFormatException nfe) {
                    Toast.makeText(getApplicationContext(), "Invalid.", Toast.LENGTH_SHORT).show();
                }
            }
            Toast.makeText(getApplicationContext(), "Settings applied.", Toast.LENGTH_SHORT).show();
        });

        Button signOut = findViewById(R.id.btnSignOut);

        signOut.setOnClickListener(view -> {
            AlertDialog.Builder signOutAlert = new AlertDialog.Builder(this);
            signOutAlert.setTitle("Are you sure you want to Sign out?");
            signOutAlert.setPositiveButton("Sign Out", (dialogInterface, i) -> {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "Sign Out Success.", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                ProfileActivity.this.finish();
            }
            );

            signOutAlert.setNeutralButton("Cancel", (dialogInterface, i) -> {

            }
            );

            signOutAlert.show();

        });

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    private void showThemeSelectionDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Theme Selection")
                .setPositiveButton("Light Mode", (dialog, which) -> {
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // dysfunctional theme code
                    recreate();
                })
                .setNegativeButton("Dark Mode", (dialog, which) -> {
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // dysfunctional theme code
                    recreate();
                })
                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = b.create();
        dialog.show();
    }
}
