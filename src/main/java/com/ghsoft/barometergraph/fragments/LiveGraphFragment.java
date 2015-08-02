package com.ghsoft.barometergraph.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ghsoft.barometergraph.R;
import com.ghsoft.barometergraph.data.TransformHelper;
import com.ghsoft.barometergraph.service.BarometerDataService;
import com.ghsoft.barometergraph.views.BarometerDataGraph;
import com.ghsoft.barometergraph.views.DataOptions;
import com.ghsoft.barometergraph.views.DataRecording;

/**
 * Created by brian on 7/21/15.
 */
public class LiveGraphFragment extends Fragment implements BarometerDataGraph.BarometerDataGraphCallbacks, CheckBox.OnCheckedChangeListener,
        DataOptions.DataOptionsEvents, DataRecording.RecordingOptionsEvents {

    private static final String SAVE_EXPAND_RECORDING = "mDataRecording";
    private static final String SAVE_EXPAND_DATA_OPTIONS = "mDataOptions";
    private static final String FLOAT_FORMAT = "%.4f";

    private BarometerDataService mService;
    private LayoutInflater mInflater;
    private LiveGraphFragmentEvents mEvents;
    private FrameLayout mChartContainer;
    private BarometerDataGraph mChart;
    private CheckBox mAutoScroll;
    private TextView mUnitView;
    private DataOptions mDataOptions;
    private DataRecording mDataRecording;

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

        mDataRecording = (DataRecording) v.findViewById(R.id.recording_expander_cont);
        mDataRecording.setEventHandler(this);

        if (savedInstanceState != null) {
            mDataOptions.expand(savedInstanceState.getBoolean(SAVE_EXPAND_DATA_OPTIONS));
            mDataRecording.expand(savedInstanceState.getBoolean(SAVE_EXPAND_RECORDING));
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SAVE_EXPAND_DATA_OPTIONS, mDataOptions.isExpanded());
        outState.putBoolean(SAVE_EXPAND_RECORDING, mDataRecording.isExpanded());
        super.onSaveInstanceState(outState);
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
        setRecordState();
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

    @Override
    public boolean onRecordRequest(boolean keepBuffer) {
        if (mService.isRecording()) {
            showRenameDialog(mService.stopRecording());
            mDataOptions.setDataOptionsEnabled(true);
            return false;
        } else {
            mService.startRecording(keepBuffer);
            mDataOptions.setDataOptionsEnabled(false);
            return true;
        }
    }

    private void setRecordState() {
        if (mService.isRecording()) {
            mDataRecording.setRecordingState(true);
            mDataOptions.setDataOptionsEnabled(false);
        } else {
            mDataRecording.setRecordingState(false);
            mDataOptions.setDataOptionsEnabled(true);
        }
    }

    @Override
    public void onSaveRequest() {
        mService.startRecording(true);
        showRenameDialog(mService.stopRecording());
    }

    public void showRenameDialog(String oldname) {
        View promptsView = mInflater.inflate(R.layout.dialog_rename_file, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);

        final EditText fileName = (EditText) promptsView.findViewById(R.id.file_name);
        fileName.setText(oldname);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mService.finalizeRecording(fileName.getText().toString());
                                dialog.dismiss();
                            }
                        }
                ).create().show();
    }

}
