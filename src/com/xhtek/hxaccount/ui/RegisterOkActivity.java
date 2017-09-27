package com.xhtek.hxaccount.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.xhtek.hxaccount.R;

public class RegisterOkActivity extends Activity {

	private Button mBack, mFixPsw, mComplete;
	private TextView mContent,mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_ok);
		String from = getIntent().getStringExtra("from");
		mTitle = (TextView)findViewById(R.id.register_tag);
		if(from.equals("login")){
			mTitle.setText("登录成功");
		}else{
			mTitle.setText("注册成功");
		}
		mBack = (Button)findViewById(R.id.prev_button);
		mContent = (TextView)findViewById(R.id.content);
		mFixPsw = (Button)findViewById(R.id.fixpassword);
		mContent.setText(getResources().getString(R.string.your_account)+getIntent().getStringExtra("account")+"\n"+getResources().getString(R.string.the_purpose_of_account)+"\n暂时还未跟系统数据库对接");
		mComplete =  (Button)findViewById(R.id.complete);
		setEvents();
	}

	private void setEvents(){
		
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mComplete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mFixPsw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				  Intent  intent = new Intent(RegisterOkActivity.this, FixPswActivity.class);
				
				  startActivity(intent);
			}
		});
	}
}
