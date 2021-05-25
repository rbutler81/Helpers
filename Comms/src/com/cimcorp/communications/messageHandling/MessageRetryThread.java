package com.cimcorp.communications.messageHandling;

import com.cimcorp.communications.threads.Message;
import com.cimcorp.logger.LogUtil;
import com.cimcorp.misc.helpers.ExceptionUtil;

public class MessageRetryThread implements Runnable {

    MessageEventData messageEventData;
    MessageHandler messageHandler;

    public MessageRetryThread(MessageEventData messageEventData, MessageHandler messageHandler) {
        this.messageEventData = messageEventData;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {

        try {

            Thread.sleep(messageEventData.getRetryDelay());

            int size = messageHandler.getMessagesWaitingForAck().size();
            Message<MessageEventData> unlocked = messageHandler.getMessagesWaitingForAck().lock();

            for (int i = 0; i < size; i++) {

                if (unlocked.getListWithoutLocking().get(i).getMsgId() == messageEventData.getMsgId()) {
                    int retries = unlocked.getListWithoutLocking().get(i).getRetryAttempts();
                    if (retries > 1) {
                        unlocked.getListWithoutLocking().get(i).setRetryAttempts(retries - 1);
                        messageHandler.addToEventQueue(messageEventData);
                        break;
                    } else {
                        unlocked.getListWithoutLocking().remove(i);
                        break;
                    }
                }
            }
            unlocked.unlock();

        } catch (Throwable t) {
            LogUtil.checkAndLog(ExceptionUtil.stackTraceToString(t),
                    messageHandler.getLogger());
        }

    }
}
