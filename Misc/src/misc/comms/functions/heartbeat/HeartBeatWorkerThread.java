package misc.comms.functions.heartbeat;

import logger.LoggerBase;
import logger.Logger;
import threads.Message;
import udp.UdpObjectServerThread;


public class HeartBeatWorkerThread implements Runnable {

    Message<HeartBeatStatus> heartBeatStatusMsg;
    HeartBeatStatus heartBeatStatus;
    Message<UdpHeartBeat> heartBeatMsg;
    Logger log;
    String addr;
    int port;
    int timeout;

    public HeartBeatWorkerThread(String addr, int port, int timeout, LoggerBase lb) {
        this.addr = addr;
        this.port = port;
        this.timeout = timeout;
        this.heartBeatStatusMsg = new Message<>();
        heartBeatStatus = new HeartBeatStatus(false);
        this.heartBeatMsg = new Message<>();
        this.log = new Logger(lb);

    }

    public Logger getLog() {
        return log;
    }

    public Message<HeartBeatStatus> getHeartBeatStatusMsg() {
        return heartBeatStatusMsg;
    }

    private void setConnectedAndNotify(boolean connected) {
        heartBeatStatus.setConnected(connected);
        heartBeatStatusMsg.addMsgAndNotify(heartBeatStatus);
    }

    @Override
    public void run() {

        UdpObjectServerThread<UdpHeartBeat> receiverThread = new UdpObjectServerThread<>(port, heartBeatMsg);
        Thread t = new Thread(receiverThread);
        t.start();
        log.logAndPrint("Started HeartBeat Receiver on port: " + port);

        setConnectedAndNotify(false);

        while (true) {

            UdpHeartBeat incomingHeartBeat = new UdpHeartBeat(0, 0);

            // wait the timeout period for an incoming message
            log.logAndPrint("Waiting for incoming message");
            heartBeatMsg.waitUntilNotifiedOrListNotEmpty(timeout);

            // check if the incoming message queue is empty and set the connected bit
            if (heartBeatMsg.isEmpty()) {
                if (heartBeatStatus.isConnected()) {
                    setConnectedAndNotify(false);
                    log.logAndPrint("Disconnected");
                }
            } else {
                if (!heartBeatStatus.isConnected()) {
                    setConnectedAndNotify(true);
                    log.logAndPrint("Connected");
                }
            }

            // consume all the messages in the queue
            while (!heartBeatMsg.isEmpty()) {
                incomingHeartBeat = heartBeatMsg.getNextMsg();
                log.logAndPrint("Received: " + incomingHeartBeat.toString());
            }

            // check the incoming message - delay for a quarter of the timeout time (if connected)
            int incomingTimeout = incomingHeartBeat.getTimeout();
            if (heartBeatStatus.isConnected()) {
                incomingHeartBeat.getHeartBeatTime().waitForMillisPast(incomingTimeout / 4);
            }

            // send a heartbeat message
            UdpHeartBeat outgoing = new UdpHeartBeat(timeout);
            outgoing.sendTo(addr, port);
            log.logAndPrint("Sent: " + outgoing.toString());
        }
    }
}
