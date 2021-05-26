package com.cimcorp.communications.tcp;

import com.cimcorp.logger.LogUtil;
import com.cimcorp.logger.Logger;
import com.cimcorp.logger.LoggerBase;
import com.cimcorp.misc.helpers.ExceptionUtil;
import com.cimcorp.misc.helpers.Timeout;
import com.cimcorp.misc.helpers.TimeoutException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class TcpSendAndReceive {

    private String destinationIp;
    private int destinationPort;
    private Logger logger;
    private int timeout;
    private int retries;

    public TcpSendAndReceive(String destinationIp, int destinationPort, int timeout, int retries) {
        this.destinationIp = destinationIp;
        this.destinationPort = destinationPort;
        this.timeout = timeout;
        this.retries = retries + 1;
    }

    public TcpSendAndReceive(String destinationIp, int destinationPort, int timeout, int retries, LoggerBase lb, String instanceName) {
        this(destinationIp, destinationPort, timeout, retries);
        this.logger = new Logger(lb, instanceName);
    }

    public List<Integer> send(String dataToSend) {

        int attempt = 1;
        boolean msgSent = false;
        boolean msgReceived = false;
        List<Integer> bytesReceived = new ArrayList<>();

        while ((!msgSent || !msgReceived) && (attempt <= retries)) {

            Socket socket = null;
            msgSent = false;
            msgReceived = false;
            bytesReceived = new ArrayList<>();

            try {

                // send data over socket
                socket = new Socket(destinationIp, destinationPort);
                OutputStream toWrite = socket.getOutputStream();
                byte[] data = dataToSend.getBytes();
                toWrite.write(data);
                msgSent = true;
                LogUtil.checkAndLog("Sent: "
                        + dataToSend, logger);

                InputStream input = socket.getInputStream();
                Timeout timeoutTimer = new Timeout((long) timeout);
                // wait for a response
                while (!msgReceived) {

                    if (input.available() > 0) {
                        // once data starts arriving, consume it and put it into the list
                        while (input.available() > 0) {
                            bytesReceived.add(input.read());
                        }
                        msgReceived = true;
                    } else {
                        // if no data has arrived yet, put the thread to sleep then check again
                        Thread.sleep(500);
                        timeoutTimer.isTimedOut();
                    }
                }

            } catch (UnknownHostException e) {
                LogUtil.checkAndLog(ExceptionUtil.stackTraceToString(e), logger);
                LogUtil.checkAndLog("TcpSendAndReceive Could not Connect to "
                        + destinationIp + ", exiting routine",
                        logger);
                attempt = retries + 1;
            } catch (IOException e) {
                LogUtil.checkAndLog(ExceptionUtil.stackTraceToString(e), logger);
                LogUtil.checkAndLog("TcpSendAndReceive Could not Connect to "
                                + destinationIp + ", exiting routine",
                        logger);
                attempt = retries + 1;

            } catch (InterruptedException e) {
                LogUtil.checkAndLog(ExceptionUtil.stackTraceToString(e), logger);

            } catch (TimeoutException e) {
                LogUtil.checkAndLog(ExceptionUtil.stackTraceToString(e), logger);
                LogUtil.checkAndLog("TcpSendAndReceive Failed, Attempt " + attempt + " of " + retries, logger);
                attempt = attempt + 1;
            }

            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                LogUtil.checkAndLog(ExceptionUtil.stackTraceToString(e), logger);
            }

            // if a message wasn't, make sure the list is empty
            if (!msgReceived) {
                bytesReceived = new ArrayList<>();
            }

        }
        return bytesReceived;
    }

}
