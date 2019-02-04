package com.maseno.franklinesable.politicalapp.sharedmethods;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Frankline Sable on 14/02/2017. from 12:27
 * at Maseno University
 * Project PoliticalAppEp
 */

public class TypefaceHandler {
    private Context mContext;

    public TypefaceHandler(Context mContext) {
        this.mContext = mContext;
    }

    public Typeface setTp(String mTypeface) {
        return Typeface.createFromAsset(mContext.getAssets(), mTypeface);
    }
}
