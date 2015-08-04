package com.ghsoft.barometergraph.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.ghsoft.barometergraph.R;
import com.ghsoft.barometergraph.data.TransformHelper;

/**
 * Created by brian on 7/28/15.
 */
public class DataOptions extends LinearLayout implements NumberPicker.OnValueChangeListener, CompoundButton.OnCheckedChangeListener, Spinner.OnItemSelectedListener, View.OnClickListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private LinearLayout mRootView;
    private NumberPicker mPicker;
    private CheckBox mAutoScale;
    private DataOptionsEvents mEvents;
    private Spinner mUnits;
    private Button mClearButton;
    private ExpanderView mExpander;
    private String mCurrentUnit;

    public interface DataOptionsEvents {
        void onRunningAverageChange(int val);
        void onAutoScaleChange(boolean enable);
        void onUnitChange(String unit);
        void onClearRequest();
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
        mContext = context;
        mCurrentUnit = null;
        mInflater = LayoutInflater.from(context);
        mRootView = (LinearLayout) mInflater.inflate(R.layout.data_options_view, this);

        mExpander= (ExpanderView)  mRootView.findViewById(R.id.expander);
        mExpander.setExpandtext("Data Options");

        mUnits = (Spinner) mRootView.findViewById(R.id.unit_picker);
        mUnits.setOnItemSelectedListener(this);
        mUnits.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, TransformHelper.UNITS));

        mClearButton = (Button) mRootView.findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(this);

        mPicker = (NumberPicker) mRootView.findViewById(R.id.picker);
        setupPicker();

        mAutoScale = (CheckBox) mRootView.findViewById(R.id.auto_scale);
        mAutoScale.setOnCheckedChangeListener(this);
    }

    private void setupPicker() {
        mPicker.setMaxValue(1000);
        mPicker.setMinValue(1);
        mPicker.setOnValueChangedListener(this);
    }

    public void setAverageSize(int averageSize) {
        mPicker.setValue(averageSize);
    }

    public void setAutoScale(boolean autoScale) {
        mAutoScale.setChecked(autoScale);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (mEvents != null)
            mEvents.onRunningAverageChange(newVal);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mEvents != null)
            mEvents.onAutoScaleChange(isChecked);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
        final ArrayAdapter<String> adapter = ((ArrayAdapter<String>) mUnits.getAdapter());
        if (mEvents != null && mCurrentUnit != null && !adapter.getItem(position).equals(mCurrentUnit)) {
            mEvents.onUnitChange(mUnits.getItemAtPosition(position).toString());
            mCurrentUnit = adapter.getItem(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void setUnit(String unit) {
        ArrayAdapter<String> adapter = ((ArrayAdapter<String>) mUnits.getAdapter());
        mUnits.setSelection((adapter).getPosition(unit));
        mCurrentUnit = unit;
    }


    public void setDataOptionsEnabled(boolean val) {
        mUnits.setEnabled(val);
        mPicker.setEnabled(val);
        mClearButton.setEnabled(val);
        if (val) {
            mRootView.findViewById(R.id.warning_view).setVisibility(View.GONE);
        } else {
            mRootView.findViewById(R.id.warning_view).setVisibility(View.VISIBLE);
        }
    }

    public boolean isExpanded() {
        return mExpander.getExpanded();
    }

    public void expand(boolean val) {
            mExpander.expandView(val);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.clear_button) {
            mEvents.onClearRequest();
        }
    }


}
