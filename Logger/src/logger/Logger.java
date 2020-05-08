package logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    protected LogSetup logSetup = null;
    protected SimpleDateFormat dateFormat = null;
    protected String instanceName = null;

    public Logger(){}

    public Logger(LogSetup l) {
        this.logSetup = l;
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    }

    public String log(String s) {
        if (logSetup != null) {
            String toLog = dateFormat.format(new Date());
            if (instanceName != null) {
                toLog = toLog + " {" + instanceName + "}: " + s;
            } else {
                toLog = toLog + ": " + s;
            }
            logSetup.getMsg().addMsg(toLog);
            return toLog;
        }
        return null;
    }

    public void logAndPrint(String s) {
        System.out.println(log(s));
    }

    public Logger setInstanceName(String instanceName) {
        this.instanceName = instanceName;
        return this;
    }
}
