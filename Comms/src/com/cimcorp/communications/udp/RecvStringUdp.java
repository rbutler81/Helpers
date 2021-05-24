package com.cimcorp.communications.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class RecvStringUdp implements UdpReceiver {

    private DatagramSocket serverSocket;
    private int port;

    public RecvStringUdp(int port) throws SocketException {

        this.port = port;
        serverSocket = new DatagramSocket(port);

    }

    public String receive() throws IOException {
        return processPacketToString(receivePacket(port));
    }

    private String processPacketToString(DatagramPacket packet) {

        byte[] chars = new byte[packet.getLength()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = packet.getData()[i];
        }
        String r = new String(chars);

        return r;
    }

    private DatagramPacket receivePacket(int port) throws IOException {

        DatagramPacket packet = null;
        byte[] buffer = new byte[5000];
        packet = new DatagramPacket(buffer, buffer.length);
        serverSocket.receive(packet);

        return packet;
    }

}
