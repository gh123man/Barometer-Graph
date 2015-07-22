package com.ghsoft.barometergraph.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghsoft.barometergraph.R;

/**
 * Created by brian on 7/21/15.
 */
public class LiveGraphFragment extends Fragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
           //setup event handlers
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()  + " must implement LoginEvents and IGlobalEvents");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_live_graph, null);

    Log.e("asfdasfdasfdasfd", "asfd");
//        LineChart chart = (LineChart) findViewById(R.id.chart);
//        LineData data = new LineData(new String[] {"1", "2", "3"});
//        chart.setData(data);
        return v;
    }
}
