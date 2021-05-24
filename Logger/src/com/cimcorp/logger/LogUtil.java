package com.cimcorp.logger;

public class LogUtil {

    public static void checkAndLog(boolean loggerUsed, String s, Logger logger) {
        if (loggerUsed) {
            logger.logAndPrint(s);
        } else {
            System.out.println(s);
        }
    }

}
