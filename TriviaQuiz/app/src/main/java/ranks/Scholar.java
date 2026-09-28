package ranks;

import com.example.triviaquiz.R;
import com.example.triviaquiz.RankBase;

import utility.StringValues;

public class Scholar extends RankBase {
    public Scholar(int points)
    {
        super(R.drawable.bg_rank_scholar, R.color.strokeRankScholar);
        maxPoints = 120;
        currentPoints = points;
        rankName = StringValues.RANK_SCHOLAR;
    }
    @Override
    public RankBase rankUp(int excessPoints) {
        return new Sage(excessPoints);
    }
    @Override
    public RankBase rankDown(int excessPoints) {
        Apprentice apprentice = new Apprentice(excessPoints);
        apprentice.currentPoints = apprentice.maxPoints + excessPoints;
        return apprentice;
    }
    @Override
    public void calculateBenefits() {

    }
}
