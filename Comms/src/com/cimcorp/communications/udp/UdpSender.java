package com.cimcorp.communications.udp;

import java.io.IOException;
import java.net.*;

public class UdpSender {

    private DatagramSocket udpSocket;
    private InetAddress serverAddress;
    private int port;

    public UdpSender(String destinationAddr, int port) throws IOException, UnknownHostException, SocketException {
        this.serverAddress = InetAddress.getByName(destinationAddr);
        this.port = port;
        udpSocket = new DatagramSocket(this.port);
    }

    public void send(String s) throws IOException {
        DatagramPacket p = new DatagramPacket(s.getBytes(),
                s.getBytes().length,
                serverAddress,
                port);

        this.udpSocket.send(p);

    }
}
