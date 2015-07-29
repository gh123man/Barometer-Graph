package com.ghsoft.barometergraph.data;

import java.util.ArrayList;

/**
 * Created by brian on 7/26/15.
 */
public class RunningAverage {

    private int mSize;
    private ArrayList<Float> mData;

    public RunningAverage(int size) {
        mSize = size;
        mData = new ArrayList<>();
    }

    public void put(float val) {
        while (mData.size() > mSize) {
            mData.remove(0);
        }
        mData.add(val);
    }

    public float get() {
        float sum = 0;

        for (float f : mData) {
            sum += f;
        }

        return sum / mData.size();
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
    }
}
