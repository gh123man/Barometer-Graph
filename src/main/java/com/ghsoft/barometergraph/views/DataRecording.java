package com.ghsoft.barometergraph.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.ghsoft.barometergraph.R;

/**
 * Created by brian on 7/28/15.
 */
public class DataRecording extends LinearLayout implements View.OnClickListener {

    private LayoutInflater mInflater;
    private LinearLayout mRootView;
    private NumberPicker mPicker;
    private RecordingOptionsEvents mEvents;
    private Button mRecordButton;

    public interface RecordingOptionsEvents {
        boolean onRecordRequest();
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

    public void setRecordingState(boolean isRecording) {

    }

    public void setupView(Context context) {
        mInflater = LayoutInflater.from(context);
        mRootView = (LinearLayout) mInflater.inflate(R.layout.data_recording_view, this);

        ExpanderView ev = (ExpanderView) mRootView.findViewById(R.id.recording_expander);
        ev.setExpandtext("Recording");

        mRecordButton = (Button) mRootView.findViewById(R.id.record_button);

        mRecordButton.setOnClickListener(this);
    }

    private void recordRequest() {
        if (mEvents.onRecordRequest()) {
            mRecordButton.setText("Stop Recording");
        } else {
            mRecordButton.setText("Start Recording");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_button:
                recordRequest();
                break;
        }
    }

}
