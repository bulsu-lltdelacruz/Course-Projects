package com.example.triviaquiz;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import utility.CurrentUser;
import utility.Helper;
import utility.StringValues;
import utility.ThemeManager;

public class TasksActivity extends AppCompatActivity {

    private ImageView btnMenu;
    private TextView tvCoins;
    private LinearLayout availableContainer;
    private LinearLayout completedContainer;

    private DatabaseReference userRef;
    private String uid;
    private String source = "";

    // Task model
    static class Task {
        String id;
        String title;
        String description;
        int rewardCoins;
        boolean isUnlocked;
        boolean isClaimed;

        Task(String id, String title, String description, int rewardCoins,
             boolean isUnlocked, boolean isClaimed) {
            this.id          = id;
            this.title       = title;
            this.description = description;
            this.rewardCoins = rewardCoins;
            this.isUnlocked  = isUnlocked;
            this.isClaimed   = isClaimed;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        source = getIntent().getStringExtra("source");
        if (CurrentUser.instance == null) {
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
            return;
        }

        uid     = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        btnMenu            = findViewById(R.id.btnMenu);
        tvCoins            = findViewById(R.id.tvCoins);
        availableContainer = findViewById(R.id.availableContainer);
        completedContainer = findViewById(R.id.completedContainer);

        tvCoins.setText(String.format("%,d", CurrentUser.instance.coins));
        if(source!=null && source.equalsIgnoreCase("Home"))
        {
            btnMenu.setBackgroundResource(0);
            Glide.with(this).load(R.drawable.ic_back).centerInside().into(btnMenu);
            btnMenu.setOnClickListener(v->{
                finish();
            });
        }else
        {
            btnMenu.setBackgroundResource(0);
            Glide.with(this).load(R.drawable.ic_burger).centerInside().into(btnMenu);
            btnMenu.setOnClickListener(v -> {
                Intent intent = new Intent(TasksActivity.this, SideMenuActivity.class);
                intent.putExtra("activeIndex", 5);
                startActivity(intent);
            });
        }



        loadClaimedTasksThenRender();
    }

    private void loadClaimedTasksThenRender() {
        userRef.child("claimedTasks").get().addOnCompleteListener(task -> {
            List<String> claimed = new ArrayList<>();
            if (task.isSuccessful() && task.getResult().exists()) {
                for (com.google.firebase.database.DataSnapshot snap
                        : task.getResult().getChildren()) {
                    claimed.add(snap.getKey());
                }
            }
            renderTasks(buildTaskList(claimed));
        });
    }

    private List<Task> buildTaskList(List<String> claimed) {
        List<Task> tasks = new ArrayList<>();

        String rank        = CurrentUser.instance.currentRank != null
                ? CurrentUser.instance.currentRank.rankName : StringValues.RANK_NOVICE;
        int rankTier       = getRankTier(rank);
        //totalCorrectItems = total questions
        //totalQuizzesPlayed × 5 = total questions answered
        int totalQuestions = CurrentUser.instance.totalGames;

        // rank tasks
        tasks.add(new Task("rank_apprentice", "Reach Apprentice",
                "Climb to Apprentice rank.", 100,
                rankTier >= 2, claimed.contains("rank_apprentice")));

        tasks.add(new Task("rank_scholar", "Reach Scholar",
                "Climb to Scholar rank.", 150,
                rankTier >= 3, claimed.contains("rank_scholar")));

        tasks.add(new Task("rank_sage", "Reach Sage",
                "Climb to Sage rank.", 200,
                rankTier >= 4, claimed.contains("rank_sage")));

        tasks.add(new Task("rank_master", "Reach Master",
                "Climb to Master rank.", 400,
                rankTier >= 5, claimed.contains("rank_master")));

        tasks.add(new Task("rank_genius", "Reach Genius",
                "Climb to Genius rank.", 600,
                rankTier >= 6, claimed.contains("rank_genius")));

        tasks.add(new Task("rank_oracle", "Reach Oracle",
                "Reach the highest rank.", 1000,
                rankTier >= 7, claimed.contains("rank_oracle")));

        //question tasks
        tasks.add(new Task("questions_10", "Answer 10 Questions",
                "Answer a total of 10 questions.", 10,
                totalQuestions >= 10, claimed.contains("questions_10")));

        tasks.add(new Task("questions_20", "Answer 20 Questions",
                "Answer a total of 20 questions.", 20,
                totalQuestions >= 20, claimed.contains("questions_20")));

        tasks.add(new Task("questions_30", "Answer 30 Questions",
                "Answer a total of 30 questions.", 50,
                totalQuestions >= 30, claimed.contains("questions_30")));

        tasks.add(new Task("questions_40", "Answer 40 Questions",
                "Answer a total of 40 questions.", 80,
                totalQuestions >= 40, claimed.contains("questions_40")));

        tasks.add(new Task("questions_50", "Answer 50 Questions",
                "Answer a total of 50 questions.", 100,
                totalQuestions >= 50, claimed.contains("questions_50")));

        tasks.add(new Task("questions_100", "Answer 100 Questions",
                "Answer a total of 100 questions.", 300,
                totalQuestions >= 100, claimed.contains("questions_100")));

        tasks.add(new Task("questions_200", "Answer 200 Questions",
                "Answer a total of 200 questions.", 1000,
                totalQuestions >= 200, claimed.contains("questions_200")));

        return tasks;
    }

    private int getRankTier(String rank) {
        switch (rank) {
            case StringValues.RANK_APPRENTICE: return 2;
            case StringValues.RANK_SCHOLAR:    return 3;
            case StringValues.RANK_SAGE:       return 4;
            case StringValues.RANK_MASTER:     return 5;
            case StringValues.RANK_GENIUS:     return 6;
            case StringValues.RANK_ORACLE:     return 7;
            default:                           return 1; //novice
        }
    }

    private void renderTasks(List<Task> tasks) {
        availableContainer.removeAllViews();
        completedContainer.removeAllViews();

        for (Task task : tasks) {
            LinearLayout row = makeTaskRow(task);
            if (task.isClaimed) {
                completedContainer.addView(row);
            } else {
                availableContainer.addView(row);
            }
        }
    }

    private LinearLayout makeTaskRow(Task task) {
        LinearLayout row = new LinearLayout(this);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        rowParams.setMargins(0, 0, 0, dp(10));
        row.setLayoutParams(rowParams);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(16), dp(14), dp(16), dp(14));
        row.setBackgroundResource(R.drawable.bg_card_white);

        int cardColor = ThemeManager.getColorFromAttr(this, R.attr.cardSurface);
        row.setBackgroundTintList(ColorStateList.valueOf(
                task.isClaimed ? adjustAlpha(cardColor, 0.5f) : cardColor));

        //title , description
        LinearLayout left = new LinearLayout(this);
        left.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        left.setOrientation(LinearLayout.VERTICAL);

        TextView tvTitle = new TextView(this);
        tvTitle.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        tvTitle.setText(task.title);
        tvTitle.setTextSize(14f);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitle.setTextColor(task.isClaimed ? ThemeManager.getColorFromAttr(this, R.attr.textSubtitle) :
                        ThemeManager.getColorFromAttr(this, R.attr.textPrimary));
        left.addView(tvTitle);

        TextView tvDesc = new TextView(this);
        tvDesc.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        tvDesc.setText(task.description);
        tvDesc.setTextSize(12f);
        tvDesc.setTextColor(ThemeManager.getColorFromAttr(this, R.attr.textSubtitle));
        left.addView(tvDesc);

        //coin reward
        LinearLayout coinRow = new LinearLayout(this);
        coinRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        coinRow.setOrientation(LinearLayout.HORIZONTAL);
        coinRow.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams coinRowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        coinRowParams.topMargin = dp(4);
        coinRow.setLayoutParams(coinRowParams);

        ImageView coinIcon = new ImageView(this);
        coinIcon.setLayoutParams(new LinearLayout.LayoutParams(dp(14), dp(14)));
        coinIcon.setImageResource(R.drawable.ic_coin);
        coinRow.addView(coinIcon);

        TextView tvReward = new TextView(this);
        LinearLayout.LayoutParams rewardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        rewardParams.setMarginStart(dp(4));
        tvReward.setLayoutParams(rewardParams);
        tvReward.setText("+" + task.rewardCoins + " coins");
        tvReward.setTextSize(11f);
        tvReward.setTypeface(null, android.graphics.Typeface.BOLD);
        tvReward.setTextColor(ThemeManager.getColorFromAttr(this, R.attr.textSubtitle));
        coinRow.addView(tvReward);
        left.addView(coinRow);

        row.addView(left);

        // claim button / checkmark
        if (task.isClaimed) {
            // Checkmark
            ImageView check = new ImageView(this);
            check.setLayoutParams(new LinearLayout.LayoutParams(dp(24), dp(24)));
            check.setImageResource(0);
            check.setColorFilter(ContextCompat.getColor(this, R.color.successGreen));
            row.addView(check);

        } else if (task.isUnlocked) {
            //claim
            Button claimBtn = new Button(this);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    dp(36));
            claimBtn.setLayoutParams(btnParams);
            claimBtn.setText("Claim");
            claimBtn.setTextSize(12f);
            claimBtn.setTypeface(null, android.graphics.Typeface.BOLD);
            claimBtn.setTextColor(ContextCompat.getColor(this, R.color.surfaceWhite));
            claimBtn.setBackgroundResource(R.drawable.bg_card_white);
            claimBtn.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.successGreen));
            claimBtn.setPadding(dp(12), 0, dp(12), 0);
            claimBtn.setOnClickListener(v -> claimTask(task, claimBtn, row));
            row.addView(claimBtn);

        } else {
            //locked
            ImageView lock = new ImageView(this);
            lock.setLayoutParams(new LinearLayout.LayoutParams(dp(24), dp(24)));
            lock.setImageResource(R.drawable.ic_lock);
            lock.setColorFilter(ContextCompat.getColor(this, R.color.textMuted));
            row.addView(lock);
        }

        return row;
    }

    private void claimTask(Task task, Button claimBtn, LinearLayout row) {
        claimBtn.setEnabled(false);
        claimBtn.setText("...");

        int newCoins = CurrentUser.instance.coins + task.rewardCoins;

        java.util.Map<String, Object> updates = new java.util.HashMap<>();
        updates.put("coins", newCoins);
        updates.put("claimedTasks/" + task.id, true);

        userRef.updateChildren(updates).addOnCompleteListener(dbTask -> {
            if (dbTask.isSuccessful()) {
                CurrentUser.instance.coins = newCoins;
                tvCoins.setText(String.format("%,d", newCoins));
                Toast.makeText(this, "+" + task.rewardCoins + " coins claimed!",
                        Toast.LENGTH_SHORT).show();

                // Move row from available to completed visually
                task.isClaimed = true;
                availableContainer.removeView(row);

                // Rebuild the row as completed and add to completed section
                completedContainer.addView(makeTaskRow(task), 0);
            } else {
                claimBtn.setEnabled(true);
                claimBtn.setText("Claim");
                Toast.makeText(this, "Failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(android.graphics.Color.alpha(color) * factor);
        return android.graphics.Color.argb(alpha,
                android.graphics.Color.red(color),
                android.graphics.Color.green(color),
                android.graphics.Color.blue(color));
    }

    private int dp(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}