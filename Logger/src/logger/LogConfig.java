package logger;

public class LogConfig {

	private int maxLogSizeBytes;
	private int oldLogsToKeep;
	
	
	public LogConfig(int maxLogSizeBytes, int oldLogsToKeep) throws ConfigException {
		
		try {
			this.setMaxLogSizeBytes(maxLogSizeBytes);
			this.setOldLogsToKeep(oldLogsToKeep);
		}
		catch (ConfigException e) {
			throw e;
		}
		
	}

	public int getMaxLogSizeBytes() {
		return maxLogSizeBytes;
	}

	public LogConfig setMaxLogSizeBytes(int maxLogSizeBytes) throws ConfigException {
		
		if (maxLogSizeBytes > 0) {
			this.maxLogSizeBytes = maxLogSizeBytes;
			return this;
		}
		else {
			throw new ConfigException("maxLogSizeBytes must be greater than 0");
		}
	}
	public int getOldLogsToKeep() {
		return oldLogsToKeep;
	}

	public LogConfig setOldLogsToKeep(int oldLogsToKeep) throws ConfigException {
		
		if (oldLogsToKeep >= 1) {
			this.oldLogsToKeep = oldLogsToKeep;
			return this;
		}
		else {
			throw new ConfigException("oldLogsToKeep must be greater to, or equal to 1");
		}
	}
}
