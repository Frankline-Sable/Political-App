package com.maseno.franklinesable.politicalapp;

/**
 * Created by Sn0wBiRD on 1/12/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class AppDatabase {
    private final Context ctx;

    public AppDatabase(Context ctx) {
        this.ctx = ctx;
    }

    public static abstract class table_Structure implements BaseColumns {

        public static final String KEY_ROW_ID = "pid";
        public static final String KEY_F_NAME = "f_name";
        public static final String KEY_L_NAME = "l_name";
        public static final String KEY_USERNAME = "username";
        public static final String KEY_HOME = "home";
        public static final String KEY_CONSTITUENCY = "constituency";
        public static final String KEY_GENDER = "gender";
        public static final String KEY_BIRTHDATE = "birthdate";
        public static final String KEY_IMAGEPATH = "photo";
        public static final String TABLE_NAME = "TableUserCredentials";
    }

    private static final String DATABASE_CREATE = "create table " + table_Structure.TABLE_NAME + " ( " + table_Structure.KEY_ROW_ID + " integer primary key autoincrement,"
            + table_Structure.KEY_F_NAME + " text," + table_Structure.KEY_L_NAME + " text," + table_Structure.KEY_USERNAME + " text," + table_Structure.KEY_HOME + " text," + table_Structure.KEY_CONSTITUENCY + " text," +
            table_Structure.KEY_GENDER + " text," + table_Structure.KEY_BIRTHDATE + " text," + table_Structure.KEY_IMAGEPATH + " text ); ";
    private static final String DROP_TABLE = "drop table if exists " + table_Structure.TABLE_NAME;
    private SQLiteDatabase dbSQL;
    private DatabaseHelper dbHelper;

    public class DatabaseHelper extends SQLiteOpenHelper {
        public static final String DATABASE_NAME = "Politics.dbSQL";
        private static final int DATABASE_VERSION = 8;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Called when no database exists in disk and the helper class needs
        // to create a new one.
        @Override
        public void onCreate(SQLiteDatabase dbSQL) {
            dbSQL.execSQL(DATABASE_CREATE);

        }

        // Called when there is a database version mismatch meaning that the version
        // of the database on disk needs to be upgraded to the current version.
        @Override
        public void onUpgrade(SQLiteDatabase dbSQL, int _oldVersion, int _newVersion) {
            dbSQL.execSQL(DROP_TABLE);
            onCreate(dbSQL);
        }

        @Override
        public void onDowngrade(SQLiteDatabase dbSQL, int oldVersion, int newVersion) {
            onUpgrade(dbSQL, oldVersion, newVersion);
        }
    }

    public AppDatabase open() throws SQLException {
        dbHelper = new DatabaseHelper(ctx);
        dbSQL = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        //dbSQL.close();
        dbHelper.close();
    }

    public boolean updateDb(long rowId, String f_name,String l_name, String userName, String home, String constituency,String gender, String birthdate,String imagePath) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(table_Structure.KEY_F_NAME, f_name);
        updatedValues.put(table_Structure.KEY_L_NAME, l_name);
        updatedValues.put(table_Structure.KEY_USERNAME, userName);
        updatedValues.put(table_Structure.KEY_HOME, home);
        updatedValues.put(table_Structure.KEY_CONSTITUENCY, constituency);
        updatedValues.put(table_Structure.KEY_GENDER, gender);
        updatedValues.put(table_Structure.KEY_BIRTHDATE, birthdate);
        updatedValues.put(table_Structure.KEY_IMAGEPATH, imagePath);

        Boolean update=dbSQL.update(table_Structure.TABLE_NAME, updatedValues, table_Structure.KEY_ROW_ID + " = " + rowId, null) > 0;
    //dbSQL.close();
        return update;
    }

    public long insertDb(String f_name,String l_name, String userName, String home, String constituency,String gender, String birthdate,String imagePath) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(table_Structure.KEY_F_NAME, f_name);
        initialValues.put(table_Structure.KEY_L_NAME, l_name);
        initialValues.put(table_Structure.KEY_USERNAME, userName);
        initialValues.put(table_Structure.KEY_HOME, home);
        initialValues.put(table_Structure.KEY_CONSTITUENCY, constituency);
        initialValues.put(table_Structure.KEY_GENDER, gender);
        initialValues.put(table_Structure.KEY_BIRTHDATE, birthdate);
        initialValues.put(table_Structure.KEY_IMAGEPATH, imagePath);

        long id=dbSQL.insert(table_Structure.TABLE_NAME, null, initialValues);
        ////dbSQL.close();
        return id;
    }

    public Cursor readDb() {
        Cursor cursor = dbSQL.query(table_Structure.TABLE_NAME, new String[]{table_Structure.KEY_ROW_ID, table_Structure.KEY_F_NAME, table_Structure.KEY_L_NAME, table_Structure.KEY_USERNAME,  table_Structure.KEY_HOME, table_Structure.KEY_CONSTITUENCY,table_Structure.KEY_GENDER,table_Structure.KEY_BIRTHDATE, table_Structure.KEY_IMAGEPATH}, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Boolean deleteDb(){
        return dbSQL.delete(table_Structure.TABLE_NAME,null, null)>0;
    }
}