package com.ghsoft.barometergraph.data;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by brian on 7/30/15.
 */
public class FileMan {

    public static final String ROOT_DIR = "BarometerGraph";
    public static final String TMP = ".tmp";

    private static File mSdCard;

    public FileMan() {
        mSdCard = Environment.getExternalStorageDirectory();
    }

    public static void clearTmp() {
        new Thread(new Runnable() {
            public void run() {
                File dir = new File(mSdCard.getAbsolutePath() + "/" + ROOT_DIR + "/" + TMP);
                for(File f: dir.listFiles()){
                    f.delete();
                }
                dir.delete();
            }
        }).start();
    }

    public static void delete(final String name) {
        new Thread(new Runnable() {
            public void run() {
                File dir = new File(mSdCard.getAbsolutePath() + "/" + ROOT_DIR + "/" + name);
                dir.delete();
            }
        }).start();
    }

    public File acquireTempFile() {
        checkDirExists();
        File dir = new File(mSdCard.getAbsolutePath() + "/" + ROOT_DIR + "/" + TMP);
        if (!dir.exists()) {
            dir.mkdir();
        }

        try {
            File tmp = File.createTempFile(ROOT_DIR, "csv", dir);
            return tmp;
        } catch (IOException e) {
            //fail
        }
        return null;

    }

    public void moveFromTemp(String newName, File oldFile) {
        File to = new File(mSdCard.getAbsolutePath() + "/" + newName);
        oldFile.renameTo(to);
    }

    private void checkDirExists() {
        File dir = new File(mSdCard.getAbsolutePath() + "/" + ROOT_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

}
