package csvUtils;

import java.util.HashMap;
import java.util.Map;

public class CSVLines {

	private Map<Integer, String[]> lines;
	
	public CSVLines(){ lines = new HashMap<Integer, String[]>(); };
	
	public CSVLines(Map<Integer, String[]> lines){
		this.lines = lines;
	}
	
	public void setLines(Map<Integer, String[]> lines) {
		this.lines = lines;
	}
	
	public void setLine(int l, String[] lines) {
		this.lines.put(l, lines);
	}
	
	public Map<Integer, String[]> getLines(){
		return lines;
	}
	
	public String[] getLine(int l) {
		if (lines.containsKey(l)) return lines.get(l);
		else return null;
	}
	
	public int getNumberOfLines(){
		return lines.entrySet().size();
	}
}
