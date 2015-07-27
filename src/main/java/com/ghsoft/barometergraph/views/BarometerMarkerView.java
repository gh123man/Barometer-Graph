package com.ghsoft.barometergraph.views;

import android.content.Context;
import android.widget.TextView;

import com.ghsoft.barometergraph.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;

/**
 * Created by brian on 7/26/15.
 */
public class BarometerMarkerView extends MarkerView {

    private static final String FLOAT_FORMAT = "%.4f";

    private TextView mMarkerText;

    public BarometerMarkerView (Context context, int layoutResource) {
        super(context, layoutResource);
        mMarkerText = (TextView) findViewById(R.id.marker_content);
    }

    @Override
    public void refreshContent(Entry e, int dataSetIndex) {
        mMarkerText.setText(String.format(FLOAT_FORMAT, e.getVal()));
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return -getHeight();
    }
}
