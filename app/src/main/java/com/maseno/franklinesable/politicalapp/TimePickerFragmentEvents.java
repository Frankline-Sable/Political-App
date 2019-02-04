package com.maseno.franklinesable.politicalapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Frankline Sable on 08/02/2017.
 */

public class TimePickerFragmentEvents extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    CheckBox timeButton;
    // Use the current time as the default time in the picker
    final Calendar mCalendar = Calendar.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int hourOfDay=mCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes=mCalendar.get(Calendar.MINUTE);

        TimePickerDialog time = new TimePickerDialog(getActivity(), this, hourOfDay,minutes, DateFormat.is24HourFormat(getActivity()));
        time.setTitle("Pick a time");
        return time;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        timeButton = (CheckBox) getActivity().findViewById(R.id.timeCheckbox);
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND,0);

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        timeButton.setText(timeFormat.format(mCalendar.getTime()));
    }
}
