package ranks;

import com.example.triviaquiz.R;
import com.example.triviaquiz.RankBase;

import utility.StringValues;

public class Sage extends RankBase {
    public Sage(int points)
    {
        super(R.drawable.bg_rank_sage, R.color.strokeRankSage);
        maxPoints = 150;
        currentPoints = points;
        rankName = StringValues.RANK_SAGE;
    }
    @Override
    public RankBase rankUp(int excessPoints) {
        return new Master(excessPoints);
    }
    @Override
    public RankBase rankDown(int excessPoints) {
        Scholar scholar = new Scholar(excessPoints);
        scholar.currentPoints = scholar.maxPoints + excessPoints;
        return scholar;
    }
    @Override
    public void calculateBenefits() {

    }
}
