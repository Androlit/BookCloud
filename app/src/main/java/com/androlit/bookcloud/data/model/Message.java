package com.androlit.bookcloud.data.model;

/**
 * Created by rubel on 4/18/2017.
 */


public class Message {

    String content;
    String sender;
    String receiver;
    long timestamps;
    boolean isLeft;

    public Message(String content, String sender, String receiver, long timestamps) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamps = timestamps;
        isLeft = false;
    }

    public Message() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public long getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(long timestamps) {
        this.timestamps = timestamps;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }
}
