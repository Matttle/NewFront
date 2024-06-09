package com.example.myapplication;

import static com.example.myapplication.ui.auth.LoginActivity.displayName;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.ui.dashboard.DashboardFragment;
import com.example.myapplication.ui.detention.DetentionActivity;
import com.example.myapplication.ui.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static androidx.appcompat.widget.Toolbar myToolbar;
    public static boolean fromMain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setTitle(displayName + "");
        findViewById(R.id.Profile).setOnClickListener(v -> {
            fromMain = true;
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu.findItem(R.id.menuDropdown) == null) {
            getMenuInflater().inflate(R.menu.home_menu, menu);
        }
        else {
            MenuItem menuItem = menu.findItem(R.id.menuDropdown);
            SubMenu submenu = menuItem.getSubMenu();
            getMenuInflater().inflate(R.menu.home_menu, submenu);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuAction1) {
            startActivity(new Intent(MainActivity.this, DetentionActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menuAction2) {
            // menu option code
            return true;
        } else if (item.getItemId() == R.id.menuAction3) {
            // menu option code
            return true;
        }  else {
            return super.onOptionsItemSelected(item);
        }
    }
}