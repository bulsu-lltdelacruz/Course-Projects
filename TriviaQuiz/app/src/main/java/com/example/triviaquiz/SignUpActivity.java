package com.example.triviaquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import utility.StringValues;
import utility.ThemeManager;

public class SignUpActivity extends AppCompatActivity {

    private Button btnSignUpSubmit;
    private TextView tvGoToLogin;
    private EditText etSignUpEmail;
    private EditText etSignUpUsername;
    private EditText etSignUpPassword;
    private EditText etSignUpConfirmPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        btnSignUpSubmit = findViewById(R.id.btnSignUpSubmit);
        tvGoToLogin     = findViewById(R.id.tvGoToLogin);
        etSignUpEmail   = findViewById(R.id.etSignUpEmail);
        etSignUpUsername = findViewById(R.id.etSignUpUsername);
        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        etSignUpConfirmPassword = findViewById(R.id.etSignUpConfirmPassword);

        setupPasswordToggle(etSignUpPassword);
        setupPasswordToggle(etSignUpConfirmPassword);
        tvGoToLogin.setOnClickListener(v -> finish());
        btnSignUpSubmit.setOnClickListener(v -> signUp());

    }

    private void signUp() {
        String email    = etSignUpEmail.getText().toString().trim();
        String username = etSignUpUsername.getText().toString().trim();
        String password = etSignUpPassword.getText().toString().trim();
        String confirmPassword = etSignUpConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etSignUpEmail.setError("Email is required.");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            etSignUpUsername.setError("Username is required.");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etSignUpPassword.setError("Password is required.");
            return;
        }
        if (!confirmPassword.equals(password)) {
            etSignUpPassword.setError("Password does not match.");
            return;
        }

        btnSignUpSubmit.setEnabled(false);
        btnSignUpSubmit.setText("Signing up...");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates =
                                    new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileTask -> {
                                        if (profileTask.isSuccessful()) {
                                            initializeUserData(user.getUid(), username);
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this,
                                "Sign up failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.d("SignupError", task.getException().getMessage());
                        btnSignUpSubmit.setEnabled(true);
                        btnSignUpSubmit.setText("Sign Up");
                    }
                });
    }

    // ── Write all default values to Firebase ──────────────────────────────────

    private void initializeUserData(String uid, String username) {
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users").child(uid);

        Map<String, Object> defaults = new HashMap<>();
        defaults.put("username",           username);
        defaults.put("rank",               StringValues.RANK_NOVICE);
        defaults.put("rankPoints",         0);
        defaults.put("coins",              0);
        defaults.put("totalQuizzesPlayed", 0);
        defaults.put("totalCorrectItems",  0);
        defaults.put("customization/selectedIconId",   4);
        defaults.put("customization/selectedBorderId", -1);
        defaults.put("customization/selectedThemeId", 0);

        userRef.updateChildren(defaults).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("SignupError", "DB init failed: " + task.getException().getMessage());
            }

            // ── Write ownedItems separately so it doesn't conflict ───────────
            // Using setValue on the child directly guarantees it's written
            userRef.child("ownedItems").child("0").setValue(true);//avatar_user
            userRef.child("ownedItems").child("1").setValue(true);//avatar_user
            userRef.child("ownedItems").child("4").setValue(true); //avatar_panda
            userRef.child("ownedItems").child("5").setValue(true)//avatar_woman
                    .addOnCompleteListener(ownedTask -> {
                        Toast.makeText(SignUpActivity.this,
                                "Account created! Please log in.",
                                Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    });
        });
    }
    @SuppressLint("ClickableViewAccessibility")
    private void setupPasswordToggle(EditText field) {
        field.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_eye_closed, 0, 0, 0);

        field.setOnTouchListener((v, event) -> {
            final int DRAWABLE_LEFT = 0;

            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                android.graphics.drawable.Drawable drawableLeft = field.getCompoundDrawables()[DRAWABLE_LEFT];

                if (drawableLeft != null) {
                    if (event.getX() <= (field.getPaddingLeft() + drawableLeft.getBounds().width())) {

                        boolean isVisible = field.getInputType() ==
                                (android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                        if (isVisible) {
                            field.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            field.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_eye_closed, 0, 0, 0);
                        } else {
                            field.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            field.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_eye_open, 0, 0, 0);
                        }

                        field.setSelection(field.getText().length());
                        return true;
                    }
                }
            }
            return false;
        });
    }
}