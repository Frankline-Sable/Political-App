package com.maseno.franklinesable.politicalapp.events_manager;

/**
 * Created by Frankline Sable on 25/02/2017. from 14:52
 * at Maseno University @ Home
 * Project PoliticalAppEp
 */

public class ItemObject {
    private int id;
    private String title;
    private String description;

    public ItemObject(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public ItemObject(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
