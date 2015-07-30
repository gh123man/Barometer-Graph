package com.ghsoft.barometergraph.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ghsoft.barometergraph.R;

/**
 * Created by brian on 7/26/15.
 */
public class ExpanderView extends LinearLayout implements View.OnClickListener {

    private LinearLayout mRootView, mContentView;
    private FrameLayout mExpandButton;
    private LayoutInflater mInflater;
    private OnExpandListener mListener;
    private String mText;
    private boolean mExpanded;

    public interface OnExpandListener {
        void onExpand(boolean expanded);
    }

    public ExpanderView(Context context) {
        super(context);
        setupView(context);
    }

    public ExpanderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public void setupView(Context context) {
        mText = "";
        mExpanded = false;
        mInflater = LayoutInflater.from(context);
        mRootView = (LinearLayout) mInflater.inflate(R.layout.expander_view, this);

        mExpandButton = (FrameLayout) mRootView.findViewById(R.id.expand_button);
        mExpandButton.setOnClickListener(this);

        mContentView = (LinearLayout) mRootView.findViewById(R.id.content);

    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if(mContentView == null){
            super.addView(child, index, params);
        } else {
            mContentView.addView(child, index, params);
        }
    }

    public void expandView(boolean expand) {
        if (expand) {
            mContentView.setVisibility(View.VISIBLE);
            setUnExpandButton();
            mExpanded = true;
            //mListener.onExpand(true);
        } else {
            mContentView.setVisibility(View.GONE);
            setExpandButton();
            mExpanded = false;
            //mListener.onExpand(false);
        }
    }

    public boolean getExpanded() {
        return mContentView.getVisibility() == View.VISIBLE;
    }

    public void setExpandtext(String text) {
        mText = text;
        if (mExpanded) {
            setUnExpandButton();
        } else {
            setExpandButton();
        }
    }

    private void setExpandButton() {
        mExpandButton.removeAllViews();
        mExpandButton.addView(mInflater.inflate(R.layout.expand_button_down, null));
        ((TextView) mExpandButton.findViewById(R.id.expand_down)).setText(mText);
    }

    private void setUnExpandButton() {
        mExpandButton.removeAllViews();
        mExpandButton.addView(mInflater.inflate(R.layout.expand_button_up, null));
        ((TextView) mExpandButton.findViewById(R.id.expand_up)).setText(mText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expand_button:
                expandView(!getExpanded());
                break;

            default:
                break;
        }

    }

    public void setOnExpandListener(OnExpandListener listener) {
        mListener = listener;
    }
}
