package com.ghsoft.barometergraph.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ghsoft.barometergraph.R;
import com.ghsoft.barometergraph.data.BarometerDataPoint;
import com.ghsoft.barometergraph.data.IDataReceiver;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by brian on 7/26/15.
 */
public class BarometerDataGraph extends LineChart implements IDataReceiver {

    private static final String FLOAT_FORMAT = "%.3f";
    private static final int VISIBLE_POINTS = 200;

    private boolean mLockAutoScroll;
    private BarometerDataGraphCallbacks mCallbacks;
    private TransformFunction mTransform;
    private Context mContext;

    public interface BarometerDataGraphCallbacks {
        void onAutoScrollChanged(boolean val);
        void onValueChanged(float value, String unit);
    }

    public interface TransformFunction {
        float transform(float val);
        String getUnit();
    }

    public BarometerDataGraph(Context context, BarometerDataGraphCallbacks callbacks, TransformFunction transform) {
        super(context);
        mCallbacks = callbacks;
        mTransform = transform;
        setupView(context);
    }

    public BarometerDataGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public BarometerDataGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    public void setCallbacks(BarometerDataGraphCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    public void setTransformFunction(TransformFunction transform) {
        mTransform = transform;
    }

    private void setupView(Context context) {
        mContext = context;
        mLockAutoScroll = true;
        LineData data = new LineData();
        setData(data);
        getAxisRight().setStartAtZero(false);
        getAxisRight().setDrawLabels(false);
        getAxisLeft().setStartAtZero(false);
        getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v) {
                return String.format(FLOAT_FORMAT, v);
            }

            ;
        });
        setMarkerView(new BarometerMarkerView(mContext, R.layout.marker_view));
        setAutoScaleMinMaxEnabled(true);

    }

    private LineDataSet getDataSet(LineData data) {
        LineDataSet set = data.getDataSetByIndex(0);

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }
        return set;
    }

    private void scrollToFront(LineData data) {
        setVisibleXRangeMaximum(VISIBLE_POINTS);
        if (mLockAutoScroll) {
            moveViewToX(data.getXValCount() - (VISIBLE_POINTS + 1));
        }
    }

    @Override
    public void write(BarometerDataPoint dataPoint) {
        LineData lineData = getData();

        if (lineData == null) return;

        LineDataSet set = getDataSet(lineData);

        addPoint(dataPoint, lineData, set);

        notifyDataSetChanged();
        invalidate();
        scrollToFront(lineData);
        mCallbacks.onValueChanged(getCorrectValue(dataPoint.getValue()), getUnit());
    }

    @Override
    public void writeHistory(LinkedList<BarometerDataPoint> data) {

        LineData lineData = getData();

        if (data == null) return;

        LineDataSet set = getDataSet(lineData);

        for (BarometerDataPoint dataPoint : data) {
            addPoint(dataPoint, lineData, set);
        }

        notifyDataSetChanged();
        scrollToFront(lineData);
    }

    private void addPoint(BarometerDataPoint dataPoint, LineData lineData, DataSet set) {
        lineData.addXValue(formatTime(dataPoint.getmTimeStamp()));
        lineData.addEntry(new Entry(getCorrectValue(dataPoint.getValue()), set.getEntryCount()), 0);
    }

    private static String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, getUnit());
        set.setColor(mContext.getResources().getColor(R.color.primaryAppColor));
        set.setLineWidth(2f);
        set.setDrawCircles(false);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mLockAutoScroll = false;
        mCallbacks.onAutoScrollChanged(mLockAutoScroll);
        return super.onTouchEvent(event);
    }

    public boolean getAutoScroll() {
        return mLockAutoScroll;
    }

    public void setAutoScroll(boolean lockAutoScroll) {
        highlightValues(null);
        mLockAutoScroll = lockAutoScroll;
    }

    private String getUnit() {
        if (mTransform != null) return mTransform.getUnit();
        return "";
    }

    private float getCorrectValue(float value) {
        if (mTransform != null) return mTransform.transform(value);
        return value;
    }

}
