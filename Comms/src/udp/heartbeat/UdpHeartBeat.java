package udp.heartbeat;

import misc.time.TimeInMillis;
import udp.SendUdp;

import java.io.Serializable;

public class UdpHeartBeat extends SendUdp implements Serializable {

    TimeInMillis heartBeatTime;
    int timeout;

    public UdpHeartBeat(int timeout) {
        this.heartBeatTime = new TimeInMillis();
        this.timeout = timeout;
    }

    public UdpHeartBeat(int timeout, long initialTime) {
        this.heartBeatTime = new TimeInMillis(initialTime);
        this.timeout = timeout;
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
