package com.maseno.franklinesable.politicalapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Frankline Sable on 15/02/2017. from 01:02
 * at Maseno University
 * Project PoliticalAppEp
 */

public class EventsAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.maseno.franklinesable.politicalapp.services.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ModuleFetchService.class);
        i.putExtra("foo", "bar");
        context.startService(i);
        Toast.makeText(context, "alarm", Toast.LENGTH_SHORT).show();
    }
}
