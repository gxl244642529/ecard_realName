package com.citywithincity.ecard.access;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.citywithincity.ecard.NewJavaServerHandler;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.citywithincity.ecard.react.AccountModule;
import com.citywithincity.utils.PackageUtil;
import com.damai.auto.DMActivity;
import com.damai.core.DMAccount;
import com.damai.core.DMLib;
import com.damai.core.LoginListener;
import com.damai.core.LoginListenerWrap;
import com.damai.helper.a.Event;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.push.Push;
import com.damai.widget.Form;
import com.damai.widget.OnSubmitListener;
import com.damai.widget.SubmitButton;
import com.damai.widget.VerifyButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RegisterActivity extends DMActivity implements OnSubmitListener {

	@Res
	private SubmitButton submit;
	
	@Res
	private EditText pass1;
	
	@Res
	private EditText code;
	
	@Res
	private VerifyButton verifyButton;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_access_register);
		setTitle("注册");
		submit.setEnabled(false);
		submit.setOnSubmitListener(this);
        findViewById(R.id.cantRecvMsg).setVisibility(View.GONE);
		
		code.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length() > 0  && verifyButton.getVerifyId() != null){
					submit.setEnabled(true);
				}else{
					submit.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public boolean shouldSubmit(Form formView, Map<String, Object> data) {
		if(!data.get("pass1").equals(data.get("pwd"))){
			pass1.setError("重复密码必须和密码一致");
			return false;
		}
		data.put("verifyId", verifyButton.getVerifyId());
		data.put("pushID", Push.getPushId());
		data.put("platform", "android");
		data.put("version", PackageUtil.getVersionCode(this));
		
		data.remove("pass1");
		return true;
	}
	
	@JobSuccess("passport/register")
	public void onRegisterSuccess(JSONObject data) throws JSONException {
		NewJavaServerHandler.runningInstance().onGetToken( data.getJSONObject("token") );
		ECardUserInfo account = DMAccount.get();
		Map<String, Object> params = submit.getJob().getParams();
		String phone = (String) verifyButton.getJob().getParams().get("phone");
		account.fromJson(data,phone,(String)params.get("pwd"));
		LoginListener listener = LoginListenerWrap.getInstance().getListener();
		if(listener!=null){
			listener.onLoginSuccess();
		}
		Push.onLogin(account);
		AccountModule.onLoginSuccess(account);
		setResult(RESULT_OK);
		finish();
	}

	private static final int REQUEST_REGISTER = 101;
	/**
	 * 收不到
	 */
	@Event
	public void cantRecvMsg(){
		startActivityForResult(new Intent(getActivity(),RegisterDirectActivity.class),REQUEST_REGISTER);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(requestCode == REQUEST_REGISTER){
				setResult(RESULT_OK);
				finish();
			}
		}
	}
}
