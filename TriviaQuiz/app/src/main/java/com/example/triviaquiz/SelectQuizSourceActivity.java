package com.example.triviaquiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import utility.CurrentUser;
import utility.ThemeManager;

public class SelectQuizSourceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_quiz_source);

        if (CurrentUser.instance == null) {
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
            return;
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        ((LinearLayout) findViewById(R.id.btnApiQuiz)).setOnClickListener(v ->
                startActivity(new Intent(this, SelectDifficultyActivity.class)));

        /*((LinearLayout) findViewById(R.id.btnCommunityQuiz)).setOnClickListener(v ->
                startActivity(new Intent(this, CommunityQuizActivity.class)));*/
        ((LinearLayout) findViewById(R.id.btnCommunityQuiz)).setOnClickListener(v ->
                startActivity(new Intent(this, SelectCommunityDifficultyActivity.class)));

        ((LinearLayout) findViewById(R.id.btnSubmitQuestion)).setOnClickListener(v ->
                startActivity(new Intent(this, SubmitQuestionActivity.class)));
    }
}