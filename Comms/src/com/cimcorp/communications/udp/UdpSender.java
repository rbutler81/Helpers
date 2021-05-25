package com.cimcorp.communications.udp;

import com.cimcorp.logger.LogUtil;
import com.cimcorp.logger.Logger;
import com.cimcorp.logger.LoggerBase;
import com.cimcorp.misc.helpers.ExceptionUtil;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UdpSender {

    private InetAddress serverAddress;
    private int port;
    Logger logger;
    boolean isUsingLogger = false;

    public UdpSender(String destinationAddr, int port) throws UnknownHostException {
        this.serverAddress = InetAddress.getByName(destinationAddr);
        this.port = port;
    }

    public UdpSender(String destinationAddr, int port, LoggerBase lb) throws UnknownHostException {
        this(destinationAddr, port);
        this.isUsingLogger = true;
        Logger logger = new Logger(lb, "UdpSender");
    }

    public void send(String s) {

        DatagramSocket udpSocket = null;

        try {

            udpSocket = new DatagramSocket(this.port);

            DatagramPacket p = new DatagramPacket(s.getBytes(),
                    s.getBytes().length,
                    serverAddress,
                    port);

            udpSocket.send(p);

        } catch (Throwable t) {
            LogUtil.checkAndLog(ExceptionUtil.stackTraceToString(t),
                    logger);
        } finally {
            udpSocket.close();
        }

        while (!udpSocket.isClosed()) {}

    }
}
