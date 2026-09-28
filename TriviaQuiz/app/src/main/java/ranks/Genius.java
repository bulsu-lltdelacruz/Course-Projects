package ranks;

import com.example.triviaquiz.R;
import com.example.triviaquiz.RankBase;

import utility.StringValues;

public class Genius extends RankBase {
    public Genius(int points)
    {
        super(R.drawable.bg_rank_genius, R.color.strokeRankGenius);
        maxPoints = 200;
        currentPoints = points;
        rankName = StringValues.RANK_GENIUS;
    }
    @Override
    public RankBase rankUp(int excessPoints) {
        return new Oracle(excessPoints);
    }
    @Override
    public RankBase rankDown(int excessPoints) {
        Master master = new Master(excessPoints);
        master.currentPoints = master.maxPoints + excessPoints;
        return master;
    }
    @Override
    public void calculateBenefits() {

    }
}
