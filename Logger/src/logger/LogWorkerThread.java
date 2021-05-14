package logger;

import threads.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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

    public LogWorkerThread(LogConfig config, Message<String> msg) {
        this.config = config;
        this.path = config.getPath();
        this.fileName = config.getLogFileName();
        this.firstLine = config.getTopLine();
        this.msg = msg;

        logFile = new File(path.concat(fileName));
        archivePath = new File(path.concat("archive\\"));

        filePrefix = fileName.substring(0, fileName.lastIndexOf("."));
        fileExtension = fileName.substring(fileName.lastIndexOf("."));

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
                List<String> ls = new LinkedList<>();
                ls.add(firstLine);
                appendLine(ls);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void appendLine(List<String> ls) {

        if (logFile.length() < config.getMaxLogSizeBytes()) {
            try {
                writeToLog(ls);
            } catch (IOException e) {
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
                    writeToLog(ls);
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
                    writeToLog(ls);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void writeToLog(String s) throws IOException {

        BufferedWriter bw = null;

        try {
            FileWriter fStream = new FileWriter(logFile.toString(), true);
            bw = new BufferedWriter(fStream);
            bw.write(s + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bw != null) {
                bw.close();
            }
        }

    }

    private void writeToLog(List<String> ls) throws IOException {

        BufferedWriter bw = null;

        try {
            FileWriter fStream = new FileWriter(logFile.toString(), true);
            bw = new BufferedWriter(fStream);
            int len = ls.size();
            for (int i = 0; i < len; i++) {
                bw.write(ls.remove(0) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
                List<String> stringsToLog = msg.removeAll();
                appendLine(stringsToLog);
            }
        }
    }
}
