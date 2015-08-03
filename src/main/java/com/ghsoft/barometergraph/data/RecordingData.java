package com.ghsoft.barometergraph.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by brian on 8/2/15.
 */
public class RecordingData implements Serializable {

    private File mFile;
    private LinkedList<BarometerDataPoint> mData;

    public RecordingData(File file) {
        mFile = file;
        mData = null;
    }

    public String getName() {
        return mFile.getName().substring(0, mFile.getName().length() - 4);
    }

    private void parseFile() throws IOException{
        // Construct BufferedReader from FileReader
        BufferedReader br = new BufferedReader(new FileReader(mFile));
        mData = new LinkedList<>();

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            mData.add(new BarometerDataPoint(Float.parseFloat(parts[1]), Long.parseLong(parts[0])));
        }

        br.close();
    }

    public LinkedList<BarometerDataPoint> getData() throws IOException{
        if (mData == null)  {
            parseFile();
        }
        return mData;
    }

}
