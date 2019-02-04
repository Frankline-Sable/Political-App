package com.maseno.franklinesable.politicalapp.notifications_package;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Frankline Sable on 23/02/2017. from 23:08
 * at Maseno University
 * Project PoliticalAppEp
 */

public class Notifications_JSON {
    public static final String JSON_ARRAY = "notifications";
    public static final String KEY_NAME = "name";
    public static final String KEY_TITLE = "title";
    public static final String KEY_SUMMARY = "summary";
    public static final String KEY_NOTIFICATION_ID = "pid";
    public static final String KEY_FLAG_COLOUR = "flag";
    public static long[] notification_id;
    public static String[] name;
    public static String[] title;
    public static String[] summary;
    public static int[] color;

    private String json;

    public Notifications_JSON(String json) {
        this.json = json;
    }

    public void parseJSON() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray notificationsArray = jsonObject.getJSONArray(JSON_ARRAY);

            notification_id = new long[notificationsArray.length()];
            name = new String[notificationsArray.length()];
            title = new String[notificationsArray.length()];
            summary = new String[notificationsArray.length()];
            color = new int[notificationsArray.length()];

            for (int i = 0; i < notificationsArray.length(); i++) {
                JSONObject jo = notificationsArray.getJSONObject(i);
                notification_id[i] = jo.getInt(KEY_NOTIFICATION_ID);
                name[i] = jo.getString(KEY_NAME);
                title[i] = jo.getString(KEY_TITLE);
                summary[i] = jo.getString(KEY_SUMMARY);
                color[i] = jo.getInt(KEY_FLAG_COLOUR);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
