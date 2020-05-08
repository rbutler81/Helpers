package logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogSetup {

	Message<String> msg;

	public LogSetup(LogConfig config, String path, String fileName, String firstLine) {

		msg = new Message<>();

		LogWorkerThread lwt = new LogWorkerThread(config, path, fileName, firstLine, msg);
		Thread t = new Thread(lwt);
		t.start();

	}

	public Message<String> getMsg() {
		return msg;
	}
}
