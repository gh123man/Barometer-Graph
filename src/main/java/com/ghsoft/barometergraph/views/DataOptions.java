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
public class DataOptions extends LinearLayout implements NumberPicker.OnValueChangeListener {

    private LayoutInflater mInflater;
    private LinearLayout mRootView;
    private NumberPicker mPicker;
    private DataOptionsEvents mEvents;

    public interface DataOptionsEvents {
        void onRunningAverageChange(int val);
    }

    public DataOptions(Context context) {
        super(context);
        setupView(context);
    }

    public DataOptions(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public void setEventHandler(DataOptionsEvents events) {
        mEvents = events;
    }

    public void setupView(Context context) {
        mInflater = LayoutInflater.from(context);
        mRootView = (LinearLayout) mInflater.inflate(R.layout.data_options_view, this);

        ExpanderView ev = (ExpanderView) mRootView.findViewById(R.id.expander);
        ev.setExpandButton(R.layout.expand_button);

        mPicker = (NumberPicker) mRootView.findViewById(R.id.picker);
        setupPicker();

    }

    private void setupPicker() {
        mPicker.setMaxValue(1000);
        mPicker.setMinValue(1);
        mPicker.setOnValueChangedListener(this);
    }

    public void setAverageSize(int averageSize) {
        mPicker.setValue(averageSize);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (mEvents != null)
            mEvents.onRunningAverageChange(newVal);
    }
}
