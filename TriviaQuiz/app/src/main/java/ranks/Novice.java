package ranks;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.triviaquiz.R;
import com.example.triviaquiz.RankBase;

import utility.StringValues;

public class Novice extends RankBase {

    public Novice(int points)
    {
        super(R.drawable.bg_rank_novice, R.color.strokeRankBronze);
        Log.d("Points", "Novice: "+points);
        maxPoints = 50;
        currentPoints = points;
        rankName = StringValues.RANK_NOVICE;
    }

    @Override
    public RankBase rankUp(int excessPoints) {
        return new Apprentice(excessPoints);
    }

    @Override
    public RankBase rankDown(int excessPoints) {
        return new Novice(0);
    }

    @Override
    public void calculateBenefits() {

    }
}
