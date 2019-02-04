package com.maseno.franklinesable.politicalapp;

/**
 * Created by Frankline Sable on 15/02/2017. from 14:01
 * at Maseno University
 * Project PoliticalAppEp
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Frankline Sable on 08/02/2017.
 */

public class DatePickerSettings extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    // Use the current date as the default date in the picker
    final Calendar mCalendar = Calendar.getInstance();
    EditText dateButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog date = new DatePickerDialog(getActivity(), this, year, month, day);
        //date.setTitle("Pick a date");date.setIcon(R.drawable.ic_insert_invitation_black_24dp);
        return date;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // Do something with the date chosen by the user
        dateButton = (EditText) getActivity().findViewById(R.id.birthField);
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
//2017-02-15
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-LLLL-yy", Locale.US);
        dateButton.setText(dateFormat.format(mCalendar.getTime()));
    }
}
