package com.xhtek.hxaccount.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xhtek.hxaccount.R;
import com.xhtek.hxaccount.http.BaseRequest;
import com.xhtek.hxaccount.http.LoginRequest;
import com.xhtek.hxaccount.util.BaseDAOListener;

public class LoginActivity extends Activity implements BaseDAOListener {
	
    private EditText mPhone,mPass;
    private Button   mConfirm;
    private TextView mGoExtra, mForgetPsw;;
    private ImageView mEye;
    private CheckBox  mCheck;
    private ProgressDialog mProgress;
    private LoginRequest   mLoginDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_account_login);
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
        mCheck =(CheckBox)findViewById(R.id.checkbox);
        mForgetPsw = (TextView)findViewById(R.id.forget);
	}
	
	private void initDatas(){
		mLoginDao = new LoginRequest();
		mLoginDao.setDaoListener(this);
	}
	
	private void registerEvents(){
		mConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(check()){
					mProgress = new ProgressDialog(LoginActivity.this);
					mProgress.setMessage(getResources().getString(R.string.wait));
					mProgress.setCancelable(true);
					mProgress.setCanceledOnTouchOutside(false);
					mProgress.show();
					sendLoginRequest();
				}
			}
		});
		
		mGoExtra.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
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
		
		mForgetPsw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				   Intent  intent = new Intent(LoginActivity.this, ForgetPswActivity.class);
				
				   startActivity(intent);
			}
		});
	}
	
	private void sendLoginRequest(){
		mLoginDao.phone = mPhone.getText().toString().trim();
		mLoginDao.password = mPass.getText().toString().trim();
		mLoginDao.run();
	}
	
	private boolean check(){
		
		if(mPhone.getText().toString().trim().length()!=11){
			Toast.makeText(this,getResources().getString(R.string.account_number_is_not_standard_phone_number), Toast.LENGTH_SHORT).show();
			return false;
		}
		if(mPass.getText().toString().trim().equals("")){
			Toast.makeText(this,getResources().getString(R.string.pass_is_empty), Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(!mCheck.isChecked()){
			Toast.makeText(this,getResources().getString(R.string.greement_protocal), Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
	
	private void gotoCompleteActivity(){
		Intent intent = new Intent();
		intent.putExtra("from", "login");
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.setClass(LoginActivity.this, RegisterOkActivity.class);
		startActivity(intent);
}

	@Override
	public void loadSuccess(BaseRequest dao) {
		// TODO Auto-generated method stub
		if(mProgress!=null)mProgress.dismiss();
		gotoCompleteActivity();
	}

	@Override
	public void loadFailure(BaseRequest dao) {
		// TODO Auto-generated method stub
		if(mProgress!=null)mProgress.dismiss();
		Toast.makeText(this,getResources().getString(dao.errorStringId), Toast.LENGTH_SHORT).show();
	}	
}
