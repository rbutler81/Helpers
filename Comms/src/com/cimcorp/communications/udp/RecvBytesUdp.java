package com.cimcorp.communications.udp;

import com.cimcorp.communications.threads.Message;
import com.cimcorp.misc.helpers.ExceptionUtil;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class RecvBytesUdp implements Runnable {

    private Message<String> msg;
    private int port;
    private int bytesToRecv;

    public RecvBytesUdp(int port, int bytesToRecv) {
        this.port = port;
        this.msg = new Message<>();
        this.bytesToRecv = bytesToRecv;
    }

    public Message<String> getMsg() {
        return msg;
    }

    @Override
    public void run() {

        DatagramSocket serverSocket = null;

        try {
            serverSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            msg.addMsg(ExceptionUtil.stackTraceToString(e));
        }

        if (serverSocket != null) {

            while (true) {

                try {

                    System.out.println(Thread.currentThread().getName() + " listening on port: " + port);

                    while (true) {

                        byte[] receiveData = new byte[bytesToRecv];

                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocket.receive(receivePacket);

                        byte[] b = new byte[receivePacket.getLength()];
                        for (int i = 0; i < receivePacket.getLength(); i++) {
                            b[i] = receivePacket.getData()[i];
                        }
                        String r = new String(b);

                        msg.addMsg(r);
                        synchronized (msg) {
                            msg.notify();
                        }
                    }
                } catch (Throwable t) {
                    msg.addMsg(ExceptionUtil.stackTraceToString(t));
                }
            }
        }

    }
}
