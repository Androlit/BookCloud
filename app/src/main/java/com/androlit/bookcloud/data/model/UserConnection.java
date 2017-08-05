package com.androlit.bookcloud.data.model;

/**
 * Created by rubel on 8/6/2017.
 */

public class UserConnection {
    private String name;
    private String messageId;
    private String senderId;
    private String lastMessage;
    private long timestamps;

    public UserConnection() {
        this(null, null, null, null, 0);
    }

    public UserConnection(String name, String messageId, String senderId, String lastMessage,
                          long timestamps) {
        this.name = name;
        this.messageId = messageId;
        this.senderId = senderId;
        this.lastMessage = lastMessage;
        this.timestamps = timestamps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(long timstamps) {
        this.timestamps = timstamps;
    }
}
