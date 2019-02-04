package com.maseno.franklinesable.politicalapp;

import android.view.animation.Interpolator;

/**
 * Created by Frankline Sable on 08/02/2017.
 */

public class BounceInterpolator implements Interpolator {
    double mAmpilitude=1;
    double mFrequency=10;

    public BounceInterpolator (double mAmpilitude, double mFrequency) {
        this.mAmpilitude = mAmpilitude;
        this.mFrequency = mFrequency;
    }

    @Override
    public float getInterpolation(float time) {
        return (float) (-1*Math.pow(Math.E, -time/mAmpilitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
