package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.MainActivity;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.ui.auth.LoginActivity;
import com.example.myapplication.ui.auth.Register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();


        final TextView textView = binding.textHome;
        Button btn = binding.button2;
        FirebaseUser user = mAuth.getCurrentUser();
        btn.setText(user.getDisplayName());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "Logout success.", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getActivity(), LoginActivity.class));
                HomeFragment.this.getActivity().finish();
            }
        });


        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}