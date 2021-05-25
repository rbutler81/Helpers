package com.cimcorp.communications.messageHandling;

import com.cimcorp.communications.threads.Message;
import com.cimcorp.communications.udp.UdpCommunicationParameters;
import com.cimcorp.communications.udp.UdpSender;
import com.cimcorp.logger.LogUtil;
import com.cimcorp.logger.Logger;
import com.cimcorp.logger.LoggerBase;
import com.cimcorp.misc.helpers.ExceptionUtil;
import com.cimcorp.misc.helpers.KeyValuePair;

import java.io.IOException;

public class MessageHandler implements Runnable {

    private static final String THREAD_NAME = "PlcMessageHandler";

    boolean synchronous;
    boolean responseRequired;
    boolean outputStringContainsMsgId;
    int localReceivingPort;
    int remoteReceivingPort;
    String remoteIp;
    int resendDelay;
    int resendAttempts;
    UdpSender udpSender;
    UdpObjectServerMessageHandler udpReceiver;
    Message<MessageEventData> events = new Message<>();
    Message<MessageEventData> receiveBufferFromRemote = new Message<>();
    Message<MessageEventData> messagesWaitingForAck = new Message<>();
    MessageParser messageParser;
    boolean usingLogger = false;
    LoggerBase lb = null;
    Logger logger = null;

    public MessageHandler(boolean synchronous, boolean responseRequired, boolean outputStringContainsMsgId, UdpCommunicationParameters cp, MessageParser messageParser) throws IOException {
        this.synchronous = synchronous;
        this.responseRequired = responseRequired;
        this.outputStringContainsMsgId = outputStringContainsMsgId;
        this.localReceivingPort = cp.getLocalReceivingPort();
        this.remoteReceivingPort = cp.getRemoteReceivingPort();
        this.remoteIp = cp.getRemoteIp();
        this.resendDelay = cp.getResendDelay();
        this.resendAttempts = cp.getResendAttempts();
        this.messageParser = messageParser;
        this.udpSender = new UdpSender(remoteIp, remoteReceivingPort);

        // start threads
        this.udpReceiver = new UdpObjectServerMessageHandler(localReceivingPort, this);
        new Thread(this, THREAD_NAME).start();
    }

    public MessageHandler(boolean synchronous, boolean responseRequired, boolean outputStringContainsMsgId, UdpCommunicationParameters cp, MessageParser messageParser, LoggerBase lb) throws IOException {
        this.synchronous = synchronous;
        this.responseRequired = responseRequired;
        this.outputStringContainsMsgId = outputStringContainsMsgId;
        this.localReceivingPort = cp.getLocalReceivingPort();
        this.remoteReceivingPort = cp.getRemoteReceivingPort();
        this.remoteIp = cp.getRemoteIp();
        this.resendDelay = cp.getResendDelay();
        this.resendAttempts = cp.getResendAttempts();
        this.lb = lb;
        this.logger = new Logger(lb, THREAD_NAME);
        this.usingLogger = true;
        this.messageParser = messageParser;
        this.udpSender = new UdpSender(remoteIp, remoteReceivingPort);

        // start threads
        this.udpReceiver = new UdpObjectServerMessageHandler(localReceivingPort, this);
        new Thread(this, THREAD_NAME).start();
    }

    public void sendAck(MessageEventData med) {
        events.addMsgAndNotify(new MessageEventData(MessageEvent.ACK,
                med.getMsgId(),
                med.getData()));
    }

    public void sendNak(MessageEventData med) {
        events.addMsgAndNotify(new MessageEventData(MessageEvent.NAK,
                med.getMsgId(),
                med.getData()));
    }

    public void sendMessage(String dataToSend, int msgId) {
        events.addMsgAndNotify((new MessageEventData(MessageEvent.DATA,
                msgId,
                dataToSend)));
    }

    public void addToEventQueue(MessageEventData med) {
        events.addMsgAndNotify(med);
    }

    public void receivedMsg(String data) {
        events.addMsgAndNotify(new MessageEventData(MessageEvent.RECEIVED_DATA, data));
    }

    @Override
    public void run() {

        while (true) {

            try {

                // wait for events from other threads
                events.waitUntilNotifiedOrListNotEmpty();

                //handle events based on their type
                MessageEventData newMessageEvent = events.getNextMsg();

                // new message received from the remote system
                if (newMessageEvent.getEvent() == MessageEvent.RECEIVED_DATA) {

                    handleReceivedMessage(newMessageEvent);

                } else if (newMessageEvent.getEvent() == MessageEvent.ACK) {

                    sendAckMsg(newMessageEvent);

                } else if (newMessageEvent.getEvent() == MessageEvent.NAK) {

                    sendNakMsg(newMessageEvent);

                } else if (newMessageEvent.getEvent() == MessageEvent.DATA) {

                    sendDataMsg(newMessageEvent);
                }
            } catch (Throwable t) {
                LogUtil.checkAndLog(ExceptionUtil.stackTraceToString(t),
                        logger);

            }
        }
    }

    private void sendDataMsg(MessageEventData newMessageEvent) throws Throwable {

        String toSend = newMessageEvent.getData();
        udpSender.send(toSend);

        LogUtil.checkAndLog("Sent New Message " + newMessageEvent.toString(),
                logger);

        if ((resendDelay > 0) && (resendAttempts > 0)) {
            int currentAttempt = newMessageEvent.getRetryAttempts();

            if ((currentAttempt < resendAttempts) && (currentAttempt != 0)) {
                newMessageEvent.setRetryAttempts(currentAttempt);
            } else {
                newMessageEvent.setRetryAttempts(resendAttempts);
                messagesWaitingForAck.addMsgAndNotify(newMessageEvent);
                newMessageEvent.setRetryDelay(resendDelay);
            }
            MessageRetryThread timer = new MessageRetryThread(newMessageEvent,this);
            new Thread(timer).start();
        }
    }

    private void sendNakMsg(MessageEventData newMessageEvent) throws Throwable {

        String toSend = messageParser.generateNak();
        udpSender.send(toSend);

        LogUtil.checkAndLog("Sent New Message " + newMessageEvent.toString(),
                logger);
    }

    private void sendAckMsg(MessageEventData newMessageEvent) throws Throwable {

        String toSend = messageParser.generateAck(newMessageEvent.getMsgId());
        udpSender.send(toSend);

        LogUtil.checkAndLog("Sent New Message " + newMessageEvent.toString(),
                logger);
    }

    private void handleReceivedMessage(MessageEventData newMessageEvent) throws Throwable {
        messageParser.newMsg(newMessageEvent.getData());
        ReceivedMessageType newMsg = messageParser.getMsgType();

        // received a command
        if (newMsg == ReceivedMessageType.INSTRUCTION) {

            LogUtil.checkAndLog(messageParser.toString(),
                    logger);

            receiveBufferFromRemote.addMsgAndNotify(new MessageEventData(MessageEvent.RECEIVED_DATA,
                    messageParser.getMsgId(),
                    messageParser.getMsgData()));

        // received an ACK
        } else if (newMsg == ReceivedMessageType.ACK) {

            LogUtil.checkAndLog(messageParser.toString(),
                    logger);

            // check and remove the ACK'd message from the sent message queue
            int size = messagesWaitingForAck.size();
            int msgId = Integer.parseInt(new KeyValuePair(newMessageEvent.getData()).getValue());
            Message<MessageEventData> unlocked = messagesWaitingForAck.lock();

            for (int i = 0; i < size; i++) {
                if (unlocked.getListWithoutLocking().get(i).getMsgId() == msgId) {
                    unlocked.getListWithoutLocking().remove(i);
                    break;
                }
            }
            messagesWaitingForAck.unlock();

        // received a NAK
        } else if (newMsg == ReceivedMessageType.NAK) {

            LogUtil.checkAndLog(messageParser.toString(),
                    logger);

        // parsing the message failed
        } else if (newMsg == ReceivedMessageType.PARSING_ERROR) {

            LogUtil.checkAndLog(messageParser.toString(),
                    logger);

            String toSend = messageParser.generateNak();
            udpSender.send(toSend);

        }
    }

    public Message<MessageEventData> getReceiveBufferFromRemote() {
        return receiveBufferFromRemote;
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean isUsingLogger() {
        return usingLogger;
    }

    public Message<MessageEventData> getEvents() {
        return events;
    }

    public Message<MessageEventData> getMessagesWaitingForAck() {
        return messagesWaitingForAck;
    }
}
