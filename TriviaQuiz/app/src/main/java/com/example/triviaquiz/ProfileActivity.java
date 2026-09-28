package com.example.triviaquiz;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import layouts.ShopItem;
import ranks.*;
import utility.CurrentUser;
import utility.Helper;
import utility.StringValues;
import utility.ThemeManager;

public class ProfileActivity extends AppCompatActivity {

    private ImageView btnMenu;
    private ImageView ivProfileAvatar;
    private ImageView ivProfileBorder;
    private TextView tvProfileName;
    private TextView tvProfileEmail;
    private TextView tvTotalQuizzes;
    private TextView tvCorrectItems;
    private TextView tvRank;
    private TextView tvCurrentScore;
    private TextView tvTotalScore;
    private TextView tvCoins;
    private View viewProgressBar;
    private View viewProgressBarTotal;
    private Button btnLogout;
    private Button btnCustomize;
    private int appliedThemeId = -999;

    FirebaseAuth mAuth;
    private boolean isViewingOtherUser = false;
    private List<ShopItem> allItems = new ArrayList<>();
    private String source = "";
    private TextView tvEquippedIcon;
    private TextView tvEquippedBorder;
    private TextView tvEquippedTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);ThemeManager.apply(this);
        appliedThemeId = CurrentUser.instance != null
                ? CurrentUser.instance.selectedThemeId : -1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        source = getIntent().getStringExtra("source");
        mAuth = FirebaseAuth.getInstance();

        ivProfileAvatar      = findViewById(R.id.ivProfileAvatar);
        ivProfileBorder      = findViewById(R.id.ivProfileBorder);
        tvProfileName        = findViewById(R.id.tvProfileName);
        tvProfileEmail       = findViewById(R.id.tvProfileEmail);
        tvTotalQuizzes       = findViewById(R.id.tvTotalQuizzes);
        tvCorrectItems       = findViewById(R.id.tvCorrectItems);
        tvRank               = findViewById(R.id.tvRank);
        viewProgressBar      = findViewById(R.id.viewProgressBar);
        viewProgressBarTotal = findViewById(R.id.viewProgressBarTotal);
        tvCurrentScore       = findViewById(R.id.tvCurrentScore);
        tvTotalScore         = findViewById(R.id.tvTotalScore);
        tvCoins              = findViewById(R.id.tvCoins);
        btnLogout            = findViewById(R.id.btnLogout);
        btnCustomize         = findViewById(R.id.btnCustomize);
        btnMenu              = findViewById(R.id.btnMenu);
        tvEquippedIcon   = findViewById(R.id.tvEquippedIconValue);
        tvEquippedBorder = findViewById(R.id.tvEquippedBorderValue);
        tvEquippedTheme  = findViewById(R.id.tvEquippedThemeValue);

        buildItemList();

        isViewingOtherUser = getIntent().hasExtra("viewUid");

        if (isViewingOtherUser) {
            setupViewOnlyMode();
        } else {
            setupOwnProfile();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (CurrentUser.instance == null) return;

        //if di pareho theme update
        if (CurrentUser.instance.selectedThemeId != appliedThemeId) {
            recreate();
            return;
        }
        if (!isViewingOtherUser) {
            applyEquippedAppearance(
                    CurrentUser.instance.selectedIconId,
                    CurrentUser.instance.selectedBorderId
            );
            setProfileValues(
                    CurrentUser.instance.currentRank,
                    CurrentUser.instance.totalGames,
                    CurrentUser.instance.totalCorrectItems,
                    CurrentUser.instance.coins,
                    FirebaseAuth.getInstance().getCurrentUser() != null
                            ? FirebaseAuth.getInstance().getCurrentUser().getDisplayName() : "Unknown",
                    FirebaseAuth.getInstance().getCurrentUser() != null
                            ? FirebaseAuth.getInstance().getCurrentUser().getEmail() : ""
            );

            updateEquippedSection();
        }
    }

    private void buildItemList() {
        allItems = new ArrayList<>();

        allItems.add(new ShopItem(1,  "Default",     0,   R.drawable.avatar_user,   "Icons"));
        allItems.add(new ShopItem(2,  "Dog",         200, R.drawable.avatar_dog,    "Icons"));
        allItems.add(new ShopItem(3,  "Lion",        350, R.drawable.avatar_lion,   "Icons"));
        allItems.add(new ShopItem(4,  "Panda",       0, R.drawable.avatar_panda,  "Icons"));
        allItems.add(new ShopItem(5,  "Woman",       0, R.drawable.avatar_woman,  "Icons"));

        // Borders
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

    private void applyEquippedAppearance(int selectedIconId, int selectedBorderId) {

        int avatarRes = R.drawable.avatar_user;
        if (selectedIconId != -1) {
            ShopItem icon = findById(selectedIconId);
            if (icon != null) avatarRes = icon.iconResId;
        }
        com.bumptech.glide.Glide.with(this)
                .load(avatarRes)
                .circleCrop()
                .into(ivProfileAvatar);

        if (selectedBorderId != -1) {
            ShopItem border = findById(selectedBorderId);
            if (border != null) {
                ivProfileBorder.setVisibility(View.VISIBLE);
                com.bumptech.glide.Glide.with(this)
                        .load(border.iconResId)
                        .into(ivProfileBorder);
            }
        } else {
            ivProfileBorder.setVisibility(View.INVISIBLE);
        }
    }


    private void setupOwnProfile() {
        btnLogout.setVisibility(View.VISIBLE);
        btnCustomize.setVisibility(View.VISIBLE);

        applyEquippedAppearance(
                CurrentUser.instance.selectedIconId,
                CurrentUser.instance.selectedBorderId
        );

        setProfileValues(
                CurrentUser.instance.currentRank,
                CurrentUser.instance.totalGames,
                CurrentUser.instance.totalCorrectItems,
                CurrentUser.instance.coins,
                FirebaseAuth.getInstance().getCurrentUser() != null
                        ? FirebaseAuth.getInstance().getCurrentUser().getDisplayName() : "Unknown",
                FirebaseAuth.getInstance().getCurrentUser() != null
                        ? FirebaseAuth.getInstance().getCurrentUser().getEmail() : ""
        );
        updateEquippedSection();

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
                Intent intent = new Intent(ProfileActivity.this, SideMenuActivity.class);
                intent.putExtra("activeIndex", 3);
                startActivity(intent);
            });
        }


        btnLogout.setOnClickListener(v -> showLogoutDialog());

        btnCustomize.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, CustomizeActivity.class)));
    }

    private void showLogoutDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log Out", (dialog, which) -> {
                    CurrentUser.instance.ownedItemIds.clear();
                    CurrentUser.instance.selectedIconId   = 1;
                    CurrentUser.instance.selectedBorderId = -1;
                    CurrentUser.instance.selectedThemeId  = -1;
                    CurrentUser.instance.coins            = 0;
                    CurrentUser.instance.totalGames       = 0;
                    CurrentUser.instance.totalCorrectItems = 0;
                    CurrentUser.instance.currentRank      = null;

                    mAuth.signOut();
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setupViewOnlyMode() {
        btnLogout.setVisibility(View.GONE);
        btnCustomize.setVisibility(View.GONE);
        tvProfileEmail.setVisibility(View.GONE);

        btnMenu.setImageResource(R.drawable.ic_back);
        btnMenu.setOnClickListener(v -> finish());

        String username  = getIntent().getStringExtra("viewUsername");
        String rankName  = getIntent().getStringExtra("viewRank");
        int rankPoints   = getIntent().getIntExtra("viewRankPoints", 0);
        int totalGames   = getIntent().getIntExtra("viewTotalGames", 0);
        int correctItems = getIntent().getIntExtra("viewCorrectItems", 0);
        int coins        = getIntent().getIntExtra("viewCoins", 0);
        String viewUid   = getIntent().getStringExtra("viewUid");

        RankBase tempRank = buildRank(rankName, rankPoints);
        setProfileValues(tempRank, totalGames, correctItems, coins, username, "");
        applyEquippedAppearance(-1, -1);

        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users").child(viewUid).child("customization");

        userRef.get().addOnCompleteListener(task -> {
            int iconId   = -1;
            int borderId = -1;
            int themeId  = 0;

            if (task.isSuccessful() && task.getResult().exists()) {
                com.google.firebase.database.DataSnapshot snap = task.getResult();
                if (snap.child("selectedIconId").exists())
                    iconId = snap.child("selectedIconId").getValue(Integer.class);
                if (snap.child("selectedBorderId").exists())
                    borderId = snap.child("selectedBorderId").getValue(Integer.class);
                if (snap.child("selectedThemeId").exists())
                    themeId = snap.child("selectedThemeId").getValue(Integer.class);
            }

            applyEquippedAppearance(iconId, borderId);

            ShopItem icon = findById(iconId);
            tvEquippedIcon.setText(icon != null ? icon.name : "Default");

            int finalBorderId = borderId;
            if (finalBorderId == -1) {
                tvEquippedBorder.setText("None");
            } else {
                ShopItem border = findById(finalBorderId);
                tvEquippedBorder.setText(border != null ? border.name : "None");
            }

            ShopItem theme = findById(themeId);
            tvEquippedTheme.setText(theme != null ? theme.name : "Light");
        });
    }

    private void setProfileValues(RankBase rank, int totalGames, int correctItems,
                                  int coins, String username, String email) {
        tvProfileName.setText(username);
        tvProfileEmail.setText(email);
        tvTotalQuizzes.setText(String.valueOf(totalGames));
        tvCorrectItems.setText(String.valueOf(correctItems));
        tvCoins.setText(String.valueOf(coins));

        tvRank.setText(rank.rankName);
        tvRank.setBackgroundResource(rank.background);
        tvRank.setTextColor(ContextCompat.getColor(this, rank.strokeColor));
        tvRank.setCompoundDrawableTintList(ColorStateList.valueOf(
                ContextCompat.getColor(this, rank.strokeColor)));

        if (isViewingOtherUser) {
            float current  = rank.currentPoints;
            float max      = rank.maxPoints > 0 ? rank.maxPoints : current;
            float progress = max > 0 ? current / max : 1f;

            tvCurrentScore.setText(String.format("%.0f Points", current));
            tvTotalScore.setText(rank instanceof Oracle
                    ? "" : String.format("%.0f Points", max));

            viewProgressBar.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, progress));
            viewProgressBarTotal.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 1f - progress));
        } else {
            LinearLayout.LayoutParams[] params = Helper.getRankBarParams();
            String[] rankText = Helper.getRankPointsText();
            tvCurrentScore.setText(rankText[0]);
            tvTotalScore.setText(rankText[1]);
            viewProgressBar.setLayoutParams(params[0]);
            viewProgressBarTotal.setLayoutParams(params[1]);
        }
    }
    private void updateEquippedSection() {
        //if (isViewingOtherUser) return;

        ShopItem icon = findById(CurrentUser.instance.selectedIconId);
        tvEquippedIcon.setText(icon != null ? icon.name : "Default");

        int borderId = CurrentUser.instance.selectedBorderId;
        if (borderId == -1) {
            tvEquippedBorder.setText("None");
        } else {
            ShopItem border = findById(borderId);
            tvEquippedBorder.setText(border != null ? border.name : "None");
        }

        int themeId = CurrentUser.instance.selectedThemeId;
        ShopItem theme = findById(themeId);
        tvEquippedTheme.setText(theme != null ? theme.name : "Light");
    }

    private RankBase buildRank(String rankName, int rankPoints) {
        switch (rankName) {
            case StringValues.RANK_APPRENTICE: return new Apprentice(rankPoints);
            case StringValues.RANK_SCHOLAR:    return new Scholar(rankPoints);
            case StringValues.RANK_SAGE:       return new Sage(rankPoints);
            case StringValues.RANK_MASTER:     return new Master(rankPoints);
            case StringValues.RANK_GENIUS:     return new Genius(rankPoints);
            case StringValues.RANK_ORACLE:     return new Oracle(rankPoints);
            default:                           return new Novice(rankPoints);
        }
    }

}