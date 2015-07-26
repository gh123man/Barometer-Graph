package com.ghsoft.barometergraph.data;

import java.util.LinkedList;

/**
 * Created by brian on 7/24/15.
 */
public interface IDataReceiver {

    public void writeHistory(LinkedList<BarometerDataPoint> data);

    public void write(BarometerDataPoint dataPoint);
}
