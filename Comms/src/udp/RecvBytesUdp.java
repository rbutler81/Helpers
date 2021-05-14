package udp;

import threads.Message;

import java.io.File;
import java.io.IOException;
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
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " listening on port: " + port);

        while (true) {

            byte[] receiveData = new byte[bytesToRecv];

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String r = new String(receivePacket.getData());

            msg.addMsg(r);
            synchronized (msg){
                msg.notify();
            }
        }

    }
}
