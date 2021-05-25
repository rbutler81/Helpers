package com.cimcorp.misc.helpers;

import java.util.Calendar;

public class Timeout {

    long startTime = 0;
    long timeoutDelay = 0;

    public Timeout(long timeoutDelay) {
        this.timeoutDelay = timeoutDelay;
        Calendar cal = Calendar.getInstance();
        startTime = cal.getTimeInMillis();
    }

    public boolean isTimedOut() throws TimeoutException {
        Calendar cal = Calendar.getInstance();
        long elapsedTime = cal.getTimeInMillis() - startTime;
        if (elapsedTime >= timeoutDelay) {
            throw new TimeoutException(timeoutDelay);
        } else {
            return false;
        }
    }
}
