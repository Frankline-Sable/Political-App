package com.maseno.franklinesable.politicalapp.events_manager;

import android.graphics.Color;

/**
 * Created by Frankline Sable on 10/02/2017.
 */

public class EventsContents {
    private String dayOfMonth,dayOfWeek,title,subtitle,timeView;
    private int eventColor,bellColor;

    public EventsContents(String dayOfMonth, String dayOfWeek, String title, String subtitle, String timeView, int eventColor,int bellColor) {
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        this.title = title;
        this.subtitle = subtitle;
        this.timeView = timeView;
        this.eventColor=eventColor;
        this.bellColor=bellColor;
    }

    public EventsContents(String title) {
        this.title = title;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTimeView() {
        return timeView;
    }

    public void setTimeView(String timeView) {
        this.timeView = timeView;
    }

    public int getEventColor() {
        return eventColor;
    }

    public void setEventColor(int eventColor) {
        this.eventColor = eventColor;
    }

    public int getBellColor() {
        return bellColor;
    }

    public void setBellColor(int bellColor) {
        this.bellColor = bellColor;
    }
}
