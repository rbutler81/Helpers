package com.cimcorp.communications.udp;

import com.cimcorp.communications.threads.Message;
import com.cimcorp.misc.helpers.ExceptionUtil;

import java.io.IOException;

public class UdpObjectServerThread implements Runnable {

	protected Message<String> msg;
	protected int port;

	public UdpObjectServerThread(int port, Message<String> msg) {

		this.port = port;
		this.msg = msg;
	}

	public UdpObjectServerThread(int port) {
		this.port = port;
	}

	@Override
	public void run() {

		UdpReceiver receiver = null;
		try {
			receiver = new RecvStringUdp(port);
		} catch (Throwable t) {
			System.out.println(ExceptionUtil.stackTraceToString(t));
		}

		while (true) {

			try {
				String receivedObject = listenForObject(receiver);
				msg.addMsgAndNotify(receivedObject);
			} catch (Throwable t) {
				System.out.println(ExceptionUtil.stackTraceToString(t));
			}
		}
	}

	protected String listenForObject(UdpReceiver objectReceiver) throws IOException {
		String receivedString = objectReceiver.receive();
		return receivedString;
	}
}
