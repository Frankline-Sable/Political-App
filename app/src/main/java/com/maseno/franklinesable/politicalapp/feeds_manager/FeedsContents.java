package com.maseno.franklinesable.politicalapp.feeds_manager;

/**
 * Created by Frankline Sable on 09/02/2017.
 */

public class FeedsContents {
    private String headLine, summary, posterTime,feedImg;
    int pid, hiddenState;

    public FeedsContents(String headLine, String summary, String posterTime, String feedImg, int pid,int hiddenState) {
        this.headLine = headLine;
        this.summary = summary;
        this.posterTime = posterTime;
        this.feedImg = feedImg;
        this.pid= pid;
        this.hiddenState=hiddenState;
    }

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPosterTime() {
        return posterTime;
    }

    public void setPosterTime(String posterTime) {
        this.posterTime = posterTime;
    }

    public String getFeedImg() {
        return feedImg;
    }

    public void setFeedImg(String feedImg) {
        this.feedImg = feedImg;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getHiddenState() {
        return hiddenState;
    }

    public void setHiddenState(int hiddenState) {
        this.hiddenState = hiddenState;
    }
}
