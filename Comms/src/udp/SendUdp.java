package udp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendUdp {

    public boolean sendObjectTo(String addr, int port) {

        boolean r = false;

        try {
            // setup address to transmit to and socket
            InetAddress address = InetAddress.getByName(addr);
            DatagramSocket clientSocket = new DatagramSocket();

            // prepare data
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.flush();
            oos.writeObject(this);
            oos.flush();
            byte[] dataToSend = baos.toByteArray();
            oos.close();
            baos.close();
            // create datagram packet
            DatagramPacket packet = new DatagramPacket(dataToSend, dataToSend.length, address, port);

            // send data
            clientSocket.send(packet);

            r = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;

    }

}
