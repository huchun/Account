package com.xhtek.hxaccount.ui;

import com.xhtek.hxaccount.R;
import com.xhtek.hxaccount.http.BaseRequest;
import com.xhtek.hxaccount.http.CodeRequest;
import com.xhtek.hxaccount.http.ForgetRequest;
import com.xhtek.hxaccount.util.BaseDAOListener;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ForgetPswActivity extends Activity implements BaseDAOListener{

	    private  EditText   mXuPhone, mForCode,mNewPsw, mNewtoPsw;
	    private  ImageView  mImgEye, mImgToeye;
	    private  Button     mCodeBtn, mSaveBtn;
	    private  ProgressDialog  mDialog;
	    private  ForgetRequest   mPswDao;
	    private  CodeRequest     mCodeDao;
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.setup_account_forgetpsw);
	    	
	    	initDatas();
	    	initView(); 	
	    }

		private void initDatas() {
		      mPswDao = new ForgetRequest();
			  mPswDao.setDaoListener(this);
			  mCodeDao = new CodeRequest();
			  mCodeDao.setDaoListener(this);
		}

		private void initView() {
			mXuPhone = (EditText)findViewById(R.id.register_phone);
			mForCode = (EditText)findViewById(R.id.forget_code);
			mNewPsw = (EditText)findViewById(R.id.forget_pass);
			mNewtoPsw = (EditText)findViewById(R.id.forget_to_pass);
			
			mImgEye = (ImageView)findViewById(R.id.eye_pass);
			mImgToeye = (ImageView)findViewById(R.id.eye_to_pass);
			mCodeBtn = (Button)findViewById(R.id.forgetPswBtn);
			mSaveBtn = (Button)findViewById(R.id.saveBtn);
			
			mImgEye.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mImgEye.isSelected()){
						mImgEye.setSelected(false);
						mNewPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					}else{
						mImgEye.setSelected(true);
						mNewPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());	
					}
					Editable etext = mNewPsw.getText();
					Selection.setSelection(etext, etext.length());
				}
			});
			mImgEye.setSelected(true);
			mNewPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());

			mImgToeye.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				    if (mImgToeye.isSelected()) {
						mImgToeye.setSelected(false);
						mNewtoPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					}else {
						mImgToeye.setSelected(true);
						mNewtoPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
					} 
				    Editable etext = mNewtoPsw.getText();
				    Selection.setSelection(etext, etext.length());
				}
			});
			mImgToeye.setSelected(true);
			mNewtoPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
			
			mCodeBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					    if (check()) {			    	
					    	startCount();
					    	sendCodeRequest();
						}	
				}
			});
			mSaveBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (checkBtn()) {
						  mDialog = new ProgressDialog(ForgetPswActivity.this);
						  mDialog.setMessage(getResources().getString(R.string.wait));
						  mDialog.setCancelable(true);
						  mDialog.setCanceledOnTouchOutside(false);
						  mDialog.show();
						  SavePswRequest();
					}
				}
			});
		}

		protected void sendCodeRequest() {
			mCodeDao.phone = mXuPhone.getText().toString().trim();
			mCodeDao.run();
		}

		protected void SavePswRequest() {
			mPswDao.phone = mXuPhone.getText().toString().trim();
			mPswDao.identifyingCode = mForCode.getText().toString().trim();
			mPswDao.password = mNewPsw.getText().toString().trim();
			mPswDao.password = mNewtoPsw.getText().toString().trim();
			mPswDao.run();
		}

		protected boolean check() {
			if(mXuPhone.getText().toString().trim().length()!=11){
				Toast.makeText(this,getResources().getString(R.string.account_number_is_not_standard_phone_number), Toast.LENGTH_SHORT).show();
				return false;
			}		
			return true;
		}
		
		protected boolean checkBtn() {
			/*if(mNewPsw.getText().toString().trim().equals("")){
			Toast.makeText(this,getResources().getString(R.string.pass_is_empty), Toast.LENGTH_SHORT).show();
			return false;
		}*/
		
		/*if(!mCheck.isChecked()){
			Toast.makeText(this,getResources().getString(R.string.greement_protocal), Toast.LENGTH_SHORT).show();
			return false;
		}*/
			return true;
		}

		private void gotoCompleteActivity() {
			Intent intent = new Intent();
			intent.putExtra("from", "forgetPsw");
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent .setClass(ForgetPswActivity.this, LoginActivity.class);
			startActivity(intent);
		}
		
		@Override
		public void loadSuccess(BaseRequest dao) {
			// TODO Auto-generated method stub
			if (dao.equals(mPswDao)) {
				 gotoCompleteActivity();
				 Toast.makeText(getApplicationContext(), R.string.fix_seccess, Toast.LENGTH_SHORT).show();
			}else if (dao.equals(mCodeDao)) {
				Toast.makeText(getApplicationContext(), R.string.send_seccess, Toast.LENGTH_SHORT).show();
			}
		}
		
		@Override
		public void loadFailure(BaseRequest dao) {
			if (dao.equals(mPswDao)) {
				
			}
		}
		
		// 倒计时逻辑
	 	private Handler mHandler;
	 	private int  mCurrent;
	 	private boolean mIsCounting;
	 	private static final int COUNT_MAX = 61;
	    
		public void startCount(){
			 if (mIsCounting) {
				  return;
			}
			 mIsCounting = true;
			 mCodeBtn.setEnabled(false);
			 mCurrent = 0;
			 if (mHandler == null) {
				   mHandler = new Handler(new Handler.Callback() {
					
					@Override
					public boolean handleMessage(Message arg0) {
					     mCurrent++;
					     if (COUNT_MAX - mCurrent > 0) {
					    	 mCodeBtn.setText(getString(R.string.verfity_account, COUNT_MAX  -  mCurrent));
					    	 mCodeBtn.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
						       mHandler.sendEmptyMessageDelayed(0, 1000);
						}else {
							mCodeBtn.setText(getText(R.string.verfity_get_account));
							mCodeBtn.setTextColor(getResources().getColor(android.R.color.white));
							mCodeBtn.setEnabled(true);
							mIsCounting = false;
						}
						return true;
					}
				});  
			}
			 mHandler.sendEmptyMessage(0);
		}
}
