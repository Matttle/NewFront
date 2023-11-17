package com.example.myapplication.ui.dashboard;

import static com.example.myapplication.MainActivity.myToolbar;
import static com.example.myapplication.ui.auth.LoginActivity.displayName;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import android.Manifest;
import com.example.myapplication.R;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.ui.auth.LoginActivity;
import com.example.myapplication.ui.detention.DetentionActivity;

import java.text.DecimalFormat;
import java.time.chrono.MinguoChronology;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;
import com.example.myapplication.ui.detention.DetentionActivity;

import java.time.chrono.MinguoChronology;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {

    private FragmentDashboardBinding binding;
    private TextView timerTextView;
    boolean timerOn;
    Timer timer;
    TimerTask secondsTask;
    TimerTask hoursTask;
    TimerTask lunchTask;
    boolean onLunch;
    private int minToMs;
    TimerTask locationTask;
    TimerTask prevLocationTask;
    GoogleMap gMap;
    FrameLayout map;
    boolean mapFailed;
    private GoogleMap mMap;
    private Location mLastKnownLocation;
    private Location mPreviousLocation;
    public static double totalMilesDriven;
    private double milesDriven;
    private static float DEFAULT_ZOOM = 0.15f;
    private static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1001;
    private static final double EARTH_RADIUS_MILES = 3958.8;
    public static double mEstimatedPay;

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
                    AlertDialog.Builder lunchAlert = new AlertDialog.Builder(requireContext()); // Code for lunch timer functionality - Matt
                    lunchAlert.setTitle("Lunch Break");
                    lunchAlert.setMessage("Enter the time you'll be on lunch. (Minutes)");
                    final EditText inputText = new EditText(getActivity());
                    inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    inputText.setRawInputType(Configuration.KEYBOARD_12KEY);
                    lunchAlert.setView(inputText);
                    lunchAlert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                            lunchTask = new TimerTask() {
                                @Override
                                public void run() {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            onLunch = !onLunch;
                                            if (!onLunch){
                                                startTimer();
                                                lunchTask.cancel();
                                            }
                                        }
                                    });
                                }
                            };
                            try {
                                minToMs = Integer.parseInt(inputText.getText().toString());
                            }
                            catch (NumberFormatException nfe) {
                                Toast.makeText(getContext(), "Invalid", Toast.LENGTH_SHORT).show();
                                minToMs = 0;
                            }
                            minToMs *= 6000;
                            try {
                                timer.scheduleAtFixedRate(lunchTask, 0, minToMs);
                            }
                            catch (IllegalArgumentException illegalArgumentException) {
                                timer.scheduleAtFixedRate(lunchTask, 0, 1000);
                            }
                        }
                    }
                    );

                    lunchAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            binding.radioButton3.setChecked(false);
                            startTimer();
                        }
                    }
                    );
                    lunchAlert.show();
                }
                else {
                    binding.radioButton3.setChecked(false);
                    Toast.makeText(getContext(), "You must be on the clock to take lunch!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        );

        map = binding.map;

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            locationTask = new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED) {
                                FusedLocationProviderClient fusedLocationClient =
                                        LocationServices.getFusedLocationProviderClient(getActivity());
                                fusedLocationClient.getLastLocation()
                                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                            @Override
                                            public void onSuccess(Location location) {
                                                if (location != null) {
                                                    mLastKnownLocation = location;
                                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                            new LatLng(mLastKnownLocation.getLatitude(),
                                                                    mLastKnownLocation.getLongitude()), 15f));
                                                }
                                            }
                                        });
                            }
                            if (mPreviousLocation != null)
                                milesDriven = getMilesDriven(mPreviousLocation.getLongitude(), mPreviousLocation.getLatitude(), mLastKnownLocation.getLongitude(), mLastKnownLocation.getLatitude());
                            else
                                milesDriven = 0;
                            totalMilesDriven += milesDriven;
                            String milesDrivenText = "Miles Driven: " + round(totalMilesDriven);
                            binding.textView3.setText(milesDrivenText);
                            mEstimatedPay = totalMilesDriven * 0.3;
                            String estimatedPay = "Estimated Pay: $" + round(totalMilesDriven * 0.3);
                            binding.textView4.setText(estimatedPay);
                            mPreviousLocation = null;
                        }
                    });
                }
            };
            timer.scheduleAtFixedRate(locationTask, 5000, 1000);
            prevLocationTask = new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mLastKnownLocation != null)
                                mPreviousLocation = mLastKnownLocation;
                        }
                    });
                }
            };
            timer.scheduleAtFixedRate(prevLocationTask, 5000, 5000);
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
        timerTextView = binding.TimerClock;
        if (!timerOn) {
            timerOn = true;
            onLunch = false;
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
            timer.scheduleAtFixedRate(hoursTask, 0, 3600000);
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

    private int getMinutesForLunch(String string) { // Code for lunch timer functionality - Matt
        int minToMs = 0;
        try {
            minToMs = Integer.parseInt(string);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        minToMs *= 6000;

        return minToMs;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    return false;
                }
            });
            mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                }
            });

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                FusedLocationProviderClient fusedLocationClient =
                        LocationServices.getFusedLocationProviderClient(getActivity());
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    mLastKnownLocation = location;
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(mLastKnownLocation.getLatitude(),
                                                    mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                }
                            }
                        });
            }

        } else {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public static double getMilesDriven(double prevLon, double prevLat, double currLon, double currLat) {
        double prevLatRad = Math.toRadians(prevLat);
        double prevLonRad = Math.toRadians(prevLon);
        double currLatRad = Math.toRadians(currLat);
        double currLonRad = Math.toRadians(currLon);

        double deltaLat = currLatRad - prevLatRad;
        double deltaLon = currLonRad - prevLonRad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(prevLatRad) * Math.cos(currLatRad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS_MILES * c;

        return distance;
    }

    public static double round(double num) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(num));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timerOn)
            secondsTask.cancel();
        locationTask.cancel();
        prevLocationTask.cancel();
        binding = null;
    }
}