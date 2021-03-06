package com.cimcorp.communications.udp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class RecvObjectUdp<T> {

    private DatagramSocket serverSocket;
    private int port;

    public RecvObjectUdp(int port){

        this.port = port;
        try {
            serverSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public T receive() {
        return processPacketCastToObject(receivePacket(port));
    }

    private <T> T processPacketCastToObject(DatagramPacket packet) {

        byte[] recvData = new byte[packet.getLength()];
        for (int i = 0; i < packet.getLength(); i++) {
            recvData[i] = packet.getData()[i];
        }

        T r = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object o = ois.readObject();
            r = (T) o;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return r;
    }

    private DatagramPacket receivePacket(int port) {

        DatagramPacket packet = null;
        try {
            byte[] buffer = new byte[5000];
            packet = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return packet;
    }
}
