package com.example.triviaquiz;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import utility.QuizStats;
import utility.StringValues;
import utility.ThemeManager;

public class QuizActivity extends AppCompatActivity {
    String difficulty;
    Context c;
    public static String token = "";
    LinearLayout btnNextQuestion;
    int correctAnswerIndex;

    private TextView tvQuestionProgress;
    private TextView tvCurrentCoins;
    private View viewProgressBar;
    private View viewProgressBarTotal;
    private TextView tvQuestionText;
    private View loadingOverlay;
    public long[] timeTaken;

    private LinearLayout[] btnOptions;
    JSONArray questions;
    int currentQuestionNumber = 0;
    int correctAnswers = 0;


    private CountDownTimer countDownTimer;
    private static int MAX_TIME_MS = 15000;
    private long questionStartTime;

    private int retryCount = 0;
    private static final int MAX_RETRIES = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent intent = getIntent();
        difficulty = intent.getStringExtra(StringValues.DIFFICULTY_VAR);

        if(difficulty.equalsIgnoreCase(StringValues.DIFFICULTY_EASY))
            MAX_TIME_MS = 20000;
        else if(difficulty.equalsIgnoreCase(StringValues.DIFFICULTY_MEDIUM))
            MAX_TIME_MS = 15000;
        else if(difficulty.equalsIgnoreCase(StringValues.DIFFICULTY_HARD))
            MAX_TIME_MS = 10000;

        c = this;
        correctAnswers = 0;
        currentQuestionNumber = 0;

        btnOptions = new LinearLayout[4];
        timeTaken = new long[5];//5 questions

        tvQuestionProgress = findViewById(R.id.tvQuestionProgress);
        viewProgressBar = findViewById(R.id.viewProgressBar);
        viewProgressBarTotal = findViewById(R.id.viewProgressBarTotal);
        tvQuestionText = findViewById(R.id.tvQuestionText);
        btnNextQuestion = findViewById(R.id.btnNextQuestion);

        btnOptions[0] = findViewById(R.id.btnOptionA);
        btnOptions[1] = findViewById(R.id.btnOptionB);
        btnOptions[2] = findViewById(R.id.btnOptionC);
        btnOptions[3] = findViewById(R.id.btnOptionD);
        btnNextQuestion.setVisibility(INVISIBLE);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        showLoading(true);

        GetRandomQuiz();


    }
    private void addListeners()
    {
        for(int i = 0; i < btnOptions.length; i++)
        {
            int finalI = i;
            btnOptions[i].setOnClickListener(v->{
                if (countDownTimer != null) countDownTimer.cancel(); // stop timer on answer
                timeTaken[currentQuestionNumber - 1] = System.currentTimeMillis() - questionStartTime;
                int index = finalI;
                btnNextQuestion.setVisibility(VISIBLE);
                setButtonsClickable(false);
                if(index == correctAnswerIndex)
                {
                    TextView letterBubble = (TextView) btnOptions[finalI].getChildAt(0);
                    letterBubble.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.successGreen));
                    letterBubble.setTextColor(ContextCompat.getColor(this, R.color.surfaceWhite));
                    btnOptions[finalI].setBackgroundResource(R.drawable.bg_card_quiz_correct);
                    correctAnswers++;
                }
                else
                {
                    TextView letterBubble = (TextView) btnOptions[finalI].getChildAt(0);
                    letterBubble.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F43F5E")));
                    letterBubble.setTextColor(ContextCompat.getColor(this, R.color.surfaceWhite));
                    btnOptions[finalI].setBackgroundResource(R.drawable.bg_card_quiz_wrong);
                    TextView letterBubbleCorrect = (TextView) btnOptions[correctAnswerIndex].getChildAt(0);
                    letterBubbleCorrect.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.successGreen));
                    letterBubbleCorrect.setTextColor(ContextCompat.getColor(this, R.color.surfaceWhite));
                    btnOptions[correctAnswerIndex].setBackgroundResource(R.drawable.bg_card_quiz_correct);

                }
            });

        }
        btnNextQuestion.setOnClickListener(v -> {
            GetNextQuestion();
            setButtonsClickable(true);
            resetButtonAppearance();
        });
    }

    private void GetRandomQuiz() {
        RequestQueue r = Volley.newRequestQueue(c);

        String url = "https://opentdb.com/api.php?amount=5&difficulty=" + difficulty
                + "&type=multiple&token=" + token;

        JsonObjectRequest quizRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        int responseCode = response.getInt("response_code");

                        if (responseCode == 3 || responseCode == 4) {
                            // Token expired — clear it and retry with no token
                            getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE)
                                    .edit().remove("api_token").apply();
                            token = "";
                            retryWithDelay("Token expired, retrying...");
                            return;
                        }

                        if (responseCode == 5) {
                            retryWithDelay("Rate limited, retrying...");
                            return;
                        }

                        if (responseCode != 0) {
                            retryWithDelay("Unexpected response, retrying...");
                            return;
                        }

                        retryCount = 0;
                        questions = response.getJSONArray("results");
                        currentQuestionNumber = 0;
                        GetNextQuestion();
                        showLoading(false);
                        addListeners();

                    } catch (JSONException e) {
                        retryWithDelay("Parse error, retrying...");
                    }
                },
                error -> retryWithDelay("Network error, retrying...")
        );

        r.add(quizRequest);
    }

    private void retryWithDelay(String reason) {
        retryCount++;
        android.util.Log.d("QuizActivity", reason + " (attempt " + retryCount + "/" + MAX_RETRIES + ")");

        if (retryCount >= MAX_RETRIES) {
            showLoading(false);
            Toast.makeText(c,
                    "Could not load questions after " + MAX_RETRIES + " attempts. Please try again later.",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(
                this::GetRandomQuiz,
                1500
        );
    }
    private void GetNextQuestion() {
        if (currentQuestionNumber >= 5) {
            if (countDownTimer != null) countDownTimer.cancel();
            Intent intent = new Intent(QuizActivity.this, QuizDoneActivity.class);
            intent.putExtra("correctAnswers", correctAnswers);
            intent.putExtra("quizStats", new QuizStats(correctAnswers, timeTaken, difficulty));
            startActivity(intent);
            finish();
            return;
        }

        btnNextQuestion.setVisibility(INVISIBLE);
        tvQuestionProgress.setText("Question " + (currentQuestionNumber + 1) + " / 5");

        try {
            JSONObject question = questions.getJSONObject(currentQuestionNumber);
            JSONArray wrongAnswers = question.getJSONArray("incorrect_answers");

            tvQuestionText.setText(Html.fromHtml(question.getString("question"), Html.FROM_HTML_MODE_LEGACY));
            correctAnswerIndex = (int)(Math.random() * 4);

            int wrongAnswersCounter = 0;
            for (int i = 0; i < 4; i++) {
                TextView option = (TextView) btnOptions[i].getChildAt(1);
                if (i == correctAnswerIndex)
                    option.setText(Html.fromHtml(question.getString("correct_answer"), Html.FROM_HTML_MODE_LEGACY));
                else {
                    option.setText(Html.fromHtml(wrongAnswers.getString(wrongAnswersCounter), Html.FROM_HTML_MODE_LEGACY));
                    wrongAnswersCounter++;
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        currentQuestionNumber++;
        startTimer(currentQuestionNumber - 1);
    }

    private void startTimer(int questionIndex) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        questionStartTime = System.currentTimeMillis();

        countDownTimer = new CountDownTimer(MAX_TIME_MS, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                float progress = (float) millisUntilFinished / MAX_TIME_MS;

                LinearLayout.LayoutParams weight = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, progress);
                LinearLayout.LayoutParams weightTotal = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f - progress);

                viewProgressBar.setLayoutParams(weight);
                viewProgressBarTotal.setLayoutParams(weightTotal);
            }

            @Override
            public void onFinish() {
                timeTaken[questionIndex] = MAX_TIME_MS;
                btnNextQuestion.setVisibility(VISIBLE);
                setButtonsClickable(false);

                TextView letterBubbleCorrect = (TextView) btnOptions[correctAnswerIndex].getChildAt(0);
                letterBubbleCorrect.setBackgroundTintList(ContextCompat.getColorStateList(c, R.color.successGreen));
                letterBubbleCorrect.setTextColor(ContextCompat.getColor(c, R.color.surfaceWhite));
                btnOptions[correctAnswerIndex].setBackgroundResource(R.drawable.bg_card_quiz_correct);
            }
        }.start();
    }
    private void showLoading(boolean show) {
        loadingOverlay.setVisibility(show ? VISIBLE : View.GONE);
    }
    private void setButtonsClickable(boolean isClickable)
    {
        for(int i = 0; i < btnOptions.length; i++)
        {
            btnOptions[i].setClickable(isClickable);
        }
    }
    private void resetButtonAppearance()
    {
        for(int i = 0; i < btnOptions.length; i++)
        {
            TextView letterBubble = (TextView) btnOptions[i].getChildAt(0);
            letterBubble.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.greyedCard));
            letterBubble.setTextColor(ContextCompat.getColor(this, R.color.textMuted));
            btnOptions[i].setBackgroundResource(R.drawable.bg_card_white);
        }
    }

}
