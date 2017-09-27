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
import com.xhtek.hxaccount.util.AccountApplication;
import com.xhtek.hxaccount.util.BaseDAOListener;
import com.xhtek.hxaccount.util.PhoneDevice;


public  class CheckRequest extends BaseRequest{

	public static String ApiHost = HttpService.API;
	public static String ApiModule = HttpService.USER;
	public static String Access = HttpService.CHECK;
	public String phone ;
	
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
        params.put("account", phone);
        JSONObject object = new JSONObject(params);
        String value = object.toString();
        RequestParams tparams = new RequestParams();
        tparams.addBodyParameter("deviceId", PhoneDevice.getSimpleDeviceId(AccountApplication.getContextObject()));
        tparams.addBodyParameter("ticket", "");
        tparams.addBodyParameter("actionType", ApiModule);
        tparams.addBodyParameter("action", Access);
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
            	Log.d("RegisterDao","onStart");
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
            	Log.d("RegisterDao","onLoading");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
            	Log.d("RegisterDao","onResult"+responseInfo.result);
            	if(listener==null)return;
         	    try {
         	    	JSONObject JsonObject = new JSONObject(responseInfo.result);
         	    	JSONObject response = JsonObject.getJSONObject("response");
         	    	if(response.getInt("response_code")==0){
         	    		
             	    	switch(JsonObject.getInt("apiStatus")){
                 	    	case 0:
                        		listener.loadSuccess(CheckRequest.this);
                 	    		break;
                 	    	case 1:
                        		errorStringId=BaseRequest.exists;
                        		listener.loadFailure(CheckRequest.this);
                 	    		break;
                 	    	case 2:
                        		errorStringId=BaseRequest.idenError;
                        		listener.loadFailure(CheckRequest.this);
                 	    		break;
             	    		default:
             	    			break;
             	    	}
         	    	}else{
                		errorStringId=BaseRequest.unknow;
                		listener.loadFailure(CheckRequest.this);
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
            		listener.loadFailure(CheckRequest.this);
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
