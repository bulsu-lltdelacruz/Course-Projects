package com.example.triviaquiz;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import utility.ThemeManager;

public class SideMenuActivity extends AppCompatActivity {

    LinearLayout[] menuButtons;
    Context c;
    private int currentSelectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);

        overridePendingTransition(R.anim.slide_in_left, R.anim.no_anim);
        c = this;
        menuButtons = new LinearLayout[6]; // ← was 6
        getMenuButtons();

        currentSelectedIndex = getIntent().getIntExtra("activeIndex", 0);
        setSelectedButtonAtIndex(currentSelectedIndex);

        findViewById(R.id.btnCloseMenu).setOnClickListener(v -> finish());
       // findViewById(R.id.viewClickableBackground).setOnClickListener(v -> finish());

        setupMenuButton(R.id.menuHome,        "Home",        0);
        setupMenuButton(R.id.menuPlayQuiz,    "Play Quiz",   1);
        setupMenuButton(R.id.menuShop,        "Shop",        2);
        setupMenuButton(R.id.menuProfile,     "Profile",     3);
        //setupMenuButton(R.id.menuCustomize,   "Customize",   4);
        setupMenuButton(R.id.menuLeaderboard, "Leaderboard", 4);
        setupMenuButton(R.id.menuTasks, "Tasks", 5);
    }

    private void setupMenuButton(int id, String label, int index) {
        LinearLayout menuButton = findViewById(id);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> {
                setUnSelectedButtonAtIndex(currentSelectedIndex);
                setSelectedButtonAtIndex(index);
                currentSelectedIndex = index;
                switch (label) {
                    case "Home":
                        startActivity(new Intent(SideMenuActivity.this, HomeActivity.class));
                        break;
                    case "Play Quiz":
                        startActivity(new Intent(SideMenuActivity.this, SelectQuizSourceActivity.class));
                        break;
                    case "Shop":
                        startActivity(new Intent(SideMenuActivity.this, ShopActivity.class));
                        break;
                    case "Profile":
                        startActivity(new Intent(SideMenuActivity.this, ProfileActivity.class));
                        break;
                    /*case "Customize":
                        startActivity(new Intent(SideMenuActivity.this, CustomizeActivity.class));
                        break;*/
                    case "Leaderboard":
                        startActivity(new Intent(SideMenuActivity.this, LeaderboardActivity.class));
                        break;
                    case "Tasks":
                        startActivity(new Intent(SideMenuActivity.this, TasksActivity.class));
                        break;
                }
                finish();
            });
        }
    }

    private void setSelectedButtonAtIndex(int index) {
        LinearLayout btn = menuButtons[index];
        btn.setBackgroundResource(R.drawable.bg_card_white);
        btn.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(c, R.color.primaryRed)));
        ((ImageView) btn.getChildAt(0)).setImageTintList(
                ColorStateList.valueOf(ContextCompat.getColor(c, R.color.surfaceWhite)));
        ((TextView) btn.getChildAt(1)).setTextColor(
                ContextCompat.getColor(c, R.color.surfaceWhite));
    }

    private void setUnSelectedButtonAtIndex(int index) {
        LinearLayout btn = menuButtons[index];
        btn.setBackgroundResource(0);
        btn.setBackgroundTintList(null);
        ((ImageView) btn.getChildAt(0)).setImageTintList(
                ColorStateList.valueOf(ContextCompat.getColor(c, R.color.textMuted)));
        ((TextView) btn.getChildAt(1)).setTextColor(
                ContextCompat.getColor(c, R.color.textMuted));
    }

    private void getMenuButtons() {
        menuButtons[0] = findViewById(R.id.menuHome);
        menuButtons[1] = findViewById(R.id.menuPlayQuiz);
        menuButtons[2] = findViewById(R.id.menuShop);
        menuButtons[3] = findViewById(R.id.menuProfile);
        //menuButtons[4] = findViewById(R.id.menuCustomize);
        menuButtons[4] = findViewById(R.id.menuLeaderboard);
        menuButtons[5] = findViewById(R.id.menuTasks);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.no_anim, R.anim.slide_out_left);
    }
}