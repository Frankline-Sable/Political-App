package com.maseno.franklinesable.politicalapp.events_manager;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.maseno.franklinesable.politicalapp.DatePickerFragmentEvents;
import com.maseno.franklinesable.politicalapp.R;
import com.maseno.franklinesable.politicalapp.TimePickerFragmentEvents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.BindView;

public class Add_Event extends AppCompatActivity {

    private final int colorButtonsIds[] = {R.id.purple_button, R.id.green_button, R.id.blue_button, R.id.yellow_button, R.id.brown_button, R.id.red_button, R.id.orange_button};
    private final int buttonColors[] = {R.color.purple_button, R.color.green_button, R.color.blue_button, R.color.yellow_button, R.color.brown_button, R.color.red_button, R.color.orange_button};
    private ImageView color_Buttons[] = new ImageView[buttonColors.length];
    private int eventColor;
    @BindView(R.id.titleField)
    EditText eventTitle;
    @BindView(R.id.bodyField)
    AutoCompleteTextView eventDetails;
    @BindView(R.id.dateCheckbox)
    CheckBox configDate;
    @BindView(R.id.timeCheckbox)
    CheckBox configTime;
    @BindView(R.id.descCheckbox)
    CheckBox viewDescription;
    private int ps_EventColor;
    private String ps_EventTitle = null;
    private String ps_DateCreated = null;
    private String ps_MonthReferee = null;
    private String ps_YearReferee = null;
    private String ps_TimeCreated = null;
    private Calendar mCalendar;
    public static String DATE_FORMAT_DB = "yyyy-MM-dd";
    public static String DATE_FORMAT_UI = "dd-LLLL-yy";
    public static String TIME_FORMAT_UI_DB = "hh:mm a";
    private Events_Database dbHandler;
    private Long mRowId = null;
    private final String nothing = "NIL";
    private Toolbar toolbar;
    private Boolean cancelOperation = false;
    private String ps_EventTime = null;
    private String ps_EventDate = null;
    private String ps_EventBody = nothing;
    public static final SimpleDateFormat monthOnly = new SimpleDateFormat("MMMM", Locale.US);
    public static final SimpleDateFormat yearOnly = new SimpleDateFormat("yyyy", Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event);
        ButterKnife.bind(this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        dbHandler = new Events_Database(Add_Event.this);
        mCalendar = Calendar.getInstance();
        applyDefaultColor();
    }

    public void applyDefaultColor() {

        eventColor = new Random().nextInt(colorButtonsIds.length);
        ps_EventColor = buttonColors[eventColor];
        ImageView imgColor = (ImageView) findViewById(colorButtonsIds[eventColor]);
        imgColor.setImageResource(R.drawable.ic_radio_button_checked);
        eventTitle.setTextColor(getResources().getColor(ps_EventColor));
        eventDetails.setTextColor(eventTitle.getTextColors());
    }

    public void eventColor(View v) {
        ImageView img = (ImageView) v;

        for (int i = 0; i < colorButtonsIds.length; i++) {

            color_Buttons[i] = (ImageView) findViewById(colorButtonsIds[i]);
            color_Buttons[i].setImageResource(R.drawable.ic_color_button_on);
            if (v.getId() == colorButtonsIds[i]) {
                ps_EventColor = buttonColors[i];
                eventTitle.setTextColor(getResources().getColor(buttonColors[i]));
                eventDetails.setTextColor(eventTitle.getTextColors());
            }
        }
        img.setImageResource(R.drawable.ic_radio_button_checked);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            prepareDataForUpload();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onDescCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        if (checked)
            eventDetails.setVisibility(View.VISIBLE);
        else
            eventDetails.setVisibility(View.GONE);
    }

    public void onDateCheckboxClicked(View view) {
        CheckBox checkBox = (CheckBox) view;
        boolean checked = (checkBox).isChecked();

        if (checked) {
            DialogFragment newFragment = new DatePickerFragmentEvents();
            newFragment.show(getFragmentManager(), "datePicker");
            ps_EventDate = nothing;
        } else {
            checkBox.setText("Set Date");
            ps_EventDate = null;
        }

    }

    public void onTimeCheckboxClicked(View view) {
        CheckBox checkBox = (CheckBox) view;
        boolean checked = (checkBox).isChecked();
        if (checked) {
            DialogFragment newFragment = new TimePickerFragmentEvents();
            newFragment.show(getFragmentManager(), "timePicker");
            ps_EventTime = nothing;
        } else {
            checkBox.setText("Set Time");
            ps_EventTime = null;
        }
    }

    private void prepareDataForUpload() {

        ps_EventTitle = eventTitle.getText().toString();
        ps_EventBody = eventDetails.getText().toString();

        eventTitle.setError(null);
        View focusView = null;

        if (TextUtils.isEmpty(ps_EventTitle)) {
            eventTitle.setError("Please enter at least a description!");
            focusView = eventTitle;
            cancelOperation = true;
        } else
            cancelOperation = false;

        if (!viewDescription.isChecked()) {
            ps_EventBody = nothing;
        }
        if (ps_EventDate != null && !ps_EventDate.equalsIgnoreCase("Set Date")) {
            Log.i("meh", "Initial date " + ps_EventDate);
            ps_EventDate = configDate.getText().toString();
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_FORMAT_DB, Locale.US);
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_UI, Locale.US);

            Date date = null;
            try {
                date = dateFormat.parse(ps_EventDate);
                mCalendar.setTime(date);
                ps_EventDate = dateTimeFormat.format(mCalendar.getTime());
            } catch (ParseException e) {
                Snackbar.make(configDate, "There is a problem setting the date" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                cancelOperation = true;
            }
        } else {
            ps_EventDate = getCurrentDate();
        }
        if (ps_EventTime != null && !ps_EventTime.equalsIgnoreCase("Set Time")) {
            ps_EventTime = configTime.getText().toString();
        } else {
            ps_EventTime = getCurrentTime();
        }
        ps_MonthReferee = monthOnly.format(mCalendar.getTime());
        ps_YearReferee = yearOnly.format(mCalendar.getTime());

        if (cancelOperation) {
            assert focusView != null;
            focusView.requestFocus();
        } else {
            AsyncInsertDb async = new AsyncInsertDb();
            async.execute();
        }
        ps_DateCreated = getCurrentDate();
        ps_TimeCreated = getCurrentTime();

    }

    private String getCurrentDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DB, Locale.US);
        return dateFormat.format(new Date());
    }

    private String getCurrentTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT_UI_DB, Locale.US);
        return dateFormat.format(new Date());
    }

    private class AsyncInsertDb extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        private Boolean updateSuccess;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(Add_Event.this,null,"Saving data",true,true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dbHandler.open();
            recordOnCalendar(ps_EventTitle, ps_EventBody, mCalendar);
            if (mRowId == null) {
                Log.i("meh", "Check " + ps_EventTitle + ", " + ps_EventBody + ", " + ps_DateCreated + ", " + ps_TimeCreated + ", " + ps_EventColor + "+, " + ps_EventDate + ", " + ps_EventTime + ", " + ps_MonthReferee);
                long id = dbHandler.insertDb(ps_EventTitle, ps_EventBody, ps_DateCreated, ps_TimeCreated, ps_EventColor, ps_EventDate, ps_EventTime, ps_MonthReferee, ps_YearReferee,System.currentTimeMillis(),false);
                if (id > 0) {
                    mRowId = id;
                }
            } else {
                updateSuccess = dbHandler.updateDb(mRowId, ps_EventTitle, ps_EventBody, ps_DateCreated, ps_TimeCreated, ps_EventColor, ps_EventDate, ps_EventTime, ps_MonthReferee, ps_YearReferee);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.cancel();
            dbHandler.close();
            setResult(RESULT_OK);
            Toast.makeText(Add_Event.this, "saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void recordOnCalendar(String title, String body, final Calendar time) {

        Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
        int intermediate = time.get(Calendar.HOUR_OF_DAY);
        intermediate += 3;
        time.set(Calendar.HOUR_OF_DAY, intermediate);

        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.Events.TITLE, title + ", : " + body);
    }

    @Override
    protected void onResume() {
        dbHandler.open();
        super.onResume();
        toolbar.setTitle("Add a New Event");
        applyDefaultColor();
        setRowIdFromIntent();
        populateFields();
    }

    private void setRowIdFromIntent() {
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(Events_Database.tb_Struct.KEY_PID) : null;
        }
    }

    private void populateFields() {

        if (mRowId != null) {
            toolbar.setTitle("Modify Event");
            populateFieldsAsync async = new populateFieldsAsync();
            async.execute();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putLong(Events_Database.tb_Struct.KEY_PID, mRowId);
        } catch (Exception ignored) {
        }
    }

    private class populateFieldsAsync extends AsyncTask<Void, Cursor, Cursor> {
        @Override
        protected Cursor doInBackground(Void... params) {
            dbHandler.open();
            Cursor rem_Cursor = dbHandler.readDb(Events_Database.tb_Struct.KEY_PID + "=" + mRowId, null, null);
            rem_Cursor.moveToFirst();
            return rem_Cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if(cursor!=null){
                eventTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(Events_Database.tb_Struct.KEY_TITLE)));
                eventDetails.setText(cursor.getString(cursor.getColumnIndexOrThrow(Events_Database.tb_Struct.KEY_DESCRIPTION)));
                eventColor = cursor.getInt(cursor.getColumnIndexOrThrow(Events_Database.tb_Struct.KEY_COLOR));
                dbHandler.close();
                cursor.close();
            }
        }
    }

    @Override
    protected void onPause() {
        dbHandler.close();
        super.onPause();
    }
}