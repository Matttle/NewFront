package com.example.myapplication.ui.dashboard;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import android.Manifest;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDashboardBinding;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
    GoogleMap gMap;
    FrameLayout map;
    boolean mapFailed;
    private GoogleMap mMap;
    private Location mLastKnownLocation;
    private Location mPreviousLocation = new Location("initial");
    public static double totalMilesDriven;
    public static double allTimeMiles;
    public static String milesDrivenText;
    public static String estimatedPayText;
    private double milesDriven;
    private static final float DEFAULT_ZOOM = 0.15f;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1001;
    private static final double EARTH_RADIUS_MILES = 3958.8;
    public static double mEstimatedPay;
    public static double totalEstimatedPay;
    public static boolean milesVisible = false;

    public static double time = 0.0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button lunchBreak = binding.radioButton3;
        Button startTimer = binding.radioButton;
        Button stopTimer = binding.radioButton2;
        Button resetTimer = binding.resetButton;
        Button logBtn = binding.logDataBtn;
        timerTextView = binding.TimerClock;

        timerTextView.setText(getTimerText());

        timer = new Timer();

        milesDrivenText = "Miles Driven: " + round(totalMilesDriven);
        estimatedPayText = "Estimated Pay: $" + round(totalMilesDriven * 0.3);

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
                resetTrip();
            }
        }
        );

        lunchBreak.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (timerOn) {
                    binding.radioButton3.setChecked(true);
                    stopTimer();
                    AlertDialog.Builder lunchAlert = new AlertDialog.Builder(requireContext());
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
                            minToMs *= 60000;
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

        logBtn.setOnClickListener(v -> {
            binding.logDataBtn.setChecked(false);
            AlertDialog.Builder logAlert = new AlertDialog.Builder(requireActivity());
            logAlert.setTitle("Log Trip");
            logAlert.setMessage("Are you sure you want to log your trip data?");
            logAlert.setPositiveButton("Yes", (dialogInterface, i) -> {
                totalEstimatedPay += mEstimatedPay;
                allTimeMiles += totalMilesDriven;

                milesVisible = true;

                if (secondsTask != null) {
                    secondsTask.cancel();
                    time = 0.0;
                    stopTimer();
                    timerTextView.setText(formatTime(0, 0, 0));
                }
                if (hoursTask != null)
                    hoursTask.cancel();
                binding.textView2.setText("Hours Worked: 00");
                binding.textView3.setText("Miles Driven: 0");
                binding.textView4.setText("Estimated Pay: $0.00");
                mEstimatedPay = 0;
                totalMilesDriven = 0;
            }
            );

            logAlert.setNeutralButton("No", (dialogInterface, i) -> {

            }
            );

            logAlert.show();
        });

        map = binding.map;

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            locationTask = new TimerTask() {
                @Override
                public void run() {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                        == PackageManager.PERMISSION_GRANTED) {
                                    FusedLocationProviderClient fusedLocationClient =
                                            LocationServices.getFusedLocationProviderClient(getActivity());
                                    fusedLocationClient.getLastLocation()
                                            .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                                @Override
                                                public void onSuccess(Location location) {
                                                    if (location != null) {
                                                        if (UserMoved(location)) {
                                                            mLastKnownLocation = location;
                                                            try {
                                                                milesDriven = getMilesDriven(mPreviousLocation.getLongitude(), mPreviousLocation.getLatitude(), mLastKnownLocation.getLongitude(), mLastKnownLocation.getLatitude());
                                                            } catch (NullPointerException npe) {
                                                                Toast.makeText(getContext(), "noo", Toast.LENGTH_SHORT).show();
                                                            }
                                                            mPreviousLocation = location;
                                                        }

                                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                                new LatLng(mLastKnownLocation.getLatitude(),
                                                                        mLastKnownLocation.getLongitude()), 15f));
                                                    }
                                                }
                                            });
                                }
                                totalMilesDriven += milesDriven;
                                milesDriven = 0;
                                milesDrivenText = "Miles Driven: " + round(totalMilesDriven);
                                binding.textView3.setText(milesDrivenText);
                                mEstimatedPay = totalMilesDriven * 0.3;
                                estimatedPayText = "Estimated Pay: $" + round(totalMilesDriven * 0.3);
                                binding.textView4.setText(estimatedPayText);
                            } catch (IllegalStateException ise) {

                            }
                        }
                    });
                }
            };
            timer.scheduleAtFixedRate(locationTask, 1000, 100);
        }
        else {
            Toast.makeText(getContext(), "Map failed to load.", Toast.LENGTH_SHORT).show();
            mapFailed = true;
        }

        return root;
    }

    public void resetTrip() {
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
                binding.textView2.setText("Hours Worked: 00");
                binding.textView3.setText("Miles Driven: 0");
                binding.textView4.setText("Estimated Pay: $0.00");
                mEstimatedPay = 0;
                totalMilesDriven = 0;
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
                                    mPreviousLocation = location;
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

    public boolean UserMoved(Location currentLocation) {
        if (mLastKnownLocation != null && currentLocation != null) {
            return !(currentLocation.getLongitude() == mLastKnownLocation.getLongitude() &&
                    currentLocation.getLatitude() == mLastKnownLocation.getLatitude());
        }
        return false;
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
        binding = null;
    }
}