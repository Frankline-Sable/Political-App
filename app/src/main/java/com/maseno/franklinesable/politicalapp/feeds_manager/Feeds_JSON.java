package com.maseno.franklinesable.politicalapp.feeds_manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Frankline Sable on 23/02/2017. from 23:08
 * at Maseno University
 * Project PoliticalAppEp
 */

public class Feeds_JSON {
    public static final String JSON_ARRAY = "Feeds";
    public static final String KEY_TITLE = "title";
    public static final String KEY_SUMMARY = "summary";
    public static final String KEY_IMAGE_URL = "image";
    public static final String KEY_FEED_ID = "pid";
    public static long[] feed_id;
    public static String[] title;
    public static String[] summary;
    public static String[] image_url;
    private JSONArray feedsArray = null;

    private String json;

    public Feeds_JSON(String json) {
        this.json = json;
    }

    public void parseJSON() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            feedsArray = jsonObject.getJSONArray(JSON_ARRAY);

            feed_id=new long[feedsArray.length()];
            title = new String[feedsArray.length()];
            summary = new String[feedsArray.length()];
            image_url = new String[feedsArray.length()];

            for (int i = 0; i < feedsArray.length(); i++) {
                JSONObject jo = feedsArray.getJSONObject(i);
                feed_id[i]=jo.getLong(KEY_FEED_ID);
                title[i] = jo.getString(KEY_TITLE);
                summary[i] = jo.getString(KEY_SUMMARY);
                image_url[i] = jo.getString(KEY_IMAGE_URL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
