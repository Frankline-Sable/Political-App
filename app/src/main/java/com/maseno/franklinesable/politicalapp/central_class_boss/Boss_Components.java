package com.maseno.franklinesable.politicalapp.central_class_boss;

import android.util.Log;

/**
 * Created by Frankline Sable on 05/02/2017.
 */

public class Boss_Components {
    protected String boss_Title, boss_Post;
    protected int post_Count, boss_Icon;

    public Boss_Components() {

    }

    public Boss_Components(String boss_Title,String boss_Post, int post_Count, int boss_Icon) {
        this.boss_Title = boss_Title;
        this.post_Count = post_Count;
        this.boss_Icon = boss_Icon;
        this.boss_Post = boss_Post;
    }

    public String getBoss_Title() {
        return boss_Title;
    }

    public void setBoss_Title(String boss_Title) {
        this.boss_Title = boss_Title;
    }

    public String getBoss_Post() {
        return boss_Post;
    }

    public void setBoss_Post(String boss_Post) {
        this.boss_Post = boss_Post;
    }

    public int getPost_Count() {
        return post_Count;
    }

    public void setPost_Count(int post_Count) {
        this.post_Count = post_Count;
    }

    public int getBoss_Icon() {
        return boss_Icon;
    }

    public void setBoss_Icon(int boss_Icon) {
        this.boss_Icon = boss_Icon;
    }
}
