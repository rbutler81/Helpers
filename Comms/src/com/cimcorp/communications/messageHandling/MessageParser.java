package com.cimcorp.communications.messageHandling;

public interface MessageParser {

    public void newMsg(String msg);
    public int getMsgId();
    public ReceivedMessageType getMsgType();
    public String getMsgData();
    public String generateNak();
    public String generateAck(int msgId);
}
