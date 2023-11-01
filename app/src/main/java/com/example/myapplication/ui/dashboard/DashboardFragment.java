package com.example.myapplication.ui.dashboard;

import static com.example.myapplication.ui.auth.LoginActivity.displayName;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.MainActivity;
import com.example.myapplication.ui.auth.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.ui.detention.DetentionActivity;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private TextView timerTextView;
    private TextView username;
    boolean timerOn;
    Timer timer;
    TimerTask task;
    public static double time = 0.0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageView menubtn = binding.imageView2;
        android.widget.Button startTimer = binding.radioButton;
        android.widget.Button stopTimer = binding.radioButton2;
        android.widget.Button resetTimer = binding.resetButton;
        timerTextView = (TextView) binding.TimerClock;
        username = (TextView) binding.username;

        if (username != null)
            username.setText(displayName);
        timerTextView.setText(getTimerText());

        timer = new Timer();

        menubtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), DetentionActivity.class));
            }
        }
        );

        startTimer.setOnClickListener(new View.OnClickListener() {
            public  void onClick(View view) {

                startTimer();
            }
        }
        );

        stopTimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stopTimer();
            }
        }
        );

        resetTimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                resetTimer();
            }
        }
        );

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

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
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(task != null)
                {
                    task.cancel();
                    time = 0.0;
                    stopTimer();
                    timerTextView.setText(formatTime(0,0,0));
                }
            }
        });

        resetAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //do nothing
            }
        });

        resetAlert.show();
    }

    public void startTimer(){
        timerTextView = (TextView) binding.TimerClock;
        if (!timerOn) {
            timerOn = true;
            task = new TimerTask() {
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
            timer.scheduleAtFixedRate(task, 0, 1000);
        }
    }

    public void stopTimer(){
        if (timerOn) {
            task.cancel();
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

    private String formatTime(int seconds, int minutes, int hours){
        return String.format("%02d",hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timerOn)
            task.cancel();
        binding = null;
    }
}