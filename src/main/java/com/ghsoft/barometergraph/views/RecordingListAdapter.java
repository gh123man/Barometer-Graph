package com.ghsoft.barometergraph.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ghsoft.barometergraph.data.RecordingData;

import java.util.ArrayList;

/**
 * Created by brian on 8/2/15.
 */
public class RecordingListAdapter extends BaseAdapter {

    private ArrayList<RecordingData> mRecordingData;
    private Context mContext;
    private IRecordingDataEvents mRecordingDataEvents;


    public RecordingListAdapter(Context context, ArrayList<RecordingData> recordingData, IRecordingDataEvents recordingDataEvents) {
        mContext = context;
        mRecordingData = recordingData;
        mRecordingDataEvents = recordingDataEvents;
    }

    @Override
    public int getCount() {
        return mRecordingData.size();
    }

    @Override
    public RecordingData getItem(int position) {
        return mRecordingData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return new RecordingDataListItem(mContext, getItem(position), mRecordingDataEvents);
    }
}
