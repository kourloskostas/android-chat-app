package com.example.mlem;

import com.google.firebase.database.Exclude;

public class Group {

    private String groupId;
    private String groupName;

    @Exclude
    private Message lastMessage;


    public Group() {
        this.groupId = "xxxxxx";
        this.groupName = "kalin usperan arxontes";
    }

    public Group(String groupId, String groupName, Message lastMessage) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.lastMessage = lastMessage;
    }

    public Group(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Message getLastMessage() {
        return this.lastMessage;
    }


    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
