package ranks;

import android.util.Log;

import com.example.triviaquiz.R;
import com.example.triviaquiz.RankBase;

import utility.StringValues;

public class Oracle extends RankBase {
    public Oracle(int points)
    {
        super(R.drawable.bg_rank_oracle, R.color.strokeRankOracle);
        maxPoints = 999999;
        currentPoints = points;
        rankName = StringValues.RANK_ORACLE;
    }
    @Override
    public RankBase increasePoints(int points){
        currentPoints+=points;
        int excessPoints;
        if(currentPoints < 0)
        {
            //rank down
            return rankDown(currentPoints);
        }
        return null;//if null just same rank
    }
    @Override
    public RankBase rankUp(int excessPoints) {
        return null;
    }
    @Override
    public RankBase rankDown(int excessPoints) {
        Genius genius = new Genius(excessPoints);
        genius.currentPoints = genius.maxPoints + excessPoints;
        return genius;
    }
    @Override
    public void calculateBenefits() {

    }
}
