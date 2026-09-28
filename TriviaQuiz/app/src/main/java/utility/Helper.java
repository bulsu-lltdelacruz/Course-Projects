package utility;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import ranks.Oracle;

public class Helper {
    public static LinearLayout.LayoutParams[] getRankBarParams()
    {
        float currentPoints = CurrentUser.instance.currentRank.currentPoints;
        float maxPoints = CurrentUser.instance.currentRank.maxPoints;

        if(CurrentUser.instance.currentRank instanceof Oracle)
            maxPoints = currentPoints;

        float progress = currentPoints / maxPoints;

        LinearLayout.LayoutParams weight = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, progress);
        LinearLayout.LayoutParams weightTotal = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f - progress);
        return  new LinearLayout.LayoutParams[]{weight, weightTotal};
    }
    public static String[] getRankPointsText()
    {
        String tvCurrentScore = "";
        String tvTotalScore = "";
        float currentPoints = CurrentUser.instance.currentRank.currentPoints;
        float maxPoints = CurrentUser.instance.currentRank.maxPoints;

        if(!(CurrentUser.instance.currentRank instanceof Oracle))
        {
            tvCurrentScore = String.format("%.0f Points", currentPoints);
            tvTotalScore = String.format("%.0f Points", maxPoints);
        }
        else
        {
            tvCurrentScore = String.format("%.0f Points", currentPoints);
            tvTotalScore = "";
        }

        return  new String[]{tvCurrentScore, tvTotalScore};
    }
}
