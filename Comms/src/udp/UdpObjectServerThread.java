package udp;

import logger.LogSetup;
import logger.Logger;
import logger.Message;
import udp.RecvObjectUdp;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpObjectServerThread<T> extends Logger implements Runnable {

	private Message<T> msg;
	private int port;

	
	public UdpObjectServerThread(int port, Message<T> msg, LogSetup ls) {
		super(ls);
		instanceName = "UdpServer";
		this.port = port;
		this.msg = msg;
	}
	
	@Override
	public void run() {

		RecvObjectUdp<T> objectReceiver = new RecvObjectUdp<>(port);

		while (true) {
			T receivedObject = objectReceiver.receive();
			synchronized (msg) {
				msg.addMsg(receivedObject);
				msg.notify();
			}
		}
	}
}
