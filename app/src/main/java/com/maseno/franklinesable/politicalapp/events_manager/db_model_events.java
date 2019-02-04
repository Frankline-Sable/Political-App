package com.maseno.franklinesable.politicalapp.events_manager;

/**
 * Created by Frankline Sable on 25/03/2017.
 */

public class db_model_events {
    String title, description, dateCreated, timeCreated, color, scheduledDate, scheduledTime, monthReferee, yearReferee,serverBased, deleted;
    long db_id,server_id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getMonthReferee() {
        return monthReferee;
    }

    public void setMonthReferee(String monthReferee) {
        this.monthReferee = monthReferee;
    }

    public String getYearReferee() {
        return yearReferee;
    }

    public void setYearReferee(String yearReferee) {
        this.yearReferee = yearReferee;
    }

    public String getServerBased() {
        return serverBased;
    }

    public void setServerBased(String serverBased) {
        this.serverBased = serverBased;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public long getDb_id() {
        return db_id;
    }

    public void setDb_id(long db_id) {
        this.db_id = db_id;
    }

    public long getServer_id() {
        return server_id;
    }

    public void setServer_id(long server_id) {
        this.server_id = server_id;
    }
}
