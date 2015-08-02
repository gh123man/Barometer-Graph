package com.ghsoft.barometergraph.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ghsoft.barometergraph.R;
import com.ghsoft.barometergraph.data.RecordingData;

/**
 * Created by brian on 8/2/15.
 */
public class RecordingDataListItem extends LinearLayout {

    private RecordingData mRecordingData;
    private LayoutInflater mInflater;
    private Context mContext;
    private View mRootView;

    public RecordingDataListItem(Context context, RecordingData recordingData) {
        super(context);
        mRecordingData = recordingData;
        setupView(context);
    }

    public RecordingDataListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public RecordingDataListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    public void setupView(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mRootView = mInflater.inflate(R.layout.view_recording_list_item, this);
        ((TextView) mRootView.findViewById(R.id.file_name)).setText(mRecordingData.getName());
    }

}
