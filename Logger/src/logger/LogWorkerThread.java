package logger;

import threads.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogWorkerThread implements Runnable {

    private Message<String> msg;
    private LogConfig config;
    private String path;
    private String fileName;
    private String firstLine;
    private File logFile;
    private File archivePath;
    private String filePrefix;
    private String fileExtension;

    public LogWorkerThread(LogConfig config, String path, String fileName, String firstLine, Message<String> msg) {
        this.config = config;
        this.path = path;
        this.fileName = fileName;
        this.firstLine = firstLine;
        this.msg = msg;

        logFile = new File(path.concat(fileName));
        archivePath = new File(path.concat("archive\\"));

        filePrefix = fileName.substring(0, fileName.lastIndexOf("."));
        fileExtension = fileName.substring(fileName.lastIndexOf("."));

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
                appendLine(firstLine);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void appendLine(String s) {

        if (logFile.length() < config.getMaxLogSizeBytes()) {
            try {
                writeToLog(s);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        else {

            if (!archivePath.exists() && config.getOldLogsToKeep() > 0) {

                archivePath.mkdir();
                File old = new File(archivePath.toString() + "\\" + filePrefix + "_0" + fileExtension);
                logFile.renameTo(old);
                try {
                    logFile.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    writeToLog(firstLine);
                    writeToLog(s);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else if (archivePath.exists() && config.getOldLogsToKeep() > 0) {

                List<File> fl = new ArrayList<File>();

                for (int i = 0; i < config.getOldLogsToKeep(); i++) {
                    fl.add(new File(archivePath.toString() + "\\" + filePrefix + "_" + Integer.toString(i) + fileExtension));
                }

                if (fl.get(config.getOldLogsToKeep() - 1).exists()) {
                    fl.get(config.getOldLogsToKeep() - 1).delete();
                }

                for (int i = config.getOldLogsToKeep() - 1; i >= 0; i--) {

                    if (fl.get(i).exists()) {
                        fl.get(i).renameTo(new File(archivePath.toString() + "\\" + filePrefix + "_" + Integer.toString(i + 1) + fileExtension));
                    }
                }

                logFile.renameTo(new File(archivePath.toString() + "\\" + filePrefix + "_0" + fileExtension));
                try {
                    logFile.createNewFile();
                } catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    writeToLog(firstLine);
                    writeToLog(s);
                }catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    private void writeToLog(String s) throws IOException {

        BufferedWriter bw = null;

        try {
            FileWriter fstream = new FileWriter(logFile.toString(), true);
            bw = new BufferedWriter(fstream);
            Date date = new Date();
            bw.write(s + "\n");
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            if(bw != null) {
                bw.close();
            }
        }

    }

    @Override
    public void run() {

        while (true) {

            msg.waitUntilNotifiedOrListNotEmpty();

            while (!msg.isEmpty()) {
                appendLine(msg.getNextMsg());
            }
        }
    }
}
