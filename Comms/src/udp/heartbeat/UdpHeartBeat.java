package udp.heartbeat;

import misc.time.TimeInMillis;
import udp.SendUdp;

import java.io.Serializable;

public class UdpHeartBeat extends SendUdp implements Serializable {

    private static final long serialVersionUID = 1L;

    TimeInMillis heartBeatTime;
    int timeout;

    public UdpHeartBeat(int timeout, long initialTime) {
        this(timeout);
        this.heartBeatTime = new TimeInMillis(initialTime);
    }

    public UdpHeartBeat(int timeout) {
        this.heartBeatTime = new TimeInMillis();
        this.timeout = timeout;
        this.msgId = 0;
    }

    public TimeInMillis getHeartBeatTime() {
        return heartBeatTime;
    }

    public int getTimeout() {
        return timeout;
    }

    public boolean sendTo(String addr, int port) {
        return sendObjectTo(addr, port);
    }

    @Override
    public String toString() {
        return "UdpHeartBeat{" +
                "heartBeatTime=" + heartBeatTime.getTime() +
                ", timeout=" + timeout +
                '}';
    }
}
