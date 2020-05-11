package logger;

import threads.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerBase {

    protected SimpleDateFormat dateFormat = null;
    Message<String> msg;

    public LoggerBase(LogConfig config, String path, String fileName, String firstLine) {

        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

        msg = new Message<>();

        LogWorkerThread lwt = new LogWorkerThread(config, path, fileName, firstLine, msg);
        Thread t = new Thread(lwt);
        t.start();
    }

    public String log(String s, String instanceName) {
        String toLog = dateFormat.format(new Date());
        synchronized (msg) {

            if (instanceName != null) {
                toLog = toLog + " {" + instanceName + "}: " + s;
            } else {
                toLog = toLog + ": " + s;
            }
            msg.addMsg(toLog);
            msg.notify();
        }

        return toLog;
    }

    public void logAndPrint(String s, String instanceName) {
        System.out.println(log(s, instanceName));
    }

}
