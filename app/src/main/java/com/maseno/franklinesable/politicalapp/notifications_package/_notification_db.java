package com.maseno.franklinesable.politicalapp.notifications_package;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.maseno.franklinesable.politicalapp.R;

/**
 * Created by Frankline Sable on 24/02/2017. from 08:22
 * at Maseno University
 * Project PoliticalAppEp
 */

public class _notification_db {
    private final Context ctx;
    private static String tableName;


    public _notification_db(Context ctx, String tableName) {
        this.ctx = ctx;
        this.tableName = tableName;
    }

    static abstract class tb_Struct implements BaseColumns {
        //TODO for message like conversation
        static String TABLE_NAME=tableName;
        static final String KEY_PID = "pid";
        static final String KEY_SENDER_TEXT="sender_text";
        static final String KEY_SENDER_TIME="sender_time";
        static final String KEY_MESSAGE_SENT="message_sent";
        static final String KEY_RECEIVER_TEXT ="receiver_text";
        static final String KEY_RECEIVER_TIME="receiver_time";
    }
    private final String CREATE_TABLE="create table "+tb_Struct.TABLE_NAME +" ("
            + tb_Struct.KEY_PID+" integer primary key autoincrement, "
            +tb_Struct.KEY_SENDER_TEXT+ " text, "
            +tb_Struct.KEY_SENDER_TIME+ " text, "
            +tb_Struct.KEY_RECEIVER_TEXT+ " text, "
            +tb_Struct.KEY_RECEIVER_TIME+ " text, "
            +tb_Struct.KEY_MESSAGE_SENT+ " BOOLEAN )";


    private final static String DROP_TABLE = "drop table if exists " + _notification_db.tb_Struct.TABLE_NAME;
    private SQLiteDatabase dbSQL;
    private _notification_db.DatabaseHelper dbHelper;

    public class DatabaseHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "politics_notifications.db";
        public static final int DATABASE_VERSION = 7;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("meh","table created ");

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

    public _notification_db open() throws SQLException {
        tb_Struct.TABLE_NAME=ctx.getString(R.string.user_table)+tableName;
        dbHelper = new _notification_db.DatabaseHelper(ctx);
        dbSQL = dbHelper.getWritableDatabase();

        return this;
    }
    public _notification_db createTable(){
        String CREATE_TABLE="create table "+tb_Struct.TABLE_NAME +" ("
            + tb_Struct.KEY_PID+" integer primary key autoincrement, "
            +tb_Struct.KEY_SENDER_TEXT+ " text, "
            +tb_Struct.KEY_SENDER_TIME+ " text, "
            +tb_Struct.KEY_RECEIVER_TEXT+ " text, "
            +tb_Struct.KEY_RECEIVER_TIME+ " text, "
            +tb_Struct.KEY_MESSAGE_SENT+ " BOOLEAN )";

        dbSQL.execSQL(CREATE_TABLE);
        return this;
}
    public void close() {
        dbSQL.close();
        dbHelper.close();

    }

    public long insertSenderDb(String sender_text,String sender_time,Boolean message_sent) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(tb_Struct.KEY_SENDER_TEXT, sender_text);
        updatedValues.put(_notification_db.tb_Struct.KEY_SENDER_TIME, sender_time);
        updatedValues.put(tb_Struct.KEY_MESSAGE_SENT, message_sent);

        //dbSQL.close();
        return dbSQL.insert(tb_Struct.TABLE_NAME, null, updatedValues);
    }
    public long insertReceiverDb(String receiver_text, String receiver_time) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(_notification_db.tb_Struct.KEY_RECEIVER_TEXT, receiver_text);
        updatedValues.put(tb_Struct.KEY_RECEIVER_TIME, receiver_time);
        //dbSQL.close();
        return dbSQL.insert(tb_Struct.TABLE_NAME, null, updatedValues);
    }

    public boolean updateSenderDb(long rowId,String sender_text,String sender_time, Boolean message_sent) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(tb_Struct.KEY_SENDER_TEXT, sender_text);
        updatedValues.put(_notification_db.tb_Struct.KEY_SENDER_TIME, sender_time);
        updatedValues.put(tb_Struct.KEY_MESSAGE_SENT, message_sent);
        //dbSQL.close();
        return dbSQL.update(tb_Struct.TABLE_NAME, updatedValues, tb_Struct.KEY_PID + " = " + rowId, null) > 0;
    }
    public boolean updateReceiverDb(long rowId, String receiver_text, String receiver_time) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(_notification_db.tb_Struct.KEY_RECEIVER_TEXT, receiver_text);
        updatedValues.put(tb_Struct.KEY_RECEIVER_TIME, receiver_time);
       //dbSQL.close();
        return dbSQL.update(tb_Struct.TABLE_NAME, updatedValues, tb_Struct.KEY_PID + " = " + rowId, null) > 0;
    }

    public boolean deleteDb(String rowId) {
        return dbSQL.delete(_notification_db.tb_Struct.TABLE_NAME, rowId, null) > 0;
    }


    public boolean updateDeleteDb(long rowId, int delete) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(_notification_db.tb_Struct.KEY_MESSAGE_SENT, delete);

        return dbSQL.update(_notification_db.tb_Struct.TABLE_NAME, updatedValues, _notification_db.tb_Struct.KEY_PID + " = " + rowId, null) > 0;
    }
    Cursor readDb(String selection, String selectionArgs[], String sortOrder) {

        Cursor cursor = dbSQL.query(_notification_db.tb_Struct.TABLE_NAME, new String[]{_notification_db.tb_Struct.KEY_PID, tb_Struct.KEY_SENDER_TEXT, _notification_db.tb_Struct.KEY_SENDER_TIME, _notification_db.tb_Struct.KEY_RECEIVER_TEXT, tb_Struct.KEY_RECEIVER_TIME,
               _notification_db.tb_Struct.KEY_MESSAGE_SENT
        }, selection, selectionArgs, null, null, sortOrder, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    boolean updateDelete(long pid,  Boolean deleted){
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(tb_Struct.KEY_MESSAGE_SENT, deleted);

        return dbSQL.update(tb_Struct.TABLE_NAME, updatedValues, tb_Struct.KEY_PID+ " = " + pid, null) > 0;
    }
}

