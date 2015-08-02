package com.ghsoft.barometergraph.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;

import com.ghsoft.barometergraph.data.FileMan;
import com.ghsoft.barometergraph.data.RecordingData;
import com.ghsoft.barometergraph.views.RecordingListAdapter;

import java.util.ArrayList;

/**
 * Created by brian on 8/2/15.
 */
public class RecordingListFragment extends ListFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FileMan fm = new FileMan();
        ArrayList<RecordingData> dataList = fm.getFileList();

        setListAdapter(new RecordingListAdapter(getActivity(), dataList));
    }
}
