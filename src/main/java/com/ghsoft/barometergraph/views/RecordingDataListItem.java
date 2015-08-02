package com.ghsoft.barometergraph.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ghsoft.barometergraph.R;
import com.ghsoft.barometergraph.data.RecordingData;

/**
 * Created by brian on 8/2/15.
 */
public class RecordingDataListItem extends LinearLayout implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private RecordingData mRecordingData;
    private LayoutInflater mInflater;
    private Context mContext;
    private View mRootView;
    private IRecordingDataEvents mRecordingDataEvents;

    public RecordingDataListItem(Context context, RecordingData recordingData, IRecordingDataEvents recordingDataEvents) {
        super(context);
        mRecordingData = recordingData;
        mRecordingDataEvents = recordingDataEvents;
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
        TextView tv = ((TextView) mRootView.findViewById(R.id.file_name));
        tv.setText(mRecordingData.getName());
        tv.setOnClickListener(this);
        mRootView.findViewById(R.id.menu_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.file_name:
                mRecordingDataEvents.onViewData(mRecordingData);
                break;
            case R.id.menu_button:
                popContextMenu(v);
                break;
        }
    }

    public void popContextMenu(View v) {
        PopupMenu popup = new PopupMenu(mContext, v);
        popup.getMenuInflater().inflate(R.menu.menu_record_options, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_recording:
                return true;

        }
        return false;
    }
}
