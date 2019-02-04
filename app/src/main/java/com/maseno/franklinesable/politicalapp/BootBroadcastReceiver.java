package com.maseno.franklinesable.politicalapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.maseno.franklinesable.politicalapp.services.ModuleFetchService;

/**
 * Created by Frankline Sable on 15/02/2017. from 01:20
 * at Maseno University
 * Project PoliticalAppEp
 */

public class BootBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Launch the specified service when this message is received
        Intent startServiceIntent = new Intent(context, ModuleFetchService.class);
        startWakefulService(context, startServiceIntent);
    }
}
