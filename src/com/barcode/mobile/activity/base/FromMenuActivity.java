package com.barcode.mobile.activity.base;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.barcode.mobile.BarcodeMobileApplication;
import com.barcode.mobile.R;
import com.barcode.mobile.common.AsyncTaskCallBack;
import com.barcode.mobile.common.CommAsyncTask;
import com.barcode.mobile.common.PageInfo;

public class FromMenuActivity extends GenernalActivity {
	
	protected String menuNo=null;	//菜单编号
	
	//请求号，用于startActivityForResult的requestCode
	public static final int REQUEST_SELECT_EQUIP=0x200;//选择设备返回
	public static final int REQUEST_SELECT_JXS=0x201;//选择供应商返回
	public static final int REQUEST_RETURN_REFLESH=0x203;//返回刷新列表
	//返回时要调用的task
	protected AsyncTaskCallBack resultAsyncTaskCallBack;
	
	//分页信息
	protected PageInfo page=new PageInfo();
	protected TextView pageInfoTv=null;
	//通用列表
	protected ListView commonList=null;
	protected List<Map<String, Object>> listViewItems=new ArrayList<Map<String, Object>>();//列表数据源
	
	protected TextView nothing=null;//无内容提示
	
	protected View searchView=null;//查询界面的view
	
	//选择设备型号后返回要更新的控件，必须在选设备前设置
	protected EditText equip=null; //设备名称
	protected EditText equipNoEt=null; //设备标识
	protected EditText unitSumEt=null; //单价
	
	//选择经销商后返回要更新的控件，必须在选择前设置
	protected EditText jxs=null;//经销商
	
	protected View.OnClickListener onClickListener=null;//postTitle用到

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		BarcodeMobileApplication app=(BarcodeMobileApplication)this.getApplication();
		app.addMenuActivity(this);
		
		menuNo=this.getIntent().getStringExtra("menuNo");
	}
	
	@Override
    protected void onDestroy() {
    	
    	BarcodeMobileApplication app=(BarcodeMobileApplication)this.getApplication();
    	app.removeMenuActivity(this);
    	super.onDestroy();
    }
	
	/**
	 * 返回主菜单，关闭打开的activity
	 */
	protected void goBackToMainMenuPage(){
		BarcodeMobileApplication app=(BarcodeMobileApplication)this.getApplication();
		app.clearMenuActivity();
	}
}
