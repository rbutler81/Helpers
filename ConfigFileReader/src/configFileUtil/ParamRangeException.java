package configFileUtil;

public class ParamRangeException extends Exception {

    public ParamRangeException(String param, int from, int val, int to) {
        super("Parameter {" + param + ":" + val + "} must be between " + from + " - " + to);
    }

    public ParamRangeException(String param, int val, int smallest) {
        super("Parameter {" + param + ":" + val + "} must be larger than " + smallest);
    }
}
