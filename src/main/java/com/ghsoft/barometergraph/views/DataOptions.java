package com.ghsoft.barometergraph.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
public class DataOptions extends LinearLayout implements NumberPicker.OnValueChangeListener, CompoundButton.OnCheckedChangeListener, Spinner.OnItemSelectedListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private LinearLayout mRootView;
    private NumberPicker mPicker;
    private CheckBox mAutoScale;
    private DataOptionsEvents mEvents;
    private Spinner mUnits;
    private int mUnitPos;

    public interface DataOptionsEvents {
        void onRunningAverageChange(int val);
        void onAutoScaleChange(boolean enable);
        void onUnitChange(String unit);
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
        mInflater = LayoutInflater.from(context);
        mRootView = (LinearLayout) mInflater.inflate(R.layout.data_options_view, this);

        ExpanderView ev = (ExpanderView)  mRootView.findViewById(R.id.expander);
        ev.setExpandButton(R.layout.expand_button);

        mUnits = (Spinner) mRootView.findViewById(R.id.unit_picker);
        mUnits.setOnItemSelectedListener(this);
        mUnitPos = 0;
        mUnits.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, TransformHelper.UNITS));

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
        if (mEvents != null && position != mUnitPos) {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(mContext.getString(R.string.reset_graph_warning)).setPositiveButton(mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mEvents.onUnitChange(mUnits.getItemAtPosition(position).toString());
                    mUnitPos = position;
                }
            }).setNegativeButton(mContext.getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mUnits.setSelection(mUnitPos);
                    dialog.dismiss();
                }
            }).show();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void setUnit(String unit) {
        ArrayAdapter<String> adapter = ((ArrayAdapter<String>) mUnits.getAdapter());
        mUnits.setSelection((adapter).getPosition(unit));
        mUnitPos = (adapter).getPosition(unit);
    }




}