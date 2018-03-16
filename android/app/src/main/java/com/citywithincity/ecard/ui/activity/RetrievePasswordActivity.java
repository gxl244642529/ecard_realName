package com.citywithincity.ecard.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.ECardModel;
import com.citywithincity.ecard.user.MyDownTime;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.KeyboardUtil;
import com.citywithincity.utils.MD5Util;
/**
 * 找回密码
 * @author admin
 *
 */
public class RetrievePasswordActivity extends BaseActivity implements OnClickListener{

	private String phoneNum;
	private TextView timeDown;
	private MyDownTime count;
	private View titleRight;
	private EditText phone;
	private EditText pass1;
	private EditText pass2;
	private EditText verify;
	private Button toggleButton;
	private Button toggleButton2;
	private String pwd;
	private int verifyId;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_retrieve_password);
		setTitle("找回密码");
		
		init();
		
	}
	
	private void init() {
		titleRight = findViewById(R.id._title_right);
		timeDown = (TextView) findViewById(R.id.id_time);
		phone = (EditText) findViewById(R.id.phone);
		verify = (EditText) findViewById(R.id.check_num);
		pass1 = (EditText) findViewById(R.id.password);
		pass2 = (EditText) findViewById(R.id.update_password);
		toggleButton = (Button) findViewById(R.id.toggle_button);
		toggleButton2 = (Button) findViewById(R.id.toggle_button_2);
		
		titleRight.setOnClickListener(this);
		toggleButton.setOnClickListener(this);
		toggleButton2.setOnClickListener(this);
		
		findViewById(R.id.register_btn).setOnClickListener(this);
	}
    
    @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id._title_right:
			phoneNum = phone.getText().toString().trim();
			if (TextUtils.isEmpty(phoneNum)) {
				Alert.showShortToast("请输入手机号码");
			} else {
				getVerify(phoneNum);
			}
			break;
		case R.id.register_btn:
			retrieveVerify();
			break;
		case R.id.toggle_button:
			if(toggleButton.getText().toString().equals("显示")){
				toggleButton.setText("隐藏");
				pass1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				
			}else{
				toggleButton.setText("显示");
				pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			break;
		case R.id.toggle_button_2:
			if(toggleButton2.getText().toString().equals("显示")){
				toggleButton2.setText("隐藏");
				pass2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				
			}else{
				toggleButton2.setText("显示");
				pass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			break;

		default:
			break;
		}
	}
    
    private void retrieveVerify(){
		
		String verifyCode = verify.getText().toString().trim();
		String new_pwd = pass1.getText().toString().trim();
		String new_pwd2 = pass2.getText().toString().trim();
		phoneNum = phone.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNum)) {
			Alert.showShortToast("请输入手机号码");
			return;
		}
		
		if(!new_pwd.equals(new_pwd2))
		{
			Alert.showShortToast("确认密码和新密码不一致");
			return;
		}
		
		if(TextUtils.isEmpty(verifyCode))
		{
			Alert.showShortToast("请输入验证码");
			return;
		}
		if(TextUtils.isEmpty(new_pwd))
		{
			Alert.showShortToast("请输入新密码");
			return;
		}
		
		if(TextUtils.isEmpty(new_pwd2))
		{
			Alert.showShortToast("请输入确认密码");
			return;
		}
		
		int len = new_pwd.length();
		
		if(len < 6)
		{
			Alert.showShortToast("密码至少必须有6位");
			return;
		}
		pwd = new_pwd;
		KeyboardUtil.hideSoftKeyboard(getActivity(),getActivity().getCurrentFocus());
		
		ECardModel.getInstance().cpSubmit(verifyId, MD5Util.md5Appkey(new_pwd), verifyCode, new IRequestResult<Boolean>() {
			
			@Override
			public void onRequestSuccess(Boolean result) {
				Alert.showShortToast("找回密码成功");
				//完成
//				ActivityUtil.startActivity(RetrievePasswordActivity.this, LoginActivity.class);
//				finish();
				Intent intent = new Intent();
				intent.putExtra("username", phoneNum);
				intent.putExtra("password",pwd);
				//完成
				setResult(Activity.RESULT_OK,intent);
				finish();
			}
			
			@Override
			public void onRequestError(String errMsg, boolean isNetworkError) {
				Alert.showShortToast(errMsg);
			}
		});
	}
    
    private void getVerify(String phone) {
		ECardModel.getInstance().cpVerify(phone, new IRequestResult<Integer>() {
			
			@Override
			public void onRequestSuccess(Integer result) {
				verifyId = result;
				count = new MyDownTime(60000, 1000,titleRight,timeDown);
				count.start();
				titleRight.setClickable(false);
			}
			
			@Override
			public void onRequestError(String errMsg, boolean isNetworkError) {
				Alert.showShortToast(errMsg);
				
			}
		});
	}

}
