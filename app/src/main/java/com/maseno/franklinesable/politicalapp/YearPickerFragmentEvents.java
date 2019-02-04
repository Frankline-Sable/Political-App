package com.maseno.franklinesable.politicalapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import com.maseno.franklinesable.politicalapp.events_manager.EventsActivity;

import java.util.Calendar;

/**
 * Created by Frankline Sable on 08/02/2017. from 14:37 at Maseno University
 * Project PoliticalAppEp
 */

public class YearPickerFragmentEvents extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    final Calendar mCalendar = Calendar.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (EventsActivity) getActivity(), year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        //This method is ignored, callbacks in the main activity
    }
}
