package com.barcode.mobile.activity.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Contacts.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.barcode.mobile.BarcodeMobileApplication;
import com.barcode.mobile.R;

public class GenernalActivity extends Activity {
	
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
	
	/**
     * 创建 【选项】 菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	if(!menu.hasVisibleItems()){
	    	menu.add(0, 1, 1, getString(R.string.setting_menu)).setIcon(getResources().getDrawable(R.drawable.setting));
	    	menu.add(0, 2, 2, getString(R.string.exit_menu)).setIcon(getResources().getDrawable(R.drawable.exit));
	    	menu.add(0, 3, 3, getString(R.string.about_menu)).setIcon(getResources().getDrawable(R.drawable.about));
    	}
    	return super.onCreateOptionsMenu(menu);
    }
    
    /**
     * 当菜单项被选择时，添加触发事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	int itemId=item.getItemId();
    	switch (itemId) {
		case 1:
			Intent intent=new Intent(Settings.class.getName());
			this.startActivity(intent);
			break;
		case 2:
			this.showExitConfirmDialog();
			break;
		case 3:
//			Toast.makeText(this, "关于本应用", Toast.LENGTH_SHORT).show();
			/*LayoutInflater factory = LayoutInflater.from(GenernalActivity.this);
			View aboutView = factory.inflate(R.layout.about, null);
			//创建对话框
			CustomAlertDialog.Builder builder=new CustomAlertDialog.Builder(GenernalActivity.this);
			builder.setTitle("关于终端直供客户端");
			builder.setContentView(aboutView);
			builder.setNegativeButton("关闭", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.cancel();
				}
			});
			builder.create().show();
			break;*/
		default:
			break;
		}
    	
    	return super.onOptionsItemSelected(item);
    }
    
    /**
	 * 显示退出确认对话框
	 */
	public void showExitConfirmDialog(){
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setMessage("确定要退出应用？");
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				GenernalActivity.this.exit();
			}
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		AlertDialog dialog=builder.create();
		dialog.show();
	}
	
	/**
	 * 完全退出
	 */
	protected void exit(){
		BarcodeMobileApplication app=(BarcodeMobileApplication)GenernalActivity.this.getApplication();
		app.closeApp();
	}
	
	/**
	 * 获取服务端url
	 * @return
	 */
	public String getHttpServiceUrl(){
		SharedPreferences defPrefer=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String serviceUrl=defPrefer.getString("serviceUrl", getString(R.string.service_url_test));
		return serviceUrl;
	}
	
	/**
	 * 通用返回按钮监听器
	 */
	protected View.OnClickListener backBtnListener=new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			GenernalActivity.this.finish();
		}
	};
}
