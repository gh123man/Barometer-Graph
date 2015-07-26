package com.ghsoft.barometergraph.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.ghsoft.barometergraph.data.BarometerDataPoint;
import com.ghsoft.barometergraph.data.IDataReceiver;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.LinkedList;

/**
 * Created by brian on 7/26/15.
 */
public class BarometerDataGraph extends LineChart implements IDataReceiver {

    private static final int DEFAULT_DATA_LIMIT = 10000;

    private int mDataLimit;

    public BarometerDataGraph(Context context) {
        super(context);
        setupView(context);
    }

    public BarometerDataGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public BarometerDataGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    private void setupView(Context context) {
        mDataLimit = DEFAULT_DATA_LIMIT;
        LineData data = new LineData();
        setData(data);
        getAxisRight().setStartAtZero(false);
        getAxisRight().setDrawLabels(false);
        getAxisLeft().setStartAtZero(false);
        setAutoScaleMinMaxEnabled(true);
    }

    private LineDataSet getDataSet(LineData data) {
        LineDataSet set = data.getDataSetByIndex(0);

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }
        return set;
    }

    private void scrollToFront(LineData data) {
        setVisibleXRangeMaximum(150);
        moveViewToX(data.getXValCount() - 151);
    }

    @Override
    public void write(BarometerDataPoint dataPoint) {
        LineData lineData = getData();

        if (lineData == null) return;

        LineDataSet set = getDataSet(lineData);

        lineData.addXValue("" + dataPoint.getmTimeStamp());
        lineData.addEntry(new Entry(dataPoint.getValue(), set.getEntryCount()), 0);

        checkAndTrim();
        notifyDataSetChanged();
        scrollToFront(lineData);

    }

    @Override
    public void writeHistory(LinkedList<BarometerDataPoint> data) {

        LineData lineData = getData();

        if (data == null) return;

        LineDataSet set = getDataSet(lineData);

        for (BarometerDataPoint dataPoint : data) {
            lineData.addXValue("" + dataPoint.getmTimeStamp());
            lineData.addEntry(new Entry(dataPoint.getValue(), set.getEntryCount()), 0);
        }

        notifyDataSetChanged();

        scrollToFront(lineData);
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(2f);
        set.setDrawCircles(false);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    public void checkAndTrim() {
        LineData data = getData();
        Log.e("test", "" + data.getDataSetCount());
        while (data.getDataSetCount() > mDataLimit) {
            data.removeEntry(data.getDataSetCount() - 1, 0);
        }
    }

    public void setDataLengthLimit(int limit) {
        mDataLimit = limit;
    }
}
