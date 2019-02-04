package com.maseno.franklinesable.politicalapp.services;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.maseno.franklinesable.politicalapp.R;
import com.maseno.franklinesable.politicalapp.central_class_boss.Main_BossClass;
import com.maseno.franklinesable.politicalapp.events_manager.Events_Database;
import com.maseno.franklinesable.politicalapp.feeds_manager.Feeds_Database;
import com.maseno.franklinesable.politicalapp.feeds_manager.Feeds_JSON;
import com.maseno.franklinesable.politicalapp.notifications_package.Notification_Database;
import com.maseno.franklinesable.politicalapp.notifications_package.Notifications_JSON;
import com.maseno.franklinesable.politicalapp.notifications_package._notification_db;
import com.maseno.franklinesable.politicalapp.sharedmethods.PreferencesHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.maseno.franklinesable.politicalapp.events_manager.Add_Event.DATE_FORMAT_DB;
import static com.maseno.franklinesable.politicalapp.events_manager.Add_Event.TIME_FORMAT_UI_DB;

/**
 * Created by Frankline Sable on 14/02/2017. from 23:05
 * at Maseno University
 * Project PoliticalAppEp
 */

public class ModuleFetchService extends IntentService {
    public static final String ACTION_REFRESH_MODULES = "com.maseno.franklinesable.politicalapp.services.action.REFRESH_MODULES";
    public static final String TAG = "com.maseno.franklinesable.politicalapp";
    public static final String url_fetch_feeds = "http://192.168.75.2/polits_server/feeds_section/feeds_section.json";
    public static final String url_fetch_notify = "http://192.168.75.2/polits_server/notifications_section/notifications_section.json";
    public static final String url_fetch_events = "http://192.168.75.2/polits_server/events_section/getData.php";
    public static final String PREFS_PRIVATE_COUNTS = "PREFS_FOR_MODULES_COUNT";
    private static final String KEY_FEED_COUNT = "KEY_FEED_COUNT";
    private static final String KEY_EVENT_COUNT = "KEY_EVENT_COUNT";
    private static final String KEY_NOTIFICATION_COUNT = "KEY_NOTIFICATIONS_COUNT";
    private Events_Database dbHandler_Events;
    private Feeds_Database dbHandler_Feeds;
    private _notification_db dbHandler_Messages;
    private Notification_Database dbHandler_Notify;
    private SharedPreferences sharedPreferences, modulesPreferences;
    private int NEW_EVENT_COUNT = 0, FEED_COUNT = 0, NOTIFICATION_COUNT = 0;
    private String jsonString;
    private Intent myIntent;
    private Context mContext;

    public ModuleFetchService() {
        super("ModuleFetchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mContext = getApplicationContext();
        dbHandler_Events = new Events_Database(mContext);
        dbHandler_Feeds = new Feeds_Database(mContext);
        dbHandler_Notify = new Notification_Database(mContext);

        NEW_EVENT_COUNT = 0;
        FEED_COUNT = 0;

        myIntent = intent;
        sharedPreferences = getSharedPreferences(PreferencesHandler.PREFS_PRIVATE, Context.MODE_PRIVATE);
        modulesPreferences = getSharedPreferences(PREFS_PRIVATE_COUNTS, Context.MODE_PRIVATE);
        FetchNewEvents();
    }

    private void OrganizeData() {

        Intent in = new Intent(ACTION_REFRESH_MODULES);
        in.putExtra("resultCode", Activity.RESULT_OK);
        in.putExtra("resultValueEvents", NEW_EVENT_COUNT);
        in.putExtra("resultValueFeeds", FEED_COUNT);
        in.putExtra("resultValueNotifications",NOTIFICATION_COUNT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);

        WakefulBroadcastReceiver.completeWakefulIntent(myIntent);
    }


    //TODO ALL FETCHES SHOULD BE WITHIN THIS BLOCK{} Here we are mainly focused on fetching new module data from the main boss class and notifying the user. We use volley library

    private void FetchNewEvents() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_fetch_events,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Data Successfully Fetched
                        try {
                            JSONObject js = new JSONObject(response);
                            JSONArray jsonArray = js.getJSONArray("events");
                            jsonString = jsonArray.toString();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("jsonString", jsonString);
                            editor.apply();

                            //savePref.saveEventsJSON(jsonString);

                            JSONArray sortedJsonArray = new JSONArray();
                            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonValues.add(jsonArray.getJSONObject(i));
                            }
                            Collections.sort(jsonValues, new Comparator<JSONObject>() {
                                //You can change "Name" with "ID" if you want to sort by ID
                                private static final String KEY_NAME = "scheduled_date";

                                @Override
                                public int compare(JSONObject a, JSONObject b) {
                                    String valA = "";
                                    String valB = "";

                                    try {
                                        valA = (String) a.get(KEY_NAME);
                                        valB = (String) b.get(KEY_NAME);
                                    } catch (JSONException e) {
                                        //do something
                                    }
                                    return valA.compareTo(valB);
                                    //if you want to change the sort order, simply use the following:
                                    //return -valA.compareTo(valB);
                                }
                            });

                            for (int i = 0; i < jsonArray.length(); i++) {
                                sortedJsonArray.put(jsonValues.get(i));
                            }
                            dbHandler_Events.open();
                            for (int i = 0; i < sortedJsonArray.length(); i++) {

                                JSONObject jsonObject = sortedJsonArray.getJSONObject(i);
                                String ps_EventTitle = jsonObject.getString("title");
                                String ps_EventBody = jsonObject.getString("description");
                                int ps_EventColor = jsonObject.getInt("color");
                                String ps_EventDate = jsonObject.getString("scheduled_date");
                                String ps_EventTime = jsonObject.getString("scheduled_time");
                                String ps_MonthReferee = jsonObject.getString("month");
                                String ps_YearReferee = jsonObject.getString("year");
                                long uniqueId = jsonObject.getInt("uniqueId");
                                String ps_DateCreated = getCurrentDate();
                                String ps_TimeCreated = getCurrentTime();

                                Boolean update = dbHandler_Events.updateDbOnline(uniqueId, ps_EventTitle, ps_EventBody, ps_DateCreated, ps_TimeCreated, ps_EventColor, ps_EventDate, ps_EventTime, ps_MonthReferee, ps_YearReferee);
                                if (!update) {
                                    long id = dbHandler_Events.insertDb(ps_EventTitle, ps_EventBody, ps_DateCreated, ps_TimeCreated, ps_EventColor, ps_EventDate, ps_EventTime, ps_MonthReferee, ps_YearReferee, uniqueId, true);
                                    NEW_EVENT_COUNT++;
                                }
                            }
                            dbHandler_Events.close();
                            callNewEventsCount();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue request = Volley.newRequestQueue(this);
        request.add(stringRequest);
    }

    public void FetchNewFeeds() {

        StringRequest stringRequest = new StringRequest(url_fetch_feeds,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Feeds_JSON pj = new Feeds_JSON(response);
                        pj.parseJSON();
                        dbHandler_Feeds.open();
                        for (int i = 0; i < Feeds_JSON.feed_id.length; i++) {
                            Boolean update = dbHandler_Feeds.updateDbOnline(Feeds_JSON.feed_id[i], Feeds_JSON.title[i], Feeds_JSON.summary[i], getDateTime(), Feeds_JSON.image_url[i]);
                            if (!update) {
                                long id = dbHandler_Feeds.insertDb(Feeds_JSON.feed_id[i], Feeds_JSON.title[i], Feeds_JSON.summary[i], getDateTime(), Feeds_JSON.image_url[i]);
                                FEED_COUNT++;
                            }
                        }
                        dbHandler_Feeds.close();
                        callNewFeedsCount();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //ignore error
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void FetchNewNotifications() {

        StringRequest stringRequest = new StringRequest(url_fetch_notify,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Notifications_JSON pj = new Notifications_JSON(response);
                        pj.parseJSON();
                        dbHandler_Notify.open();
                        for (int i = 0; i < Notifications_JSON.notification_id.length; i++) {
                            Boolean update = dbHandler_Notify.updateDbOnline(Notifications_JSON.notification_id[i], Notifications_JSON.name[i], Notifications_JSON.title[i], Notifications_JSON.summary[i], getDateTime(), Notifications_JSON.color[i]);
                            if (!update) {
                                long id = dbHandler_Notify.insertDb(Notifications_JSON.notification_id[i], Notifications_JSON.name[i], Notifications_JSON.title[i], Notifications_JSON.summary[i], getDateTime(), Notifications_JSON.color[i]);

                                dbHandler_Messages=new _notification_db(mContext, Long.toString(Notifications_JSON.notification_id[i]));
                                dbHandler_Messages.open();
                                dbHandler_Messages.createTable();
                                inputDataIntoNotifications(Notifications_JSON.notification_id[i],Notifications_JSON.title[i], Notifications_JSON.summary[i]);

                                NOTIFICATION_COUNT++;
                            }
                            inputDataIntoNotifications(Notifications_JSON.notification_id[i],Notifications_JSON.title[i], Notifications_JSON.summary[i]);
                        }
                        dbHandler_Notify.close();
                        callNewNotificationsCount();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //ignore error
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    //TODO ALL CALLS TO FETCHES SHOULD BE WITHIN THIS BLOCK{} Here we are mainly focused on fetching new module data from the main boss class and notifying the user. We use volley library

    public int callNewEventsCount() {//OrganizeData();

        if (NEW_EVENT_COUNT > 0) {

            int count = modulesPreferences.getInt(KEY_EVENT_COUNT, 0) + NEW_EVENT_COUNT;
            SharedPreferences.Editor eventEditor = modulesPreferences.edit();
            eventEditor.putInt(KEY_EVENT_COUNT, count);
            eventEditor.apply();

            int requestCode = 3242;
            final Bitmap picture = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            final String ticker = getString(R.string.ticker_text, NEW_EVENT_COUNT);
            final String title = getResources().getString(R.string.title_and_description, ticker);
            final String text = "From Political App";

            Intent resultIntent = new Intent(ModuleFetchService.this, Main_BossClass.class);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(ModuleFetchService.this)
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setSmallIcon(R.drawable.ic_event_notification_icon)
                    .setContentTitle(NEW_EVENT_COUNT + " new events are available")
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setLargeIcon(picture)
                    .setNumber(NEW_EVENT_COUNT)
                    .setContentIntent(PendingIntent.getActivity(ModuleFetchService.this, requestCode,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(text)
                            .setBigContentTitle(title)
                            .setSummaryText("Click to view reminder"))
                    .addAction(android.R.drawable.ic_menu_view, "View", null
                    )
                    .addAction(android.R.drawable.ic_menu_close_clear_cancel, "DISMISS", null)
                    .setAutoCancel(true);

            builder.setDefaults(Notification.DEFAULT_VIBRATE);

            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(TAG, requestCode, builder.build());

        }
        FetchNewFeeds();
        return NEW_EVENT_COUNT;
    }

    public int callNewFeedsCount() {

        if (FEED_COUNT > 0) {

            int count = modulesPreferences.getInt(KEY_FEED_COUNT, 0) + FEED_COUNT;
            SharedPreferences.Editor eventEditor = modulesPreferences.edit();
            eventEditor.putInt(KEY_FEED_COUNT, count);
            eventEditor.apply();

            int requestCode = 3343;
            final Bitmap picture = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            final String ticker = getString(R.string.ticker_text_feeds, FEED_COUNT);
            final String title = getResources().getString(R.string.title_and_description, ticker);
            final String text = "From Political App";

            Intent resultIntent = new Intent(ModuleFetchService.this, Main_BossClass.class);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(ModuleFetchService.this)
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setSmallIcon(R.drawable.ic_event_notification_icon)
                    .setContentTitle(FEED_COUNT + " new feeds are available")
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setLargeIcon(picture)
                    .setNumber(FEED_COUNT)
                    .setContentIntent(PendingIntent.getActivity(ModuleFetchService.this, requestCode,
                            resultIntent,
                            PendingIntent.FLAG_ONE_SHOT))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(text)
                            .setBigContentTitle(title)
                            .setSummaryText("Click to view reminder"))
                    .addAction(android.R.drawable.ic_menu_view, "View", null
                    )
                    .addAction(android.R.drawable.ic_menu_close_clear_cancel, "DISMISS", null)
                    .setAutoCancel(true);

            builder.setDefaults(Notification.DEFAULT_VIBRATE);

            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(TAG, requestCode, builder.build());
        }
        FetchNewNotifications();
        return FEED_COUNT;
    }

    public int callNewNotificationsCount() {

        if (NOTIFICATION_COUNT > 0) {

            int count = modulesPreferences.getInt(KEY_NOTIFICATION_COUNT, 0) + NOTIFICATION_COUNT;
            SharedPreferences.Editor eventEditor = modulesPreferences.edit();
            eventEditor.putInt(KEY_NOTIFICATION_COUNT, count);
            eventEditor.apply();

            int requestCode = 3444;
            final Bitmap picture = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            final String ticker = getString(R.string.ticker_text_feeds, NOTIFICATION_COUNT);
            final String title = getResources().getString(R.string.title_and_description, ticker);
            final String text = "From Political App";

            Intent resultIntent = new Intent(ModuleFetchService.this, Main_BossClass.class);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(ModuleFetchService.this)
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setSmallIcon(R.drawable.ic_event_notification_icon)
                    .setContentTitle(NOTIFICATION_COUNT + " new notifications are available")
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setLargeIcon(picture)
                    .setNumber(NOTIFICATION_COUNT)
                    .setContentIntent(PendingIntent.getActivity(ModuleFetchService.this, requestCode,
                            resultIntent,
                            PendingIntent.FLAG_ONE_SHOT))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(text)
                            .setBigContentTitle(title)
                            .setSummaryText("Click to view the notification"))
                    .addAction(android.R.drawable.ic_menu_view, "View", null
                    )
                    .addAction(android.R.drawable.ic_menu_close_clear_cancel, "DISMISS", null)
                    .setAutoCancel(true);

            builder.setDefaults(Notification.DEFAULT_VIBRATE);

            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(TAG, requestCode, builder.build());
        }
        OrganizeData();
        return NOTIFICATION_COUNT;
    }


    //TODO other non-essentials are to be coded in this block nothing depending on the service should get past here please
    private String getCurrentDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DB, Locale.US);
        return dateFormat.format(System.currentTimeMillis());
    }

    private String getCurrentTime() {

        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT_UI_DB, Locale.US);
        return timeFormat.format(System.currentTimeMillis());
    }

    private String getDateTime() {
        return Long.toString(System.currentTimeMillis());
    }

    private void inputDataIntoNotifications(long id, String title, String summary){
        dbHandler_Messages=new _notification_db(mContext, Long.toString(id));
        dbHandler_Messages.open();
        dbHandler_Messages.insertReceiverDb(title,getCurrentTime());
    }

}