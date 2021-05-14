package misc.comms.functions.heartbeat;

public class HeartBeatStatus {

    boolean connected;

    public HeartBeatStatus(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public HeartBeatStatus setConnected(boolean connected) {
        this.connected = connected;
        return this;
    }

    @Override
    public String toString() {
        return "HeartBeatStatus{" +
                "connected=" + connected +
                '}';
    }
}
