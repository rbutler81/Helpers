package com.cimcorp.communications.messageHandling;

import com.cimcorp.communications.udp.RecvStringUdp;
import com.cimcorp.communications.udp.UdpObjectServerThread;
import com.cimcorp.communications.udp.UdpReceiver;
import com.cimcorp.logger.LogUtil;
import com.cimcorp.misc.helpers.ExceptionUtil;

public class UdpObjectServerMessageHandler extends UdpObjectServerThread {

    MessageHandler mh;

    public UdpObjectServerMessageHandler(int port, MessageHandler mh) {
        super(port);
        this.mh = mh;
        new Thread(this).start();
    }

    @Override
    public void run() {

        UdpReceiver objectReceiver = null;
        try {
            objectReceiver = new RecvStringUdp(port);
            LogUtil.checkAndLog(mh.getLogger().getInstanceName() + "listening on port: " + port,
                    mh.getLogger());
        } catch (Throwable t) {
            LogUtil.checkAndLog(ExceptionUtil.stackTraceToString(t),
                    mh.getLogger());
        }

        while (true) {

            try {
                String receivedData = super.listenForObject(objectReceiver);
                mh.receivedMsg(receivedData);
            } catch (Throwable t) {
                LogUtil.checkAndLog(ExceptionUtil.stackTraceToString(t),
                        mh.getLogger());
            }
        }

    }
}
