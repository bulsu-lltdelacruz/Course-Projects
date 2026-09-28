package ranks;

import com.example.triviaquiz.R;
import com.example.triviaquiz.RankBase;

import utility.StringValues;

public class Master extends RankBase {
    public Master(int points)
    {
        super(R.drawable.bg_rank_master, R.color.strokeRankMaster);
        maxPoints = 180;
        currentPoints = points;
        rankName = StringValues.RANK_MASTER;
    }
    @Override
    public RankBase rankUp(int excessPoints) {
        return new Genius(excessPoints);
    }
    @Override
    public RankBase rankDown(int excessPoints) {
        Sage sage = new Sage(excessPoints);
        sage.currentPoints = sage.maxPoints + excessPoints;
        return sage;
    }
    @Override
    public void calculateBenefits() {

    }
}
