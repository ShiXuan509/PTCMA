package com.project;

public class Messages {

    String message;
    String senderName, receiverName;
    long timestamp;
    String currenttime;

    public Messages(){

    }

    public Messages(String message, String senderName, String receiverName, long timestamp, String currenttime) {
        this.message = message;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.timestamp = timestamp;
        this.currenttime = currenttime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }
}

