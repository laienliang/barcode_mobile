package com.barcode.mobile.common;

import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.barcode.mobile.R;
import com.barcode.mobile.activity.base.GenernalActivity;
import com.barcode.mobile.dialog.CustomProgressDialog;

/**
 * 异步取数基类
 * @author linyaojian
 *
 */
public class CommAsyncTask extends AsyncTask<String,Integer,String> {
	
	private static final int MAX_PROGRESS=100;
	private Context baseContext;
	private Handler mProgressHandler;
	private CustomProgressDialog mProgressDialog;
	private AsyncTaskCallBack asyncTaskCallBack;
	
	//是否紧接着执行另一个线程任务
	private boolean executeAnthoerTask=false;
	private CommAsyncTask anthoerTask=null;//要在主线程启动
	private String[] params=null;
	
	private PageInfo page=null;
	
	//是否执行完后立马结束当前activity
	private boolean finishCurrActivity=false;
	private boolean hasResult=false;//结束时是否返回结果
	private Bundle extraData=null;//返回值
	private int resultCode;//结果号

	public CommAsyncTask(Context baseContext, AsyncTaskCallBack asyncTaskCallBack) {
		super();
		// TODO Auto-generated constructor stub
		this.baseContext=baseContext;
		this.asyncTaskCallBack=asyncTaskCallBack;
	}
	
	public CommAsyncTask(Context baseContext, AsyncTaskCallBack asyncTaskCallBack, PageInfo page) {
		super();
		this.baseContext=baseContext;
		this.asyncTaskCallBack=asyncTaskCallBack;
		this.page=page;
	}

	@Override
	protected void onPreExecute() {
		
		super.onPreExecute();
		mProgressDialog = new CustomProgressDialog(baseContext,R.style.progress_dialog_theme);
        mProgressDialog.setMax(CommAsyncTask.MAX_PROGRESS);
//        mProgressDialog.setCancelable(false);//物理返回键是否可以取消进度条
        
        mProgressHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.v("进度条进度：", ""+mProgressDialog.getProgress());
                if (mProgressDialog.getProgress() >= mProgressDialog.getMax()) {
                	try {
                		Log.v("对话框结束：", ""+mProgressDialog.getProgress());
                		mProgressDialog.dismiss();
                	}catch(Exception e){
                		//e.printStackTrace();
                	}
                } 
                else {
					mProgressDialog.incrementProgressBy(1);
					this.sendEmptyMessageDelayed(0, 2000);
                }
            }
        };
        
        try {
			mProgressDialog.show();
			mProgressDialog.setProgress(0);
			mProgressHandler.sendEmptyMessage(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected String doInBackground(String... params) {
		
		String msg=null;
		String serviceUrl=((GenernalActivity)baseContext).getHttpServiceUrl();
		this.publishProgress(5);//进度5
		try {
			msg=asyncTaskCallBack.process(this, serviceUrl, this.page, params);
		} catch (ConnectionPoolTimeoutException e) {
			e.printStackTrace();
			msg="请求超时！";
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			msg="请求超时！";
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			msg="请求超时！";
		} catch (Exception e) {
			e.printStackTrace();
			msg="发生错误:"+e.getMessage();
		}
		this.publishProgress(100);//进度100
		return msg;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		
		try{
			super.onProgressUpdate(values);
			mProgressDialog.setProgress(values[0]);
		}catch(Exception e){
//			e.printStackTrace();
		}
	}
	
	@Override
	protected void onPostExecute(String result) {
		
		super.onPostExecute(result);
		if(null!=result){//显示信息
			Toast.makeText(baseContext, result, Toast.LENGTH_SHORT).show();
		}
		if(executeAnthoerTask){//执行下一个task
			this.anthoerTask.execute(params);
		}
		if(finishCurrActivity){//结束当前activity，并返回结果
			if(hasResult){
				Intent intent=new Intent();
				intent.putExtras(extraData);
				((Activity)baseContext).setResult(resultCode, intent);
			}
			((Activity)baseContext).finish();
		}
	}

	public void publishProgress_(int progress){
		this.publishProgress(progress);
	}
	
	public boolean isExecuteAnthoerTask() {
		return executeAnthoerTask;
	}

	public void setExecuteAnthoerTask(boolean executeAnthoerTask) {
		this.executeAnthoerTask = executeAnthoerTask;
	}

	public CommAsyncTask getAnthoerTask() {
		return anthoerTask;
	}

	public void setAnthoerTask(CommAsyncTask anthoerTask) {
		this.anthoerTask = anthoerTask;
	}
	
	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}
	public PageInfo getPage() {
		return page;
	}

	public void setPage(PageInfo page) {
		this.page = page;
	}

	public boolean isFinishCurrActivity() {
		return finishCurrActivity;
	}

	public void setFinishCurrActivity(boolean finishCurrActivity) {
		this.finishCurrActivity = finishCurrActivity;
	}

	public boolean isHasResult() {
		return hasResult;
	}

	public void setHasResult(boolean hasResult) {
		this.hasResult = hasResult;
	}

	public Bundle getExtraData() {
		return extraData;
	}

	public void setExtraData(Bundle extraData) {
		this.extraData = extraData;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
}
