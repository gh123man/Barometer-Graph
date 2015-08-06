package com.ghsoft.barometergraph.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ghsoft.barometergraph.R;
import com.ghsoft.barometergraph.data.CSVFileNameSanitizer;
import com.ghsoft.barometergraph.data.FileMan;
import com.ghsoft.barometergraph.data.RecordingData;
import com.ghsoft.barometergraph.intents.CSVIntentHelper;

/**
 * Created by brian on 8/2/15.
 */
public class RecordingDataListItem extends LinearLayout implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, DialogInterface.OnClickListener {

    private RecordingData mRecordingData;
    private LayoutInflater mInflater;
    private Context mContext;
    private View mRootView;
    private IRecordingDataEvents mRecordingDataEvents;
    private DataListItemEvents mDataListItemEvents;

    public interface  DataListItemEvents {
        void onDelete();
        void onRename();
    }

    public RecordingDataListItem(Context context, RecordingData recordingData, IRecordingDataEvents recordingDataEvents, DataListItemEvents events) {
        super(context);
        mRecordingData = recordingData;
        mRecordingDataEvents = recordingDataEvents;
        mDataListItemEvents = events;
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

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String filename = ((EditText) ((AlertDialog) dialog).findViewById(R.id.file_name)).getText().toString();
        FileMan fm = new FileMan();
        fm.rename(CSVFileNameSanitizer.sanitize(filename), mRecordingData.getFile());
        mDataListItemEvents.onRename();

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

            case R.id.share_recording:
                share();
                return true;

            case R.id.rename_recording:
                View v = mInflater.inflate(R.layout.dialog_rename_file, null);
                final EditText fileName = (EditText) v.findViewById(R.id.file_name);
                fileName.setText(mRecordingData.getName());
                DialogHelper.showEditDialog(mContext, v, this, true);
                return true;

            case R.id.delete_recording:
                onDeleteData();
                return true;

        }
        return false;
    }

    public void onDeleteData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(mContext.getString(R.string.are_you_sure)).setPositiveButton(mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mRecordingData.delete();
                mDataListItemEvents.onDelete();
            }
        }).setNegativeButton(mContext.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public void share() {
        try {
            mContext.startActivity(Intent.createChooser(CSVIntentHelper.get(mRecordingData.getFile()), "Send " + mRecordingData.getFile().getName()));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "You have no applications to recieve the file", Toast.LENGTH_SHORT).show();
        }
    }
}
