package com.ghsoft.barometergraph.data;

import java.io.Serializable;

/**
 * Created by brian on 7/24/15.
 */
public class BarometerDataPoint implements Serializable {

    private final float mValue;
    private final long mTimeStamp;

    public BarometerDataPoint(float value, long timestamp) {
        mValue = value;
        mTimeStamp = timestamp;
    }

    public float getValue() {
        return mValue;
    }

    public long getmTimeStamp() {
        return mTimeStamp;
    }
}
