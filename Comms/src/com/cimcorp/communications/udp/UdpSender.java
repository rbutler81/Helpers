package com.cimcorp.communications.udp;

import java.io.IOException;
import java.net.*;

public class UdpSender {

    private InetAddress serverAddress;
    private int port;

    public UdpSender(String destinationAddr, int port) throws IOException, UnknownHostException, SocketException {
        this.serverAddress = InetAddress.getByName(destinationAddr);
        this.port = port;
    }

    public void send(String s) throws IOException {

        DatagramSocket udpSocket = new DatagramSocket(this.port);

        DatagramPacket p = new DatagramPacket(s.getBytes(),
                s.getBytes().length,
                serverAddress,
                port);

        udpSocket.send(p);
        udpSocket.close();

        while (!udpSocket.isClosed()) {}

    }
}
