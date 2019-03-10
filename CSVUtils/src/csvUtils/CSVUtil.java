package csvUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {

	public static List<String[]> read(String file, String separator) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "";
		List<String[]> o = new ArrayList<String[]>();
		
		while ((line = br.readLine()) != null){
				String[] s = line.split(separator, -1);
				o.add(s);
		}
		br.close();
		
		return o;
	}
	
	public static String[] mergeElements(String[] l, String separator, String start, String end) {
		
		int s = Array.getLength(l);
		int first = 0;
		int last = 0;
		
		for (int i = 0; i < s; i++) {
			
			if (l[i].startsWith(start) && !l[i].endsWith(end)){
				first = i;
			}
			if (!l[i].startsWith(start) && l[i].endsWith(end)){
				last = i;
			}
		}
		
		if (first == last) return l;
		else if (last > first) {
			
			int d = last - first;
			int n = s - d;
			String[] r = new String[n];
			r[first] = "";
			
			for (int i = 0; i < s; i++) {
				if (i < first) r[i] = l[i];
				else if (i > last) r[i - d] = l[i];
				else {
					if (i < last) r[first] = r[first] + l[i] + separator;
					else r[first] = r[first] + l[i];
				}
			}
			
			return r;
		}
		else return l;
		
		
	}
	
	public static void write(List<String[]> c, String file, String separator) throws IOException, FileNotFoundException {
		
		BufferedWriter bw;
		
		bw = new BufferedWriter(new FileWriter(file));
		
		for (int i = 0; i < c.size(); i++){
			
			int arraySize = Array.getLength(c.get(i));
			String line = "";
			
			for (int j = 0; j < arraySize; j++){
				
				line = line + c.get(i)[j];
				if (j < (arraySize - 1)) line = line + separator;
			}
			
			line = line + "\r\n";
			bw.write(line);
		}
		bw.close();
	} 
	
	@SuppressWarnings("unchecked")
	public static void writeObject(List<? extends CSVWriter> l, String file, String separator) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		
		try{
		Class<?> cls = l.get(0).getClass();
		Object obj = cls.newInstance();
		Method m = cls.getMethod("toCSV", List.class);
		List<String[]> i = (List<String[]>) m.invoke(obj, l);
		write(i, file, separator);
		} catch (Exception e){}
		
	}
}
