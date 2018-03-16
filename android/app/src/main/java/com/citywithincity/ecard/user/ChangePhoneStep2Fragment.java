package com.citywithincity.ecard.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;

public class ChangePhoneStep2Fragment extends Fragment implements OnClickListener {
	
	private EditText phoneView;
	private EditText verifyView;
	private int verifyId;
	private MyDownTime downTime;
	private Button btnVerify;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_change_phone_step2, null);
		
		phoneView = (EditText) view.findViewById(R.id.phone);
		verifyView = (EditText) view.findViewById(R.id.check_num);
		btnVerify = (Button) view.findViewById(R.id._id_ok);
		
		view.findViewById(R.id.register_btn).setOnClickListener(this);
		btnVerify.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.register_btn) {
			String code =  verifyView.getText().toString().trim();
			if (TextUtils.isEmpty(code)) {
				Alert.showShortToast("请输入验证码");
				return;
			}
			verify(verifyId, code);
		}
		if (v.getId() == R.id._id_ok) {
			String phoneNum =  phoneView.getText().toString().trim();
			if (TextUtils.isEmpty(phoneNum)) {
				Alert.showShortToast("请输入手机号码");
				return;
			}
			getVerify(phoneNum);
			downTime = new MyDownTime(60000, 1000, btnVerify, btnVerify);
			downTime.start();
			btnVerify.setClickable(false);
		}
		
	}
	
	private void verify(int verifyId, String code) {
		ModelHelper.getModel(UserModel.class).bindChangePhone(verifyId, code, new IRequestResult<Object>() {

			@Override
			public void onRequestError(String errMsg, boolean isNetworkError) {
				if (isNetworkError) {
					Alert.showShortToast("网络错误，请重试");
				} else {
					Alert.showShortToast(errMsg);
				}
			}

			@Override
			public void onRequestSuccess(Object result) {
				System.out.println(result);
				getActivity().finish();
			}
		});
	}
	
	private void getVerify(String phone) {
		ModelHelper.getModel(UserModel.class).bindChangePhoneCheck(phone,new IRequestResult<Integer>() {

			@Override
			public void onRequestError(String errMsg, boolean isNetworkError) {
				if (isNetworkError) {
					Alert.showShortToast("网络错误，请重试");
				} else {
					Alert.showShortToast(errMsg);
				}
				
			}
			
			@Override
			public void onRequestSuccess(Integer result) {
				verifyId = result;
			}
		});
	}
	
}
