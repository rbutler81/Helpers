package com.cimcorp.communications.messageHandling;

import com.cimcorp.communications.udp.RecvStringUdp;
import com.cimcorp.communications.udp.UdpObjectServerThread;
import com.cimcorp.communications.udp.UdpReceiver;
import com.cimcorp.logger.LogUtil;
import com.cimcorp.misc.helpers.ExceptionUtil;

public class UdpObjectServerMessageHandler extends UdpObjectServerThread {

    MessageHandler messageHandler;

    public UdpObjectServerMessageHandler(int port, MessageHandler messageHandler) {
        super(port);
        this.messageHandler = messageHandler;
        new Thread(this).start();
    }

    @Override
    public void run() {

        UdpReceiver objectReceiver = null;
        try {
            objectReceiver = new RecvStringUdp(port);
            LogUtil.checkAndLog(messageHandler.getLogger().getInstanceName() + " listening on port: " + port,
                    messageHandler.getLogger());
        } catch (Throwable t) {
            LogUtil.checkAndLog(ExceptionUtil.stackTraceToString(t),
                    messageHandler.getLogger());
        }

        while (true) {

            try {
                String receivedData = super.listenForObject(objectReceiver);
                messageHandler.receivedMsg(receivedData);
            } catch (Throwable t) {
                LogUtil.checkAndLog(ExceptionUtil.stackTraceToString(t),
                        messageHandler.getLogger());
            }
        }

    }
}
