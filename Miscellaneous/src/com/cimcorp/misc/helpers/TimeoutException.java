package com.cimcorp.misc.helpers;

public class TimeoutException extends Exception {


    public TimeoutException(long timeoutDelay) {
        super(Long.toString(timeoutDelay) + "ms Elapsed -- Timeout Occurred" );
    }
}
