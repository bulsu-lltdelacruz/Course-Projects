package com.example.triviaquiz;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.Objects;

import ranks.Oracle;
import utility.CurrentUser;
import utility.QuizStats;
import utility.StringValues;
import utility.ThemeManager;

public class QuizDoneActivity extends AppCompatActivity {

    private ImageView ivTrophy;
    private TextView tvCongrats;
    private TextView tvFinalScore;
    private TextView tvRankPointsEarned;
    private TextView tvCoinsEarned;
    private LinearLayout btnPlayAgain;
    private LinearLayout btnGoHome;
    private TextView tvRank;
    private TextView tvCurrentScore;
    private TextView tvTotalScore;

    View viewProgressBar;
    View viewProgressBarTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_done);

        ivTrophy = findViewById(R.id.ivTrophy);
        tvCongrats = findViewById(R.id.tvCongrats);

        viewProgressBar= findViewById(R.id.viewProgressBar);
        viewProgressBarTotal= findViewById(R.id.viewProgressBarTotal);

        tvRankPointsEarned = findViewById(R.id.tvRankPointsEarned);
        tvCoinsEarned = findViewById(R.id.tvCoinsEarned);

        tvFinalScore = findViewById(R.id.tvFinalScore);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnGoHome = findViewById(R.id.btnGoHome);

        tvRank = findViewById(R.id.tvRank);

        tvCurrentScore = findViewById(R.id.tvCurrentScore);
        tvTotalScore = findViewById(R.id.tvTotalScore);

        Intent intent = getIntent();
        QuizStats quizStats = intent.getParcelableExtra("quizStats");
        float percentCorrect = (float)(quizStats.numberOfCorrectItems/5f)*100f;

        CurrentUser.instance.coins += quizStats.coinsGained;
        RankBase newRank = CurrentUser.instance.currentRank.increasePoints(quizStats.rankPointsGained);
        if(newRank != null)//nagbago yung rank either up or down
        {
            //also add to firebase later
            CurrentUser.instance.currentRank = newRank;
            tvRank.setText(newRank.rankName);
            tvRank.setBackgroundResource(newRank.background);
            tvRank.setTextColor(ContextCompat.getColor(this, newRank.strokeColor));
            tvRank.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(this, newRank.strokeColor)));
        }else
        {
            RankBase rank = CurrentUser.instance.currentRank;
            tvRank.setText(rank.rankName);
            tvRank.setBackgroundResource(rank.background);
            tvRank.setTextColor(ContextCompat.getColor(this, rank.strokeColor));
            tvRank.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(this, rank.strokeColor)));
        }
        //setting view values
        String scoreText = String.format("You Scored %d out of 5 (%.0f%%)", quizStats.numberOfCorrectItems, percentCorrect);

        float currentPoints = CurrentUser.instance.currentRank.currentPoints;
        float maxPoints = CurrentUser.instance.currentRank.maxPoints;
        if(!(CurrentUser.instance.currentRank instanceof Oracle))
        {
            tvCurrentScore.setText(String.format("%.0f Points", currentPoints));
            tvTotalScore.setText(String.format("%.0f Points", maxPoints));
        }
        else
        {
            tvCurrentScore.setText(String.format("%.0f Points", currentPoints));
            tvTotalScore.setText("");
            maxPoints = currentPoints;
        }
        tvFinalScore.setText(scoreText);
        String coinSign = (quizStats.coinsGained >= 0) ? "+" : "-";
        String rankSign = (quizStats.rankPointsGained >= 0) ? "+" : "-";

        tvRankPointsEarned.setText(rankSign + Math.abs(quizStats.rankPointsGained));
        tvCoinsEarned.setText(coinSign + Math.abs(quizStats.coinsGained));

        float progress = currentPoints / maxPoints;

        LinearLayout.LayoutParams weight = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, progress);
        LinearLayout.LayoutParams weightTotal = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f - progress);

        viewProgressBar.setLayoutParams(weight);
        viewProgressBarTotal.setLayoutParams(weightTotal);


        btnPlayAgain.setOnClickListener(v -> {
            startActivity(new Intent(QuizDoneActivity.this, SelectQuizSourceActivity.class));
            finish();
        });

        btnGoHome.setOnClickListener(v -> {
            startActivity(new Intent(QuizDoneActivity.this, HomeActivity.class));
            finish();
        });
        addToDatabase(quizStats);
    }
    private void addToDatabase(QuizStats quizStats) {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        CurrentUser.instance.totalGames += 5;
        CurrentUser.instance.totalCorrectItems += quizStats.numberOfCorrectItems;

        userRef.child("coins").setValue(CurrentUser.instance.coins);
        userRef.child("rank").setValue(CurrentUser.instance.currentRank.rankName);
        userRef.child("rankPoints").setValue(CurrentUser.instance.currentRank.currentPoints);

        userRef.child("totalCorrectItems").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData data) {
                int current = data.getValue(Integer.class) == null ? 0 : data.getValue(Integer.class);
                data.setValue(current + quizStats.numberOfCorrectItems);
                return Transaction.success(data);
            }
            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {}
        });

        userRef.child("totalCoins").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData data) {
                int current = data.getValue(Integer.class) == null ? 0 : data.getValue(Integer.class);
                data.setValue(current + quizStats.coinsGained);
                return Transaction.success(data);
            }
            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {}
        });

        userRef.child("totalQuizzesPlayed").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData data) {
                int current = data.getValue(Integer.class) == null ? 0 : data.getValue(Integer.class);
                data.setValue(current + 5);
                return Transaction.success(data);
            }
            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {}
        });

        String difficultyKey;
        switch (quizStats.difficulty) {
            case StringValues.DIFFICULTY_EASY:   difficultyKey = "easy";   break;
            case StringValues.DIFFICULTY_MEDIUM: difficultyKey = "medium"; break;
            case StringValues.DIFFICULTY_HARD:   difficultyKey = "hard";   break;
            default: difficultyKey = "easy";
        }
        DatabaseReference diffRef = userRef.child("difficultyStats").child(difficultyKey);

        diffRef.child("totalCorrectItems").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData data) {
                int current = data.getValue(Integer.class) == null ? 0 : data.getValue(Integer.class);
                data.setValue(current + quizStats.numberOfCorrectItems);
                return Transaction.success(data);
            }
            @Override
            public void onComplete(DatabaseError e, boolean committed, DataSnapshot s) {}
        });

        diffRef.child("totalQuestions").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData data) {
                int current = data.getValue(Integer.class) == null ? 0 : data.getValue(Integer.class);
                data.setValue(current + 5);
                return Transaction.success(data);
            }
            @Override
            public void onComplete(DatabaseError e, boolean committed, DataSnapshot s) {}
        });

    }
}