package com.cimcorp.logger;

public class LogUtil {

    public static void checkAndLog(String s, Logger logger) {
        if (logger != null) {
            logger.logAndPrint(s);
        } else {
            System.out.println(s);
        }
    }

}
