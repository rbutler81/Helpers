package logger;

import threads.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerBase {

    protected SimpleDateFormat dateFormat = null;
    private Boolean useTimeStamp;
    Message<String> msg;

    public LoggerBase(LogConfig config) {

        this.useTimeStamp = config.isUseTimeStamp();

        if (useTimeStamp) {
            this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        }

        msg = new Message<>();

        LogWorkerThread lwt = new LogWorkerThread(config, msg);
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

}
