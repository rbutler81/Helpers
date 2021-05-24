package com.cimcorp.configFile;

import java.math.BigDecimal;

public class ParamRangeException extends Exception {

    public ParamRangeException(String s, int from, int value, int to) {
        super("Parameter {" + s + ":" + value + "} must be between " + from + " - " + to);
    }

    public ParamRangeException(String s, int value, int greaterThan) {
        super("Parameter {" + s + ":" + value + "} must be larger than " + greaterThan);
    }

    public ParamRangeException(String s, float value, float greaterThan) {
        super("Parameter {" + s + ":" + value + "} must be larger than " + greaterThan);
    }

    public ParamRangeException(String s, float value, double greaterThan) {
        super("Parameter {" + s + ":" + value + "} must be larger than " + greaterThan);
    }

    public ParamRangeException(String s, BigDecimal value, double greaterThan) {
        super("Parameter {" + s + ":" + value.toString() + "} must be larger than " + greaterThan);
    }
}
