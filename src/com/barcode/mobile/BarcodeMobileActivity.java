package com.barcode.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.barcode.mobile.activity.LoginActivity;

public class BarcodeMobileActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=new Intent(LoginActivity.class.getName());
		startActivity(intent);
		
		
    }
}