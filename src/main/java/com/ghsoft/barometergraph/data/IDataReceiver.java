package com.ghsoft.barometergraph.data;

import java.util.LinkedList;

/**
 * Created by brian on 7/24/15.
 */
public interface IDataReceiver {

    void writeHistory(LinkedList<BarometerDataPoint> data);

    void write(BarometerDataPoint dataPoint);
}
