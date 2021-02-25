package com.example.mlem;

// Message.java
public class Message {

    private String text; // message body
    private String senderId; // data of the user that sent this message
    private String senderName;
    private String timestamp;
    //@Exclude
    //private boolean belongsToCurrentUser;

    public Message() {
        this.text = "kalispera";
    }

    public Message(String text) {
        this.text = text;
    }


    public Message(String text, String senderId, String senderName, String timestamp) {
        this.text = text;
        this.senderId = senderId;
        this.senderName = senderName;
        this.timestamp = timestamp;
    }


    /*
    public Message(String text, String senderId, String senderName,String timestamp,boolean belongsToCurrentUser) {
        this.text = text;
        this.senderId = senderId;
        this.senderName = senderName;
        this.timestamp = timestamp;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }*/


    public String getText() {
        return text;
    }

    public String getSender() {
        return senderId;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getSenderName() {
        return this.senderName;
    }

/*
    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public void setBelongsToCurrentUser(boolean belongsToCurrentUser) {
        this.belongsToCurrentUser = belongsToCurrentUser;
    }*/
}

