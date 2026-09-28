package com.example.triviaquiz;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utility.CommunityQuestion;
import utility.QuizStats;
import utility.StringValues;
import utility.ThemeManager;

public class CommunityQuizActivity extends AppCompatActivity {

    private TextView tvQuestionProgress;
    private View viewProgressBar, viewProgressBarTotal, loadingOverlay;
    private TextView tvQuestionText;
    private LinearLayout btnNextQuestion;
    private LinearLayout[] btnOptions;

    private List<CommunityQuestion> questions = new ArrayList<>();
    private List<String> selectedQuestionKeys  = new ArrayList<>();

    private int currentQuestionNumber = 0;
    private int correctAnswers        = 0;
    private int correctAnswerIndex    = 0;
    private long[] timeTaken          = new long[5];
    private long questionStartTime;
    private CountDownTimer countDownTimer;
    private static final int MAX_TIME_MS  = 15000;
    private static final int REQUIRED_QS  = 5;
    String selectedDifficulty;

    private DatabaseReference historyRef;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        String difficulty = getIntent().getStringExtra(StringValues.DIFFICULTY_VAR);
        if (difficulty == null) difficulty = StringValues.DIFFICULTY_MEDIUM;
        selectedDifficulty = difficulty;

        uid        = FirebaseAuth.getInstance().getCurrentUser().getUid();
        historyRef = FirebaseDatabase.getInstance()
                .getReference("userQuestionHistory")
                .child(uid);

        tvQuestionProgress   = findViewById(R.id.tvQuestionProgress);
        viewProgressBar      = findViewById(R.id.viewProgressBar);
        viewProgressBarTotal = findViewById(R.id.viewProgressBarTotal);
        tvQuestionText       = findViewById(R.id.tvQuestionText);
        btnNextQuestion      = findViewById(R.id.btnNextQuestion);
        loadingOverlay       = findViewById(R.id.loadingOverlay);

        btnOptions    = new LinearLayout[4];
        btnOptions[0] = findViewById(R.id.btnOptionA);
        btnOptions[1] = findViewById(R.id.btnOptionB);
        btnOptions[2] = findViewById(R.id.btnOptionC);
        btnOptions[3] = findViewById(R.id.btnOptionD);

        btnNextQuestion.setVisibility(INVISIBLE);
        showLoading(true);
        loadQuestions();
    }

    private void loadQuestions() {
        // First get the user's history
        historyRef.get().addOnCompleteListener(historyTask -> {
            Set<String> seenKeys = new HashSet<>();

            if (historyTask.isSuccessful() && historyTask.getResult() != null) {
                for (DataSnapshot snap : historyTask.getResult().getChildren())
                    seenKeys.add(snap.getKey());
            }

            fetchApprovedQuestions(seenKeys);
        });
    }


    private void fetchApprovedQuestions(Set<String> seenKeys) {
        DatabaseReference approvedRef = FirebaseDatabase.getInstance()
                .getReference("approvedQuestions");

        approvedRef.get().addOnCompleteListener(task -> {
            showLoading(false);

            if (!task.isSuccessful() || task.getResult() == null) {
                Toast.makeText(this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            List<CommunityQuestion> unseenQuestions = new ArrayList<>();
            List<String>            unseenKeys      = new ArrayList<>();

            for (DataSnapshot snap : task.getResult().getChildren()) {
                String key      = snap.getKey();
                String q        = snap.child("question").getValue(String.class);
                String correct  = snap.child("correct_answer").getValue(String.class);
                String snapDiff = snap.child("difficulty").getValue(String.class);

                List<String> wrong = new ArrayList<>();
                for (DataSnapshot w : snap.child("incorrect_answers").getChildren())
                    wrong.add(w.getValue(String.class));

                boolean matchesDifficulty = selectedDifficulty.equalsIgnoreCase(snapDiff);
                boolean notSeen           = !seenKeys.contains(key);
                boolean validData         = q != null && correct != null && wrong.size() >= 3;

                if (validData && matchesDifficulty && notSeen) {
                    unseenQuestions.add(new CommunityQuestion(q, correct, wrong));
                    unseenKeys.add(key);
                }
            }

            if (unseenQuestions.size() < REQUIRED_QS) {
                int available = unseenQuestions.size();
               // if (available == 0) {
                    Toast.makeText(this,
                            "You've seen all " + selectedDifficulty + " community questions!" +
                                    "Check back later for new ones.",
                            Toast.LENGTH_LONG).show();
                /*} else {
                    Toast.makeText(this,
                            "Only " + available + " unseen " + selectedDifficulty +
                                    " questions available. Need " + REQUIRED_QS + " to start a quiz.",
                            Toast.LENGTH_LONG).show();
                }*/
                finish();
                return;
            }

            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < unseenQuestions.size(); i++) indices.add(i);
            Collections.shuffle(indices);

            for (int i = 0; i < REQUIRED_QS; i++) {
                int idx = indices.get(i);
                questions.add(unseenQuestions.get(idx));
                selectedQuestionKeys.add(unseenKeys.get(idx));
            }

            timeTaken = new long[questions.size()];
            addListeners();
            GetNextQuestion();
        });
    }


    private void markQuestionsAsSeen() {
        for (String key : selectedQuestionKeys) {
            historyRef.child(key).setValue(true);
        }
    }


    private void addListeners() {
        for (int i = 0; i < btnOptions.length; i++) {
            int finalI = i;
            btnOptions[i].setOnClickListener(v -> {
                if (countDownTimer != null) countDownTimer.cancel();
                timeTaken[currentQuestionNumber - 1] =
                        System.currentTimeMillis() - questionStartTime;
                btnNextQuestion.setVisibility(VISIBLE);
                setButtonsClickable(false);

                if (finalI == correctAnswerIndex) {
                    markCorrect(finalI);
                    correctAnswers++;
                } else {
                    markWrong(finalI);
                    markCorrect(correctAnswerIndex);
                }
            });
        }
        btnNextQuestion.setOnClickListener(v -> {
            GetNextQuestion();
            setButtonsClickable(true);
            resetButtonAppearance();
        });
    }

    private void GetNextQuestion() {
        if (currentQuestionNumber >= questions.size()) {
            if (countDownTimer != null) countDownTimer.cancel();
            markQuestionsAsSeen();

            Intent intent = new Intent(this, QuizDoneActivity.class);
            intent.putExtra("quizStats",
                    new QuizStats(correctAnswers, timeTaken, selectedDifficulty));
            startActivity(intent);
            finish();
            return;
        }

        btnNextQuestion.setVisibility(INVISIBLE);
        tvQuestionProgress.setText("Question " + (currentQuestionNumber + 1)
                + " / " + questions.size());

        CommunityQuestion q = questions.get(currentQuestionNumber);
        tvQuestionText.setText(q.question);

        List<String> allAnswers = new ArrayList<>(q.incorrectAnswers);
        allAnswers.add(q.correctAnswer);
        Collections.shuffle(allAnswers);
        correctAnswerIndex = allAnswers.indexOf(q.correctAnswer);

        for (int i = 0; i < 4; i++) {
            TextView option = (TextView) btnOptions[i].getChildAt(1);
            option.setText(allAnswers.get(i));
        }

        currentQuestionNumber++;
        startTimer(currentQuestionNumber - 1);
    }

    private void startTimer(int questionIndex) {
        if (countDownTimer != null) countDownTimer.cancel();
        questionStartTime = System.currentTimeMillis();

        countDownTimer = new CountDownTimer(MAX_TIME_MS, 100) {
            public void onTick(long ms) {
                float p = (float) ms / MAX_TIME_MS;
                viewProgressBar.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, p));
                viewProgressBarTotal.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, 1f - p));
            }
            public void onFinish() {
                timeTaken[questionIndex] = MAX_TIME_MS;
                btnNextQuestion.setVisibility(VISIBLE);
                setButtonsClickable(false);
                markCorrect(correctAnswerIndex);
            }
        }.start();
    }

    private void markCorrect(int i) {
        TextView b = (TextView) btnOptions[i].getChildAt(0);
        b.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.successGreen));
        b.setTextColor(ContextCompat.getColor(this, R.color.surfaceWhite));
        btnOptions[i].setBackgroundResource(R.drawable.bg_card_quiz_correct);
    }

    private void markWrong(int i) {
        TextView b = (TextView) btnOptions[i].getChildAt(0);
        b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F43F5E")));
        b.setTextColor(ContextCompat.getColor(this, R.color.surfaceWhite));
        btnOptions[i].setBackgroundResource(R.drawable.bg_card_quiz_wrong);
    }

    private void setButtonsClickable(boolean clickable) {
        for (LinearLayout btn : btnOptions) btn.setClickable(clickable);
    }

    private void resetButtonAppearance() {
        for (LinearLayout btn : btnOptions) {
            TextView b = (TextView) btn.getChildAt(0);
            b.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.greyedCard));
            b.setTextColor(ContextCompat.getColor(this, R.color.textMuted));
            btn.setBackgroundResource(R.drawable.bg_card_white);
        }
    }

    private void showLoading(boolean show) {
        loadingOverlay.setVisibility(show ? VISIBLE : View.GONE);
    }

}