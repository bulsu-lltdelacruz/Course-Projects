package com.example.triviaquiz;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.DrawableRes;

public abstract class RankBase {
    public int currentPoints;
    public int maxPoints;
    protected String rankName;
    protected int background;
    protected int strokeColor;

    public RankBase(int background, int strokeColor) {
        this.background = background;
        this.strokeColor = strokeColor;
    }
    public RankBase increasePoints(int points){
        currentPoints+=points;
        Log.d("Points Added", "increasePoints: "+points);
        int excessPoints;
        if(currentPoints >= maxPoints)
        {
            //rank up
            excessPoints = currentPoints-maxPoints;
            return rankUp(excessPoints);
        }
        else if(currentPoints < 0)
        {
            //rank down
            excessPoints = maxPoints+currentPoints;
            return rankDown(excessPoints);
        }
        return null;//if null just same rank
    }
    public abstract RankBase rankUp(int excessPoints);
    public abstract RankBase rankDown(int excessPoints);

    public abstract void calculateBenefits();
}
