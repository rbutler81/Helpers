package udp;

import threads.Message;

public class UdpObjectServerThread<T> implements Runnable {

	private Message<T> msg;
	private int port;

	public UdpObjectServerThread(int port, Message<T> msg) {

		this.port = port;
		this.msg = msg;
	}
	
	@Override
	public void run() {

		RecvObjectUdp<T> objectReceiver = new RecvObjectUdp<>(port);

		while (true) {
			T receivedObject = objectReceiver.receive();
			msg.addMsgAndNotify(receivedObject);
		}
	}
}
