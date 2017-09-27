package com.xhtek.hxaccount.http;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xhtek.hxaccount.mode.AccountType;
import com.xhtek.hxaccount.util.AccountApplication;
import com.xhtek.hxaccount.util.BaseDAOListener;
import com.xhtek.hxaccount.util.PhoneDevice;


public  class FixPswRequest extends BaseRequest{
	
	public static String ApiHost = HttpService.API;
	public static String ApiModule = HttpService.USER;
	public static String Acfixpsw= HttpService.UPDATEPW;
	
	public String oldPassword;
	
	public long  userId;
	
	public String newPassword;

	private BaseDAOListener listener = null;
	
	public void setDaoListener(BaseDAOListener l){
		this.listener = l;
	}
	
	public void getReslut(){
		
	}

	@Override
	HttpMethod getRequestType() {
		// TODO Auto-generated method stub
		return HttpRequest.HttpMethod.POST;
	}

	@Override
	RequestParams getParams() {
		// TODO Auto-generated method stub
        HashMap<Object, Object> params = new HashMap<Object, Object>();
        params.put("oldPassword", oldPassword);
        params.put("userId", AccountType.getInstance().userId);
        params.put("newPassword", newPassword); 
        JSONObject object = new JSONObject(params);
        String value = object.toString();
        RequestParams tparams = new RequestParams();
        tparams.addBodyParameter("deviceId", PhoneDevice.getSimpleDeviceId(AccountApplication.getContextObject()));
        tparams.addBodyParameter("ticket", AccountType.getInstance().token);
        tparams.addBodyParameter("actionType", ApiModule);
        tparams.addBodyParameter("action", HttpService.UPDATEPW);
        tparams.addBodyParameter("value", value);  
        return tparams;
	}

	@Override
	String getAPI() {
		// TODO Auto-generated method stub
		return ApiHost+ApiModule;
	}

	@Override
	RequestCallBack<String> callBack() {
		// TODO Auto-generated method stub
		return new RequestCallBack<String>() {

            @Override
            public void onStart() {
            	Log.d("FixPswDao","onStart");
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
            	Log.d("FixPswDao","onLoading");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
            	Log.d("FixPswDao","onResult"+responseInfo.result);
            	if(listener==null)return;
         	    try {
         	    	JSONObject JsonObject = new JSONObject(responseInfo.result);
         	    	JSONObject response = JsonObject.getJSONObject("response");
         	    	if(response.getInt("response_code")==0){
         	    		
             	    	switch(JsonObject.getInt("apiStatus")){
                 	    	case 0:
                        		listener.loadSuccess(FixPswRequest.this);
                 	    		break;
                 	    	case 1:
                        		errorStringId=BaseRequest.noexists;
                        		listener.loadFailure(FixPswRequest.this);
                 	    		break;
             	    		default:
             	    			break;
             	    	}
         	    	}else{
                		errorStringId=BaseRequest.oldpswerror ;
                		listener.loadFailure(FixPswRequest.this);
         	    	}
    	
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            @Override
            public void onFailure(HttpException error, String msg) {
            	Log.d("phone_guide","onFailure"+msg);
            	if(listener!=null){
            		errorStringId=BaseRequest.unknow;
            		listener.loadFailure(FixPswRequest.this);
            	}
            }
        } ;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		loadData();
	}

}
