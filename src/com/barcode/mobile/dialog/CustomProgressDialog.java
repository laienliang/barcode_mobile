package com.barcode.mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;

import com.barcode.mobile.R;

public class CustomProgressDialog extends Dialog {
	private ProgressBar mProgress;
	private int mMax;
	private int mProgressVal;
	private boolean mHasStarted;
	private int mIncrementBy;

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.progress_dialog);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		mProgress = (ProgressBar) findViewById(R.id.progressBar);
		
		if (mMax > 0) {
            setMax(mMax);
        }
		if (mProgressVal > 0) {
            setProgress(mProgressVal);
        }
		if (mIncrementBy > 0) {
            incrementProgressBy(mIncrementBy);
        }
		
		super.onCreate(savedInstanceState);
	}

	public void setMax(int max) {
        if (mProgress != null) {
            mProgress.setMax(max);
        } else {
            mMax = max;
        }
    }
	
	public int getMax() {
        if (mProgress != null) {
            return mProgress.getMax();
        }
        return mMax;
    }
	
	public int getProgress() {
        if (mProgress != null) {
            return mProgress.getProgress();
        }
        return mProgressVal;
    }
	
	public void setProgress(int value) {
        if (mHasStarted) {
            mProgress.setProgress(value);
        } else {
            mProgressVal = value;
        }
    }
	
	public void incrementProgressBy(int diff) {
        if (mProgress != null) {
            mProgress.incrementProgressBy(diff);
        } else {
            mIncrementBy += diff;
        }
    }
	
	@Override
    public void onStart() {
        super.onStart();
        mHasStarted = true;
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        mHasStarted = false;
    }
}
