package com.example.michaelpenberthy.messaging_android;

import java.security.Timestamp;

/**
 * Created by michaelpenberthy on 9/1/16.
 */
public class Message {
    private String senderId;

    private String text;

    public Message() {
    }

    public Message(String senderId,String text) {
        this.senderId = senderId;

        this.text = text;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
