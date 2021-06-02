package com.cimcorp.logger;

import com.cimcorp.communications.threads.Message;
import com.cimcorp.misc.helpers.ExceptionUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

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
    private boolean stopThread = false;
    private ReentrantLock stopLock = new ReentrantLock(true);

    public LogWorkerThread(LogConfig config, Message<String> msg) throws IOException {
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
            logFile.createNewFile();
            List<String> ls = new LinkedList<>();
            ls.add(firstLine);
            appendLine(ls);
        }
    }

    public void appendLine(List<String> ls) throws IOException {

        if (logFile.length() < config.getMaxLogSizeBytes()) {
            writeToLog(ls);
        }

        else {

            if (!archivePath.exists() && config.getOldLogsToKeep() > 0) {

                archivePath.mkdir();
                File old = new File(archivePath.toString() + "\\" + filePrefix + "_0" + fileExtension);
                logFile.renameTo(old);
                logFile.createNewFile();

                writeToLog(firstLine);
                writeToLog(ls);

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
                logFile.createNewFile();

                writeToLog(firstLine);
                writeToLog(ls);

            }
        }
    }

    private void writeToLog(String s) throws IOException {

        FileWriter fStream = new FileWriter(logFile.toString(), true);
        BufferedWriter bw = new BufferedWriter(fStream);
        try {
            bw.write(s);
            bw.newLine();
        } catch (IOException e) {
            throw e;
        } finally {
            bw.close();
        }

    }

    private void writeToLog(List<String> ls) throws IOException {

        FileWriter fStream = new FileWriter(logFile.toString(), true);
        BufferedWriter bw = new BufferedWriter(fStream);
        try {
            int len = ls.size();
            for (int i = 0; i < len; i++) {
                bw.write(ls.remove(0));
                bw.newLine();
            }
        } catch (IOException e) {
                throw e;
        } finally {
            bw.close();
        }
    }

    public LogWorkerThread stopThread() {
        while (stopLock.isLocked()) {}
        stopLock.lock();
        this.stopThread = true;
        stopLock.unlock();
        return this;
    }

    public boolean isStopThread() {
        while (stopLock.isLocked()) {}
        stopLock.lock();
        boolean r = stopThread;
        stopLock.unlock();
        return r;
    }

    @Override
    public void run() {

        while (!isStopThread()) {

            try {
                msg.waitUntilNotifiedOrListNotEmpty();
                while (!msg.isEmpty()) {
                    List<String> stringsToLog = msg.removeAll();
                    appendLine(stringsToLog);
                }
            } catch (Throwable t) {
                msg.addMsg(ExceptionUtil.stackTraceToString(t));
            }
        }
    }
}
