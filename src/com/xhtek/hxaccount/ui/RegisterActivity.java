package com.xhtek.hxaccount.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xhtek.hxaccount.R;
import com.xhtek.hxaccount.http.BaseRequest;
import com.xhtek.hxaccount.http.CheckRequest;
import com.xhtek.hxaccount.http.RegisterRequest;
import com.xhtek.hxaccount.util.BaseDAOListener;
import com.xhtek.hxaccount.util.OnRegisterConfirmIml;
import com.xhtek.hxaccount.util.RegisterDialog;

public class RegisterActivity extends Activity implements BaseDAOListener,OnRegisterConfirmIml{
	
    private EditText mPhone,mPass;
    private Button mConfirm;
    private TextView mGoExtra;
    private ImageView mEye;
    private CheckBox mCheck;
    private ProgressDialog mProgress;
    private RegisterDialog mDialog;
    private RegisterRequest mRegisterDao;
    private CheckRequest mCheckDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_account_page);
		findViews();
		initDatas();
		registerEvents();
	}

	private void findViews(){
        mPhone = (EditText) findViewById(R.id.register_phone);
        mPass = (EditText) findViewById(R.id.register_pass);
        mConfirm = (Button) findViewById(R.id.confirm);
        mGoExtra = (TextView) findViewById(R.id.goextra);
        mEye = (ImageView) findViewById(R.id.eye_pass);
        mCheck = (CheckBox)findViewById(R.id.checkbox);
	}
	
	private void initDatas(){
		mRegisterDao = new RegisterRequest();
		mRegisterDao.setDaoListener(this);
		mCheckDao = new CheckRequest();
		mCheckDao.setDaoListener(this);
	}
	
	private void sendRegisterRequest(String iden){
		mRegisterDao.phone = mPhone.getText().toString().trim();
		mRegisterDao.pass = mPass.getText().toString().trim();
		mRegisterDao.identify = iden;
		mRegisterDao.run();
	}
	
	private void sendCheckRequest(){
		mCheckDao.phone = mPhone.getText().toString().trim();
		mCheckDao.run();	
	}
	
	
	private void registerEvents(){
		mConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(check()){
					mProgress = new ProgressDialog(RegisterActivity.this);
					mProgress.setMessage(getResources().getString(R.string.wait));
					mProgress.setCancelable(true);
					mProgress.setCanceledOnTouchOutside(false);
					mProgress.show();
					sendCheckRequest();
					
					/* if(mProgress!=null)mProgress.dismiss();
					 showIdentifyWaitPop();*/
		    	}
			}
		});
		
		mGoExtra.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(RegisterActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});	
		
		mEye.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mEye.isSelected()){
					mEye.setSelected(false);
					mPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					mEye.setSelected(true);
					mPass.setTransformationMethod(PasswordTransformationMethod.getInstance());	
				}
				Editable etext = mPass.getText();
				Selection.setSelection(etext, etext.length());
			}
		});
		mEye.setSelected(true);
		mPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
		
	}
	
	private void gotoCompleteActivity(){
			Intent intent = new Intent();
			intent.putExtra("from", "register");
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent .setClass(RegisterActivity.this, RegisterOkActivity.class);
			startActivity(intent);
	}
	
	
	private boolean check(){
		boolean isDigit = false; //包含数字
	    boolean isLetter = false; //包含字母
	    String regex = "^[a-zA-Z0-9]+$"; 
	    
		if(mPhone.getText().toString().trim().length()!=11){
			Toast.makeText(this,getResources().getString(R.string.account_number_is_not_standard_phone_number), Toast.LENGTH_SHORT).show();
			return false;
		}
		if(mPass.getText().toString().trim().equals("")){
			Toast.makeText(this,getResources().getString(R.string.pass_is_empty), Toast.LENGTH_SHORT).show();
			return false;
		}
		
//		if (mPass.length() > 5) {
//			Toast.makeText(this, getResources().getString(R.string.pass_lenght), Toast.LENGTH_SHORT).show();
//			
//			return false;
//		}
		
//		if (mPass.length() < 21) {
//			Toast.makeText(this, getResources().getString(R.string.pass_lenght), Toast.LENGTH_SHORT).show();
//			return false;
//		}
		
		if(!mCheck.isChecked()){
			Toast.makeText(this,getResources().getString(R.string.greement_protocal), Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
	
	private void showIdentifyWaitPop(){
		runOnUiThread(new Runnable()    
        {    
            @SuppressWarnings("deprecation")
			public void run()    
            {    	
        	 if(mDialog==null||!mDialog.isShowing()){
	           	 mDialog = new RegisterDialog(RegisterActivity.this, mPhone.getText().toString().trim());
	           	 mDialog.setRegisterConfirmIml(RegisterActivity.this);
	           	 mDialog.show(); 
	           	 WindowManager windowManager = getWindowManager();
	           	 Display display = windowManager.getDefaultDisplay();
	           	 WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
	           	 lp.width = (int)(display.getWidth()); 
	           	 mDialog.getWindow().setAttributes(lp);
           	 }
            }       
        });  
	}
	

	@Override
	public void loadSuccess(BaseRequest dao) {
		// TODO Auto-generated method stub
		 if(dao.equals(mRegisterDao)){
			 if(mDialog!=null)mDialog.dismiss();
			 gotoCompleteActivity();
		 }else if(dao.equals(mCheckDao)){
			 if(mProgress!=null)mProgress.dismiss();
			 showIdentifyWaitPop();
		 }
	}

	@Override
	public void loadFailure(BaseRequest dao) {
		// TODO Auto-generated method stub
		if(dao.equals(mRegisterDao)){
			 Toast.makeText(this,getResources().getString(mRegisterDao.errorStringId), Toast.LENGTH_SHORT).show(); 
		 }else if(dao.equals(mCheckDao)){
			 if(mProgress!=null)mProgress.dismiss();
			 Toast.makeText(this,getResources().getString(mCheckDao.errorStringId), Toast.LENGTH_SHORT).show();
		 }
	}

	@Override
	public void confirm(String identify) {
		// TODO Auto-generated method stub
		sendRegisterRequest(identify);
	}

	@Override
	public void recheck() {
		// TODO Auto-generated method stub
		sendCheckRequest();
	}
}
