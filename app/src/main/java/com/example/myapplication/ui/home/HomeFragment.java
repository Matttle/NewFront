package com.example.myapplication.ui.home;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myapplication.ui.dashboard.DashboardFragment.allTimeMiles;
import static com.example.myapplication.ui.dashboard.DashboardFragment.mEstimatedPay;
import static com.example.myapplication.ui.dashboard.DashboardFragment.milesVisible;
import static com.example.myapplication.ui.dashboard.DashboardFragment.round;
import static com.example.myapplication.ui.dashboard.DashboardFragment.totalEstimatedPay;
import static com.example.myapplication.ui.payment.PaymentActivity.expenses;
import static com.example.myapplication.ui.payment.PaymentActivity.revenue;
import static com.example.myapplication.ui.profile.ProfileActivity.mName;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.ui.auth.LoginActivity;
import com.example.myapplication.ui.auth.Register;
import com.example.myapplication.ui.payment.PaymentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    public static boolean fromHome;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();

        binding.cardTransactions.setOnClickListener(v -> {
            fromHome = true;
            startActivity(new Intent(getContext(), PaymentActivity.class));
        });

        if (!Objects.equals(mName, "Enter your name"))
            binding.welcomeUser.setText(mName);

        if (round(mEstimatedPay) == 0.0)
            binding.revenue.setText("$" + round(totalEstimatedPay) + "0");
        else
            binding.revenue.setText("$" + round(totalEstimatedPay));

        binding.miles.setText("" + round(allTimeMiles));

        if (milesVisible) {
            binding.mileageTxt.setVisibility(View.INVISIBLE);
            binding.mileageBlock.setVisibility(View.VISIBLE);
        }

        revenue += Double.parseDouble(binding.revenue.getText().toString().replaceAll("[^\\d.]+", ""));
        binding.revenue.setText("$" + revenue);
        revenue = 0;

        expenses += Double.parseDouble(binding.expenses.getText().toString().replaceAll("[^\\d.]+", ""));
        binding.expenses.setText("$" + expenses);
        expenses = 0;

        // profit = revenue - expenses but complicated looking.
        double profit = Double.parseDouble(binding.revenue.getText().toString().replaceAll("[^\\d.]+", "")) - Double.parseDouble(binding.expenses.getText().toString().replaceAll("[^\\d.]+", ""));

        if (profit == 0)
            binding.profit.setText("$" + profit + "0");
        else
            binding.profit.setText("$" + profit);

        if (binding.revenue.getText().toString().equals("$0.0"))
            binding.revenue.setText("$0.00");

        if (binding.expenses.getText().toString().equals("$0.0"))
            binding.expenses.setText("$0.00");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}