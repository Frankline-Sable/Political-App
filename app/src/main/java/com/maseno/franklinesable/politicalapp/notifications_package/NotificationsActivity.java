package com.maseno.franklinesable.politicalapp.notifications_package;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.maseno.franklinesable.politicalapp.DividerItemDecoration;
import com.maseno.franklinesable.politicalapp.R;
import com.maseno.franklinesable.politicalapp.sharedmethods.PreferencesHandler;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;
    private Notifications_Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Notifications> notesList=new ArrayList<>();
    PreferencesHandler savePrefs,modulePrefs;
    private fetchNotificationData asyncFetch=null;
    private View mProgressView;
    public static final int REQUEST_VIEW_NOTIFICATION=3243;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        modulePrefs=new PreferencesHandler(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mProgressView = findViewById(R.id.notification_progress);

        mAdapter = new Notifications_Adapter(notesList, this);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.addOnItemTouchListener(new Notifications_Adapter.RecyclerTouchListener(this, mRecyclerView, new Notifications_Adapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Notifications notes=notesList.get(position);
                //notes.getTitle
            }

            @Override
            public void onLongClick(View view, int position) {

            }
            @Override
            public void onDoubleTap(View view, int position) {
                Notifications notes=notesList.get(position);
                Intent intent=new Intent(NotificationsActivity.this,_ViewNotifications.class);
                intent.putExtra(Notification_Database.tb_Struct.KEY_NOTIFICATION_ID,notes.getNotes_Id());
                startActivityForResult(intent,REQUEST_VIEW_NOTIFICATION);
            }

        }));
        prepareNotificationData();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareNotificationData(){

        asyncFetch = new fetchNotificationData();
        asyncFetch.execute();
    }
    public class fetchNotificationData extends AsyncTask<Void, Cursor, Cursor> {
        private String notificationsFetch[];
        private Notification_Database  dbHandler;
        @Override
        protected void onPreExecute() {
            showProgress(true);
            dbHandler=new  Notification_Database(NotificationsActivity.this);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            dbHandler.open();
            Cursor cursor = dbHandler.readDb(null,null,Notification_Database.tb_Struct.KEY_CREATED_DATE_TIME+" ASC");
            startManagingCursor(cursor);
            notesList.clear();
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            cursor.moveToFirst();

            int pid = cursor.getColumnIndexOrThrow(Notification_Database.tb_Struct.KEY_PID);
            int title= cursor.getColumnIndexOrThrow(Notification_Database.tb_Struct.KEY_TITLE);
            int summary = cursor.getColumnIndexOrThrow(Notification_Database.tb_Struct.KEY_SUMMARY);
            int name = cursor.getColumnIndexOrThrow(Notification_Database.tb_Struct.KEY_USERNAME);
            int notification_id = cursor.getColumnIndexOrThrow(Notification_Database.tb_Struct.KEY_NOTIFICATION_ID);
            int time = cursor.getColumnIndexOrThrow(Notification_Database.tb_Struct.KEY_CREATED_DATE_TIME);
            int deleted = cursor.getColumnIndexOrThrow(Notification_Database.tb_Struct.KEY_DELETED);
            int flag = cursor.getColumnIndexOrThrow(Notification_Database.tb_Struct.KEY_FLAG_COLOR);

            while (!cursor.isAfterLast()) {
                Notifications notes =new Notifications(cursor.getString(name),cursor.getString(title), cursor.getString(summary),cursor.getString(time),cursor.getInt(flag), cursor.getLong(notification_id));
                notesList.add(notes);
                cursor.moveToNext();
            }
            mAdapter.notifyDataSetChanged();

            asyncFetch = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            asyncFetch = null;
            showProgress(false);
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        modulePrefs.saveNotifications(0);
    }
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
