package com.example.myapplication.ui.payment;

import static com.example.myapplication.ui.home.HomeFragment.fromHome;
import static com.example.myapplication.ui.profile.ProfileActivity.fromProfile;

import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
//import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.profile.ProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private static final String PREF_NAME = "ExpenseData";
    private static final String KEY_REVENUE = "revenue";
    private static final String KEY_EXPENSES = "expenses";
    private static final String KEY_BLOCK_COUNT = "blockCount";

    public static ConstraintLayout constraintLayout;
    public static CardView expenseCardView;
    private EditText editTextDescription, editTextAmount;
    private DatePicker datePicker;
    public LinearLayout blockContainer;
    public ScrollView scrollView;
    public static ArrayList<View> blockViews;
    public static double revenue;
    public static double expenses;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        FloatingActionButton fabAdd;
        TextView saveButton;
        TextView cancelButton;

        fabAdd = findViewById(R.id.fabAdd);
        saveButton = findViewById(R.id.save);
        cancelButton = findViewById(R.id.cancel);
        blockContainer = findViewById(R.id.blockContainer);
        scrollView = findViewById(R.id.scrollView);
        constraintLayout = findViewById(R.id.paymentConstraint);
        expenseCardView = findViewById(R.id.expenseAdd);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextAmount = findViewById(R.id.editTextAmount);
        datePicker = findViewById(R.id.datePicker);
        Button back = findViewById(R.id.btnBackProfile);

        loadExpenseData();

        back.setOnClickListener(v -> {
            if (fromHome) {
                fromHome = false;
                startActivity(new Intent(PaymentActivity.this, MainActivity.class));
            }
            if (fromProfile) {
                fromProfile = false;
                startActivity(new Intent(PaymentActivity.this, ProfileActivity.class));
            }
        });
        if (blockViews == null)
            blockViews = new ArrayList<>();

        fabAdd.setOnClickListener(v -> showExpenseCardView());

        saveButton.setOnClickListener(v -> {
            saveExpense();
            hideExpenseCardView();
        });

        cancelButton.setOnClickListener(v -> hideExpenseCardView());
    }

    private void showExpenseCardView() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        constraintSet.setVisibility(R.id.expenseAdd, ConstraintSet.VISIBLE);

        TransitionCardView transitionCardView = new TransitionCardView(expenseCardView, false);
        transitionCardView.setDuration(300);
        expenseCardView.startAnimation(transitionCardView);
        constraintSet.applyTo(constraintLayout);
    }

    private void hideExpenseCardView() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        constraintSet.setVisibility(R.id.expenseAdd, ConstraintSet.INVISIBLE);

        TransitionCardView transitionCardView = new TransitionCardView(expenseCardView, false);
        transitionCardView.setDuration(300);
        expenseCardView.startAnimation(transitionCardView);
        constraintSet.applyTo(constraintLayout);
    }

    private void saveExpense() {
        String description = editTextDescription.getText().toString();
        String amount = editTextAmount.getText().toString();
        String date = getDateFromDatePicker(datePicker);

        View blockView = getLayoutInflater().inflate(R.layout.payment_block, getContentScene().getSceneRoot());
        TextView textViewDescription = blockView.findViewById(R.id.blockDescription);
        TextView textViewAmount = blockView.findViewById(R.id.blockAmount);
        TextView textViewDate = blockView.findViewById(R.id.blockDate);
        ImageView imageViewDelete = blockView.findViewById(R.id.blockDelete);

        textViewDescription.setText(description);
        textViewAmount.setText(MessageFormat.format("${0}", amount));
        textViewDate.setText(date);

        try {
            if (Double.parseDouble(amount) >= 0.01) {
                textViewAmount.setTextColor(Color.rgb(120, 255, 120));
                revenue += Double.parseDouble(amount);
            } else {
                textViewAmount.setTextColor(Color.rgb(255, 140, 140));
                expenses += Double.parseDouble(amount);
            }
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        imageViewDelete.setOnClickListener(v -> {
            blockContainer.removeView(blockView);
            blockViews.remove(blockView);
            updateRevenue();
            updateExpenses();
            saveExpenseData();
        });

        blockContainer.addView(blockView, 0);
        blockViews.add(blockView);

        scrollView.fullScroll(View.FOCUS_UP);
    }

    private void loadExpenseData() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        revenue = preferences.getFloat(KEY_REVENUE, 0);
        expenses = preferences.getFloat(KEY_EXPENSES, 0);
        int blockCount = preferences.getInt(KEY_BLOCK_COUNT, 0);

        try {
            blockContainer.removeAllViews();
            blockViews.clear();
        } catch (NullPointerException npe) {
            Toast.makeText(this, "Null Pointer Exception", Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < blockCount; i++) {
            String description = preferences.getString("blockDescription" + i, "");
            String amount = preferences.getString("blockAmount" + i, "");
            String date = preferences.getString("blockDate" + i, "");

            View blockView = getLayoutInflater().inflate(R.layout.payment_block, getContentScene().getSceneRoot());
            TextView textViewDescription = blockView.findViewById(R.id.blockDescription);
            TextView textViewAmount = blockView.findViewById(R.id.blockAmount);
            TextView textViewDate = blockView.findViewById(R.id.blockDate);
            ImageView imageViewDelete = blockView.findViewById(R.id.blockDelete);

            textViewDescription.setText(description);
            textViewAmount.setText(amount);
            try {
                double amountValue = Double.parseDouble(amount.replace("$", ""));
                if (amountValue >= 0.01) {
                    textViewAmount.setTextColor(Color.rgb(120, 255, 120));
                } else {
                    textViewAmount.setTextColor(Color.rgb(255, 140, 140));
                }
            } catch (NumberFormatException nfe) {
                Toast.makeText(this, "Number Format Exception", Toast.LENGTH_SHORT).show();
            }
            textViewDate.setText(date);

            imageViewDelete.setOnClickListener(v -> {
                blockContainer.removeView(blockView);
                blockViews.remove(blockView);
                updateRevenue();
                updateExpenses();
                saveExpenseData();
            });

            try {
                blockContainer.addView(blockView, 0);
                blockViews.add(blockView);
            } catch (NullPointerException npe) {
                Toast.makeText(this, "Number Pointer Exception", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveExpenseData() {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putFloat(KEY_REVENUE, (float) revenue);
        editor.putFloat(KEY_EXPENSES, (float) expenses);

        for (int i = 0; i < blockViews.size(); i++) {
            View blockView = blockViews.get(i);
            TextView textViewDescription = blockView.findViewById(R.id.blockDescription);
            TextView textViewAmount = blockView.findViewById(R.id.blockAmount);
            TextView textViewDate = blockView.findViewById(R.id.blockDate);

            editor.putString("blockDescription" + i, textViewDescription.getText().toString());
            editor.putString("blockAmount" + i, textViewAmount.getText().toString());
            editor.putString("blockDate" + i, textViewDate.getText().toString());
        }

        editor.putInt(KEY_BLOCK_COUNT, blockViews.size());
        editor.apply();
    }

    private void updateRevenue() {
        revenue = 0;

        for (View blockView : blockViews) {
            TextView textViewAmount = blockView.findViewById(R.id.blockAmount);
            try {
                double amountValue = Double.parseDouble(textViewAmount.getText().toString().replace("$", ""));
                if (amountValue >= 0.01) {
                    revenue += amountValue;
                }
            } catch (NumberFormatException nfe) {
                Toast.makeText(this, "Number Format Exception", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateExpenses() {
        expenses = 0;

        for (View blockView : blockViews) {
            TextView textViewAmount = blockView.findViewById(R.id.blockAmount);
            try {
                double amountValue = Double.parseDouble(textViewAmount.getText().toString().replace("$", ""));
                if (amountValue < -0.01) {
                    expenses += amountValue;
                }
            } catch (NumberFormatException nfe) {
                Toast.makeText(this, "Number Format Exception", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        updateRevenue();
        updateExpenses();
        saveExpenseData();
    }


    private String getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // month starts at 0 so +1 is normal
        int year = datePicker.getYear();

        return String.format(Locale.US, "%02d/%02d/%d", month, day, year);
    }

    public class TransitionCardView extends Animation {
        private final CardView cardView;
        private final boolean expanding;

        private final int initialHeight;
        private final int targetHeight;

        public TransitionCardView(CardView cardView, boolean expanding) {
            this.cardView = cardView;
            this.expanding = expanding;

            initialHeight = cardView.getHeight();
            targetHeight = expanding ? cardView.getChildAt(0).getHeight() : 0;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            cardView.getLayoutParams().height = (int) (initialHeight + (targetHeight - initialHeight) * interpolatedTime);
            cardView.requestLayout();

            if (!expanding) {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        }
    }
}