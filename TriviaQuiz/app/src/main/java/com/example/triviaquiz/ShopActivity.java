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

import layouts.ShopItem;
import utility.CurrentUser;
import utility.ThemeManager;

public class ShopActivity extends AppCompatActivity {

    private ImageView btnMenu;
    private TextView tvCoins;
    private TextView filterAll, filterIcons, filterBorders, filterThemes;
    private LinearLayout itemsContainer;
    private TextView currentSectionLabel;

    private List<ShopItem> allItems;
    private String activeFilter = "All";

    // Firebase
    private DatabaseReference userRef;
    private String uid;
    private String source = "";
    private List<Integer> ownedItemIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        if (CurrentUser.instance == null) {
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
            return;
        }
        source = getIntent().getStringExtra("source");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        bindViews();
        buildItemList();
        addListeners();
        updateCoinDisplay();

        ownedItemIds = CurrentUser.instance.ownedItemIds;
        renderItems(activeFilter);
    }

    private void bindViews() {
        btnMenu             = findViewById(R.id.btnMenu);
        tvCoins             = findViewById(R.id.tvCoins);
        filterAll           = findViewById(R.id.filterAll);
        filterIcons         = findViewById(R.id.filterIcons);
        filterBorders       = findViewById(R.id.filterBorders);
        filterThemes        = findViewById(R.id.filterThemes);
        itemsContainer      = findViewById(R.id.itemsContainer);
        currentSectionLabel = findViewById(R.id.tvSectionLabel);
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

    private void handlePurchase(ShopItem item, Button buyBtn) {
        if (CurrentUser.instance.coins < item.price) {
            Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show();
            return;
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Confirm Purchase")
                .setMessage("Buy " + item.name + " for " + item.price + " coins?")
                .setPositiveButton("Buy", (dialog, which) -> executePurchase(item, buyBtn))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void executePurchase(ShopItem item, Button buyBtn) {
        buyBtn.setEnabled(false);
        buyBtn.setText("Buying...");

        int newCoins = CurrentUser.instance.coins - item.price;

        java.util.Map<String, Object> updates = new java.util.HashMap<>();
        updates.put("coins", newCoins);
        updates.put("ownedItems/" + item.itemId, true);

        userRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                CurrentUser.instance.coins = newCoins;
                ownedItemIds.add(item.itemId);
                updateCoinDisplay();
                markOwned(buyBtn);
                Toast.makeText(this, item.name + " purchased!", Toast.LENGTH_SHORT).show();
            } else {
                buyBtn.setEnabled(true);
                buyBtn.setText("Buy");
                Toast.makeText(this, "Purchase failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addListeners() {
        btnMenu.setBackgroundResource(R.drawable.ic_back);

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
            btnMenu.setOnClickListener(v->{
                finish();
            });
            btnMenu.setOnClickListener(v -> {
                Intent intent = new Intent(ShopActivity.this, SideMenuActivity.class);
                intent.putExtra("activeIndex", 2);
                startActivity(intent);
            });
        }


        filterAll.setOnClickListener(v     -> switchFilter("All",     filterAll));
        filterIcons.setOnClickListener(v   -> switchFilter("Icons",   filterIcons));
        filterBorders.setOnClickListener(v -> switchFilter("Borders", filterBorders));
        filterThemes.setOnClickListener(v  -> switchFilter("Themes",  filterThemes));
    }

    private void switchFilter(String filter, TextView selected) {
        activeFilter = filter;
        setFilterSelected(selected);
        renderItems(filter);
    }

    private void setFilterSelected(TextView selected) {
        TextView[] all = {filterAll, filterIcons, filterBorders, filterThemes};
        for (TextView tv : all) {
            boolean isSelected = tv == selected;
            int color =isSelected ? ContextCompat.getColor(this,R.color.textDark): ThemeManager.getColorFromAttr(this, R.attr.textPrimary);
            tv.setTextColor(color);
            tv.setBackgroundResource(isSelected
                    ? R.drawable.bg_card_black_rounded
                    : R.drawable.bg_card_white_rounded);
            tv.setBackgroundTintList(ColorStateList.valueOf(
                    isSelected
                            ? ContextCompat.getColor(this, R.color.surfaceWhite)
                            : ThemeManager.getColorFromAttr(this, R.attr.cardSurfaceGreyed)));
        }
    }

    private void updateCoinDisplay() {
        tvCoins.setText(String.format("%,d", CurrentUser.instance.coins));
    }

    private void markOwned(Button btn) {
        btn.setText("Owned");
        btn.setEnabled(false);
        btn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.textMuted));
    }

    private void renderItems(String filter) {
        itemsContainer.removeAllViews();

        List<ShopItem> filtered = new ArrayList<>();
        for (ShopItem item : allItems) {
            if (filter.equals("All") || item.category.equals(filter)) {
                filtered.add(item);
            }
        }

        currentSectionLabel.setText(filter.equals("All") ? "All Items" : "Exclusive " + filter);

        int cols = 2;
        for (int i = 0; i < filtered.size(); i += cols) {
            LinearLayout row = makeRow();
            row.addView(makeCard(filtered.get(i)));
            if (i + 1 < filtered.size()) {
                row.addView(makeCard(filtered.get(i + 1)));
            } else {
                Space space = new Space(this);
                LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                sp.setMargins(dp(8), dp(8), dp(8), dp(8));
                space.setLayoutParams(sp);
                row.addView(space);
            }
            itemsContainer.addView(row);
        }
    }

    private LinearLayout makeRow() {
        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);
        return row;
    }

    private LinearLayout makeCard(ShopItem item) {
        boolean owned = ownedItemIds.contains(item.itemId);

        LinearLayout card = new LinearLayout(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        cardParams.setMargins(dp(8), dp(8), dp(8), dp(8));
        card.setLayoutParams(cardParams);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setBackgroundResource(R.drawable.bg_card_white);
        int themeColor = ThemeManager.getColorFromAttr(this, R.attr.cardSurface);
        card.setBackgroundTintList(ColorStateList.valueOf(themeColor));

        card.setPadding(dp(8), dp(12), dp(8), dp(12));

        //icon
        ImageView icon = new ImageView(this);
        icon.setLayoutParams(new LinearLayout.LayoutParams(dp(64), dp(64)));
        com.bumptech.glide.Glide.with(this)
                .load(item.iconResId)
                .into(icon);
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        card.addView(icon);

        //namme
        TextView name = new TextView(this);
        name.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        name.setText(item.name);
        name.setTextSize(13f);
        name.setTypeface(null, android.graphics.Typeface.BOLD);
        int textThemeColor = ThemeManager.getColorFromAttr(this, R.attr.textPrimary);
        name.setTextColor(textThemeColor);
        name.setPadding(0, dp(8), 0, 0);
        card.addView(name);

        //price row
        LinearLayout priceRow = new LinearLayout(this);
        priceRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        priceRow.setOrientation(LinearLayout.HORIZONTAL);
        priceRow.setGravity(Gravity.CENTER_VERTICAL);
        priceRow.setPadding(0, dp(4), 0, 0);

        ImageView coinIcon = new ImageView(this);
        coinIcon.setLayoutParams(new LinearLayout.LayoutParams(dp(16), dp(16)));
        coinIcon.setImageResource(R.drawable.ic_coin);
        priceRow.addView(coinIcon);

        TextView priceText = new TextView(this);
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        priceParams.setMarginStart(dp(4));
        priceText.setLayoutParams(priceParams);
        priceText.setText(String.valueOf(item.price));
        priceText.setTextSize(12f);
        priceText.setTypeface(null, android.graphics.Typeface.BOLD);
        int textPriceThemeColor = ThemeManager.getColorFromAttr(this, R.attr.textPrimary);
        priceText.setTextColor(textPriceThemeColor);
        priceRow.addView(priceText);
        card.addView(priceRow);

        //by button
        Button buyBtn = new Button(this);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(35));
        btnParams.topMargin = dp(8);
        buyBtn.setLayoutParams(btnParams);
        buyBtn.setTextSize(12f);
        buyBtn.setBackgroundResource(R.drawable.bg_card_white);
        buyBtn.setTextColor(ContextCompat.getColor(this, R.color.surfaceWhite));
        buyBtn.setPadding(0, 0, 0, 0);

        if (owned) {
            markOwned(buyBtn);
        } else {
            buyBtn.setText("Buy");
            buyBtn.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.successGreen));
            buyBtn.setOnClickListener(v -> handlePurchase(item, buyBtn));
        }

        card.addView(buyBtn);
        return card;
    }

    private int dp(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}