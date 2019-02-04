package com.maseno.franklinesable.politicalapp.notifications_package;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Frankline Sable on 24/02/2017. from 08:22
 * at Maseno University
 * Project PoliticalAppEp
 */

public class Notification_Database {private final Context ctx;

    public Notification_Database(Context ctx) {
        this.ctx = ctx;
    }

    static abstract class tb_Struct implements BaseColumns {

        static final String TABLE_NAME = "notifications_table";
        static final String KEY_PID = "pid";
        static final String KEY_USERNAME = "username";
        static final String KEY_NOTIFICATION_ID= "unique_id";
        static final String KEY_TITLE = "notifications_title";
        static final String KEY_SUMMARY = "notifications_description";
        static final String KEY_CREATED_DATE_TIME = "created_date_time";
        static final String KEY_DELETED="_deleted";
        static final String KEY_FLAG_COLOR="flag_color";
    }

    private final static String CREATE_TABLE = "create table " + Notification_Database.tb_Struct.TABLE_NAME + " ("
            + Notification_Database.tb_Struct.KEY_PID + " integer primary key autoincrement, "
            + Notification_Database.tb_Struct.KEY_NOTIFICATION_ID+" real not null, "
            + Notification_Database.tb_Struct.KEY_TITLE + " text not null, "
            + tb_Struct.KEY_USERNAME + " text not null, "
            + Notification_Database.tb_Struct.KEY_SUMMARY + " text not null, "
            + tb_Struct.KEY_CREATED_DATE_TIME + " text not null, "
            + tb_Struct.KEY_FLAG_COLOR+" integer not null, "
            + Notification_Database.tb_Struct.KEY_DELETED+" BOOLEAN not null )";

    private final static String DROP_TABLE = "drop table if exists " + Notification_Database.tb_Struct.TABLE_NAME;
    private SQLiteDatabase dbSQL;
    private Notification_Database.DatabaseHelper dbHelper;

    public class DatabaseHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "politics_notifications.db";
        public static final int DATABASE_VERSION = 7;

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

    public Notification_Database open() throws SQLException {

        dbHelper = new Notification_Database.DatabaseHelper(ctx);
        dbSQL = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbSQL.close();
        dbHelper.close();

    }

    public long insertDb(long uniqueId,String name,String title, String body, String createdDate,int color) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(tb_Struct.KEY_NOTIFICATION_ID, uniqueId);
        updatedValues.put(tb_Struct.KEY_USERNAME, name);
        updatedValues.put(Notification_Database.tb_Struct.KEY_TITLE, title);
        updatedValues.put(Notification_Database.tb_Struct.KEY_SUMMARY, body);
        updatedValues.put(tb_Struct.KEY_CREATED_DATE_TIME, createdDate);
        updatedValues.put(tb_Struct.KEY_FLAG_COLOR, color);
        updatedValues.put(tb_Struct.KEY_DELETED, false);

        //dbSQL.close();
        return dbSQL.insert(tb_Struct.TABLE_NAME, null, updatedValues);
    }

    public boolean updateDb(long rowId,long uniqueId,String name,String title, String body, String createdDate,int color) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(tb_Struct.KEY_NOTIFICATION_ID, uniqueId);
        updatedValues.put(tb_Struct.KEY_USERNAME, name);
        updatedValues.put(Notification_Database.tb_Struct.KEY_TITLE, title);
        updatedValues.put(Notification_Database.tb_Struct.KEY_SUMMARY, body);
        updatedValues.put(tb_Struct.KEY_CREATED_DATE_TIME, createdDate);
        updatedValues.put(tb_Struct.KEY_FLAG_COLOR, color);
        updatedValues.put(tb_Struct.KEY_DELETED, false);

        //dbSQL.close();
        return dbSQL.update(tb_Struct.TABLE_NAME, updatedValues, tb_Struct.KEY_PID + " = " + rowId, null) > 0;
    }
    public boolean updateDbOnline(long uniqueId,String name,String title, String body, String createdDate,int color) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(tb_Struct.KEY_NOTIFICATION_ID, uniqueId);
        updatedValues.put(tb_Struct.KEY_USERNAME, name);
        updatedValues.put(Notification_Database.tb_Struct.KEY_TITLE, title);
        updatedValues.put(Notification_Database.tb_Struct.KEY_SUMMARY, body);
        updatedValues.put(tb_Struct.KEY_CREATED_DATE_TIME, createdDate);
        updatedValues.put(tb_Struct.KEY_FLAG_COLOR, color);
        updatedValues.put(tb_Struct.KEY_DELETED, false);
        //   dbSQL.close();
        return dbSQL.update(tb_Struct.TABLE_NAME, updatedValues, tb_Struct.KEY_NOTIFICATION_ID + " = " + uniqueId, null) > 0;
    }

    public boolean deleteDb(String rowId) {
        return dbSQL.delete(Notification_Database.tb_Struct.TABLE_NAME, rowId, null) > 0;
    }


    public boolean updateDeleteDb(long rowId, int delete) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(Notification_Database.tb_Struct.KEY_DELETED, delete);

        return dbSQL.update(Notification_Database.tb_Struct.TABLE_NAME, updatedValues, Notification_Database.tb_Struct.KEY_PID + " = " + rowId, null) > 0;
    }
    Cursor readDb(String selection, String selectionArgs[], String sortOrder) {

        Cursor cursor = dbSQL.query(Notification_Database.tb_Struct.TABLE_NAME, new String[]{Notification_Database.tb_Struct.KEY_PID, tb_Struct.KEY_USERNAME, Notification_Database.tb_Struct.KEY_TITLE, Notification_Database.tb_Struct.KEY_SUMMARY, tb_Struct.KEY_CREATED_DATE_TIME,
               tb_Struct.KEY_NOTIFICATION_ID, Notification_Database.tb_Struct.KEY_DELETED, tb_Struct.KEY_FLAG_COLOR
        }, selection, selectionArgs, null, null, sortOrder, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    boolean updateDelete(long pid,  Boolean deleted){
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(tb_Struct.KEY_DELETED, deleted);
        updatedValues.put(tb_Struct.KEY_NOTIFICATION_ID, pid);

        return dbSQL.update(tb_Struct.TABLE_NAME, updatedValues, tb_Struct.KEY_NOTIFICATION_ID+ " = " + pid, null) > 0;
    }
}
