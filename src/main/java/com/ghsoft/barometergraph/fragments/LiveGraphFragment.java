package com.ghsoft.barometergraph.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghsoft.barometergraph.R;
import com.ghsoft.barometergraph.data.BarometerDataPoint;
import com.ghsoft.barometergraph.data.IDataReceiver;
import com.ghsoft.barometergraph.service.BarometerDataService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.LinkedList;

/**
 * Created by brian on 7/21/15.
 */
public class LiveGraphFragment extends Fragment implements IDataReceiver {

    private BarometerDataService mService;
    private LiveGraphFragmentEvents mEvents;
    private LineChart mChart;
    private boolean mLiveWrite;

    public LiveGraphFragment() {
    }

    public interface LiveGraphFragmentEvents {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mLiveWrite = false;
            mEvents = (LiveGraphFragmentEvents) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()  + " must implement LoginEvents and IGlobalEvents");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_live_graph, null);
        mChart = (LineChart) v.findViewById(R.id.chart);
        LineData data = new LineData();
        //data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.getAxisRight().setStartAtZero(false);
        mChart.getAxisRight().setDrawLabels(false);
        mChart.getAxisLeft().setStartAtZero(false);
        mChart.setAutoScaleMinMaxEnabled(true);
       // mChart.setVisibleYRangeMaximum(1f, YAxis.AxisDependency.RIGHT);
        return v;
    }

    @Override
    public void writeHistory(LinkedList<BarometerDataPoint> data) {

        LineData lineData = mChart.getData();

        if (data != null) {

            LineDataSet set = lineData.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                lineData.addDataSet(set);
            }

            for (BarometerDataPoint dataPoint : data) {
                lineData.addXValue("" + dataPoint.getmTimeStamp());
                lineData.addEntry(new Entry(dataPoint.getValue(), set.getEntryCount()), 0);
            }

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(150);
            mChart.moveViewToX(lineData.getXValCount() - 151);
        }
        mLiveWrite = true;
    }

    @Override
    public void write(BarometerDataPoint dataPoint) {
        if (!mLiveWrite)
            return;

        LineData data = mChart.getData();

        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            // add a new x-value first
            data.addXValue("" + dataPoint.getmTimeStamp());
            data.addEntry(new Entry(dataPoint.getValue(), set.getEntryCount()), 0);

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(150);
            mChart.moveViewToX(data.getXValCount() - 151);
        }
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

    public void setService(BarometerDataService service) {
        mService = service;
        mService.setDataReceiver(this);
    }


}
