package com.xhtek.hxaccount.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xhtek.hxaccount.R;

public class RegisterDialog extends Dialog{

	private Context  context;
	private String   phone,testcode="";
	private TextView getvertify;
	private EditText edit;
	private Button   vertify;
	private OnRegisterConfirmIml mOnRegisterConfirmIml;

	public RegisterDialog(Context context,String phone) {
		super(context);
		// T1ODO Auto-generated constructor stub
		this.context = context;
		this.phone = phone;
	}
	
	public RegisterDialog(Context context,String phone,String testcode) {
		super(context);
		// T1ODO Auto-generated constructor stub
		this.context = context;
		this.phone = phone;
		this.testcode = testcode;
	}
	
	public void setRegisterConfirmIml(OnRegisterConfirmIml iml){
		this.mOnRegisterConfirmIml = iml;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.register_dialog);
        TextView title = (TextView)findViewById(R.id.title);
        getvertify =  (TextView)findViewById(R.id.get_vertify);
        edit =  (EditText)findViewById(R.id.vertify_edit);
        if(!testcode.equals("")){
        	edit.setText(testcode);
        }
        vertify = (Button)findViewById(R.id.vertify_bt);
        title.setText("已发送一条验证短信至"+phone);    
        setEvents();
    }

    public void setVertify(String identify){
    	edit.setText(identify);
    }   
    
    private void setEvents(){
    	
    	getvertify.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				edit.setText("");
				mOnRegisterConfirmIml.recheck();			
				startCount();
			}
		});
    	vertify.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(edit.getText().equals("")||edit.getText().length()<4){
					Toast.makeText(context,context.getResources().getString(R.string.identify_code_lenght), Toast.LENGTH_SHORT).show();  
				}else{
					mOnRegisterConfirmIml.confirm(edit.getText().toString().trim());
				}	
			}
		});	
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
		 getvertify.setEnabled(false);
		 mCurrent = 0;
		 if (mHandler == null) {
			   mHandler = new Handler(new Handler.Callback() {
				
				@Override
				public boolean handleMessage(Message arg0) {
				     mCurrent++;
				     if (COUNT_MAX - mCurrent > 0) {
					       getvertify.setText(getContext().getString(R.string.verfity_account, COUNT_MAX  -  mCurrent));
					       getvertify.setTextColor(getvertify.getResources().getColor(android.R.color.holo_red_dark));
					       mHandler.sendEmptyMessageDelayed(0, 1000);
					}else {
						getvertify.setText(getContext().getText(R.string.verfity_get_account));
						getvertify.setTextColor(getContext().getResources().getColor(android.R.color.white));
						getvertify.setEnabled(true);
						mIsCounting = false;
					}
					return true;
				}
			});  
		}
		 mHandler.sendEmptyMessage(0);
	}

}
