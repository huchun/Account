package com.xhtek.hxaccount.util;

import android.app.Application;
import android.content.Context;

public class AccountApplication extends Application{
	
	private static Context context;  
     
	@Override  
	public void onCreate() {  
		//获取Context  
		super.onCreate();
		context = getApplicationContext();  
	}  
	      

	public synchronized static Context getContextObject(){  
		return context;  
	}  
}
