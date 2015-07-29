package com.ghsoft.barometergraph.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ghsoft.barometergraph.R;
import com.ghsoft.barometergraph.service.BarometerDataService;
import com.ghsoft.barometergraph.views.BarometerDataGraph;
import com.ghsoft.barometergraph.views.DataOptions;
import com.ghsoft.barometergraph.views.TransformHelper;

/**
 * Created by brian on 7/21/15.
 */
public class LiveGraphFragment extends Fragment implements BarometerDataGraph.BarometerDataGraphCallbacks, CheckBox.OnCheckedChangeListener, DataOptions.DataOptionsEvents {

    private static final String FLOAT_FORMAT = "%.4f";

    private BarometerDataService mService;
    private LiveGraphFragmentEvents mEvents;
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
        View v = inflater.inflate(R.layout.fragment_live_graph, null);
        mChart = (BarometerDataGraph) v.findViewById(R.id.chart);
        mUnitView = (TextView) v.findViewById(R.id.unit_view);

        mChart.setCallbacks(this);
        mChart.setTransformFunction(TransformHelper.TO_PSI);

        mAutoScroll = (CheckBox) v.findViewById(R.id.auto_scroll);
        mAutoScroll.setChecked(mChart.getAutoScroll());
        mAutoScroll.setOnCheckedChangeListener(this);

        mDataOptions = (DataOptions) v.findViewById(R.id.data_options);
        mDataOptions.setEventHandler(this);

        return v;
   }

    public void setService(BarometerDataService service) {
        mService = service;
        mService.setDataReceiver(mChart);
        mDataOptions.setAverageSize(mService.getAverageSize());
    }

    @Override
    public void onRunningAverageChange(int val) {
        mService.setRunningAverageSize(val);
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
    public void onValueChanged(float value, String unit) {
        mUnitView.setText(String.format(FLOAT_FORMAT, value) + " " + unit);
    }

}
