package com.example.triviaquiz;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.*;

import utility.CurrentUser;
import utility.LeaderboardUser;
import utility.StringValues;
import utility.ThemeManager;

public class LeaderboardActivity extends AppCompatActivity {

    private ImageView btnMenu;
    private TextView filterRank, filterWinRate, filterGamesPlayed;
    private LinearLayout leaderboardContainer;
    private ProgressBar progressBar;

    private List<LeaderboardUser> allUsers = new ArrayList<>();
    private String activeFilter = "Rank";
    private String currentUid;
    private String source = "";
    private int appliedThemeId = -999;

    private static final Map<String, Integer> RANK_ORDER = new HashMap<>();
    static {
        RANK_ORDER.put(StringValues.RANK_NOVICE,     1);
        RANK_ORDER.put(StringValues.RANK_APPRENTICE, 2);
        RANK_ORDER.put(StringValues.RANK_SCHOLAR,    3);
        RANK_ORDER.put(StringValues.RANK_SAGE,       4);
        RANK_ORDER.put(StringValues.RANK_MASTER,     5);
        RANK_ORDER.put(StringValues.RANK_GENIUS,     6);
        RANK_ORDER.put(StringValues.RANK_ORACLE,     7);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
// In onCreate() after ThemeManager.apply(this)
        appliedThemeId = CurrentUser.instance != null
                ? CurrentUser.instance.selectedThemeId : -1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        if (CurrentUser.instance == null) {
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
            return;
        }

        source = getIntent().getStringExtra("source");
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        btnMenu              = findViewById(R.id.btnMenu);
        filterRank           = findViewById(R.id.filterRank);
        filterWinRate        = findViewById(R.id.filterWinRate);
        filterGamesPlayed    = findViewById(R.id.filterGamesPlayed);
        leaderboardContainer = findViewById(R.id.leaderboardContainer);
        progressBar          = findViewById(R.id.progressBar);

        addListeners();
        loadLeaderboard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CurrentUser.instance == null) return;

        if (CurrentUser.instance.selectedThemeId != appliedThemeId) {
            recreate();
            return;
        }

        loadLeaderboard();
    }

    private void addListeners() {
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
                Intent intent = new Intent(LeaderboardActivity.this, SideMenuActivity.class);
                intent.putExtra("activeIndex", 4); // add index 5 to your SideMenuActivity
                startActivity(intent);
            });
        }


        filterRank.setOnClickListener(v        -> switchFilter("Rank",        filterRank));
        filterWinRate.setOnClickListener(v     -> switchFilter("WinRate",     filterWinRate));
        filterGamesPlayed.setOnClickListener(v -> switchFilter("GamesPlayed", filterGamesPlayed));
    }

    private void switchFilter(String filter, TextView selected) {
        activeFilter = filter;
        setFilterSelected(selected);
        renderList();
    }

    private void setFilterSelected(TextView selected) {
        TextView[] all = {filterRank, filterWinRate, filterGamesPlayed};
        for (TextView tv : all) {
            boolean isSelected = tv == selected;
            int color =isSelected ? ContextCompat.getColor(this,R.color.textDark): ThemeManager.getColorFromAttr(this, R.attr.textPrimary);
            tv.setTextColor(color);
            tv.setBackgroundResource(isSelected
                    ? R.drawable.bg_card_black_rounded
                    : R.drawable.bg_card_white_rounded);
            tv.setBackgroundTintList(ColorStateList.valueOf(
                    isSelected
                    ? ThemeManager.getColorFromAttr(this, R.color.surfaceWhite)
                    :  ThemeManager.getColorFromAttr(this, R.attr.cardSurfaceGreyed)));
        }
    }

    private void loadLeaderboard() {
        progressBar.setVisibility(View.VISIBLE);
        leaderboardContainer.removeAllViews();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.limitToFirst(200).get().addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);

            if (!task.isSuccessful() || task.getResult() == null) {
                Toast.makeText(this, "Failed to load leaderboard.", Toast.LENGTH_SHORT).show();
                return;
            }

            allUsers.clear();
            for (DataSnapshot snap : task.getResult().getChildren()) {
                //if admin dont add
                Boolean isAdmin = snap.child("isAdmin").getValue(Boolean.class);
                if (isAdmin != null && isAdmin) continue;

                LeaderboardUser user = new LeaderboardUser();
                user.uid = snap.getKey();

                user.username = snap.child("username").exists()
                        ? snap.child("username").getValue(String.class) : "Unknown";
                user.rank = snap.child("rank").exists()
                        ? snap.child("rank").getValue(String.class) : StringValues.RANK_NOVICE;
                user.rankPoints = snap.child("rankPoints").exists()
                        ? snap.child("rankPoints").getValue(Integer.class) : 0;
                user.totalGames = snap.child("totalQuizzesPlayed").exists()
                        ? snap.child("totalQuizzesPlayed").getValue(Integer.class) : 0;
                user.totalCorrectItems = snap.child("totalCorrectItems").exists()
                        ? snap.child("totalCorrectItems").getValue(Integer.class) : 0;
                user.coins = snap.child("coins").exists()
                        ? snap.child("coins").getValue(Integer.class) : 0;

                allUsers.add(user);
            }

            renderList();
        });
    }

    private void renderList() {
        List<LeaderboardUser> sorted = new ArrayList<>(allUsers);

        switch (activeFilter) {
            case "Rank":
                sorted.sort((a, b) -> {
                    int tierDiff = getRankOrder(b.rank) - getRankOrder(a.rank);
                    if (tierDiff != 0) return tierDiff;
                    return b.rankPoints - a.rankPoints;
                });
                break;
            case "WinRate":
                sorted.sort((a, b) -> Double.compare(b.getWinRate(), a.getWinRate()));
                break;
            case "GamesPlayed":
                sorted.sort((a, b) -> b.totalGames - a.totalGames);
                break;
        }

        if (sorted.size() > 50) sorted = sorted.subList(0, 50);

        leaderboardContainer.removeAllViews();
        for (int i = 0; i < sorted.size(); i++) {
            leaderboardContainer.addView(makeRow(sorted.get(i), i + 1));
        }
    }

    private int getRankOrder(String rank) {
        Integer order = RANK_ORDER.get(rank);
        return order != null ? order : 0;
    }

    private LinearLayout makeRow(LeaderboardUser user, int position) {
        boolean isCurrentUser = user.uid.equals(currentUid);

        //outer card
        LinearLayout row = new LinearLayout(this);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        rowParams.setMargins(0, 0, 0, dp(8));
        row.setLayoutParams(rowParams);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(16), dp(12), dp(16), dp(12));
        row.setBackgroundResource(isCurrentUser
                ? R.drawable.bg_card_coins      //highlight if user
                : R.drawable.bg_card_white);
        row.setClickable(true);
        row.setFocusable(true);

        //rank
        TextView tvPosition = new TextView(this);
        LinearLayout.LayoutParams posParams = new LinearLayout.LayoutParams(dp(32),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvPosition.setLayoutParams(posParams);
        tvPosition.setText(String.valueOf(position));
        tvPosition.setTextSize(14f);
        tvPosition.setTypeface(null, android.graphics.Typeface.BOLD);
        tvPosition.setTextColor(ContextCompat.getColor(this,
                position <= 3 ? R.color.strokeRankBronze : R.color.textMuted));
        row.addView(tvPosition);

        //username, stat
        LinearLayout middle = new LinearLayout(this);
        middle.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        middle.setOrientation(LinearLayout.VERTICAL);

        TextView tvUsername = new TextView(this);
        tvUsername.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        tvUsername.setText(isCurrentUser ? user.username + " (You)" : user.username);
        tvUsername.setTextSize(14f);
        tvUsername.setTypeface(null, android.graphics.Typeface.BOLD);
        tvUsername.setTextColor(ContextCompat.getColor(this, R.color.textDark));
        middle.addView(tvUsername);

        TextView tvStat = new TextView(this);
        tvStat.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        tvStat.setText(getStatLabel(user));
        tvStat.setTextSize(11f);
        tvStat.setTextColor(ContextCompat.getColor(this, R.color.textMuted));
        middle.addView(tvStat);

        row.addView(middle);

        //tankbadge
        TextView tvRank = new TextView(this);
        LinearLayout.LayoutParams rankParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvRank.setLayoutParams(rankParams);
        tvRank.setText(user.rank);
        tvRank.setTextSize(11f);
        tvRank.setTypeface(null, android.graphics.Typeface.BOLD);
        tvRank.setPadding(dp(8), dp(4), dp(8), dp(4));
        tvRank.setTextColor(ContextCompat.getColor(this, getRankColor(user.rank)));
        tvRank.setBackgroundResource(getRankBackground(user.rank));
        row.addView(tvRank);

        row.setOnClickListener(v -> {
            Intent intent = new Intent(LeaderboardActivity.this, ProfileActivity.class);
            intent.putExtra("viewUid",           user.uid);
            intent.putExtra("viewUsername",      user.username);
            intent.putExtra("viewRank",          user.rank);
            intent.putExtra("viewRankPoints",    user.rankPoints);
            intent.putExtra("viewTotalGames",    user.totalGames);
            intent.putExtra("viewCorrectItems",  user.totalCorrectItems);
            intent.putExtra("viewCoins",         user.coins);
            startActivity(intent);
        });

        return row;
    }

    private String getStatLabel(LeaderboardUser user) {
        switch (activeFilter) {
            case "WinRate":
                return String.format("%.1f%% win rate", user.getWinRate() * 100);
            case "GamesPlayed":
                return user.totalGames + " games played";
            default:
                return user.rankPoints + " RP";
        }
    }

    private int getRankColor(String rank) {
        switch (rank) {
            case StringValues.RANK_ORACLE:     return R.color.strokeRankOracle;
            case StringValues.RANK_GENIUS:     return R.color.strokeRankGenius;
            case StringValues.RANK_MASTER:     return R.color.strokeRankMaster;
            case StringValues.RANK_SAGE:       return R.color.strokeRankSage;
            case StringValues.RANK_SCHOLAR:    return R.color.strokeRankScholar;
            case StringValues.RANK_APPRENTICE: return R.color.strokeRankApprentice;
            case StringValues.RANK_NOVICE:
            default:                           return R.color.strokeRankBronze;
        }
    }

    private int getRankBackground(String rank) {
        switch (rank) {
            case StringValues.RANK_ORACLE:     return R.drawable.bg_rank_oracle;
            case StringValues.RANK_GENIUS:     return R.drawable.bg_rank_genius;
            case StringValues.RANK_MASTER:     return R.drawable.bg_rank_master;
            case StringValues.RANK_SAGE:       return R.drawable.bg_rank_sage;
            case StringValues.RANK_SCHOLAR:    return R.drawable.bg_rank_scholar;
            case StringValues.RANK_APPRENTICE: return R.drawable.bg_rank_apprentice;
            case StringValues.RANK_NOVICE:
            default:                           return R.drawable.bg_rank_novice;
        }
    }

    private int dp(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}