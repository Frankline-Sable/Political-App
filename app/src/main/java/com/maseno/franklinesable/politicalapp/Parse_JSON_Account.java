package com.maseno.franklinesable.politicalapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Frankline Sable on 05/02/2017.
 */

public class Parse_JSON_Account {
    public static String[] id;
    public static String[] f_name;
    public static String[] l_name;
    public static String[] username;
    public static String[] home;
    public static String[] constituency;
    public static String[] password;
    public static String[] gender;
    public static String[] birthdate;

    private static final String KEY_ID = "pid";
    private static final String KEY_F_NAME = "f_name";
    private static final String KEY_L_NAME = "l_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_HOME = "home";
    private static final String KEY_CONSTITUENCY = "constituency";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_BIRTHDATE = "birthdate";

    private static final String JSON_ARRAY = "result";
    private String json;

    public Parse_JSON_Account(String json) {
        this.json = json;
    }

    public void ParseJSON() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray user = jsonObject.getJSONArray(JSON_ARRAY);
            id = new String[user.length()];
            f_name = new String[user.length()];
            l_name = new String[user.length()];
            username = new String[user.length()];
            home = new String[user.length()];
            constituency = new String[user.length()];
            password = new String[user.length()];
            gender = new String[user.length()];
            birthdate = new String[user.length()];

            for (int i = 0; i < user.length(); i++) {
                JSONObject object = user.getJSONObject(i);
                id[i] = object.getString(KEY_ID);
                f_name[i] = object.getString(KEY_F_NAME);
                l_name[i] = object.getString(KEY_L_NAME);
                username[i] = object.getString(KEY_USERNAME);
                home[i] = object.getString(KEY_HOME);
                constituency[i] = object.getString(KEY_CONSTITUENCY);
                password[i] = object.getString(KEY_PASSWORD);
                gender[i] = object.getString(KEY_GENDER);
               birthdate[i] = object.getString(KEY_BIRTHDATE);
            }
        } catch (Exception e) {
            Log.i("meh", "executing error " + e.getMessage() + "\n" + e.toString());
        }
    }
}
