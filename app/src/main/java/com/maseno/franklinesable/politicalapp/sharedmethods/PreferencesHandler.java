package com.maseno.franklinesable.politicalapp.sharedmethods;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Frankline Sable on 06/02/2017. Distribution
 */

public class PreferencesHandler {
    public static final String PREFS_PRIVATE = "PREFS_PRIVATE";
    public static final String PREFS_PRIVATE_COUNTS = "PREFS_FOR_MODULES_COUNT";
    private static final String KEY_PRIVATE_CONFIRMATION_INTERNET = "KEY_PRIVATE_CONFIRMATION_INTERNET";
    private static final String KEY_PRIVATE_CONFIRMATION = "KEY_PRIVATE_CONFIRMATION";
    private static final String KEY_PRIVATE_ACCOUNT = "KEY_PRIVATE_GET_ACCOUNT";
    private static final String KEY_HAS_LOGGED_IN_B4 = "KEY_HAS_LOGGED_IN_B4";
    private static final String KEY_EVENT_SPINNER_DAT = "KEY_EVENT_SPINNER_DAT";
    private static final String KEY_BOSS_LAYOUT_MANAGER = "KEY_BOSS_LAYOUT_MANAGER";
    private static final String FIRST_LAUNCH = "KEY_HAS_THE_APP_EVER_BEEN_LAUNCHED?";
    private static final String KEY_FEED_COUNT = "KEY_FEED_COUNT";
    private static final String KEY_EVENT_COUNT = "KEY_EVENT_COUNT";
    private static final String KEY_FULL_USERNAME = "KEY_FULL_USERNAME";
    private static final String KEY_NOTIFICATION_COUNT = "KEY_NOTIFICATIONS_COUNT";
    SharedPreferences modulePreferences;
    SharedPreferences.Editor modulePreferencesEditor;


    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private Context mContext;

    public PreferencesHandler(Context mContext) {
        this.mContext = mContext;
        sharedPrefs = mContext.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
        modulePreferences = mContext.getSharedPreferences(PREFS_PRIVATE_COUNTS, Context.MODE_PRIVATE);
    }

    public void SaveAccountState(Boolean NoAskAgain, Boolean getAccount) {
        prefsEditor = sharedPrefs.edit();
        prefsEditor.putBoolean(KEY_PRIVATE_CONFIRMATION, NoAskAgain);
        prefsEditor.putBoolean(KEY_PRIVATE_ACCOUNT, getAccount);
        prefsEditor.apply();
    }

    public void SaveAccountState(Boolean loggedOut) {
        prefsEditor = sharedPrefs.edit();
        prefsEditor.putBoolean(KEY_PRIVATE_ACCOUNT, loggedOut);
        prefsEditor.apply();
    }

    public Boolean getAccountState() {
        return sharedPrefs.getBoolean(KEY_PRIVATE_ACCOUNT, false);
    }

    public Boolean getRemState() {
        return sharedPrefs.getBoolean(KEY_PRIVATE_CONFIRMATION, true);
    }

    public int getLoggedInState() {
        return sharedPrefs.getInt(KEY_HAS_LOGGED_IN_B4, 0);
    }

    public void SaveLoggedInState(int i) {
        prefsEditor = sharedPrefs.edit();
        prefsEditor.putInt(KEY_HAS_LOGGED_IN_B4, i);
        prefsEditor.apply();
    }

    public Boolean getAskConnection() {
        return sharedPrefs.getBoolean(KEY_PRIVATE_CONFIRMATION_INTERNET, true);
    }

    public void saveAskConnection(Boolean saveAsk) {
        prefsEditor = sharedPrefs.edit();
        prefsEditor.putBoolean(KEY_PRIVATE_CONFIRMATION_INTERNET, saveAsk);
        prefsEditor.apply();
    }

    public void saveEventSpinner(int spin) {
        prefsEditor = sharedPrefs.edit();
        prefsEditor.putInt(KEY_EVENT_SPINNER_DAT, spin);
        prefsEditor.apply();
    }

    public int getEventSpinner() {
        return sharedPrefs.getInt(KEY_EVENT_SPINNER_DAT, 1);
    }

    public int getView() {
        return sharedPrefs.getInt(KEY_BOSS_LAYOUT_MANAGER, 2);
    }

    public void setView(int viewId) {
        prefsEditor = sharedPrefs.edit();
        prefsEditor.putInt(KEY_BOSS_LAYOUT_MANAGER, viewId);
        prefsEditor.apply();
    }


    public Boolean getFirstLaunch() {
        return sharedPrefs.getBoolean(FIRST_LAUNCH, false);
    }

    public void setFirstLaunch(Boolean state) {
        prefsEditor = sharedPrefs.edit();
        prefsEditor.putBoolean(FIRST_LAUNCH, state);
        prefsEditor.apply();
    }

    public String getFullName() {
        return sharedPrefs.getString(KEY_FULL_USERNAME, "user");
    }

    public void setFullName(String name) {
        sharedPrefs.edit().putString(KEY_FULL_USERNAME, name).apply();
    }

    //TODO: For modules events, feeds, e.t.c
    public void saveFeeds(int feedCount) {
        modulePreferences.edit().putInt(KEY_FEED_COUNT, feedCount).apply();
    }

    public int getFeeds() {
        return modulePreferences.getInt(KEY_FEED_COUNT, 0);
    }

    public void saveEvents(int eventCount) {
        modulePreferences.edit().putInt(KEY_EVENT_COUNT, eventCount).apply();
    }

    public int getEvents() {
        return modulePreferences.getInt(KEY_EVENT_COUNT, 0);
    }

    public void saveNotifications(int notificationCount) {
        modulePreferences.edit().putInt(KEY_NOTIFICATION_COUNT, notificationCount).apply();
    }

    public int getNotifications() {
        return modulePreferences.getInt(KEY_NOTIFICATION_COUNT, 0);
    }
}
