package com.cimcorp.configFile;

import com.cimcorp.misc.math.BD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    private Map<String,List<String>> params;
    private String iniFile = null;

    public Map<String, List<String>> getParams() {
        return params;
    }

    private Config(Map<String,List<String>> params, String iniFile) {
        this.iniFile = iniFile;
        this.params = params;
    }

    public boolean hasParam(String s) {
        return params.containsKey(s);
    }

    public List<String> getParam(String s) {
        return params.get(s);
    }

    public List<Integer> getParamAsInt(String s) {
        List<String> l = params.get(s);
        List<Integer> r = new ArrayList<>();
        for (String p : l) {
            r.add(Integer.parseInt(p));
        }
        return r;
    }

    public int getSingleParamAsInt(String s) {
        return getParamAsInt(s).get(0);
    }

    public int getSingleParamAsInt(String s, int from, int to) throws ParamRangeException {
        int value = getParamAsInt(s).get(0);

        if ((value < from) || (value > to)) {
            throw new ParamRangeException(s,from,value,to);
        } else {
            return value;
        }
    }

    public int getSingleParamAsInt(String s, int greaterThan) throws ParamRangeException {
        int value = getParamAsInt(s).get(0);

        if (value <= greaterThan) {
            throw new ParamRangeException(s,value,greaterThan);
        } else {
            return value;
        }
    }

    public List<Float> getParamAsFloat(String s) {
        List<String> l = params.get(s);
        List<Float> r = new ArrayList<>();
        for (String p : l) {
            r.add(Float.parseFloat(p));
        }
        return r;
    }

    public float getSingleParamAsFloat(String s) {
        return getParamAsFloat(s).get(0);
    }

    public float getSingleParamAsFloat(String s, float greaterThan) throws ParamRangeException {
        float value = getSingleParamAsFloat(s);

        if (value <= greaterThan) {
            throw new ParamRangeException(s,value,greaterThan);
        } else {
            return value;
        }
    }

    public List<Double> getParamAsDouble(String s) {
        List<String> l = params.get(s);
        List<Double> r = new ArrayList<>();
        for (String p : l) {
            r.add(Double.parseDouble(p));
        }
        return r;
    }

    public double getSingleParamAsDouble(String s) {
        return getParamAsDouble(s).get(0);
    }

    public double getSingleParamAsDouble(String s, double greaterThan) throws ParamRangeException {
        float value = getSingleParamAsFloat(s);

        if (value <= greaterThan) {
            throw new ParamRangeException(s,value,greaterThan);
        } else {
            return value;
        }
    }

    public List<BigDecimal> getParamAsBigDecimal(String s) {
        List<String> l = params.get(s);
        List<BigDecimal> r = new ArrayList<>();
        for (String p : l) {
            r.add(new BigDecimal(p));
        }
        return r;
    }

    public BigDecimal getSingleParamAsBigDecimal(String s) {
        return getParamAsBigDecimal(s).get(0);
    }

    public BigDecimal getSingleParamAsBigDecimal(String s, double greaterThan) throws ParamRangeException {

        BigDecimal value = getSingleParamAsBigDecimal(s);
        BigDecimal bdGreaterThan = new BigDecimal(greaterThan);

        if (BD.LEQ(value, bdGreaterThan)) {
            throw new ParamRangeException(s,value,greaterThan);
        } else {
            return value;
        }
    }

    public List<Boolean> getParamAsBool(String s) {
        List<String> l = params.get(s);
        List<Boolean> r = new ArrayList<>();
        for (String p : l) {
            r.add(Boolean.parseBoolean(p));
        }
        return r;
    }

    public boolean getSingleParamAsBool(String s) {
        return getParamAsBool(s).get(0);
    }

    public String getSingleParamAsString(String s) { return params.get(s).get(0); }

    public static Config readIniFile(String str) throws IOException {

        Map<String,List<String>> configParams = new HashMap<>();
        try {

            BufferedReader br = new BufferedReader(new FileReader(str));
            String line = br.readLine();
            List<String> p = new ArrayList<>();


            while (line != null) {
                line = br.readLine();

                if ((line != null) && !line.startsWith("#") && line.contains("=") && (line.length() > 1)) {

                    int equals = line.indexOf("=");
                    String key = line.substring(0, equals);
                    String value = line.substring(equals + 1);
                    List<String> values = new ArrayList<>();

                    if (value.indexOf(",") >= 0) {

                        String[] lineValues = value.split(",",-1);
                        for (int i = 0; i < Array.getLength(lineValues); i++) {
                            values.add(lineValues[i]);
                        }
                    }
                    else {
                        values.add(value);
                    }

                    configParams.put(key,values);
                }
            }
        }
         catch (Exception e) {
            throw e;
        }

        return new Config(configParams, str);

    }

}
