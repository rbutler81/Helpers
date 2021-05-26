package com.cimcorp.communications.messageHandling;

import com.cimcorp.misc.helpers.KeyValuePair;

public class MessageEventData {

    private MessageEvent event;
    private int msgId;
    private String data = "";
    private int retryDelay = 0;
    private int retryAttempts = 0;

    public MessageEventData(MessageEvent event, int msgId) {
        this.event = event;
        this.msgId = msgId;
    }

    public MessageEventData(MessageEvent event, int msgId, String data) {
        this.event = event;
        this.msgId = msgId;
        this.data = data;
    }

    public MessageEventData(MessageEvent event, String data){
        this.event = event;
        this.data = data;
    }

    public MessageEvent getEvent() {
        return event;
    }

    public int getMsgId() {
        return msgId;
    }

    public String getData() {
        return data;
    }

    public int getRetryDelay() {
        return retryDelay;
    }

    public MessageEventData setRetryDelay(int retryDelay) {
        this.retryDelay = retryDelay;
        return this;
    }

    public int getRetryAttempts() {
        return retryAttempts;
    }

    public MessageEventData setRetryAttempts(int retryAttempts) {
        this.retryAttempts = retryAttempts;
        return this;
    }

    public String toString() {
        String r = event.toString()
                + " "
                + KeyValuePair.kVPToString("msgId",msgId).toString();
        if (event == MessageEvent.DATA) {
            r = r + " " + data;
        }
        return r;
    }
}
