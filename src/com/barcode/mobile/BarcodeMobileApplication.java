package com.barcode.mobile;

import java.util.LinkedList;

import android.app.Activity;
import android.app.Application;

import com.barcode.mobile.common.CustomerHttpClient;

public class BarcodeMobileApplication extends Application {
	
	private LinkedList<Activity> allActivityList=new LinkedList<Activity>();//整个应用打开的activity
	private LinkedList<Activity> menuActivityList=new LinkedList<Activity>();//每个主菜单打开的activity
	
	/*
	 * admin allActivityList
	 */
	public void addActivity(Activity act){
		this.allActivityList.add(act);
	}
	
	public void removeActivity(Activity act){
		this.allActivityList.remove(act);
	}
	
	public void closeApp(){
		while(!allActivityList.isEmpty()){
			Activity tempAct=allActivityList.poll();
			if(!tempAct.isFinishing()){
				tempAct.finish();
			}
		}
	}
	
	/*
	 * admin menuActivityList
	 */
	public void addMenuActivity(Activity act){
		this.menuActivityList.add(act);
	}
	
	public void removeMenuActivity(Activity act){
		this.menuActivityList.remove(act);
	}
	
	public void clearMenuActivity(){
		while(!menuActivityList.isEmpty()){
			Activity tempAct=menuActivityList.poll();
			if(!tempAct.isFinishing()){
				tempAct.finish();
			}
		}
	}
	
	//注销
	public void clearCookies(){
		CustomerHttpClient.resetClient();
	}
}