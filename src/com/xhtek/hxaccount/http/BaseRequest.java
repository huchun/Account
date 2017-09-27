package com.xhtek.hxaccount.http;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xhtek.hxaccount.R;

public abstract class BaseRequest implements Runnable{
	
	public static int exists = R.string.account_already_exists;
	public static int noexists = R.string.account_no_exists;
	public static int passError = R.string.password_error;
	public static int notPhone = R.string.account_number_is_not_standard_phone_number;
	public static int blacklist = R.string.account_has_been_included_in_the_blacklist;
	public static int idenError = R.string.identify_code_error;
	public static int idenOverdue = R.string.identify_code_overdue;
	public static int idenNorule = R.string.identify_code_lenght;
	public static int unknow = R.string.error_back_msg;
	public static int pswerror = R.string.error_back_psw;
	public static int oldpswerror = R.string.error_old_psw;
	public static int devicemore = R.string.error_back_device;
	public static int smserror = R.string.error_back_sms;
	public static int deviceerror = R.string.error_device_sms;
	public static int noaccount = R.string.error_no_account;
	
	public int errorStringId;
	
	abstract HttpMethod getRequestType();
	
	abstract RequestParams getParams();
	
	abstract String getAPI();
	
	abstract RequestCallBack<String> callBack();
	
	public void loadData(){
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 5);
 		http.send(getRequestType(),getAPI(),getParams(),callBack());
	}
}
