package configFileReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigFileReader {

    public static Map<String,List<String>> fromFile(String str) {

        Map<String,List<String>> configParams = new HashMap<>();

        try {

            BufferedReader br = new BufferedReader(new FileReader(str));
            String line = br.readLine();
            List<String> p = new ArrayList<>();


            while (line != null) {
                line = br.readLine();

                if (line.startsWith("#")) {

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

        return configParams;

    }

}
