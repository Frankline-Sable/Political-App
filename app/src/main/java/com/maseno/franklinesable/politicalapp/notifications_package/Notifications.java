package com.maseno.franklinesable.politicalapp.notifications_package;

/**
 * Created by Frankline Sable on 25/02/2017. from 01:13
 * at Maseno University @ Home
 * Project PoliticalAppEp
 */

public class Notifications {
    String name,title,summary,time;
    int flag;
    long notes_Id;

    public Notifications() {
    }

    public Notifications(String name, String title, String summary, String time, int flag, long id) {
        this.name = name;
        this.title = title;
        this.summary = summary;
        this.time = time;
        this.flag = flag;
        this.notes_Id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getNotes_Id() {
        return notes_Id;
    }

    public void setNotes_Id(int notes_Id) {
        this.notes_Id = notes_Id;
    }
}
