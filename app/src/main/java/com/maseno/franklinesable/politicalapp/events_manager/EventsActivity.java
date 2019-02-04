package com.maseno.franklinesable.politicalapp.events_manager;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.maseno.franklinesable.politicalapp.R;
import com.maseno.franklinesable.politicalapp.YearPickerFragmentEvents;
import com.maseno.franklinesable.politicalapp.sharedmethods.PreferencesHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private List<EventsContents> eventsList = new ArrayList<>();
    private ListView listView;
    private EventsAdapter eAdapter;
    private Events_Database dbHandler;
    public static final int ACTIVITY_CREATE = 69;
    public static final int ACTIVITY_EDIT = 32;
    private Calendar mCalendar = Calendar.getInstance();
    private String sortOrderMonthly;
    private int sortOrderYearly = 2017;
    private Spinner monthSpinner;
    private Button yearButton;
    private String[] monthArray;
    ProgressDialog progressDialog;
    private int standardColors[] = {Color.rgb(0, 164, 59), Color.rgb(225, 48, 0), Color.rgb(0, 101, 177)};
    private PreferencesHandler savePrefs, modulePrefs;
    private String sortOrder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        decorView.setSystemUiVisibility(uiOptions);

        dbHandler = new Events_Database(EventsActivity.this);
        savePrefs = new PreferencesHandler(this);
        modulePrefs = new PreferencesHandler(this);
        yearButton = (Button) findViewById(R.id.pickYear);
        monthArray = getResources().getStringArray(R.array.month_array);
        progressDialog = new ProgressDialog(this);

        handleIntent(getIntent());


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(EventsActivity.this, Add_Event.class), ACTIVITY_CREATE);
            }
        });
        listView = (ListView) findViewById(R.id.eventsListView);
        listView.setEmptyView(findViewById(R.id.emptyView));
        //TODO animate the listview in 3D
       // listView.animate().rotationY(15).rotationY(-15).setInterpolator(getResources().getInteger()).setDuration(3000).start();
        monthSpinner = (Spinner) findViewById(R.id.spinner3);
        monthSpinner.setSelection(savePrefs.getEventSpinner());
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                sortOrderMonthly = monthSpinner.getSelectedItem().toString();
                refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });

        eAdapter = new EventsAdapter(this, R.layout.cardview_events, eventsList);
        listView.setAdapter(eAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intentClick = new Intent(EventsActivity.this, Add_Event.class);
                intentClick.putExtra(Events_Database.tb_Struct.KEY_PID, id);
                startActivityForResult(intentClick, ACTIVITY_EDIT);
            }
        });
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                {
                    Toast.makeText(EventsActivity.this, "click", Toast.LENGTH_SHORT).show();
                    final int checkedCount = listView.getCheckedItemCount();
                    // Set the  CAB title according to total checked items
                    actionMode.setTitle(checkedCount + "  Selected");
                    // Calls  toggleSelection method from ListViewAdapter Class
                    eAdapter.toggleSelection(position);
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.contextual_actionbar_events, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                switch (itemId) {
                    case R.id.selectAll:
                        final int checkedCount = eventsList.size();
                        // If item  is already selected or checked then remove or
                        // unchecked  and again select all
                        eAdapter.removeSelection();
                        for (int i = 0; i < checkedCount; i++) {
                            listView.setItemChecked(i, true);
                        }
                        actionMode.setTitle(checkedCount + " Events Selected");
                        return true;
                    case R.id.delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(EventsActivity.this, R.style.MaterialEventDialog);
                        builder.setMessage("Do you  want to delete selected record(s)?");


                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SparseBooleanArray selected = eAdapter.getSelectedIds();
                                for (int i = (selected.size() - 1); i >= 0; i--) {
                                    if (selected.valueAt(i)) {
                                        EventsContents selecteditem = eAdapter.getItem(selected.keyAt(i));
                                        // Remove  selected items following the ids
                                         //Log.i("meh","view "+eAdapter.getPosition(selecteditem));
                                        eAdapter.remove(selecteditem);
                                        deleteColumn(selected.keyAt(i));

                                    }
                                }
                                actionMode.finish();
                                selected.clear();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.setIcon(R.drawable.ic_report_problem_black_24dp);// dialog  Icon
                        alert.setTitle("Confirmation"); // dialog  Title
                        alert.show();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
        refresh();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_events, menu);
    }

    public void refresh() {
        AsyncRefresh asyncRefresh = new AsyncRefresh();
        asyncRefresh.execute();
    }

    public class AsyncRefresh extends AsyncTask<Void, Cursor, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            dbHandler.open();
            Cursor cursor = dbHandler.readDb(Events_Database.tb_Struct.KEY_MONTH_REF + " =\"" + sortOrderMonthly + "\" AND " + Events_Database.tb_Struct.KEY_YEAR_REF + " = " + sortOrderYearly + " AND " + Events_Database.tb_Struct.KEY_DELETED + " = " + 0, null, sortOrder);
            cursor.moveToFirst();
            eventsList.clear();
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            int tit = cursor.getColumnIndexOrThrow(Events_Database.tb_Struct.KEY_TITLE);
            int bod = cursor.getColumnIndexOrThrow(Events_Database.tb_Struct.KEY_DESCRIPTION);
            int col = cursor.getColumnIndexOrThrow(Events_Database.tb_Struct.KEY_COLOR);
            int date = cursor.getColumnIndexOrThrow(Events_Database.tb_Struct.KEY_SCHEDULED_DATE);
            int time = cursor.getColumnIndexOrThrow(Events_Database.tb_Struct.KEY_SCHEDULED_TIME);
            int internet = cursor.getColumnIndexOrThrow(Events_Database.tb_Struct.KEY_SERVER_BASED);

            while (!cursor.isAfterLast()) {
                String title_Sp = cursor.getString(tit);
                String body_Sp = cursor.getString(bod);
                int color_Sp = cursor.getInt(col);
                String date_Sp = cursor.getString(date);
                String time_Sp = cursor.getString(time);
                int intern = cursor.getInt(internet);
                int bellColor;

                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);//2017-02-15
                Date date2 = null;
                try {
                    date2 = dateTimeFormat.parse(date_Sp);
                    mCalendar.setTime(date2);

                } catch (ParseException ignored) {
                }
                if (intern == 1) {
                    bellColor = standardColors[2];
                } else {
                    bellColor = standardColors[0];
                }
                if (System.currentTimeMillis() < mCalendar.getTimeInMillis()) {
                    bellColor = standardColors[1];
                }

                SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.US);//2017-02-15
                SimpleDateFormat dayFormatNumber = new SimpleDateFormat("dd", Locale.US);//2017-02-15

                EventsContents events = new EventsContents(dayFormatNumber.format(mCalendar.getTime()), dayFormat.format(mCalendar.getTime()), title_Sp, body_Sp, time_Sp, color_Sp, bellColor);
                eventsList.add(events);//cc fri
                cursor.moveToNext();
            }
            cursor.close();
            eAdapter.notifyDataSetChanged();
        }
    }

    public void launchYearPicker(View v) {
        DialogFragment newFragment = new YearPickerFragmentEvents();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar yearCalendar = Calendar.getInstance();
        yearCalendar.set(Calendar.YEAR, year);
        yearCalendar.set(Calendar.MONTH, month);
        yearCalendar.set(Calendar.DAY_OF_MONTH, day);

        sortOrderMonthly = Add_Event.monthOnly.format(yearCalendar.getTime());
        String yearShow = Add_Event.yearOnly.format(yearCalendar.getTime());
        sortOrderYearly = Integer.parseInt(yearShow);
        yearButton.setText(yearShow);

        for (int i = 0; i < monthArray.length; i++) {
            if (sortOrderMonthly.equalsIgnoreCase(monthArray[i])) {
                monthSpinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_CREATE || requestCode == ACTIVITY_EDIT) {
            refresh();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.events_activity, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        // Configure the search info and add any event listeners
        //TODO COMPLETE THE OPTION MENU
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // TODO Do something when expanded
                return true;// Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // TODO Do something when collapsed
                return true;// Return true to expand action view
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        if (id == R.id.sortTitle) {
            sortOrder = Events_Database.tb_Struct.KEY_TITLE + " ASC";

        } else if (id == R.id.sortDate) {
            sortOrder = Events_Database.tb_Struct.KEY_SCHEDULED_DATE + " ASC";

        } else if (id == R.id.sortStatus) {
            sortOrder = Events_Database.tb_Struct.KEY_SERVER_BASED + " ASC";
        }
        refresh();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        dbHandler.close();
        super.onPause();
        savePrefs.saveEventSpinner(monthSpinner.getSelectedItemPosition());
    }

    public void deleteColumn(final long info) {
        //Log.i("meh","pid is "+info);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dbHandler.open();
                Log.i("meh", "here " + Events_Database.tb_Struct.KEY_PID + " = " + info);
                Boolean bol = dbHandler.updateDeleteDb(info, 1);
                Log.i("meh", "Deleted " + info + " , state " + bol);
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EventsActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                        // refresh();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onResume() {
        dbHandler.open();
        super.onResume();
        savePrefs.saveEvents(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
    private void handleIntent(Intent intent){
        // Get the intent, verify the action and get the query
        Log.i("meh","dao? ");
       // if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Log.i("meh","search");
            String query = intent.getStringExtra(SearchManager.QUERY);
            doEventSearch(query);

        //}
    }
    public List<EventsContents>  searchDictionaryWords(String searchWord){

        List<EventsContents> mItems=new ArrayList<>();
        String query= Events_Database.tb_Struct.KEY_TITLE+" like "+"'%"+searchWord+"%'";

        dbHandler.open();
        Cursor cursor = dbHandler.searchDb(query,null,null);
        //startManagingCursor(cursor);
        ArrayList<String> wordTerms=new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                int id=cursor.getInt(0);
                String word = cursor.getString(cursor.getColumnIndexOrThrow("events_title"));
                mItems.add(new EventsContents(word));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return mItems;
    }
    private void doEventSearch(String searchWord){

        List<EventsContents> eventsObjects=searchDictionaryWords((searchWord));
    }
}
