package ranks;

import android.util.Log;
import android.widget.Toast;

import com.example.triviaquiz.R;
import com.example.triviaquiz.RankBase;

import utility.StringValues;

public class Apprentice extends RankBase {
    public Apprentice(int points)
    {
        super(R.drawable.bg_rank_apprentice, R.color.strokeRankApprentice);
        Log.d("Points", "Apprentice: "+points);
        maxPoints = 100;
        currentPoints = points;
        rankName = StringValues.RANK_APPRENTICE;
    }
    @Override
    public RankBase rankUp(int excessPoints) {
        return new Scholar(excessPoints);
    }

    @Override
    public RankBase rankDown(int excessPoints) {
        Novice novice = new Novice(excessPoints);
        novice.currentPoints = novice.maxPoints + excessPoints;
        return novice;
    }
    @Override
    public void calculateBenefits() {

    }
}
