package com.example.triviaquiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import utility.StringValues;
import utility.ThemeManager;

public class SelectCommunityDifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_community_difficulty);

        ((ImageView) findViewById(R.id.btnBack)).setOnClickListener(v -> finish());

        ((LinearLayout) findViewById(R.id.btnEasy)).setOnClickListener(v ->
                launchCommunityQuiz(StringValues.DIFFICULTY_EASY));

        ((LinearLayout) findViewById(R.id.btnMedium)).setOnClickListener(v ->
                launchCommunityQuiz(StringValues.DIFFICULTY_MEDIUM));

        ((LinearLayout) findViewById(R.id.btnHard)).setOnClickListener(v ->
                launchCommunityQuiz(StringValues.DIFFICULTY_HARD));
    }

    private void launchCommunityQuiz(String difficulty) {
        Intent intent = new Intent(this, CommunityQuizActivity.class);
        intent.putExtra(StringValues.DIFFICULTY_VAR, difficulty);
        startActivity(intent);
    }
}