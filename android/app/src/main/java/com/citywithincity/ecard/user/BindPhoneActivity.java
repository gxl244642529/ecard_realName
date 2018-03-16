package com.citywithincity.ecard.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.KeyboardUtil;

public class BindPhoneActivity extends BaseActivity implements OnClickListener {

	private String phoneNum;
	private TextView timeDown;
	private MyDownTime count;
	private View titleRight;
	private EditText phone;
	private EditText verify;
	private int verifyId;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_bind_phone);
		setTitle("绑定手机");

		
		init();
	}
	
	private void init() {
		titleRight = findViewById(R.id._title_right);
		timeDown = (TextView) findViewById(R.id.id_time);
		phone = (EditText) findViewById(R.id.phone);
		verify = (EditText) findViewById(R.id.check_num);
		
		titleRight.setOnClickListener(this);
		
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
			bindVerify();
			break;

		default:
			break;
		}
	}
	
	private void getVerify(String phone) {
		ModelHelper.getModel(UserModel.class).bindPhoneCheck(phone, new IRequestResult<Integer>() {
			
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
	
	private void bindVerify(){
		
		String verifyCode = verify.getText().toString().trim();
		phoneNum = phone.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNum)) {
			Alert.showShortToast("请输入手机号码");
			return;
		}
		
		KeyboardUtil.hideSoftKeyboard(getActivity(),getActivity().getCurrentFocus());
		
		ModelHelper.getModel(UserModel.class).bindPhone(verifyId, verifyCode, new IRequestResult<Object>() {
			
			@Override
			public void onRequestSuccess(Object result) {
				Alert.showShortToast("手机绑成功");
				phoneNum = (String) result;
//				Intent intent = new Intent();
//				intent.putExtra("username", phoneNum);
//				intent.putExtra("password",pwd);
//				//完成
//				setResult(Activity.RESULT_OK,intent);
				finish();
			}
			
			@Override
			public void onRequestError(String errMsg, boolean isNetworkError) {
				Alert.showShortToast(errMsg);
			}
		});
	}

}
