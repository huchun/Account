package com.xhtek.hxaccount.ui;

import com.xhtek.hxaccount.R;
import com.xhtek.hxaccount.http.BaseRequest;
import com.xhtek.hxaccount.http.FixPswRequest;
import com.xhtek.hxaccount.util.BaseDAOListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class FixPswActivity extends Activity implements BaseDAOListener{

	   private  EditText  mOldPsw, mNewPsw, mNewToPsw;
	   private  ImageView  mImgeye, mImgToeye;
	   private  Button      mSaveBtn;
	   private   FixPswRequest  mFixDao;
	   private   ProgressDialog  mDialog;

	     @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	// TODO Auto-generated method stub
	    	super.onCreate(savedInstanceState);
	        setContentView(R.layout.setup_account_fixgetpsw);
	        
	        initDatas();
	        initVIews();
	        initListener();
	     }

		private void initDatas() {
		       mFixDao = new FixPswRequest();
			   mFixDao.setDaoListener(this);
		}

		private void initVIews() {
			  mOldPsw = (EditText)findViewById(R.id.old_psw);
			  mNewPsw = (EditText)findViewById(R.id.new_pass);
			  mNewToPsw = (EditText)findViewById(R.id.new_to_pass);
			  mImgeye = (ImageView)findViewById(R.id.eye_pass);
			  mImgToeye = (ImageView)findViewById(R.id.eye_to_pass);
			  mSaveBtn = (Button)findViewById(R.id.saveBtn); 
		}
		
		private void initListener() {
			   mImgeye.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (mImgeye.isSelected()) {
						mImgeye.setSelected(false);
						mNewPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
						
					}else {
						mImgeye.setSelected(true);
						mNewPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
						
					}
					Editable  etext = mNewPsw.getText();
					Selection.setSelection(etext, etext.length());
				}
			});
			
			   mImgToeye.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					  if (mImgToeye.isSelected()) {
						  mImgToeye.setSelected(false);
						  mNewToPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					}else {
						 mImgToeye.setSelected(true);
						 mNewToPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
					}
					Editable etoext = mNewToPsw.getText();
					Selection.setSelection(etoext, etoext.length());		
				}
			});
			   
			   mSaveBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					   if(savecheck()){
						    mDialog = new ProgressDialog(FixPswActivity.this);
						    mDialog.setMessage(getResources().getString(R.string.wait));
						    mDialog.setCancelable(true);
						    mDialog.setCanceledOnTouchOutside(false);
	                        mDialog.show();
	                        SaveFixPswRequest();
					   }				
				}
			});
		}

		protected void SaveFixPswRequest() {
			   mFixDao.oldPassword = mOldPsw.getText().toString().trim();
			   mFixDao.newPassword = mNewPsw.getText().toString().trim();
			   mFixDao.newPassword = mNewToPsw.getText().toString().trim();
			   mFixDao.run();
		}

		protected boolean savecheck() {
			if(mOldPsw.getText().toString().trim().equals("")){
				Toast.makeText(this,getResources().getString(R.string.pass_is_empty), Toast.LENGTH_SHORT).show();
				return false;
			}
			
			if (mOldPsw.equals(" ")) {
				Toast.makeText(this, getResources().getString(R.string.error_old_psw), Toast.LENGTH_SHORT).show();
				return false;
			}
			
			if (mNewPsw.length() > 5 || mNewPsw.length() < 21) {
				Toast.makeText(this, getResources().getString(R.string.new_pass_lenght), Toast.LENGTH_SHORT).show();
				return false;
			}		
			
			if (mNewPsw.getText().toString().equals(" ")) {
				Toast.makeText(this,getResources().getString(R.string.pass_is_empty), Toast.LENGTH_SHORT).show();
				return false;
			}
			
			if (mNewToPsw.getText().toString().equals(" ")) {
				Toast.makeText(this,getResources().getString(R.string.pass_is_empty), Toast.LENGTH_SHORT).show();
				return false;
			}
			
			if (!mNewPsw.equals(mNewToPsw)) {
				Toast.makeText(this, getResources().getString(R.string.password_no_diff), Toast.LENGTH_SHORT).show();
			    return false;
			}
			
			return true;
		}

		private void gotoCompleteActivity() {
			Intent intent = new Intent();
			intent.putExtra("from", "fixPsw");
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent .setClass(FixPswActivity.this, RegisterOkActivity.class);
			startActivity(intent);
		}
		
		@Override
		public void loadSuccess(BaseRequest dao) {
			if (dao.equals(mFixDao)) {
				gotoCompleteActivity();
				 Toast.makeText(getApplicationContext(), R.string.fix_seccess, Toast.LENGTH_SHORT).show();			
			}			
		}

		@Override
		public void loadFailure(BaseRequest dao) {
			     Toast.makeText(this, R.string.fix_failed, Toast.LENGTH_SHORT).show();
			
		}
}
