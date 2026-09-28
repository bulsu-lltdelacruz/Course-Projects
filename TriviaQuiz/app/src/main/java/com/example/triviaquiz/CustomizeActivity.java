package com.example.triviaquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layouts.ShopItem;
import utility.CurrentUser;
import utility.ThemeManager;

public class CustomizeActivity extends AppCompatActivity {

    private ImageView btnBack;
    private ImageView previewIcon;
    private ImageView previewBorderRing;

    private LinearLayout iconsGrid;
    private LinearLayout bordersGrid;
    private LinearLayout themesGrid;

    private List<ShopItem> allItems = new ArrayList<>();

    // Track selected card per category
    private ConstraintLayout selectedIconCard   = null;
    private ConstraintLayout selectedBorderCard = null;
    private ConstraintLayout selectedThemeCard  = null;
    private EditText etUsername;
    private Button btnSaveUsername;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);
        if (CurrentUser.instance == null) {
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
            return;
        }

        userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        bindViews();
        buildItemList();
        addListeners();
        renderAllGrids();
        applyLoadedPreview();
        loadAndShowUsername();
    }

    private void bindViews() {
        btnBack           = findViewById(R.id.btnBack);
        previewIcon       = findViewById(R.id.previewIcon);
        previewBorderRing = findViewById(R.id.previewBorderRing);
        iconsGrid         = findViewById(R.id.iconsGrid);
        bordersGrid       = findViewById(R.id.bordersGrid);
        themesGrid        = findViewById(R.id.themesGrid);
        etUsername      = findViewById(R.id.etUsername);
        btnSaveUsername = findViewById(R.id.btnSaveUsername);
    }

    private void buildItemList() {
        allItems = new ArrayList<>();

        allItems.add(new ShopItem(1,  "Default",     0,   R.drawable.avatar_user,   "Icons"));
        allItems.add(new ShopItem(2,  "Dog",         200, R.drawable.avatar_dog,    "Icons"));
        allItems.add(new ShopItem(3,  "Lion",        350, R.drawable.avatar_lion,   "Icons"));
        allItems.add(new ShopItem(4,  "Panda",       0, R.drawable.avatar_panda,  "Icons"));
        allItems.add(new ShopItem(5,  "Woman",       0, R.drawable.avatar_woman,  "Icons"));

        //border
        allItems.add(new ShopItem(6,  "Chinese",     300, R.mipmap.chinese,       "Borders"));
        allItems.add(new ShopItem(7,  "Glitch",      400, R.mipmap.glitch,        "Borders"));
        allItems.add(new ShopItem(8,  "Pink Flower", 350, R.mipmap.pink_flower,   "Borders"));
        allItems.add(new ShopItem(9,  "Purple",      400, R.mipmap.purple,        "Borders"));
        allItems.add(new ShopItem(10, "Red Flower",  350, R.mipmap.red_flower,    "Borders"));
        allItems.add(new ShopItem(11, "Wave",        1000, R.mipmap.wave,          "Borders"));

        //theeme
        allItems.add(new ShopItem(0,  "Light",        0,   R.drawable.theme_light,  "Themes")); // free default
        allItems.add(new ShopItem(12, "Dark Mode",    400, R.drawable.theme_dark,   "Themes"));
        allItems.add(new ShopItem(13, "Ocean Theme",  600, R.drawable.theme_ocean,  "Themes"));
        allItems.add(new ShopItem(14, "Sunset Theme", 600, R.drawable.theme_sunset, "Themes"));
    }

    private void applyLoadedPreview() {
        int avatarRes = R.drawable.avatar_user;
        ShopItem selectedIcon = findById(CurrentUser.instance.selectedIconId);
        if (selectedIcon != null) avatarRes = selectedIcon.iconResId;
        com.bumptech.glide.Glide.with(this)
                .load(avatarRes)
                .circleCrop()
                .into(previewIcon);

        //border
        int selectedBorderId = CurrentUser.instance.selectedBorderId;
        if (selectedBorderId != -1) {
            ShopItem selectedBorder = findById(selectedBorderId);
            if (selectedBorder != null) {
                previewBorderRing.setVisibility(View.VISIBLE);
                com.bumptech.glide.Glide.with(this)
                        .load(selectedBorder.iconResId)
                        .into(previewBorderRing);
            }
        } else {
            previewBorderRing.setVisibility(View.GONE);
        }
    }

    private ShopItem findById(int itemId) {
        for (ShopItem item : allItems) {
            if (item.itemId == itemId) return item;
        }
        return null;
    }

    private void addListeners() {
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void renderAllGrids() {
        renderGrid(iconsGrid,   "Icons");
        renderGrid(themesGrid,  "Themes");
        renderGrid(bordersGrid, "Borders");
    }

    private void renderGrid(LinearLayout grid, String category) {
        grid.removeAllViews();

        List<ShopItem> items = new ArrayList<>();
        for (ShopItem item : allItems) {
            if (item.category.equals(category)) items.add(item);
        }
        android.util.Log.d("GRID", category + " → " + items.size() + " items, grid is " + (grid == null ? "NULL" : "OK"));
        android.util.Log.d("GRID", "ownedIds: " + CurrentUser.instance.ownedItemIds);

        while (items.size() % 4 != 0) items.add(null);

        for (int i = 0; i < items.size(); i += 4) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            row.setOrientation(LinearLayout.HORIZONTAL);

            for (int j = i; j < i + 4 && j < items.size(); j++) {
                ShopItem item = items.get(j);
                if (item == null) {
                    View spacer = new View(this);
                    spacer.setLayoutParams(new LinearLayout.LayoutParams(0,
                            1, 1f));
                    row.addView(spacer);
                } else {
                    boolean owned = CurrentUser.instance.ownedItemIds.contains(item.itemId);

                    row.addView(makeOptionCard(item, owned, category));
                }
            }
            grid.addView(row);
        }
    }

    private LinearLayout makeOptionCard(ShopItem item, boolean owned, String category) {
        LinearLayout wrapper = new LinearLayout(this);
        wrapper.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        wrapper.setOrientation(LinearLayout.VERTICAL);
        wrapper.setGravity(Gravity.CENTER);
        wrapper.setPadding(dp(5), dp(5), dp(5), dp(5));

        ConstraintLayout card = new ConstraintLayout(this);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        boolean isSelected = isCurrentlySelected(item, category);
        card.setBackgroundResource(isSelected
                ? R.drawable.bg_option_card_selected
                : R.drawable.bg_option_card_unselected);

        if (isSelected) {
            switch (category) {
                case "Icons":   selectedIconCard   = card; break;
                case "Borders": selectedBorderCard = card; break;
                case "Themes":  selectedThemeCard  = card; break;
            }
        }

        //ratio
        View ratioHelper = new View(this);
        ratioHelper.setId(View.generateViewId());
        ConstraintLayout.LayoutParams ratioParams = new ConstraintLayout.LayoutParams(0, 0);
        ratioParams.dimensionRatio  = "1:1";
        ratioParams.startToStart    = ConstraintSet.PARENT_ID;
        ratioParams.endToEnd        = ConstraintSet.PARENT_ID;
        ratioParams.topToTop        = ConstraintSet.PARENT_ID;
        ratioParams.bottomToBottom  = ConstraintSet.PARENT_ID;
        ratioHelper.setLayoutParams(ratioParams);
        card.addView(ratioHelper);

        //inner frame
        FrameLayout innerFrame = new FrameLayout(this);
        ConstraintLayout.LayoutParams frameParams = new ConstraintLayout.LayoutParams(dp(44), dp(44));
        frameParams.startToStart    = ConstraintSet.PARENT_ID;
        frameParams.endToEnd        = ConstraintSet.PARENT_ID;
        frameParams.topToTop        = ConstraintSet.PARENT_ID;
        frameParams.bottomToBottom  = ConstraintSet.PARENT_ID;
        innerFrame.setLayoutParams(frameParams);

        if (owned) {
            ImageView iconView = new ImageView(this);
            FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams(dp(32), dp(32));
            iconParams.gravity = Gravity.CENTER;
            iconView.setLayoutParams(iconParams);
            iconView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            innerFrame.addView(iconView);

            com.bumptech.glide.Glide.with(this)
                    .load(item.iconResId)
                    .into(iconView);

            card.setOnClickListener(v -> handleSelection(item, card, category));
        } else {
            innerFrame.setBackgroundResource(R.drawable.bg_lock_wrap);
            ImageView lockIcon = new ImageView(this);
            FrameLayout.LayoutParams lockParams = new FrameLayout.LayoutParams(dp(22), dp(22));
            lockParams.gravity = Gravity.CENTER;
            lockIcon.setLayoutParams(lockParams);
            lockIcon.setImageResource(R.drawable.ic_lock);
            innerFrame.addView(lockIcon);
        }

        card.addView(innerFrame);
        wrapper.addView(card);

        TextView label = new TextView(this);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        labelParams.topMargin = dp(6);
        label.setLayoutParams(labelParams);
        label.setText(item.name);
        label.setTextSize(11f);
        label.setGravity(Gravity.CENTER);
        label.setTextColor(ContextCompat.getColor(this,
                owned ? R.color.textDark : R.color.textMuted));
        wrapper.addView(label);

        return wrapper;
    }

    private boolean isCurrentlySelected(ShopItem item, String category) {
        switch (category) {
            case "Icons":   return item.itemId == CurrentUser.instance.selectedIconId;
            case "Borders": return item.itemId == CurrentUser.instance.selectedBorderId;
            case "Themes":  return item.itemId == CurrentUser.instance.selectedThemeId;
            default:        return false;
        }
    }

    private void handleSelection(ShopItem item, ConstraintLayout card, String category) {
        switch (category) {
            case "Icons":
                if (selectedIconCard != null)
                    selectedIconCard.setBackgroundResource(R.drawable.bg_option_card_unselected);
                selectedIconCard = card;
                CurrentUser.instance.selectedIconId = item.itemId;
                com.bumptech.glide.Glide.with(this)
                        .load(item.iconResId)
                        .circleCrop()
                        .into(previewIcon);
                break;

            case "Borders":
                if (selectedBorderCard != null)
                    selectedBorderCard.setBackgroundResource(R.drawable.bg_option_card_unselected);
                selectedBorderCard = card;
                CurrentUser.instance.selectedBorderId = item.itemId;
                previewBorderRing.setVisibility(View.VISIBLE);
                com.bumptech.glide.Glide.with(this)
                        .load(item.iconResId)
                        .into(previewBorderRing);
                break;

            case "Themes":
                if (selectedThemeCard != null)
                    selectedThemeCard.setBackgroundResource(R.drawable.bg_option_card_unselected);
                selectedThemeCard = card;
                CurrentUser.instance.selectedThemeId = item.itemId;
                ThemeManager.saveLocal(this, item.itemId); // works for 0 too
                Toast.makeText(this, item.name + " theme applied!", Toast.LENGTH_SHORT).show();
                break;
        }

        card.setBackgroundResource(R.drawable.bg_option_card_selected);
        saveCustomization();
        if (category.equals("Themes")) {
            recreate();
        }
    }

    private void saveCustomization() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("customization/selectedIconId",   CurrentUser.instance.selectedIconId);
        updates.put("customization/selectedBorderId", CurrentUser.instance.selectedBorderId);
        updates.put("customization/selectedThemeId",  CurrentUser.instance.selectedThemeId);

        userRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(this, "Failed to save. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAndShowUsername() {
        com.google.firebase.auth.FirebaseUser user =
                com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getDisplayName() != null) {
            etUsername.setText(user.getDisplayName());
        }

        btnSaveUsername.setOnClickListener(v -> saveUsername());

        etUsername.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                saveUsername();
                return true;
            }
            return false;
        });
    }

    private void saveUsername() {
        String newUsername = etUsername.getText().toString().trim();
        if (newUsername.isEmpty()) {
            etUsername.setError("Username can't be empty.");
            return;
        }

        btnSaveUsername.setEnabled(false);
        btnSaveUsername.setText("Saving...");

        com.google.firebase.auth.FirebaseUser user =
                com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        com.google.firebase.auth.UserProfileChangeRequest profileUpdate =
                new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(newUsername)
                        .build();

        user.updateProfile(profileUpdate).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                userRef.child("username").setValue(newUsername)
                        .addOnCompleteListener(dbTask -> {
                            btnSaveUsername.setEnabled(true);
                            btnSaveUsername.setText("Save");
                            if (dbTask.isSuccessful()) {
                                Toast.makeText(this, "Username updated!", Toast.LENGTH_SHORT).show();
                                android.view.inputmethod.InputMethodManager imm =
                                        (android.view.inputmethod.InputMethodManager)
                                                getSystemService(INPUT_METHOD_SERVICE);
                                if (imm != null) imm.hideSoftInputFromWindow(
                                        etUsername.getWindowToken(), 0);
                            } else {
                                Toast.makeText(this, "Failed to save. Try again.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                btnSaveUsername.setEnabled(true);
                btnSaveUsername.setText("Save");
                Toast.makeText(this, "Failed to update username.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int dp(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}