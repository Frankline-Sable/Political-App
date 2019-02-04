package com.maseno.franklinesable.politicalapp.notifications_package;

/**
 * Created by Frankline Sable on 26/02/2017. from 18:48
 * at Maseno University @ Home
 * Project PoliticalAppEp
 */

public class _notification_contents {
    private String senderTime, senderMessage, recipientTime, recipientMessage, otherAdditional;
    private int messageId;
    private Boolean sentStatus;

    public _notification_contents(String senderTime, String senderMessage, String recipientTime, String recipientMessage, String otherAdditional, int messageId, Boolean sentStatus) {
        this.senderTime = senderTime;
        this.senderMessage = senderMessage;
        this.recipientTime = recipientTime;
        this.recipientMessage = recipientMessage;
        this.otherAdditional = otherAdditional;
        this.messageId = messageId;
        this.sentStatus = sentStatus;
    }

    public String getSenderTime() {
        return senderTime;
    }

    public void setSenderTime(String senderTime) {
        this.senderTime = senderTime;
    }

    public String getSenderMessage() {
        return senderMessage;
    }

    public void setSenderMessage(String senderMessage) {
        this.senderMessage = senderMessage;
    }

    public String getRecipientTime() {
        return recipientTime;
    }

    public void setRecipientTime(String recipientTime) {
        this.recipientTime = recipientTime;
    }

    public String getRecipientMessage() {
        return recipientMessage;
    }

    public void setRecipientMessage(String recipientMessage) {
        this.recipientMessage = recipientMessage;
    }

    public String getOtherAdditional() {
        return otherAdditional;
    }

    public void setOtherAdditional(String otherAdditional) {
        this.otherAdditional = otherAdditional;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public Boolean getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(Boolean sentStatus) {
        this.sentStatus = sentStatus;
    }
}
