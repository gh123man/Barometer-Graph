package com.ghsoft.barometergraph.data;

import java.io.PrintWriter;

/**
 * Created by brian on 7/30/15.
 */
public class CSVWriter {

    private PrintWriter mWriter;

    public void setWriter(PrintWriter writer) {
        mWriter = writer;
    }

    public void writeRow(String[] row) {
        if (mWriter == null) return;

        String line = "";

        for (String s : row) {
            line += s + ",";
        }
        mWriter.println(line.substring(0, line.length() - 1));
    }

    public void finish() {
        mWriter.close();
    }


}
