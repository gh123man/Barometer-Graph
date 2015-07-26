package com.ghsoft.barometergraph.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghsoft.barometergraph.R;
import com.ghsoft.barometergraph.service.BarometerDataService;
import com.ghsoft.barometergraph.views.BarometerDataGraph;

/**
 * Created by brian on 7/21/15.
 */
public class LiveGraphFragment extends Fragment {

    private BarometerDataService mService;
    private LiveGraphFragmentEvents mEvents;
    private BarometerDataGraph mChart;
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
        mChart = (BarometerDataGraph) v.findViewById(R.id.chart);
        return v;
   }

    public void setService(BarometerDataService service) {
        mService = service;
        mService.setDataReceiver(mChart);
    }


}
