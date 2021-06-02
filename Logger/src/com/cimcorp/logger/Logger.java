package com.cimcorp.logger;

public class Logger {

    LoggerBase logBase;
    String instanceName = null;

   public Logger(LoggerBase logBase, String instanceName) {
        this.logBase = logBase;
        this.instanceName = instanceName;
    }

    public Logger(LoggerBase logBase) {
        this.logBase = logBase;
    }

    public Logger setInstanceName(String instanceName) {
        this.instanceName = instanceName;
        return this;
    }

    public void log(String s) {
       logBase.log(s, instanceName);
    }

    public void logAndPrint(String s) {
       logBase.logAndPrint(s, instanceName);
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void stop(){
       logBase.stopThread();
    }
}
