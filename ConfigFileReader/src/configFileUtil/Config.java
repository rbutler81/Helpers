package configFileUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    private Map<String,List<String>> params;

    public Map<String, List<String>> getParams() {
        return params;
    }

    private Config(Map<String,List<String>> params) {
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

    public Integer getSingleParamAsInt(String s) {
        return getParamAsInt(s).get(0);
    }

    public String getSingleParamAsString(String s) { return params.get(s).get(0); }

    public static Config readIniFile(String str) {

        Map<String,List<String>> configParams = new HashMap<>();
        try {

            BufferedReader br = new BufferedReader(new FileReader(str));
            String line = br.readLine();
            List<String> p = new ArrayList<>();


            while (line != null) {
                line = br.readLine();

                if ((line != null) && line.startsWith("#")) {

                    line = line.substring(1);
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
            e.printStackTrace();
        }

        return new Config(configParams);

    }

}
