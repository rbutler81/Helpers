package logger;

public class LogConfig {

	private int maxLogSizeBytes;
	private int oldLogsToKeep;
	private String logFileName = null;
	private String path = null;
	private String topLine = null;
	private boolean useTimeStamp;
	
	public LogConfig(int maxLogSizeBytes, int oldLogsToKeep, String path, String fileName, String topLine, boolean useTimeStamp) {
		
		this.maxLogSizeBytes = maxLogSizeBytes;
		this.oldLogsToKeep = oldLogsToKeep;
		this.logFileName = fileName;
		this.topLine = topLine;
		this.useTimeStamp = useTimeStamp;
		this.path = path;

	}

	public int getMaxLogSizeBytes() {
		return maxLogSizeBytes;
	}

	public int getOldLogsToKeep() {
		return oldLogsToKeep;
	}

	public String getLogFileName() {
		return logFileName;
	}

	public String getPath() {
		return path;
	}

	public String getTopLine() {
		return topLine;
	}

	public boolean isUseTimeStamp() {
		return useTimeStamp;
	}

}
