package com.cimcorp.logger;

import com.cimcorp.communications.threads.Message;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerBase {

    protected SimpleDateFormat dateFormat = null;
    private Boolean useTimeStamp;
    Message<String> msg;
    private LogWorkerThread lwt = null;

    public LoggerBase(LogConfig config) throws IOException {

        this.useTimeStamp = config.isUseTimeStamp();

        if (useTimeStamp) {
            this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        }

        msg = new Message<>();

        lwt = new LogWorkerThread(config, msg);
        Thread t = new Thread(lwt);
        t.start();
    }

    public Message<String> getMsg() {
        return msg;
    }

    public String log(String s, String instanceName) {

        String toLog = null;

        synchronized (msg) {

            if ((instanceName != null) & (useTimeStamp)) {
                toLog = dateFormat.format(new Date()) + " {" + instanceName + "}: " + s;
            } else if ((instanceName != null) & (!useTimeStamp)) {
                toLog = "{" + instanceName + "}: " + s;
            } else if ((instanceName == null) & (!useTimeStamp)) {
                toLog = s;
            } else if ((instanceName == null) & (useTimeStamp)) {
                toLog = dateFormat.format(new Date()) + ": " + s;
            }

            msg.addMsgAndNotify(toLog);

        }
        return toLog;
    }

    public void logAndPrint(String s, String instanceName) {
        System.out.println(log(s, instanceName));
    }

    public void stopThread() {
        lwt.stopThread();
    }

}
