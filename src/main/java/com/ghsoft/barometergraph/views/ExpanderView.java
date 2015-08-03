package com.ghsoft.barometergraph.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ghsoft.barometergraph.R;

/**
 * Created by brian on 7/26/15.
 */
public class ExpanderView extends LinearLayout implements View.OnClickListener {

    private LinearLayout mRootView, mContentView;
    private View mExpandButton;
    private LayoutInflater mInflater;
    private OnExpandListener mListener;
    private ImageView mImage1, mImage2;
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

        mExpandButton = mRootView.findViewById(R.id.expand_button);
        mExpandButton.setOnClickListener(this);
        mImage1 = (ImageView) mRootView.findViewById(R.id.image_1);
        mImage2 = (ImageView) mRootView.findViewById(R.id.image_2);

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
        ((TextView) mExpandButton.findViewById(R.id.expand_text)).setText(mText);
        mImage1.setImageResource(R.drawable.ic_down_arrow);
        mImage2.setImageResource(R.drawable.ic_down_arrow);
    }

    private void setUnExpandButton() {
        ((TextView) mExpandButton.findViewById(R.id.expand_text)).setText(mText);
        mImage1.setImageResource(R.drawable.ic_up_arrow);
        mImage2.setImageResource(R.drawable.ic_up_arrow);
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
