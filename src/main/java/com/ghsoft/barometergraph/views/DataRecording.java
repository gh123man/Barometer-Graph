package com.ghsoft.barometergraph.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ghsoft.barometergraph.R;

/**
 * Created by brian on 7/28/15.
 */
public class DataRecording extends LinearLayout implements View.OnClickListener {

    private LayoutInflater mInflater;
    private LinearLayout mRootView;
    private RecordingOptionsEvents mEvents;
    private Button mRecordButton;
    private Button mSave;
    private Button mSaveRecord;
    private ExpanderView mExpander;
    private Button mStopRecording;

    public interface RecordingOptionsEvents {
        boolean onRecordRequest(boolean keepBuffer);
        void onSaveRequest();
    }

    public DataRecording(Context context) {
        super(context);
        setupView(context);
    }

    public DataRecording(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public void setEventHandler(RecordingOptionsEvents events) {
        mEvents = events;
    }

    public void setupView(Context context) {
        mInflater = LayoutInflater.from(context);
        mRootView = (LinearLayout) mInflater.inflate(R.layout.data_recording_view, this);

        mExpander = (ExpanderView) mRootView.findViewById(R.id.recording_expander);
        mExpander.setExpandtext(getResources().getString(R.string.recording));

        mRecordButton = (Button) mRootView.findViewById(R.id.record_button);
        mRecordButton.setOnClickListener(this);

        mSave = (Button) mRootView.findViewById(R.id.save_history_button);
        mSave.setOnClickListener(this);

        mSaveRecord = (Button) mRootView.findViewById(R.id.save_record_button);
        mSaveRecord.setOnClickListener(this);

        mStopRecording = (Button) mRootView.findViewById(R.id.stop_recording);
        mStopRecording.setOnClickListener(this);

    }

    public void setRecordingState(boolean val) {
        if (val) {
            mRecordButton.setVisibility(View.GONE);
            mSave.setVisibility(View.GONE);
            mSaveRecord.setVisibility(View.GONE);
            mStopRecording.setVisibility(View.VISIBLE);

        } else {
            mStopRecording.setVisibility(View.GONE);
            mRecordButton.setVisibility(View.VISIBLE);
            mSave.setVisibility(View.VISIBLE);
            mSaveRecord.setVisibility(View.VISIBLE);
        }
    }

    private void recordRequest(boolean save) {
        setRecordingState(mEvents.onRecordRequest(save));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_button:
                recordRequest(false);
                break;

            case R.id.save_history_button:
                mEvents.onSaveRequest();
                break;

            case R.id.save_record_button:
                recordRequest(true);
                break;

            case R.id.stop_recording:
                //BOOL doesnt matter here because we expect it to be a stop recording request.
                recordRequest(false);
                break;
        }
    }

    public boolean isExpanded() {
        return mExpander.getExpanded();
    }

    public void expand(boolean val) {
        mExpander.expandView(val);
    }

}
