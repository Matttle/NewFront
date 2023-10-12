package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Code added to display the login screen.
        setContentView(R.layout.activity_login);

        TextView user = (TextView) findViewById(R.id.ed_email);
        TextView pass = (TextView) findViewById(R.id.ed_password);

        MaterialButton signinbtn = (MaterialButton) findViewById(R.id.sign_in);

        //Code for only proceeding if login credentials are correct.
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getText().toString().equals("newfront") && pass.getText().toString().equals("newfront")){
                    Toast.makeText(MainActivity.this, "Login Successful.", Toast.LENGTH_SHORT);

                    binding = ActivityMainBinding.inflate(getLayoutInflater());
                    setContentView(binding.getRoot());

                    androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.my_toolbar);
                    setSupportActionBar(myToolbar);

                    BottomNavigationView navView = findViewById(R.id.nav_view);
                    // Passing each menu ID as a set of Ids because each
                    // menu should be considered as top level destinations.
                    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                            .build();
                    NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_activity_main);
                    NavigationUI.setupActionBarWithNavController(MainActivity.this, navController, appBarConfiguration);
                    NavigationUI.setupWithNavController(binding.navView, navController);
                }
                else
                    Toast.makeText(MainActivity.this, "Login Failed.", Toast.LENGTH_SHORT);
            }
        });


    }

}