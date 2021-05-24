package com.cimcorp.communications.messageHandling;

import com.cimcorp.misc.helpers.KeyValuePair;

public class KVPMessageParser implements MessageParser {

    static final String MESSAGE_HEADER = "msg";
    static final String ACK_HEADER = "ack";
    static final String NAK_HEADER = "nak";
    private static final String MSG_ERROR = "@@@@@";

    String msg = "";
    int msgId = -1;
    ReceivedMessageType msgType;
    String data = "";

    @Override
    public void newMsg(String msg) {

        this.msg = "";
        this.msg = msg;
        this.msgId = -1;
        this.data = "";

        String instructionTest = KeyValuePair.stringContainsKeyPairInt(msg, MESSAGE_HEADER, MSG_ERROR);
        String ackTest = KeyValuePair.stringContainsKeyPairInt(msg, ACK_HEADER, MSG_ERROR);
        String nakTest = KeyValuePair.stringContainsString(msg, NAK_HEADER, MSG_ERROR);

        // a new message instruction was received from the plc
        // received a new instruction
        if (!instructionTest.equals(MSG_ERROR)) {
            this.msgType = ReceivedMessageType.INSTRUCTION;
            this.msgId = Integer.parseInt(instructionTest);
            this.data = msg;
        // received an ACK
        } else if (!ackTest.equals(MSG_ERROR)) {
            this.msgId = Integer.parseInt(ackTest);
            this.msgType = ReceivedMessageType.ACK;
            this.data = msg;
        // received a NAK
        } else if (!nakTest.equals(MSG_ERROR)) {
            this.msgId = -1;
            this.msgType = ReceivedMessageType.NAK;
            this.data = msg;
        // could not parse the message
        } else {
            this.msgType = ReceivedMessageType.PARSING_ERROR;
            this.data = msg;
        }

    }

    public String generateNak() {
        return "{nak}";
    }

    public String generateAck(int msgId) {
        return "{ack:" + msgId + "}";
    }

    @Override
    public int getMsgId() {
        return msgId;
    }

    @Override
    public ReceivedMessageType getMsgType() {
        return msgType;
    }

    @Override
    public String getMsgData() {
        return data;
    }

    public String toString() {
        String r = "Received New Message " +
                msgType.toString() +
                " " +
                KeyValuePair.kVPToString("packet",data).toString();
        return r;

    }

}
