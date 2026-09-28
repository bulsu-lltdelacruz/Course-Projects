package com.example.triviaquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import utility.StringValues;
import utility.ThemeManager;

public class SelectDifficultyActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout btnEasy;
    private LinearLayout btnMedium;
    private LinearLayout btnHard;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_difficulty);

        c = this;
        btnBack = findViewById(R.id.btnBack);
        btnEasy = findViewById(R.id.btnEasy);
        btnMedium = findViewById(R.id.btnMedium);
        btnHard = findViewById(R.id.btnHard);

        btnBack.setOnClickListener(v -> finish());

        btnEasy.setOnClickListener(v -> {
            Intent intent = new Intent(SelectDifficultyActivity.this, QuizActivity.class);
            intent.putExtra(StringValues.DIFFICULTY_VAR, StringValues.DIFFICULTY_EASY);
            GetRandomQuiz(intent);
        });

        btnMedium.setOnClickListener(v -> {
            Intent intent = new Intent(SelectDifficultyActivity.this, QuizActivity.class);
            intent.putExtra(StringValues.DIFFICULTY_VAR, StringValues.DIFFICULTY_MEDIUM);
            GetRandomQuiz(intent);
        });

        btnHard.setOnClickListener(v -> {
            Intent intent = new Intent(SelectDifficultyActivity.this, QuizActivity.class);
            intent.putExtra(StringValues.DIFFICULTY_VAR, StringValues.DIFFICULTY_HARD);
            GetRandomQuiz(intent);
        });
    }

    private void GetRandomQuiz(Intent intent) {
        SharedPreferences prefs = getSharedPreferences(StringValues.PREF_NAME, Context.MODE_PRIVATE);
        String savedToken = prefs.getString(StringValues.KEY_TOKEN, null);

        if (savedToken != null) {
            //already have a token use it
            QuizActivity.token = savedToken;
            startActivity(intent);
            finish();
        } else {
            //no token get1 new one
            fetchNewToken(intent);
        }
    }

    private void fetchNewToken(Intent intent) {
        RequestQueue r = Volley.newRequestQueue(c);
        String url = "https://opentdb.com/api_token.php?command=request";

        JsonObjectRequest quizRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String token = response.getString("token");
                        QuizActivity.token = token;

                        //save token local
                        getSharedPreferences(StringValues.PREF_NAME, Context.MODE_PRIVATE)
                                .edit()
                                .putString(StringValues.KEY_TOKEN, token)
                                .apply();

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    startActivity(intent);
                },
                error -> {
                    Toast.makeText(c, "Failed to get token. Check your connection.", Toast.LENGTH_SHORT).show();
                }
        );

        r.add(quizRequest);
    }
    private void handleTokenExpired(Intent retryIntent) {
        getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE)
                .edit()
                .remove("api_token")
                .apply();
        QuizActivity.token = null;

        fetchNewToken(retryIntent);
    }
}
