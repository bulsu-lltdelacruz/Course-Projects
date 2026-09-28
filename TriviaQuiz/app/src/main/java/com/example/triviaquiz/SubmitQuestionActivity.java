package com.example.triviaquiz;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utility.CurrentUser;
import utility.StringValues;
import utility.ThemeManager;

public class SubmitQuestionActivity extends AppCompatActivity {

    private EditText etQuestion, etCorrectAnswer, etWrong1, etWrong2, etWrong3;
    private Button btnDiffEasy, btnDiffMedium, btnDiffHard, btnSubmit;
    private String selectedDifficulty = StringValues.DIFFICULTY_EASY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_question);

        getWindow().setSoftInputMode(
                android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                        android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ScrollView scrollView = findViewById(R.id.scrollView);
        ViewTreeObserver.OnGlobalLayoutListener keyboardListener = () -> {
            android.graphics.Rect r = new android.graphics.Rect();
            scrollView.getWindowVisibleDisplayFrame(r);
            int screenHeight = scrollView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;

            if (keypadHeight > screenHeight * 0.15f) {

                View focused = getCurrentFocus();
                if (focused != null) {
                    scrollView.post(() -> {
                        int[] location = new int[2];
                        focused.getLocationInWindow(location);
                        int scrollTo = location[1] - (screenHeight / 2);
                        scrollView.smoothScrollTo(0, scrollView.getScrollY() + scrollTo);
                    });
                }
            }
        };
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(keyboardListener);

        etQuestion      = findViewById(R.id.etQuestion);
        etCorrectAnswer = findViewById(R.id.etCorrectAnswer);
        etWrong1        = findViewById(R.id.etWrong1);
        etWrong2        = findViewById(R.id.etWrong2);
        etWrong3        = findViewById(R.id.etWrong3);
        btnDiffEasy     = findViewById(R.id.btnDiffEasy);
        btnDiffMedium   = findViewById(R.id.btnDiffMedium);
        btnDiffHard     = findViewById(R.id.btnDiffHard);
        btnSubmit       = findViewById(R.id.btnSubmit);

        ((ImageView) findViewById(R.id.btnBack)).setOnClickListener(v -> finish());

        selectDifficulty(StringValues.DIFFICULTY_EASY);

        btnDiffEasy.setOnClickListener(v   -> selectDifficulty(StringValues.DIFFICULTY_EASY));
        btnDiffMedium.setOnClickListener(v -> selectDifficulty(StringValues.DIFFICULTY_MEDIUM));
        btnDiffHard.setOnClickListener(v   -> selectDifficulty(StringValues.DIFFICULTY_HARD));
        btnSubmit.setOnClickListener(v     -> submitQuestion());
    }

    private void selectDifficulty(String difficulty) {
        selectedDifficulty = difficulty;
        int activeColor   = ContextCompat.getColor(this, R.color.primaryRed);
        int inactiveColor = ThemeManager.getColorFromAttr(this, R.attr.cardSurface);

        btnDiffEasy.setBackgroundTintList(ColorStateList.valueOf(
                difficulty.equals(StringValues.DIFFICULTY_EASY)   ? ContextCompat.getColor(this, R.color.easyGreenTextHead) : inactiveColor));
        btnDiffMedium.setBackgroundTintList(ColorStateList.valueOf(
                difficulty.equals(StringValues.DIFFICULTY_MEDIUM) ? ContextCompat.getColor(this, R.color.mediumOrangeTextHead) : inactiveColor));
        btnDiffHard.setBackgroundTintList(ColorStateList.valueOf(
                difficulty.equals(StringValues.DIFFICULTY_HARD)   ? ContextCompat.getColor(this, R.color.hardRedTextHead) : inactiveColor));

        btnDiffEasy.setTextColor(ColorStateList.valueOf(
                difficulty.equals(StringValues.DIFFICULTY_EASY)   ? ThemeManager.getColorFromAttr(this, R.attr.textPrimary) : ThemeManager.getColorFromAttr(this, R.attr.textSubtitle)));
        btnDiffMedium.setTextColor(ColorStateList.valueOf(
                difficulty.equals(StringValues.DIFFICULTY_MEDIUM)   ? ThemeManager.getColorFromAttr(this, R.attr.textPrimary) : ThemeManager.getColorFromAttr(this, R.attr.textSubtitle)));
        btnDiffHard.setTextColor(ColorStateList.valueOf(
                difficulty.equals(StringValues.DIFFICULTY_HARD)   ? ThemeManager.getColorFromAttr(this, R.attr.textPrimary) : ThemeManager.getColorFromAttr(this, R.attr.textSubtitle)));
    }

    private void submitQuestion() {
        String question = etQuestion.getText().toString().trim();
        String correct  = etCorrectAnswer.getText().toString().trim();
        String wrong1   = etWrong1.getText().toString().trim();
        String wrong2   = etWrong2.getText().toString().trim();
        String wrong3   = etWrong3.getText().toString().trim();

        if (TextUtils.isEmpty(question)) { etQuestion.setError("Required"); return; }
        if (TextUtils.isEmpty(correct))  { etCorrectAnswer.setError("Required"); return; }
        if (TextUtils.isEmpty(wrong1))   { etWrong1.setError("Required"); return; }
        if (TextUtils.isEmpty(wrong2))   { etWrong2.setError("Required"); return; }
        if (TextUtils.isEmpty(wrong3))   { etWrong3.setError("Required"); return; }

        btnSubmit.setEnabled(false);
        btnSubmit.setText("Submitting...");

        String uid      = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        Map<String, Object> entry = new HashMap<>();
        entry.put("question",             question);
        entry.put("correct_answer",       correct);
        entry.put("incorrect_answers",    Arrays.asList(wrong1, wrong2, wrong3));
        entry.put("difficulty",           selectedDifficulty);
        entry.put("submittedBy",          uid);
        entry.put("submittedByUsername",  username);
        entry.put("timestamp",            System.currentTimeMillis());

        FirebaseDatabase.getInstance()
                .getReference("pendingQuestions")
                .push()
                .setValue(entry)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Question submitted for review!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Submission failed. Try again.", Toast.LENGTH_SHORT).show();
                        btnSubmit.setEnabled(true);
                        btnSubmit.setText("Submit Question");
                    }
                });
    }
}