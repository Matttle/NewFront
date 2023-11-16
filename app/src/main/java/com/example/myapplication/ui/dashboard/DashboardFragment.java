package com.example.myapplication.ui.dashboard;

import static com.example.myapplication.MainActivity.myToolbar;
import static com.example.myapplication.ui.auth.LoginActivity.displayName;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.ui.auth.LoginActivity;
import com.example.myapplication.ui.detention.DetentionActivity;

import java.time.chrono.MinguoChronology;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;
import com.example.myapplication.ui.detention.DetentionActivity;

import java.time.chrono.MinguoChronology;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {

    private FragmentDashboardBinding binding;
    private TextView timerTextView;
    boolean timerOn;
    boolean onLunchBreak;
    Timer timer;
    TimerTask secondsTask;
    TimerTask hoursTask;
    /*TimerTask lunchTask = new TimerTask() { // Code for lunch timer functionality - Matt
        public void run() {
            if (!timerOn)
                startTimer();
            else
                stopTimer();
        }
    };*/

    GoogleMap gMap;
    FrameLayout map;
    boolean mapFailed;

    public static double time = 0.0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        android.widget.Button lunchBreak = binding.radioButton3;
        android.widget.Button startTimer = binding.radioButton;
        android.widget.Button stopTimer = binding.radioButton2;
        android.widget.Button resetTimer = binding.resetButton;
        timerTextView = binding.TimerClock;

        timerTextView.setText(getTimerText());

        timer = new Timer();


        startTimer.setOnClickListener(new View.OnClickListener() {
            public  void onClick(View view) {
                binding.radioButton.setChecked(false);
                startTimer();
            }
        }
        );

        stopTimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                binding.radioButton2.setChecked(false);
                stopTimer();
            }
        }
        );

        resetTimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                binding.resetButton.setChecked(false);
                resetTimer();
            }
        }
        );

        lunchBreak.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (timerOn) {
                    binding.radioButton3.setChecked(true);
                    stopTimer();
                    onLunchBreak = true;
                    /*AlertDialog.Builder lunchAlert = new AlertDialog.Builder(requireActivity()); // Code for lunch timer functionality - Matt
                    lunchAlert.setTitle("Lunch Break");
                    lunchAlert.setMessage("Enter the time you'll be on lunch. (Minutes)");
                    final EditText inputText = new EditText(getActivity());
                    inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    inputText.setRawInputType(Configuration.KEYBOARD_12KEY);
                    lunchAlert.setView(inputText);
                    lunchAlert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                            timer.scheduleAtFixedRate(lunchTask, getMinutesForLunch(inputText.toString()), 10000);
                        }
                    }
                    );

                    lunchAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            binding.radioButton3.setChecked(false);
                        }
                    }
                    );
                    lunchAlert.show();*/
                }
                else {
                    if (onLunchBreak) {
                        startTimer();
                    }
                    binding.radioButton3.setChecked(false);
                    /*if (lunchTask != null) // Code for lunch timer functionality - Matt
                        lunchTask.cancel();*/
                }
            }
        }
        );

        map = binding.map;

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        else {
            Toast.makeText(getContext(), "Map failed to load.", Toast.LENGTH_SHORT).show();
            mapFailed = true;
        }

        return root;
    }

    public void resetTimer() {
        timerTextView = (TextView) binding.TimerClock;
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(requireActivity());
        resetAlert.setTitle("Reset Time");
        resetAlert.setMessage("Are you sure you want to reset your time?");
        resetAlert.setPositiveButton("Reset", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(secondsTask != null) {
                    secondsTask.cancel();
                    time = 0.0;
                    stopTimer();
                    timerTextView.setText(formatTime(0,0,0));
                }
                if (hoursTask != null)
                    hoursTask.cancel();
            }
        }
        );

        resetAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }
        );

        resetAlert.show();
    }

    public void startTimer(){
        binding.radioButton3.setChecked(false);
        onLunchBreak = false;
        timerTextView = (TextView) binding.TimerClock;
        if (!timerOn) {
            timerOn = true;
            secondsTask = new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            time++;
                            timerTextView.setText(getTimerText());
                        }
                    });
                }
            };
            timer.scheduleAtFixedRate(secondsTask, 0, 1000);
            hoursTask = new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.textView2.setText("Hours Worked: " + getHours());
                        }
                    });
                }
            };
            timer.scheduleAtFixedRate(hoursTask, 0, 36000000);
        }
    }

    public void stopTimer(){
        if (timerOn) {
            secondsTask.cancel();
            hoursTask.cancel();
            timerOn = false;
        }
    }

    private String getTimerText(){
        int round = (int) Math.round(time);

        int seconds = ((round % 86400) % 3600) % 60;
        int minutes = ((round % 86400) % 3600) / 60;
        int hours = ((round % 86400) / 3600);

        return (String) formatTime(seconds, minutes, hours);
    }

    private String getHours(){
        int round = (int) Math.round(time);

        int hours = ((round % 86400) / 3600);

        return String.format("%02d",hours);
    }

    private String formatTime(int seconds, int minutes, int hours){
        return String.format("%02d",hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }

    /*private long getMinutesForLunch(String string) { // Code for lunch timer functionality - Matt
        long minToMs = 0;
        try {
            minToMs = Long.parseLong(string);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        minToMs *= 6000;

        return minToMs;
    }

    private void startLunch() {

    }*/

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (!mapFailed) {
            this.gMap = googleMap;

            com.google.android.gms.maps.model.LatLng mapUnitedStates = new LatLng(37.0902, 95.7129);
            this.gMap.addMarker(new MarkerOptions().position(mapUnitedStates).title("Marker in US"));
            this.gMap.moveCamera(CameraUpdateFactory.newLatLng(mapUnitedStates));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timerOn)
            secondsTask.cancel();
        binding = null;
    }
}