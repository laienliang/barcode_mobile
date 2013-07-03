package com.barcode.mobile.activity.base;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.barcode.mobile.BarcodeMobileApplication;

public class PreferentActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		BarcodeMobileApplication app=(BarcodeMobileApplication)this.getApplication();
		app.addActivity(this);
	}
	
	@Override
    protected void onDestroy() {
    	
    	super.onDestroy();
    	BarcodeMobileApplication app=(BarcodeMobileApplication)this.getApplication();
    	app.removeActivity(this);
    }
}
