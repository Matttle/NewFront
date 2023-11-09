package com.example.myapplication.ui.detention;

import static com.example.myapplication.ui.auth.LoginActivity.displayName;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.auth.LoginActivity;
import com.example.myapplication.ui.profile.ProfileActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class DetentionActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar username;
    private TextInputEditText companyInput;
    private TextInputEditText startTimeInput;
    private TextInputEditText endTimeInput;
    private DatePicker datePicker;
    private String day;
    private String month;
    private String year;
    public static String inputTextString = " ";
    public static boolean fromDetention = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detention);
        setSupportActionBar(findViewById(R.id.toolbar));
        username = findViewById(R.id.toolbar);
        username.setTitle(displayName);

        companyInput = findViewById(R.id.companyInput);
        startTimeInput = findViewById(R.id.StartTimeEdit);
        endTimeInput = findViewById(R.id.EndTimeEdit);

        companyInput.setText(inputTextString);
        inputTextString = companyInput.getText().toString();
        companyInput.setText(inputTextString);

        datePicker = findViewById(R.id.datePicker);
        day = intToString(datePicker.getDayOfMonth());
        month = intToString(datePicker.getMonth());
        year = intToString(datePicker.getYear());

        startTimeInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(DetentionActivity.this,
                    (view, hourOfDay, minute12) -> {
                        String period;
                        if (hourOfDay >= 12) {
                            period = "PM";
                            if (hourOfDay > 12) {
                                hourOfDay -= 12;
                            }
                        } else {
                            period = "AM";
                            if (hourOfDay == 0) {
                                hourOfDay = 12;
                            }
                        }

                        String selectedTime = hourOfDay + ":" + String.format("%02d", minute) + " " + period;
                        startTimeInput.setText(selectedTime);
                    }, hour, minute, false);

            timePickerDialog.show();
        }
        );

        endTimeInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(DetentionActivity.this,
                    (view, hourOfDay, minute1) -> {
                        String period;
                        if (hourOfDay >= 12) {
                            period = "PM";
                            if (hourOfDay > 12) {
                                hourOfDay -= 12;
                            }
                        } else {
                            period = "AM";
                            if (hourOfDay == 0) {
                                hourOfDay = 12;
                            }
                        }

                        String selectedTime = hourOfDay + ":" + String.format("%02d", minute) + " " + period;
                        endTimeInput.setText(selectedTime);
                    }, hour, minute, false);

            timePickerDialog.show();
        }
        );
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
        } else if (item.getItemId() == R.id.Profile) {
            fromDetention = true;
            startActivity(new Intent(DetentionActivity.this, ProfileActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public String intToString(int intToConvert) {
        String intConverted;
        intConverted = String.valueOf(intToConvert);
        return intConverted;
    }
}