package com.example.myapplication.ui.detention;

import static com.example.myapplication.ui.auth.LoginActivity.displayName;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.EditText;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class DetentionActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar username;
    public static TextInputEditText companyInput;
    public static String inputTextString = " ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detention);
        setSupportActionBar(findViewById(R.id.toolbar));
        username = findViewById(R.id.toolbar);
        username.setTitle(displayName);

        companyInput = findViewById(R.id.companyInput);
        companyInput.setText(inputTextString);
        inputTextString = companyInput.getText().toString();
        companyInput.setText(inputTextString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu.findItem(R.id.menuDropdown) == null) {
            getMenuInflater().inflate(R.menu.detention_pay_menu, menu);
        }
        else {
            MenuItem menuItem = menu.findItem(R.id.menuDropdown);
            SubMenu submenu = menuItem.getSubMenu();
            getMenuInflater().inflate(R.menu.detention_pay_menu, submenu);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuAction1) {
            startActivity(new Intent(DetentionActivity.this, MainActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menuAction2) {
            // menu option code
            return true;
        } else if (item.getItemId() == R.id.menuAction3) {
            // menu option code
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}