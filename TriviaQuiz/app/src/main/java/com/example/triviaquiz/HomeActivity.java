package com.example.triviaquiz;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import layouts.ShopItem;
import ranks.Oracle;
import utility.CurrentUser;
import utility.ThemeManager;

public class HomeActivity extends AppCompatActivity {

    private ImageView btnMenu;
    private LinearLayout btnPlayQuiz;
    private LinearLayout btnShop;
    private LinearLayout btnLeaderboard;
    private TextView tvRank;
    private TextView tvCoins;
    private LinearLayout btnProfile;
    private LinearLayout btnTasks;
    private View viewProgressBar;
    private View viewProgressBarTotal;
    private TextView tvCurrentScore;
    private TextView tvTotalScore;
    private TextView tvUserName;
    private ImageView ivBorder;
    private ImageView ivIcon;
    private List<ShopItem> allItems = new ArrayList<>();
    private int appliedThemeId = -999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (CurrentUser.instance == null) {
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
            return;
        }
        appliedThemeId = CurrentUser.instance.selectedThemeId;

        btnPlayQuiz        = findViewById(R.id.btnPlayQuiz);
        btnShop            = findViewById(R.id.btnShop);
        btnProfile         = findViewById(R.id.btnProfile);
        btnTasks         = findViewById(R.id.btnTasks);
        tvRank             = findViewById(R.id.tvRank);
        tvCoins            = findViewById(R.id.tvCoins);
        btnMenu            = findViewById(R.id.btnMenu);
        viewProgressBar    = findViewById(R.id.viewProgressBar);
        viewProgressBarTotal = findViewById(R.id.viewProgressBarTotal);
        tvCurrentScore     = findViewById(R.id.tvCurrentScore);
        tvTotalScore       = findViewById(R.id.tvTotalScore);
        ivBorder           = findViewById(R.id.ivBorder);
        ivIcon             = findViewById(R.id.ivIcon);
        btnLeaderboard     = findViewById(R.id.btnLeaderboard);
        tvUserName          = findViewById(R.id.tvUserName);

        String username = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getDisplayName() : "Unknown";
        tvUserName.setText(username);
        buildItemList();
        applyCustomization();
        applyRankInfo();
        addListeners();

        //back
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Exit App")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Exit", (dialog, which) -> {
                            finishAffinity();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (CurrentUser.instance == null) return;

        if (CurrentUser.instance.selectedThemeId != appliedThemeId) {
            recreate();
            return;
        }
        String username = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getDisplayName() : "Unknown";
        tvUserName.setText(username);
        tvCoins.setText(String.format("%,d", CurrentUser.instance.coins));
        applyCustomization();
        applyRankInfo();
    }

    private void buildItemList() {
        allItems = new ArrayList<>();

        allItems.add(new ShopItem(1,  "Default",     0,   R.drawable.avatar_user,   "Icons"));
        allItems.add(new ShopItem(2,  "Dog",         200, R.drawable.avatar_dog,    "Icons"));
        allItems.add(new ShopItem(3,  "Lion",        350, R.drawable.avatar_lion,   "Icons"));
        allItems.add(new ShopItem(4,  "Panda",       0, R.drawable.avatar_panda,  "Icons"));
        allItems.add(new ShopItem(5,  "Woman",       0, R.drawable.avatar_woman,  "Icons"));

        allItems.add(new ShopItem(6,  "Chinese",     300, R.drawable.chinese,       "Borders"));
        allItems.add(new ShopItem(7,  "Glitch",      400, R.drawable.glitch,        "Borders"));
        allItems.add(new ShopItem(8,  "Pink Flower", 350, R.drawable.pink_flower,   "Borders"));
        allItems.add(new ShopItem(9,  "Purple",      400, R.drawable.purple,        "Borders"));
        allItems.add(new ShopItem(10, "Red Flower",  350, R.drawable.red_flower,    "Borders"));
        allItems.add(new ShopItem(11, "Wave",        1000, R.drawable.wave,          "Borders"));


        allItems.add(new ShopItem(0,  "Light",        0,   R.drawable.theme_light,  "Themes")); // free default
        allItems.add(new ShopItem(12, "Dark Mode",    400, R.drawable.theme_dark,   "Themes"));
        allItems.add(new ShopItem(13, "Ocean Theme",  600, R.drawable.theme_ocean,  "Themes"));
        allItems.add(new ShopItem(14, "Sunset Theme", 600, R.drawable.theme_sunset, "Themes"));
    }

    private ShopItem findById(int itemId) {
        for (ShopItem item : allItems) {
            if (item.itemId == itemId) return item;
        }
        return null;
    }

    private void applyCustomization() {
        int avatarRes = R.drawable.avatar_user;
        int selectedIconId = CurrentUser.instance.selectedIconId;
        if (selectedIconId != -1) {
            ShopItem icon = findById(selectedIconId);
            if (icon != null) avatarRes = icon.iconResId;
        }
        com.bumptech.glide.Glide.with(this)
                .load(avatarRes)
                .circleCrop()
                .into(ivIcon);

        int selectedBorderId = CurrentUser.instance.selectedBorderId;
        if (selectedBorderId != -1) {
            ShopItem border = findById(selectedBorderId);
            if (border != null) {
                ivBorder.setVisibility(View.VISIBLE);
                com.bumptech.glide.Glide.with(this)
                        .load(border.iconResId)
                        .into(ivBorder);
            }
        } else {
            ivBorder.setVisibility(View.GONE);
        }
    }

    // ── Rank card ─────────────────────────────────────────────────────────────

    private void applyRankInfo() {
        RankBase currentRank = CurrentUser.instance.currentRank;
        tvRank.setText(currentRank.rankName);
        tvRank.setBackgroundResource(currentRank.background);
        tvRank.setTextColor(ContextCompat.getColor(this, currentRank.strokeColor));
        tvRank.setCompoundDrawableTintList(ColorStateList.valueOf(
                ContextCompat.getColor(this, currentRank.strokeColor)));

        tvCoins.setText(String.format("%,d", CurrentUser.instance.coins));

        float currentPoints = CurrentUser.instance.currentRank.currentPoints;
        float maxPoints     = CurrentUser.instance.currentRank.maxPoints;

        if (!(CurrentUser.instance.currentRank instanceof Oracle)) {
            tvCurrentScore.setText(String.format("%.0f Points", currentPoints));
            tvTotalScore.setText(String.format("%.0f Points", maxPoints));
        } else {
            tvCurrentScore.setText(String.format("%.0f Points", currentPoints));
            tvTotalScore.setText("");
            maxPoints = currentPoints;
        }

        float progress = currentPoints / maxPoints;

        viewProgressBar.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, progress));
        viewProgressBarTotal.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f - progress));
    }

    private void addListeners() {
        tvRank.setOnClickListener(v -> showRankInfoDialog());
        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SideMenuActivity.class);
            intent.putExtra("activeIndex", 0);
            startActivity(intent);
        });

        btnPlayQuiz.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, SelectQuizSourceActivity.class)));

        btnShop.setOnClickListener(v ->
                {
                    Intent intent = new Intent(HomeActivity.this, ShopActivity.class);
                    intent.putExtra("source", "Home");
                    startActivity(intent);
                });
        btnTasks.setOnClickListener(v ->
                {
                    Intent intent = new Intent(HomeActivity.this, TasksActivity.class);
                    intent.putExtra("source", "Home");
                    startActivity(intent);
                });

        btnProfile.setOnClickListener(v ->
                {
                    Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    intent.putExtra("source", "Home");
                    startActivity(intent);
                });
        btnLeaderboard.setOnClickListener(v ->
                {
                    Intent intent = new Intent(HomeActivity.this, LeaderboardActivity.class);
                    intent.putExtra("source", "Home");
                    startActivity(intent);
                });
    }
    private void showRankInfoDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);


        // Scroll container
        ScrollView scrollView = new ScrollView(this);
        scrollView.setPadding(dp(8), dp(8), dp(8), dp(8));

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(dp(16), dp(16), dp(16), dp(8));
        scrollView.addView(container);
        scrollView.setBackgroundResource(R.drawable.bg_card_white);
        scrollView.setBackgroundTintList(ColorStateList.valueOf(ThemeManager.getColorFromAttr(this, R.attr.backgroundBase)));
        //container.setBackgroundResource(R.drawable.bg_card_white);
        //container.setBackgroundTintList(ColorStateList.valueOf(ThemeManager.getColorFromAttr(this, R.attr.backgroundBase)));

        //title
        TextView title = new TextView(this);
        title.setText("All Ranks");
        title.setTextSize(18f);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setTextColor(ThemeManager.getColorFromAttr(this, R.attr.textPrimary));
        title.setGravity(android.view.Gravity.CENTER);
        title.setPadding(0, 0, 0, dp(16));
        container.addView(title);

        //rank
        int[][] ranks = {
                {R.string.rank_novice,     R.drawable.bg_rank_novice,     R.color.strokeRankBronze,     0},
                {R.string.rank_apprentice, R.drawable.bg_rank_apprentice, R.color.strokeRankApprentice, 50},
                {R.string.rank_scholar,    R.drawable.bg_rank_scholar,    R.color.strokeRankScholar,    150},
                {R.string.rank_sage,       R.drawable.bg_rank_sage,       R.color.strokeRankSage,       270},
                {R.string.rank_master,     R.drawable.bg_rank_master,     R.color.strokeRankMaster,     420},
                {R.string.rank_genius,     R.drawable.bg_rank_genius,     R.color.strokeRankGenius,     600},
                {R.string.rank_oracle,     R.drawable.bg_rank_oracle,     R.color.strokeRankOracle,     800},
        };

        String currentRankName = CurrentUser.instance.currentRank.rankName;

        for (int[] rank : ranks) {
            String rankName   = getString(rank[0]);
            int bgDrawable    = rank[1];
            int strokeColor   = rank[2];
            int pointReq      = rank[3];
            boolean isCurrent = rankName.equals(currentRankName);

            //row
            LinearLayout row = new LinearLayout(this);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            rowParams.setMargins(0, 0, 0, dp(8));
            row.setLayoutParams(rowParams);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);
            row.setPadding(dp(12), dp(10), dp(12), dp(10));
            row.setBackgroundResource(isCurrent
                    ? R.drawable.bg_option_card_selected
                    : R.drawable.bg_card_white);
            row.setBackgroundTintList(ColorStateList.valueOf(isCurrent?
                    ThemeManager.getColorFromAttr(this, R.attr.cardSurfaceGreyed)
        :ThemeManager.getColorFromAttr(this, R.attr.cardSurface)));

            //rank lbadge
            TextView badge = new TextView(this);
            LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            badge.setLayoutParams(badgeParams);
            badge.setText(rankName);
            badge.setTextSize(12f);
            badge.setTypeface(null, android.graphics.Typeface.BOLD);
            badge.setPadding(dp(10), dp(5), dp(10), dp(5));
            badge.setBackgroundResource(bgDrawable);
            badge.setTextColor(ContextCompat.getColor(this, strokeColor));
            badge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trophy, 0, 0, 0);
            badge.setCompoundDrawablePadding(dp(5));
            badge.setCompoundDrawableTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, strokeColor)));
            row.addView(badge);

            View spacer = new View(this);
            spacer.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            row.addView(spacer);

            //point requirement
            TextView points = new TextView(this);
            points.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            points.setText(pointReq == 0 ? "Starting rank" : pointReq + " RP total");
            points.setTextSize(11f);
            points.setTextColor(isCurrent ?ThemeManager.getColorFromAttr(this, R.attr.cardSurface) :ThemeManager.getColorFromAttr(this, R.attr.textSubtitle)
            );

            //you label if current
            if (isCurrent) {
                LinearLayout rightCol = new LinearLayout(this);
                rightCol.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                rightCol.setOrientation(LinearLayout.VERTICAL);
                rightCol.setGravity(android.view.Gravity.END);

                TextView youLabel = new TextView(this);
                youLabel.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                youLabel.setText("YOU");
                youLabel.setTextSize(9f);
                youLabel.setTypeface(null, android.graphics.Typeface.BOLD);
                youLabel.setTextColor(ContextCompat.getColor(this, R.color.surfaceWhite));
                rightCol.addView(youLabel);
                rightCol.addView(points);
                row.addView(rightCol);
            } else {
                row.addView(points);
            }

            container.addView(row);
        }

        builder.setView(scrollView);
        builder.setPositiveButton("Close", null);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();

        if (dialog.getWindow() != null) {
            android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
            bg.setColor(ThemeManager.getColorFromAttr(this, R.attr.backgroundBase));
            bg.setCornerRadius(dp(16));
            dialog.getWindow().setBackgroundDrawable(bg);
        }
    }

    private int dp(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}