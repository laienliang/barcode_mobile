package com.barcode.mobile.activity;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.barcode.mobile.BarcodeMobileApplication;
import com.barcode.mobile.R;
import com.barcode.mobile.activity.base.GenernalActivity;
import com.barcode.mobile.bean.CommonResult;
import com.barcode.mobile.common.AsyncTaskCallBack;
import com.barcode.mobile.common.CommAsyncTask;
import com.barcode.mobile.common.CustomerHttpClient;
import com.barcode.mobile.common.PageInfo;
import com.barcode.mobile.common.StringUtil;

public class LoginActivity extends GenernalActivity{
	// 记住密码复选框
	private CheckBox remenberMeCb=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		final EditText usernameEt=(EditText)findViewById(R.id.username);
		final EditText passwordEt=(EditText)findViewById(R.id.password);		
		remenberMeCb=(CheckBox)findViewById(R.id.remenber_me);
		//提交按钮
		Button submitBtn=(Button)findViewById(R.id.submit);
		//退出按钮
		ImageButton exitBtn=(ImageButton)findViewById(R.id.btn_exit);
		
		SharedPreferences defPrefer=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if(defPrefer.getBoolean("isRemenberMe", false)){
			usernameEt.setText(defPrefer.getString("username", ""));
			passwordEt.setText(defPrefer.getString("password", ""));
			remenberMeCb.setChecked(true);
		}
		//提交事件
		submitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String username=usernameEt.getText().toString();
				String password=passwordEt.getText().toString();
				
				if(StringUtil.isBlank(username)){
					Toast.makeText(LoginActivity.this, R.string.login_warn_input_user, Toast.LENGTH_SHORT).show();
				}else{
					new CommAsyncTask(LoginActivity.this,asyncTaskCallBack).execute(username,password);
				}
			}
		});
		//退出单击事件
		exitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showExitConfirmDialog();
			}
		});
	}
	
	/**
	 * 回调类
	 */
	private AsyncTaskCallBack asyncTaskCallBack=new AsyncTaskCallBack() {
		@Override
		public String process(CommAsyncTask task, String serviceUrl, PageInfo newPage, String... params)
				throws ConnectionPoolTimeoutException, ConnectTimeoutException,
				SocketTimeoutException, Exception {
			
			String msg=null;
			String username=params[0];
			String password=params[1];
			SharedPreferences defPrefer=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			String fullUrl=serviceUrl+getString(R.string.service_login);
			
			NameValuePair param1 = new BasicNameValuePair("account", username);
			NameValuePair param2 = new BasicNameValuePair("password", password);
			List<NameValuePair> urlParams=new ArrayList<NameValuePair>();
			urlParams.add(param1);
			urlParams.add(param2);
			String response=CustomerHttpClient.post(fullUrl, urlParams);
			System.out.println(response);
			task.publishProgress_(50);//进度50
			if (StringUtil.isBlank(response)) {
				return getString(R.string.login_error_msg);
			}
			CommonResult result = parseJson(response);
			//CommonResult result = JsonUtil.json2Bean(response, CommonResult.class);
			if(!result.isSuccess()){
				//登录失败
				msg=result.getErrorMessage();
			}else{
				//登录成功
				//记住我
				SharedPreferences.Editor editor=defPrefer.edit();
				if(remenberMeCb.isChecked()){
					editor.putString("username", username);
					editor.putString("password", password);
				}
				editor.putBoolean("isRemenberMe", remenberMeCb.isChecked());
				editor.commit();
				
				msg = "登陆成功";
				System.out.println("登陆成功");
				//跳转到主菜单
				///Intent intent=new Intent(MainMenuActivity.class.getName());
				//startActivity(intent);
			}
			return msg;
		}

		private CommonResult parseJson(String jsonStr) {
			CommonResult commonResult = new CommonResult();
			try {
				JSONObject jsonObject = new JSONObject(jsonStr);
				if (jsonObject.has("success")) {
					boolean success = jsonObject.getBoolean("success");
					commonResult.setSuccess(success);
				}
				if (jsonObject.has("errorMessage")) {
					String errorMessage = jsonObject.getString("errorMessage");
					commonResult.setErrorMessage(errorMessage);
					
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return commonResult;
		}
	};
	
	/**
	 * 硬件返回按钮事件
	 */
	@Override
	public void onBackPressed() {
		
		this.showExitConfirmDialog();
	}
	
	@Override
	protected void onStart() {
		//每次开始这个activity时清除cookies信息
		BarcodeMobileApplication app=(BarcodeMobileApplication)this.getApplication();
		app.clearCookies();
		super.onStart();
	}
}
