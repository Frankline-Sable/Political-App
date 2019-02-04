package com.maseno.franklinesable.politicalapp.notifications_package;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.maseno.franklinesable.politicalapp.R;

import java.util.ArrayList;
import java.util.List;

public class _ViewNotifications extends AppCompatActivity {

    private RecyclerView mRecycleView;
    private _notification_adapters mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<_notification_contents> notesList = new ArrayList<>();
    private View mProgressView;
    private fetchUserNotification asyncFetch=null;
    private Long mUserUniqueId=null;
    _notification_db dbHandler;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notifications);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserUniqueId=savedInstanceState!=null?savedInstanceState.getLong(Notification_Database.tb_Struct.KEY_NOTIFICATION_ID):null;

        mRecycleView = (RecyclerView) findViewById(R.id.notes_recycler_view);
        mProgressView = findViewById(R.id.load_notesProgress);

        mAdapter = new _notification_adapters(notesList);

        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setAdapter(mAdapter);

        mRecycleView.addOnItemTouchListener(new _notification_adapters.RecyclerTouchListener(this, mRecycleView, new _notification_adapters.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //notes.getTitle
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try{
            outState.putLong(Notification_Database.tb_Struct.KEY_NOTIFICATION_ID, mUserUniqueId);
        }
        catch (Exception ignored){}
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("Fake ZTitle");
        setUniqueUserIdFromIntent();
        populateMessageFields();
    }
    private void setUniqueUserIdFromIntent(){
        if(mUserUniqueId==null){
            Bundle extras=getIntent().getExtras();
            mUserUniqueId=extras!=null?extras.getLong(Notification_Database.tb_Struct.KEY_NOTIFICATION_ID):null;
            dbHandler=new _notification_db(this,Long.toString(mUserUniqueId));
        }
    }
    private void populateMessageFields(){
        if(mUserUniqueId!=null){
            asyncFetch=new fetchUserNotification(mUserUniqueId);
           // asyncFetch.execute();
for(int i=0; i<3;i++){

    _notification_contents notes=new _notification_contents("12:00","I wish this could work","12:10","Yes it must","",2,false);
    notesList.add(notes);

    _notification_contents notes1=new _notification_contents("12:00","I wish this could work",null,null,"",2,false);
    notesList.add(notes1);

    _notification_contents notes2=new _notification_contents("12:00",null,null,"Yes it must","",2,false);
    notesList.add(notes2);


    _notification_contents notes3=new _notification_contents("12:00","I wish this could work","12:10",getString(R.string.trial_text_feed),"",2,false);
    notesList.add(notes3);

}

        mAdapter.notifyDataSetChanged();

    }
    }

    private class fetchUserNotification extends AsyncTask<Void, Cursor, Cursor> {
        private long userUniqueId;

        public fetchUserNotification(long userUniqueId) {
            this.userUniqueId = userUniqueId;
        }

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            dbHandler.open();
            Cursor cursor = dbHandler.readDb(null,null,_notification_db.tb_Struct.KEY_SENDER_TIME+" ASC");
            startManagingCursor(cursor);
            notesList.clear();
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            cursor.moveToFirst();

            int pid = cursor.getColumnIndexOrThrow(_notification_db.tb_Struct.KEY_PID);
            int senderText=  cursor.getColumnIndexOrThrow(_notification_db.tb_Struct.KEY_SENDER_TEXT);
            int senderTime = cursor.getColumnIndexOrThrow(_notification_db.tb_Struct.KEY_SENDER_TIME);
            int receiverText = cursor.getColumnIndexOrThrow(_notification_db.tb_Struct.KEY_RECEIVER_TEXT);
            int receiverTime = cursor.getColumnIndexOrThrow(_notification_db.tb_Struct.KEY_RECEIVER_TIME);
            int messageSent = cursor.getColumnIndexOrThrow(_notification_db.tb_Struct.KEY_MESSAGE_SENT);

            while (!cursor.isAfterLast()) {
                Log.i("meh",cursor.getString(receiverText));
                _notification_contents notes=new _notification_contents(cursor.getString(senderTime),cursor.getString(senderText),cursor.getString(receiverTime),cursor.getString(receiverText),"",2,false);
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
