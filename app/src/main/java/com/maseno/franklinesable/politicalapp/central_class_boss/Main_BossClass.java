package com.maseno.franklinesable.politicalapp.central_class_boss;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.maseno.franklinesable.politicalapp.Account_Settings;
import com.maseno.franklinesable.politicalapp.AppDatabase;
import com.maseno.franklinesable.politicalapp.R;
import com.maseno.franklinesable.politicalapp.events_manager.EventsActivity;
import com.maseno.franklinesable.politicalapp.feeds_manager.Feeds_Activity;
import com.maseno.franklinesable.politicalapp.log_in_package.Front_EndActivity;
import com.maseno.franklinesable.politicalapp.log_in_package.LogInFragment;
import com.maseno.franklinesable.politicalapp.notifications_package.NotificationsActivity;
import com.maseno.franklinesable.politicalapp.services.EventsAlarmReceiver;
import com.maseno.franklinesable.politicalapp.services.ModuleFetchService;
import com.maseno.franklinesable.politicalapp.sharedmethods.PreferencesHandler;
import com.maseno.franklinesable.politicalapp.sharedmethods.TypefaceHandler;

import java.util.ArrayList;
import java.util.List;

public class Main_BossClass extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Boolean refresh = true;
    TypefaceHandler tp;
    RecyclerView.LayoutManager mLayoutManagerLinear, mLayoutManagerGrid, mLayoutManagerStaggered;
    Boss_Components boss_components;
    private RecyclerView recyclerView;
    private Boss_RecycleView bossAdapter;
    private List<Boss_Components> bossList = new ArrayList<>();
    private String userCredentials[] = null;
    private TextView nameIntro;
    private PreferencesHandler savePref, modulePrefs;
    private AppDatabase dbHandler;
    private Resources res;
    private int RESULT_ACCOUNT_SETTINGS=32;
    private int LAYOUT_SET;
    // Setup the callback for when data is received from the service
    // Define the callback for what to do when data is received
    private BroadcastReceiver eventsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (resultCode == RESULT_OK) {
                int resultValueEvents = intent.getIntExtra("resultValueEvents", 0);
                int resultValueFeeds = intent.getIntExtra("resultValueFeeds", 0);
                int resultValueNotifications=intent.getIntExtra("resultValueNotifications",0);

                setPostsNumbers();
            }
        }
    };

    SharedPreferences modulePreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__boss_class);
        res = getResources();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dbHandler = new AppDatabase(Main_BossClass.this);
        savePref = new PreferencesHandler(Main_BossClass.this);
        modulePrefs= new PreferencesHandler(Main_BossClass.this);
       scheduleAlarm();

        mLayoutManagerLinear = new LinearLayoutManager(getApplicationContext());
        mLayoutManagerGrid = new GridLayoutManager(this, 2);
        mLayoutManagerStaggered=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);

        LAYOUT_SET=savePref.getView();
       savePref.SaveAccountState(true);

        tp = new TypefaceHandler(this);
        nameIntro = (TextView) findViewById(R.id.nameIntro);
        TextView titV = (TextView) findViewById(R.id.bossTitleText);

        userCredentials = savedInstanceState != null ? savedInstanceState.getStringArray(LogInFragment.EXTRA_MESSAGE) : null;

        nameIntro.setTypeface(tp.setTp("pmn_caecilia.ttf"));
        titV.setTypeface(tp.setTp("aftershock_debris.ttf"));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setNavigationIcon(R.drawable.ic_nav);

        recyclerView = (RecyclerView) findViewById(R.id.bossList);
        bossAdapter = new Boss_RecycleView(bossList, Main_BossClass.this);

        //TODO: check

        recyclerView.setItemAnimator(new DefaultItemAnimator());
      //7  recyclerView.addItemDecoration(new DividerItemDecoration(this, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(bossAdapter);

        prepareBossData();

        recyclerView.addOnItemTouchListener(new Boss_RecycleView.RecyclerTouchListener(getApplicationContext(), recyclerView, new Boss_RecycleView.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                boss_components = bossList.get(position);
                boss_components.getBoss_Title();
                if(userCredentials!=null)
                    savePref.setFullName(userCredentials[1]+" "+userCredentials[2]);

                switch (position) {
                    case 0:
                        Intent viewFeeds = new Intent(Main_BossClass.this, Feeds_Activity.class);
                        //accountSettings.putExtra(LogInFragment.EXTRA_MESSAGE, userCredentials);
                        startActivity(viewFeeds);
                        break;
                    case 1:

                        Intent viewEvents = new Intent(Main_BossClass.this, EventsActivity.class);
                        //accountSettings.putExtra(LogInFragment.EXTRA_MESSAGE, userCredentials);
                        startActivity(viewEvents);
                        break;
                    case 3:
                        Intent viewNotifications=new Intent(Main_BossClass.this, NotificationsActivity.class);
                        startActivity(viewNotifications);

                        break;

                    case 4:
                        Intent accountSettings = new Intent(Main_BossClass.this, Account_Settings.class);
                        accountSettings.putExtra(LogInFragment.EXTRA_MESSAGE, userCredentials);
                        startActivityForResult(accountSettings,RESULT_ACCOUNT_SETTINGS);
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                //view.setAnimation(getAn);
            }
        }));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main__boss_class, menu);
        int mLayout=loadDefaultLayoutManager();
        if(mLayout==1)
            menu.findItem(R.id.linearLayout).setChecked(true);
            else if(mLayout==2)
            menu.findItem(R.id.gridLayout).setChecked(true);
            else
            menu.findItem(R.id.staggeredLayout).setChecked(true);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Main_BossClass.this, R.style.MaterialDialogStyle);
            LayoutInflater inflater = getLayoutInflater();
            final View layout = inflater.inflate(R.layout.logout_dialog, null);
            final CheckBox checkBox = (CheckBox) layout.findViewById(R.id.checkBoxLogout);

            if (savePref.getRemState()) {
                builder.setCancelable(false).setView(layout).setIcon(R.drawable.ic_report_black_24dp)
                        .setPositiveButton("SURE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                executeSignOut(!checkBox.isChecked());
                            }
                        }).setNegativeButton("NO WAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setTitle("Do you want to logout?");
                builder.create().show();
            } else {
                executeSignOut(false);
            }


            return true;
        } else {
            item.setChecked(true);
            changeLayoutManager(id);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       /* if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void prepareBossData() {
        Boss_Components boss = new Boss_Components("Feeds", "View the latest ground breaking headlines", 0, R.drawable.ic_feed);
        bossList.add(boss);

        boss = new Boss_Components("Events", "About events and their venues",0, R.drawable.ic_today_black);
        bossList.add(boss);

        boss = new Boss_Components("Post", "Share your thoughts with the rest of the world", 0, R.drawable.ic_library_add_black);
        bossList.add(boss);

        boss = new Boss_Components("Notifications", "View your notifications here", 0, R.drawable.ic_announcement_black);
        bossList.add(boss);

        boss = new Boss_Components("Account", "Manage your account", 0, R.drawable.ic_account_circle_black);
        bossList.add(boss);

        boss = new Boss_Components("Quit", "Close and exit app", 0, R.drawable.ic_settings_power_black);
        bossList.add(boss);

        bossAdapter.notifyDataSetChanged();
    }

    private void executeSignOut(final Boolean rem) {

        ProgressDialog.show(this,"Please wait","Logging out...", true,false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dbHandler.open();
                final Boolean done=dbHandler.deleteDb();
                savePref.SaveAccountState(rem, false);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if(done){
                            startActivity(new Intent(Main_BossClass.this, Front_EndActivity.class));
                            finish();
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putStringArray(LogInFragment.EXTRA_MESSAGE, userCredentials);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onResume() {
        dbHandler.open();
        super.onResume();
        if (refresh) {
            setUserArrayFromIntent();
            populateFieldsFromDatabase();
        }
        refresh = false;
        setPostsNumbers();

        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(ModuleFetchService.ACTION_REFRESH_MODULES);
        LocalBroadcastManager.getInstance(this).registerReceiver(eventsReceiver, filter);



    }

    public void setUserArrayFromIntent() {
        if (userCredentials == null) {
            Bundle extras = getIntent().getExtras();
            userCredentials = extras != null ? extras.getStringArray(LogInFragment.EXTRA_MESSAGE) : null;
        }
    }

    private void populateFieldsFromDatabase() {
        if (userCredentials == null) {
            new populateFieldsAsync().execute();
        } else {
            new saveCredentialsOfflineAsync().execute();
        }
    }

    private Boolean changeLayoutManager(int id) {

        switch (id){
            case R.id.linearLayout:
                LAYOUT_SET=1;
                recyclerView.setLayoutManager(mLayoutManagerLinear);
                break;
            case R.id.gridLayout:
                LAYOUT_SET=2;
                recyclerView.setLayoutManager(mLayoutManagerGrid);
                break;
            case R.id.staggeredLayout:
                LAYOUT_SET=3;
                recyclerView.setLayoutManager(mLayoutManagerStaggered);
                break;
        }
        bossAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    protected void onPause() {
        dbHandler.close();
        super.onPause();
        savePref.setView(LAYOUT_SET);
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(this).unregisterReceiver(eventsReceiver);
        // or `unregisterReceiver(eventsReceiver)` for a normal broadcast
    }

    // Setup a recurring alarm every half hour
    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), EventsAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, EventsAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, 300000, pIntent);
    }

    private class populateFieldsAsync extends AsyncTask<Void, Cursor, Cursor> {
        @Override
        protected Cursor doInBackground(Void... voids) {
            dbHandler.open();
            Cursor cursor = dbHandler.readDb();
            cursor.moveToFirst();
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            userCredentials = new String[]{AppDatabase.table_Structure.KEY_ROW_ID, AppDatabase.table_Structure.KEY_F_NAME, AppDatabase.table_Structure.KEY_L_NAME, AppDatabase.table_Structure.KEY_USERNAME,
                    AppDatabase.table_Structure.KEY_HOME, AppDatabase.table_Structure.KEY_CONSTITUENCY, AppDatabase.table_Structure.KEY_GENDER, AppDatabase.table_Structure.KEY_BIRTHDATE, AppDatabase.table_Structure.KEY_IMAGEPATH};

            for (int i = 0; i < userCredentials.length; i++) {
//                userCredentials[i] = cursor.getString(cursor.getColumnIndex(userCredentials[i]));
            }
            cursor.close();

            //nameIntro.setText(res.getString(R.string.wel,userCredentials[1]));
        }
    }

    private class saveCredentialsOfflineAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            nameIntro.setText(res.getString(R.string.wel,userCredentials[1]));
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (userCredentials != null) {
                dbHandler.open();
                int rowOnProgress = 1;
                Boolean result = dbHandler.updateDb(rowOnProgress, userCredentials[1], userCredentials[2], userCredentials[3], userCredentials[4], userCredentials[5], userCredentials[6], userCredentials[7], userCredentials[8]);
                if (!result) {
                    dbHandler.insertDb(userCredentials[1], userCredentials[2], userCredentials[3], userCredentials[4], userCredentials[5], userCredentials[6], userCredentials[7], userCredentials[8]);
                }
            }
            return null;
        }
    }
    private int loadDefaultLayoutManager(){
        if(LAYOUT_SET==1)
            recyclerView.setLayoutManager(mLayoutManagerLinear);
            else if(LAYOUT_SET==2)
            recyclerView.setLayoutManager(mLayoutManagerGrid);
            else
            recyclerView.setLayoutManager(mLayoutManagerStaggered);
        return LAYOUT_SET;
    }
    private void setPostsNumbers(){
        int newEvents = modulePrefs.getEvents();
        int newFeeds=modulePrefs.getFeeds();
        int newNotes=modulePrefs.getNotifications();

        boss_components = bossList.get(0);
        boss_components.setPost_Count(newFeeds);

        boss_components = bossList.get(1);
        boss_components.setPost_Count(newEvents);

        boss_components = bossList.get(3);
        boss_components.setPost_Count(newNotes);

        bossAdapter.notifyDataSetChanged();
    }

}


