package com.example.triviaquiz;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import utility.CurrentUser;
import utility.ThemeManager;

public class AdminActivity extends AppCompatActivity {

    private LinearLayout pendingContainer;
    private TextView tvPendingCount;
    private DatabaseReference pendingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        pendingContainer = findViewById(R.id.pendingContainer);
        tvPendingCount   = findViewById(R.id.tvPendingCount);

        findViewById(R.id.btnAdminLogout).setOnClickListener(v -> {
            showLogoutDialog();

        });

        pendingRef = FirebaseDatabase.getInstance().getReference("pendingQuestions");
        loadPendingQuestions();
    }
    private void showLogoutDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log Out", (dialog, which) -> {
                    CurrentUser.instance = null;
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this, LoginActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void loadPendingQuestions() {
        pendingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                pendingContainer.removeAllViews();
                long count = snapshot.getChildrenCount();
                tvPendingCount.setText(count + " question(s) awaiting review");

                for (DataSnapshot child : snapshot.getChildren()) {
                    String key        = child.getKey();
                    String question   = child.child("question").getValue(String.class);
                    String correct    = child.child("correct_answer").getValue(String.class);
                    String difficulty = child.child("difficulty").getValue(String.class);
                    String submitter  = child.child("submittedByUsername").getValue(String.class);

                    String wrong1 = "", wrong2 = "", wrong3 = "";
                    DataSnapshot wrongSnap = child.child("incorrect_answers");
                    int wi = 0;
                    for (DataSnapshot w : wrongSnap.getChildren()) {
                        String val = w.getValue(String.class);
                        if (wi == 0) wrong1 = val;
                        else if (wi == 1) wrong2 = val;
                        else if (wi == 2) wrong3 = val;
                        wi++;
                    }

                    pendingContainer.addView(makePendingCard(
                            key, question, correct, wrong1, wrong2, wrong3,
                            difficulty, submitter));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AdminActivity.this,
                        "Failed to load questions.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private View makePendingCard(String key, String question, String correct,
                                 String wrong1, String wrong2, String wrong3,
                                 String difficulty, String submitter) {

        LinearLayout card = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dp(16));
        card.setLayoutParams(params);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.bg_card_white);
        card.setBackgroundTintList(ColorStateList.valueOf(
                ThemeManager.getColorFromAttr(this, R.attr.cardSurface)));
        card.setPadding(dp(16), dp(16), dp(16), dp(16));

        addLabel(card, "By: " + submitter + "  •  " + difficulty.toUpperCase(), 11,
                ContextCompat.getColor(this, R.color.textMuted));
        addLabel(card, question, 15, ThemeManager.getColorFromAttr(this, R.attr.textPrimary));
        addLabel(card, "✓ " + correct, 13, ContextCompat.getColor(this, R.color.successGreen));
        addLabel(card, "✗ " + wrong1, 13, ContextCompat.getColor(this, R.color.errorRed));
        addLabel(card, "✗ " + wrong2, 13, ContextCompat.getColor(this, R.color.errorRed));
        addLabel(card, "✗ " + wrong3, 13, ContextCompat.getColor(this, R.color.errorRed));

        // Buttons row
        LinearLayout btnRow = new LinearLayout(this);
        btnRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        rowParams.setMargins(0, dp(12), 0, 0);
        btnRow.setLayoutParams(rowParams);

        Button btnApprove = new Button(this);
        btnApprove.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        btnApprove.setText("Approve");
        btnApprove.setBackgroundResource(R.drawable.bg_card_white);
        btnApprove.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.successGreen)));
        btnApprove.setTextColor(ContextCompat.getColor(this, R.color.surfaceWhite));
        btnApprove.setOnClickListener(v -> {
            btnApprove.setEnabled(false);
            approveQuestion(key, question, correct,
                wrong1, wrong2, wrong3, difficulty);});

        Button btnReject = new Button(this);
        LinearLayout.LayoutParams rejectParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        rejectParams.setMarginStart(dp(8));
        btnReject.setLayoutParams(rejectParams);
        btnReject.setText("Reject");
        btnReject.setBackgroundResource(R.drawable.bg_card_white);
        btnReject.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.errorRed)));
        btnReject.setTextColor(ContextCompat.getColor(this, R.color.surfaceWhite));
        btnReject.setOnClickListener(v -> rejectQuestion(key));

        btnRow.addView(btnReject);
        btnRow.addView(btnApprove);
        card.addView(btnRow);

        return card;
    }

    private void approveQuestion(String key, String question, String correct,
                                 String wrong1, String wrong2, String wrong3, String difficulty) {

        String adminUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> approved = new HashMap<>();
        approved.put("question",           question);
        approved.put("correct_answer",     correct);
        approved.put("incorrect_answers",  Arrays.asList(wrong1, wrong2, wrong3));
        approved.put("difficulty",         difficulty);
        approved.put("approvedBy",         adminUid);

        DatabaseReference approvedRef = FirebaseDatabase.getInstance()
                .getReference("approvedQuestions");

        approvedRef.push().setValue(approved).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                pendingRef.child(key).removeValue();
                Toast.makeText(this, "Question approved!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rejectQuestion(String key) {
        pendingRef.child(key).removeValue().addOnCompleteListener(task ->
                Toast.makeText(this, "Question rejected.", Toast.LENGTH_SHORT).show());
    }

    private void addLabel(LinearLayout parent, String text, int sizeSp, int color) {
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(0, dp(4), 0, 0);
        tv.setLayoutParams(p);
        tv.setText(text);
        tv.setTextSize(sizeSp);
        tv.setTextColor(color);
        parent.addView(tv);
    }

    private int dp(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}