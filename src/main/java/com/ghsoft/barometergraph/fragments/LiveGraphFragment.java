package com.ghsoft.barometergraph.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ghsoft.barometergraph.R;
import com.ghsoft.barometergraph.data.TransformHelper;
import com.ghsoft.barometergraph.service.BarometerDataService;
import com.ghsoft.barometergraph.views.BarometerDataGraph;
import com.ghsoft.barometergraph.views.DataOptions;

/**
 * Created by brian on 7/21/15.
 */
public class LiveGraphFragment extends Fragment implements BarometerDataGraph.BarometerDataGraphCallbacks, CheckBox.OnCheckedChangeListener, DataOptions.DataOptionsEvents {

    private static final String FLOAT_FORMAT = "%.4f";

    private BarometerDataService mService;
    private LayoutInflater mInflater;
    private LiveGraphFragmentEvents mEvents;
    private FrameLayout mChartContainer;
    private BarometerDataGraph mChart;
    private CheckBox mAutoScroll;
    private TextView mUnitView;
    private DataOptions mDataOptions;

    public LiveGraphFragment() {
    }

    public interface LiveGraphFragmentEvents {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mEvents = (LiveGraphFragmentEvents) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()  + " must implement LoginEvents and IGlobalEvents");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        View v = mInflater.inflate(R.layout.fragment_live_graph, null);
        mChartContainer = (FrameLayout) v.findViewById(R.id.chart);
        setupChart();

        mUnitView = (TextView) v.findViewById(R.id.unit_view);

        mAutoScroll = (CheckBox) v.findViewById(R.id.auto_scroll);
        mAutoScroll.setOnCheckedChangeListener(this);
        mAutoScroll.setChecked(mChart.getAutoScroll());

        mDataOptions = (DataOptions) v.findViewById(R.id.data_options);
        mDataOptions.setEventHandler(this);
        mDataOptions.setAutoScale(true);

        return v;
   }

    private void setupChart() {
        mChart = new BarometerDataGraph(getActivity(), this);
        mChartContainer.removeAllViews();
        mChartContainer.addView(mChart);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    public void setService(BarometerDataService service) {
        mService = service;
        mDataOptions.setAverageSize(mService.getAverageSize());
        mService.setDataReceiver(mChart);
        mDataOptions.setUnit(mService.getUnit());
    }

    @Override
    public void onRunningAverageChange(int val) {
        mService.setRunningAverageSize(val);
    }

    @Override
    public void onAutoScaleChange(boolean enabled) {
        mChart.setAutoScale(enabled);
    }

    @Override
    public void onUnitChange(String unit) {
        mService.setTransformFunction(TransformHelper.fromUnit(unit));
        setupChart();
        mService.setDataReceiver(mChart);
    }

    @Override
    public void onAutoScrollChanged(boolean val) {
        mAutoScroll.setChecked(val);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mChart.setAutoScroll(isChecked);
    }

    @Override
    public void onValueChanged(float value) {
        mUnitView.setText(String.format(FLOAT_FORMAT, value) + " " + mService.getUnit());
    }

    @Override
    public String getUnit() {
        return mService.getUnit();
    }

}
