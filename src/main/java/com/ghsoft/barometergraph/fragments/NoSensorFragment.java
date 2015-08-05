package com.ghsoft.barometergraph.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghsoft.barometergraph.R;

/**
 * Created by brian on 7/21/15.
 */
public class NoSensorFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_no_sensor, null);
        return v;
    }

}
