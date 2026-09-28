package utility;

import com.example.triviaquiz.RankBase;

import java.util.ArrayList;
import java.util.List;

public class CurrentUser {
    public static CurrentUser instance;
    public RankBase currentRank;
    public int coins;
    public int totalGames;
    public int totalCorrectItems;
    public List<Integer> ownedItemIds = new ArrayList<>();

    //defaults
    public int selectedIconId   = 1;
    public int selectedBorderId = -1;
    public int selectedThemeId  = -1;
    public boolean isAdmin = false;

    public CurrentUser() {
        if (instance == null)
            instance = this;
    }
}