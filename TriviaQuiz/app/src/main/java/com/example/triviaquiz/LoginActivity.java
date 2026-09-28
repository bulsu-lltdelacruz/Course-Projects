package com.example.triviaquiz;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ranks.Apprentice;
import ranks.Genius;
import ranks.Master;
import ranks.Novice;
import ranks.Oracle;
import ranks.Sage;
import ranks.Scholar;
import utility.CurrentUser;
import utility.StringValues;
import utility.ThemeManager;

public class LoginActivity extends AppCompatActivity {

    private ImageView ivLogo;
    private TextView tvWelcomeTitle;
    private TextView tvWelcomeSubtitle;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvSignUp;
    private FrameLayout loadingOverlay;
    FirebaseAuth mAuth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CurrentUser tempUser = new CurrentUser();
        CurrentUser.instance.selectedThemeId = ThemeManager.loadLocal(this);
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (CurrentUser.instance == null) {
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
            return;
        }

        mAuth = FirebaseAuth.getInstance();

        ivLogo = findViewById(R.id.ivLogo);
        tvWelcomeTitle = findViewById(R.id.tvWelcomeTitle);
        tvWelcomeSubtitle = findViewById(R.id.tvWelcomeSubtitle);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        loadingOverlay = findViewById(R.id.loadingOverlay);

        showLoading(false);

        btnLogin.setOnClickListener(v -> loginUser());

        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
        etPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_LEFT = 0;

            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                android.graphics.drawable.Drawable drawableLeft = etPassword.getCompoundDrawables()[DRAWABLE_LEFT];

                if (drawableLeft != null) {
                    if (event.getX() <= (etPassword.getPaddingLeft() + drawableLeft.getBounds().width())) {

                        boolean isVisible = etPassword.getInputType() ==
                                (android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                        if (isVisible) {
                            etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_eye_closed, 0, 0, 0);
                        } else {
                            etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_eye_open, 0, 0, 0);
                        }

                        etPassword.setSelection(etPassword.getText().length());
                        return true;
                    }
                }
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            showLoading(true);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            new CurrentUser();
            String uid = currentUser.getUid();

            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");

            userReference.child(uid).child("rankPoints").get().addOnCompleteListener(task->{
               if(task.isSuccessful())
               {
                   int rankPoints = Integer.valueOf(task.getResult().getValue().toString());
                    GetRank(userReference, uid, rankPoints, intent);
               }
            });
        }
    }
    private void GetRank(DatabaseReference userReference, String uid, int rankPoints, Intent intent)
    {
        userReference.child(uid).child("rank").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String rank = String.valueOf(task.getResult().getValue());

                if (rank != null) {
                    switch (rank) {
                        case StringValues.RANK_NOVICE:
                            CurrentUser.instance.currentRank = new Novice(rankPoints);
                            break;
                        case StringValues.RANK_APPRENTICE:
                            CurrentUser.instance.currentRank = new Apprentice(rankPoints);
                            break;
                        case StringValues.RANK_SCHOLAR:
                            CurrentUser.instance.currentRank = new Scholar(rankPoints);
                            break;
                        case StringValues.RANK_SAGE:
                            CurrentUser.instance.currentRank = new Sage(rankPoints);
                            break;
                        case StringValues.RANK_MASTER:
                            CurrentUser.instance.currentRank = new Master(rankPoints);
                            break;
                        case StringValues.RANK_GENIUS:
                            CurrentUser.instance.currentRank = new Genius(rankPoints);
                            break;
                        case StringValues.RANK_ORACLE:
                            CurrentUser.instance.currentRank = new Oracle(rankPoints);
                            break;
                    }
                }
                GetCoins(userReference, uid, intent);

            }
        });
    }
    private void GetCoins(DatabaseReference userReference, String uid, Intent intent)
    {
        userReference.child(uid).child("coins").get().addOnCompleteListener(
        task -> {
            if(task.isSuccessful())
            {
                CurrentUser.instance.coins = Integer.valueOf(task.getResult().getValue().toString());
                GetGameStats(userReference, uid, intent);
            }
        });
    }
    private void GetGameStats(DatabaseReference userReference, String uid, Intent intent) {
        userReference.child(uid).child("totalQuizzesPlayed").get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        CurrentUser.instance.totalGames = Integer.valueOf(task.getResult().getValue().toString());
                        userReference.child(uid).child("totalCorrectItems").get().addOnCompleteListener(
                                task1 -> {
                                    if (task1.isSuccessful()) {
                                        CurrentUser.instance.totalCorrectItems = Integer.valueOf(task1.getResult().getValue().toString());
                                        GetOwnedItems(userReference, uid, intent); // ← changed from startActivity
                                    }
                                });
                    }
                });
    }

    private void GetOwnedItems(DatabaseReference userReference, String uid, Intent intent) {
        userReference.child(uid).child("ownedItems").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                for (com.google.firebase.database.DataSnapshot snap : task.getResult().getChildren()) {
                    CurrentUser.instance.ownedItemIds.add(Integer.parseInt(snap.getKey()));
                }
            }
            GetCustomization(userReference, uid, intent);
        });
    }

    private void GetCustomization(DatabaseReference userReference, String uid, Intent intent) {
        userReference.child(uid).child("customization").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                com.google.firebase.database.DataSnapshot snap = task.getResult();

                CurrentUser.instance.selectedIconId = snap.child("selectedIconId").exists()
                        ? snap.child("selectedIconId").getValue(Integer.class) : 1;
                CurrentUser.instance.selectedBorderId = snap.child("selectedBorderId").exists()
                        ? snap.child("selectedBorderId").getValue(Integer.class) : -1;
                CurrentUser.instance.selectedThemeId = snap.child("selectedThemeId").exists()
                        ? snap.child("selectedThemeId").getValue(Integer.class) : 0;

                ThemeManager.saveLocal(this, CurrentUser.instance.selectedThemeId);
            }
            userReference.child(uid).child("isAdmin").get().addOnCompleteListener(adminTask -> {
                if (adminTask.isSuccessful() && adminTask.getResult().exists()) {
                    Boolean isAdmin = adminTask.getResult().getValue(Boolean.class);
                    CurrentUser.instance.isAdmin = isAdmin != null && isAdmin;
                }

                //if admin
                if (CurrentUser.instance.isAdmin) {
                    Intent adminIntent = new Intent(LoginActivity.this, AdminActivity.class);
                    adminIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(adminIntent);
                } else {
                    startActivity(intent);
                }
                finish();
            });
            //startActivity(intent);
            //finish();

        });

    }
    private void loginUser() {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etUsername.setError("Email is required.");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required.");
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Welcome back!", Toast.LENGTH_SHORT).show();

                        showLoading(true);
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        new CurrentUser();
                        String uid = currentUser.getUid();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        DatabaseReference userReference = FirebaseDatabase.getInstance()
                                .getReference("users");

                        userReference.child(uid).child("rankPoints").get()
                                .addOnCompleteListener(rankTask -> {
                                    if (rankTask.isSuccessful()) {
                                        int rankPoints = Integer.parseInt(
                                                rankTask.getResult().getValue().toString());
                                        GetRank(userReference, uid, rankPoints, intent);
                                    }
                                });

                    } else {
            String errorMessage;
            String errorCode = "";

            if (task.getException() instanceof com.google.firebase.auth.FirebaseAuthException) {
                errorCode = ((com.google.firebase.auth.FirebaseAuthException)
                        task.getException()).getErrorCode();
            }

            switch (errorCode) {
                case "ERROR_INVALID_EMAIL":
                    errorMessage = "Invalid email address.";
                    break;
                case "ERROR_USER_NOT_FOUND":
                    errorMessage = "No account found with this email.";
                    break;
                case "ERROR_WRONG_PASSWORD":
                    errorMessage = "Incorrect password.";
                    break;
                case "ERROR_USER_DISABLED":
                    errorMessage = "This account has been disabled.";
                    break;
                case "ERROR_TOO_MANY_REQUESTS":
                    errorMessage = "Too many attempts. Try again later.";
                    break;
                case "ERROR_NETWORK_REQUEST_FAILED":
                    errorMessage = "No internet connection.";
                    break;
                case "ERROR_INVALID_CREDENTIAL":
                    errorMessage = "Wrong email or password.";
                    break;
                default:
                    errorMessage = "Login failed. Please try again.";
                    break;
            }

            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            btnLogin.setEnabled(true);
            btnLogin.setText("Log In");
        }
                });
    }
    private void showLoading(boolean show) {
        loadingOverlay.setVisibility(show ? VISIBLE : View.GONE);
    }
}
