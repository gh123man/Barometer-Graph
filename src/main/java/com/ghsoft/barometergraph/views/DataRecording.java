package com.ghsoft.barometergraph.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.ghsoft.barometergraph.R;

/**
 * Created by brian on 7/28/15.
 */
public class DataRecording extends LinearLayout {

    private LayoutInflater mInflater;
    private LinearLayout mRootView;
    private NumberPicker mPicker;
//    private DataOptionsEvents mEvents;

//    public interface DataOptionsEvents {
//        void onRunningAverageChange(int val);
//    }

    public DataRecording(Context context) {
        super(context);
        setupView(context);
    }

    public DataRecording(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

//    public void setEventHandler(DataOptionsEvents events) {
//        mEvents = events;
//    }

    public void setupView(Context context) {
        mInflater = LayoutInflater.from(context);
        mRootView = (LinearLayout) mInflater.inflate(R.layout.data_recording_view, this);

        ExpanderView ev = (ExpanderView) mRootView.findViewById(R.id.recording_expander);
        ev.setExpandButton(R.layout.expand_button);

    }

}
