package com.ghsoft.barometergraph.data;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by brian on 7/30/15.
 */
public class FileMan {

    public static final String ROOT_DIR = "BarometerGraph";
    public static final String RECORDINGS = "Recordings";
    public static final String TMP = ".tmp";
    public static final String CSV = ".csv";

    private static File mSdCard;

    public FileMan() {
        mSdCard = Environment.getExternalStorageDirectory();
    }

    public static String getRootPath() {
        return mSdCard.getAbsolutePath() + "/" + ROOT_DIR;
    }

    public static String getTmpPath() {
        return getRootPath() + "/" + TMP;
    }

    public static String getRecordingPath() {
        return getRootPath() + "/" + RECORDINGS;
    }

    public static void clearTmp() {
        new Thread(new Runnable() {
            public void run() {
                File dir = new File(getTmpPath());
                if(dir.exists()) {
                    for (File f : dir.listFiles()) {
                        f.delete();
                    }
                    dir.delete();
                }
            }
        }).start();
    }

    public static void delete(final String name) {
        new Thread(new Runnable() {
            public void run() {
                File dir = new File(getRootPath() + "/" + name);
                dir.delete();
            }
        }).start();
    }

    public File acquireTempFile() {
        checkDirExists();
        File dir = new File(getTmpPath());
        if (!dir.exists()) {
            dir.mkdir();
        }

        try {
            File tmp = File.createTempFile(ROOT_DIR, CSV, dir);
            return tmp;
        } catch (IOException e) {
            //fail
        }
        return null;

    }

    public void moveFromTemp(String newName, File oldFile) {
        File dir = new File(getRecordingPath());
        if (!dir.exists()) {
            dir.mkdir();
        }

        File to = new File(getRecordingPath() + "/" + newName);
        oldFile.renameTo(to);
    }

    private void checkDirExists() {
        File dir = new File(getRootPath());
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public ArrayList<RecordingData> getFileList() {
        File dir = new File(getRecordingPath());
        ArrayList<RecordingData> dataList = new ArrayList<>();
        if(dir.exists()) {
            for (File f : dir.listFiles()) {
                if (f.getName().endsWith(CSV) ) {
                    dataList.add(new RecordingData(f));
                }
            }
        }
        return dataList;
    }

}
