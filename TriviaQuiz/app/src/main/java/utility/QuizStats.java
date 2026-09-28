package utility;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.example.triviaquiz.RankBase;

import ranks.Apprentice;
import ranks.Genius;
import ranks.Master;
import ranks.Novice;
import ranks.Oracle;
import ranks.Sage;
import ranks.Scholar;

public class QuizStats implements Parcelable {
    public long[] timeTaken;
    public int numberOfCorrectItems;
    public int rankPointsGained;
    public int coinsGained;
    public String difficulty;
    public QuizStats(int numberOfCorrectItems, long[] timeTaken, String difficulty) {
        this.timeTaken = timeTaken;
        this.numberOfCorrectItems = numberOfCorrectItems;
        this.difficulty = difficulty;
        //temporary lang
        rankPointsGained = calculateRankPointsGained();
        coinsGained = calculateCoinsGained();
    }
    public int calculateRankPointsGained()
    {
        long averageTimeTaken = 0;//seconds
        for (long time:timeTaken) {
            averageTimeTaken+=time;
        }
        averageTimeTaken/=timeTaken.length;
        averageTimeTaken/=1000;
        //easy, max is at 15, medium 20, hard 25
        //time is 20, 15, 10

        float timeMultiplier = 0;
        int quarterTime = 0;
        float difficultyMultiplier = 1;
        int correctItemsMultiplier= 0;
        int rankBonus = 0;// the higher the less bonuc points

        switch (difficulty)
        {
            case StringValues.DIFFICULTY_EASY:
                quarterTime = 20/4;
                difficultyMultiplier = 1f;
            break;
            case StringValues.DIFFICULTY_MEDIUM:
                quarterTime = 15/4;
                difficultyMultiplier = 1.5f;
                break;
            case StringValues.DIFFICULTY_HARD:
                quarterTime = 10/4;
                difficultyMultiplier = 2f;
                break;
        }
        if(averageTimeTaken < quarterTime)
        {
            timeMultiplier = 3f;
        }else if(averageTimeTaken < quarterTime*2)
        {
            timeMultiplier = 2f;
        }else if(averageTimeTaken < quarterTime*3)
        {
            timeMultiplier = 1f;
        }else if(averageTimeTaken <= quarterTime*4)
        {
            timeMultiplier = 0.5f;
        }
        RankBase currentRank = CurrentUser.instance.currentRank;
        if(currentRank instanceof Novice)
            rankBonus = 20;
        else if(currentRank instanceof Apprentice)
            rankBonus = 12;
        else if(currentRank instanceof Scholar)
            rankBonus = 8;
        else if(currentRank instanceof Sage)
            rankBonus = 6;
        else if(currentRank instanceof Master)
            rankBonus = 4;
        else if(currentRank instanceof Genius ||currentRank instanceof Oracle)
            rankBonus = 0;

        correctItemsMultiplier = numberOfCorrectItems-2;
        return (int)((correctItemsMultiplier*timeMultiplier)*difficultyMultiplier) + rankBonus;
    }
    public int calculateCoinsGained()
    {
        int coins = 0;
        Log.d("difficulty", difficulty);
        switch (difficulty)
        {
            case StringValues.DIFFICULTY_EASY:
                coins = 10;
                break;
            case StringValues.DIFFICULTY_MEDIUM:
                coins = 15;
                break;
            case StringValues.DIFFICULTY_HARD:
                coins = 20;
                break;
        }

        return coins*numberOfCorrectItems;
    }
    protected QuizStats(Parcel in) {
        numberOfCorrectItems = in.readInt();
        rankPointsGained = in.readInt();
        coinsGained = in.readInt();
        difficulty = in.readString();
        timeTaken = in.createLongArray();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numberOfCorrectItems);
        dest.writeInt(rankPointsGained);
        dest.writeInt(coinsGained);
        dest.writeString(difficulty);
        dest.writeLongArray(timeTaken);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuizStats> CREATOR = new Creator<QuizStats>() {
        @Override
        public QuizStats createFromParcel(Parcel in) {
            return new QuizStats(in);
        }
        @Override
        public QuizStats[] newArray(int size) {
            return new QuizStats[size];
        }
    };
}
