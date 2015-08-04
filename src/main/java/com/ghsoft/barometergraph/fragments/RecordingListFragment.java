package com.ghsoft.barometergraph.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;

import com.ghsoft.barometergraph.views.IRecordingDataEvents;
import com.ghsoft.barometergraph.views.RecordingListAdapter;

/**
 * Created by brian on 8/2/15.
 */
public class RecordingListFragment extends ListFragment {

    private IRecordingDataEvents mRecordingDataEvents;
    private RecordingListAdapter mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mRecordingDataEvents = (IRecordingDataEvents) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()  + " must implement IRecordingDataEvents");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new RecordingListAdapter(getActivity(), mRecordingDataEvents);
        setListAdapter(mAdapter);
    }

    public void referesh() {
        mAdapter.refresh();
    }
}
