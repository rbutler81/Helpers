package com.cimcorp.communications.udp;

public class UdpCommunicationParameters {

    int localReceivingPort;
    int remoteReceivingPort;
    String remoteIp;
    int resendDelay;
    int resendAttempts;

    public UdpCommunicationParameters(int localReceivingPort, int remoteReceivingPort, String remoteIp, int resendDelay, int resendAttempts) {
        this.localReceivingPort = localReceivingPort;
        this.remoteReceivingPort = remoteReceivingPort;
        this.remoteIp = remoteIp;
        this.resendDelay = resendDelay;
        this.resendAttempts = resendAttempts;
    }

    public int getLocalReceivingPort() {
        return localReceivingPort;
    }

    public int getRemoteReceivingPort() {
        return remoteReceivingPort;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public int getResendDelay() {
        return resendDelay;
    }

    public int getResendAttempts() {
        return resendAttempts;
    }
}
