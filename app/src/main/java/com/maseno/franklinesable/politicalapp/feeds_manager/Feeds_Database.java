package com.maseno.franklinesable.politicalapp.feeds_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Frankline Sable on 24/02/2017. from 08:22
 * at Maseno University
 * Project PoliticalAppEp
 */

public class Feeds_Database {private final Context ctx;

    public Feeds_Database(Context ctx) {
        this.ctx = ctx;
    }

    static abstract class tb_Struct implements BaseColumns {

        static final String TABLE_NAME = "feeds_table";
        static final String KEY_PID = "pid";
        static final String KEY_FEED_ID= "unique_id";
        static final String KEY_TITLE = "feeds_title";
        static final String KEY_IMAGE_URL = "image_url";
        static final String KEY_DESCRIPTION = "feeds_description";
        static final String KEY_CREATED_DATE_TIME = "created_date_time";
        static final String KEY_DELETED="_deleted";
        static final String KEY_HIDDEN="_hidden";
    }

    private final static String CREATE_TABLE = "create table " + Feeds_Database.tb_Struct.TABLE_NAME + " ("
            + Feeds_Database.tb_Struct.KEY_PID + " integer primary key autoincrement, "
            + Feeds_Database.tb_Struct.KEY_FEED_ID+" integer not null, "
            + Feeds_Database.tb_Struct.KEY_TITLE + " text not null, "
            + tb_Struct.KEY_IMAGE_URL + " text not null, "
            + Feeds_Database.tb_Struct.KEY_DESCRIPTION + " text not null, "
            + tb_Struct.KEY_CREATED_DATE_TIME + " text not null, "
            + tb_Struct.KEY_HIDDEN+" BOOLEAN, "
            + Feeds_Database.tb_Struct.KEY_DELETED+" BOOLEAN not null )";


    private final static String DROP_TABLE = "drop table if exists " + Feeds_Database.tb_Struct.TABLE_NAME;
    private SQLiteDatabase dbSQL;
    private Feeds_Database.DatabaseHelper dbHelper;

    public class DatabaseHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "politics_feeds.db";
        public static final int DATABASE_VERSION = 2;

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

    public Feeds_Database open() throws SQLException {

        dbHelper = new Feeds_Database.DatabaseHelper(ctx);
        dbSQL = dbHelper.getWritableDatabase();

        return this;
    }

    public void close() {
        dbSQL.close();
        dbHelper.close();

    }

    public long insertDb(long uniqueId,String title, String body, String createdDate,String imgUrl) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(Feeds_Database.tb_Struct.KEY_TITLE, title);
        updatedValues.put(Feeds_Database.tb_Struct.KEY_DESCRIPTION, body);
        updatedValues.put(tb_Struct.KEY_CREATED_DATE_TIME, createdDate);
        updatedValues.put(tb_Struct.KEY_FEED_ID, uniqueId);
        updatedValues.put(tb_Struct.KEY_IMAGE_URL, imgUrl);
        updatedValues.put(tb_Struct.KEY_HIDDEN, false);
        updatedValues.put(tb_Struct.KEY_DELETED, false);

        //dbSQL.close();
        return dbSQL.insert(tb_Struct.TABLE_NAME, null, updatedValues);
    }

    public boolean updateDb(long rowId, String title, String body, String createdDate, long uniqueId,String imgUrl) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(Feeds_Database.tb_Struct.KEY_TITLE, title);
        updatedValues.put(Feeds_Database.tb_Struct.KEY_DESCRIPTION, body);
        updatedValues.put(tb_Struct.KEY_CREATED_DATE_TIME, createdDate);
        updatedValues.put(tb_Struct.KEY_FEED_ID, uniqueId);
        updatedValues.put(tb_Struct.KEY_IMAGE_URL, imgUrl);
        updatedValues.put(tb_Struct.KEY_HIDDEN, false);
        updatedValues.put(tb_Struct.KEY_DELETED, false);

        //dbSQL.close();
        return dbSQL.update(tb_Struct.TABLE_NAME, updatedValues, tb_Struct.KEY_PID + " = " + rowId, null) > 0;
    }
    public boolean updateDbOnline(long uniqueId, String title, String body, String createdDate,String imgUrl) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(tb_Struct.KEY_FEED_ID, uniqueId);
        updatedValues.put(Feeds_Database.tb_Struct.KEY_TITLE, title);
        updatedValues.put(Feeds_Database.tb_Struct.KEY_DESCRIPTION, body);
        updatedValues.put(tb_Struct.KEY_CREATED_DATE_TIME, createdDate);
        updatedValues.put(tb_Struct.KEY_IMAGE_URL, imgUrl);


        //   dbSQL.close();
        return dbSQL.update(tb_Struct.TABLE_NAME, updatedValues, tb_Struct.KEY_FEED_ID + " = " + uniqueId, null) > 0;
    }

    public boolean deleteDb(String rowId) {
        return dbSQL.delete(Feeds_Database.tb_Struct.TABLE_NAME, rowId, null) > 0;
    }


    public boolean updateDeleteDb(long rowId, int delete) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(Feeds_Database.tb_Struct.KEY_DELETED, delete);

        return dbSQL.update(Feeds_Database.tb_Struct.TABLE_NAME, updatedValues, Feeds_Database.tb_Struct.KEY_PID + " = " + rowId, null) > 0;
    }
    Cursor readDb(String selection, String selectionArgs[], String sortOrder) {

        Cursor cursor = dbSQL.query(Feeds_Database.tb_Struct.TABLE_NAME, new String[]{Feeds_Database.tb_Struct.KEY_PID, Feeds_Database.tb_Struct.KEY_TITLE, Feeds_Database.tb_Struct.KEY_DESCRIPTION, tb_Struct.KEY_CREATED_DATE_TIME,
               tb_Struct.KEY_FEED_ID, Feeds_Database.tb_Struct.KEY_DELETED, tb_Struct.KEY_IMAGE_URL,tb_Struct.KEY_HIDDEN,tb_Struct.KEY_DELETED
        }, selection, selectionArgs, null, null, sortOrder, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    boolean updateFeedsDb(int pid, Boolean hidden, Boolean deleted){
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(tb_Struct.KEY_HIDDEN, hidden);
        updatedValues.put(tb_Struct.KEY_DELETED, deleted);
        updatedValues.put(tb_Struct.KEY_FEED_ID, pid);

        return dbSQL.update(tb_Struct.TABLE_NAME, updatedValues, tb_Struct.KEY_FEED_ID+ " = " + pid, null) > 0;
    }
}
