package com.maseno.franklinesable.politicalapp.events_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Frankline Sable on 7/20/2016.
 * This class is used explicitly for storing the users data in an sql db;
 */
public class Events_Database {

    private Context ctx;

    public Events_Database(Context ctx) {
        this.ctx = ctx;
    }

    public static abstract class tb_Struct implements BaseColumns {

        public static final String TABLE_NAME = "politics_events";
        public static final String KEY_PID = "pid";
        public static final String KEY_TITLE = "events_title";
        public static final String KEY_DESCRIPTION = "events_description";
        public static final String KEY_CREATED_DATE = "created_date";
        public static final String KEY_CREATED_TIME = "created_time";
        public static final String KEY_COLOR = "events_color";
        public static final String KEY_SCHEDULED_DATE = "date_scheduled";
        public static final String KEY_SCHEDULED_TIME = "time_scheduled";
        public static final String KEY_MONTH_REF = "month_referee";
        public static final String KEY_YEAR_REF="year_referee";
        public static final String KEY_SERVER_BASED="server_based";
        public static final String KEY_EVENT_ID="event_id";
        public static final String KEY_DELETED="_deleted";
    }

    private final static String CREATE_TABLE = "create table " + tb_Struct.TABLE_NAME + " (" + tb_Struct.KEY_PID + " integer primary key autoincrement, "
            + tb_Struct.KEY_TITLE + " text not null, " + tb_Struct.KEY_DESCRIPTION + " text not null, " + tb_Struct.KEY_CREATED_DATE + " text not null, " + tb_Struct.KEY_CREATED_TIME
            + " text not null, " + tb_Struct.KEY_COLOR + " integer not null, " + tb_Struct.KEY_SCHEDULED_DATE + " text not null, " + tb_Struct.KEY_SCHEDULED_TIME + " text not null, "
            + tb_Struct.KEY_MONTH_REF+ " text not null, "+tb_Struct.KEY_YEAR_REF+" integer not null, "+tb_Struct.KEY_SERVER_BASED+" BOOLEAN, "+tb_Struct.KEY_EVENT_ID+" REAL, "+tb_Struct.KEY_DELETED+" integer not null )";


    private final static String DROP_TABLE = "drop table if exists " + tb_Struct.TABLE_NAME;
    private SQLiteDatabase dbSQL;
    private DatabaseHelper dbHelper;

    public class DatabaseHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "politics_events.db";
        public static final int DATABASE_VERSION = 12;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
    public Events_Database open() throws SQLException {

        dbHelper = new DatabaseHelper(ctx);
        dbSQL = dbHelper.getWritableDatabase();

        return this;
    }

    public void close() {
        if(dbHelper!=null){
            dbHelper.close();
        }

    }

    public long insertDb(String title, String body, String createdDate, String createdTime, int color, String scheduledDate, String scheduledTime, String month,String year,long uniqueId, Boolean fromServer) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(tb_Struct.KEY_TITLE, title);
        initialValues.put(tb_Struct.KEY_DESCRIPTION, body);
        initialValues.put(tb_Struct.KEY_CREATED_DATE, createdDate);
        initialValues.put(tb_Struct.KEY_CREATED_TIME, createdTime);
        initialValues.put(tb_Struct.KEY_COLOR, color);
        initialValues.put(tb_Struct.KEY_SCHEDULED_DATE, scheduledDate);
        initialValues.put(tb_Struct.KEY_SCHEDULED_TIME, scheduledTime);
        initialValues.put(tb_Struct.KEY_MONTH_REF, month);
        initialValues.put(tb_Struct.KEY_YEAR_REF, year);
        initialValues.put(tb_Struct.KEY_EVENT_ID, uniqueId);
        initialValues.put(tb_Struct.KEY_SERVER_BASED, fromServer);
        initialValues.put(tb_Struct.KEY_DELETED, 0);

        long id=dbSQL.insert(tb_Struct.TABLE_NAME, null, initialValues);
       // dbSQL.close();
        return id;
    }

    public boolean updateDb(long rowId, String title, String body, String createdDate, String createdTime, int color, String scheduledDate, String scheduledTime,String month,String year) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(tb_Struct.KEY_TITLE, title);
        updatedValues.put(tb_Struct.KEY_DESCRIPTION, body);
        updatedValues.put(tb_Struct.KEY_CREATED_DATE, createdDate);
        updatedValues.put(tb_Struct.KEY_CREATED_TIME, createdTime);
        updatedValues.put(tb_Struct.KEY_COLOR, color);
        updatedValues.put(tb_Struct.KEY_SCHEDULED_DATE, scheduledDate);
        updatedValues.put(tb_Struct.KEY_SCHEDULED_TIME, scheduledTime);
        updatedValues.put(tb_Struct.KEY_MONTH_REF, month);
        updatedValues.put(tb_Struct.KEY_YEAR_REF, year);

        Boolean update= dbSQL.update(tb_Struct.TABLE_NAME, updatedValues, tb_Struct.KEY_PID + " = " + rowId, null) > 0;
     //   dbSQL.close();
        return update;
    }
    public boolean updateDbOnline(long uniqueId, String title, String body, String createdDate, String createdTime, int color, String scheduledDate, String scheduledTime,String month,String year) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(tb_Struct.KEY_TITLE, title);
        updatedValues.put(tb_Struct.KEY_DESCRIPTION, body);
        updatedValues.put(tb_Struct.KEY_CREATED_DATE, createdDate);
        updatedValues.put(tb_Struct.KEY_CREATED_TIME, createdTime);
        updatedValues.put(tb_Struct.KEY_COLOR, color);
        updatedValues.put(tb_Struct.KEY_SCHEDULED_DATE, scheduledDate);
        updatedValues.put(tb_Struct.KEY_SCHEDULED_TIME, scheduledTime);
        updatedValues.put(tb_Struct.KEY_MONTH_REF, month);
        updatedValues.put(tb_Struct.KEY_YEAR_REF, year);

        Boolean update=dbSQL.update(tb_Struct.TABLE_NAME, updatedValues, tb_Struct.KEY_EVENT_ID + " = " + uniqueId, null) > 0;
     //   dbSQL.close();
        return update;
    }

    public boolean deleteDb(String rowId) {
        return dbSQL.delete(tb_Struct.TABLE_NAME, rowId, null) > 0;
    }


    public boolean updateDeleteDb(long rowId, int delete) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(tb_Struct.KEY_DELETED, delete);

        return dbSQL.update(tb_Struct.TABLE_NAME, updatedValues, tb_Struct.KEY_PID + " = " + rowId, null) > 0;
    }
    public Cursor readDb(String selection, String selectionArgs[], String sortOrder) {

        Cursor cursor = dbSQL.query(tb_Struct.TABLE_NAME, new String[]{tb_Struct.KEY_PID, tb_Struct.KEY_TITLE, tb_Struct.KEY_DESCRIPTION, tb_Struct.KEY_CREATED_DATE, tb_Struct.KEY_CREATED_TIME,
                tb_Struct.KEY_COLOR, tb_Struct.KEY_SCHEDULED_DATE, tb_Struct.KEY_SCHEDULED_TIME, tb_Struct.KEY_COLOR,tb_Struct.KEY_MONTH_REF,tb_Struct.KEY_YEAR_REF,tb_Struct.KEY_EVENT_ID,tb_Struct.KEY_SERVER_BASED,tb_Struct.KEY_DELETED}, selection, selectionArgs, null, null, sortOrder, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor searchDb(String selection, String selectionArgs[], String sortOrder) {

        Cursor cursor = dbSQL.query(tb_Struct.TABLE_NAME, new String[]{tb_Struct.KEY_PID, tb_Struct.KEY_TITLE, tb_Struct.KEY_DESCRIPTION, tb_Struct.KEY_CREATED_DATE, tb_Struct.KEY_CREATED_TIME,
                tb_Struct.KEY_COLOR, tb_Struct.KEY_SCHEDULED_DATE, tb_Struct.KEY_SCHEDULED_TIME, tb_Struct.KEY_COLOR,tb_Struct.KEY_MONTH_REF,tb_Struct.KEY_YEAR_REF,tb_Struct.KEY_EVENT_ID,tb_Struct.KEY_SERVER_BASED,tb_Struct.KEY_DELETED}, selection, selectionArgs, null, null, sortOrder, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}
