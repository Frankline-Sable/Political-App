package com.maseno.franklinesable.politicalapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Frankline Sable on 05/02/2017.
 */

public class Parse_JSON_Emails {
    public static String[] id;
    public static String[] username;

    private static final String KEY_ID = "pid";
    private static final String KEY_USERNAME = "username";
    public static final String JSON_ARRAY = "result";
    private JSONArray user = null;
    private String json;

    public Parse_JSON_Emails(String json) {
        this.json = json;
    }

    protected void ParseJSON() {
        JSONObject jsonObject = null;
        try {
            Log.i("meh", "executing parser");
            jsonObject = new JSONObject(json);
            user = jsonObject.getJSONArray(JSON_ARRAY);
            Log.i("meh", "executing parser2");
            id = new String[user.length()];
            username = new String[user.length()];

            for (int i = 0; i < user.length(); i++) {
                JSONObject object = user.getJSONObject(i);
                id[i] = object.getString(KEY_ID);
                username[i] = object.getString(KEY_USERNAME);
            }
            Log.i("meh", "executing parser");
        } catch (Exception e) {
            Log.i("meh", "executing error " + e.getMessage() + "\n" + e.toString());
        }
    }
}
