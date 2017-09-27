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


public  class RegisterRequest extends BaseRequest{
	
	public static String ApiHost = HttpService.API;
	public static String ApiModule = HttpService.USER;
	public static String Access = HttpService.REGISTER;
	
	public String phone ;
	
	public String pass;
	
	public String identify;
	
	private BaseDAOListener listener = null;
	
	private AccountType  mType;
	
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
        params.put("password", pass);
        params.put("identifyingCode", identify); 
        params.put("accountKind", 0);      
        
        JSONObject object = new JSONObject(params);
        String value = object.toString();
        RequestParams tparams = new RequestParams();
        tparams.addBodyParameter("deviceId", PhoneDevice.getSimpleDeviceId(AccountApplication.getContextObject()));
        tparams.addBodyParameter("ticket", "");
        tparams.addBodyParameter("actionType", ApiModule);
        tparams.addBodyParameter("action", HttpService.REGISTER);
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
                 	    		
                        		listener.loadSuccess(RegisterRequest.this);
                        		
                 	    		break;
                 	    	case 1:
                        		errorStringId=BaseRequest.exists;
                        		listener.loadFailure(RegisterRequest.this);
                 	    		break;
                 	    	case 2:
                        		errorStringId=BaseRequest.idenError;
                        		listener.loadFailure(RegisterRequest.this);
                 	    		break;
             	    		default:
             	    			break;
             	    	}
         	    	}else{
                		errorStringId=BaseRequest.unknow;
                		listener.loadFailure(RegisterRequest.this);
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
            		listener.loadFailure(RegisterRequest.this);
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
