package logger;

public class LoggerInstance {

    LoggerBase logBase;
    String instanceName = null;

    public LoggerInstance(LoggerBase logBase, String instanceName) {
        this.logBase = logBase;
        this.instanceName = instanceName;
    }

    public LoggerInstance(LoggerBase logBase) {
        this.logBase = logBase;
    }

    public LoggerInstance setInstanceName(String instanceName) {
        this.instanceName = instanceName;
        return this;
    }

    public void log(String s) {
        logBase.log(s, instanceName);
    }

    public void logAndPrint(String s) {
        logBase.logAndPrint(s, instanceName);
    }
}
