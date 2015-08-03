package com.ghsoft.barometergraph.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ghsoft.barometergraph.R;
import com.ghsoft.barometergraph.data.RecordingData;
import com.ghsoft.barometergraph.views.BarometerDataGraph;

import java.io.IOException;

/**
 * Created by brian on 7/21/15.
 */
public class RecordedDataViewFragment extends Fragment implements BarometerDataGraph.BarometerDataGraphCallbacks {


    public static final String PACKAGE_DATA_KEY = "graph_data";

    private LayoutInflater mInflater;
    private FrameLayout mChartContainer;
    private BarometerDataGraph mChart;
    private RecordingData mData;
    private Context mContext;


    public RecordedDataViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mData = (RecordingData) getArguments().getSerializable(PACKAGE_DATA_KEY);

        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(mData.getName());

        mContext = getActivity();
        mInflater = inflater;
        View v = mInflater.inflate(R.layout.fragment_recorded_data, null);
        mChartContainer = (FrameLayout) v.findViewById(R.id.recorded_chart);

        new ParseFileHandler(this).execute();

        return v;
    }

    public RecordingData getData() {
        return mData;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void setupChart() {
        mChart = new BarometerDataGraph(getActivity(), this);
        mChartContainer.removeAllViews();
        mChartContainer.addView(mChart);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
        mChart.writeHistory(mData.getData());
    }


    @Override
    public void onAutoScrollChanged(boolean val) {
    }

    @Override
    public void onValueChanged(float value) {
    }

    @Override
    public String getUnit() {
        return null;
    }

    public class ParseFileHandler extends AsyncTask<RecordingData, Integer, Integer> {

        private RecordedDataViewFragment mRecordedDataViewFragment;

        public ParseFileHandler (RecordedDataViewFragment fragmnet) {
            mRecordedDataViewFragment = fragmnet;
        }


        @Override
        protected Integer doInBackground(RecordingData... params) {
            try {
                mRecordedDataViewFragment.getData().parseFile();
            } catch (IOException e) {

            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mRecordedDataViewFragment.setupChart();
        }
    }
}
