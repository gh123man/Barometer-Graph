package com.ghsoft.barometergraph.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ghsoft.barometergraph.data.FileMan;
import com.ghsoft.barometergraph.data.RecordingData;

import java.util.ArrayList;

/**
 * Created by brian on 8/2/15.
 */
public class RecordingListAdapter extends BaseAdapter implements RecordingDataListItem.DataListItemEvents {

    private ArrayList<RecordingData> mRecordingData;
    private Context mContext;
    private IRecordingDataEvents mRecordingDataEvents;
    private FileMan mFileManager;


    public RecordingListAdapter(Context context, IRecordingDataEvents recordingDataEvents) {
        mContext = context;
        mRecordingDataEvents = recordingDataEvents;

        mFileManager = new FileMan();
        mRecordingData = mFileManager.getFileList();
    }

    public void refresh() {
        mRecordingData = mFileManager.getFileList();
        notifyDataSetChanged();
    }

    public ArrayList<RecordingData> getData() {
        return mRecordingData;
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
        return new RecordingDataListItem(mContext, getItem(position), mRecordingDataEvents, this);
    }

    @Override
    public void onDelete() {
        refresh();
    }

    @Override
    public void onRename() {
        refresh();
    }
}
