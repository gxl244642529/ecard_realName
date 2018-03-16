package com.citywithincity.ecard.access;

import android.content.Intent;
import android.os.Bundle;

import com.citywithincity.ecard.NewJavaServerHandler;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.citywithincity.ecard.react.AccountModule;
import com.citywithincity.utils.PackageUtil;
import com.damai.auto.DMActivity;
import com.damai.core.DMAccount;
import com.damai.core.LoginListener;
import com.damai.core.LoginListenerWrap;
import com.damai.helper.a.Event;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.push.Push;
import com.damai.widget.Form;
import com.damai.widget.OnSubmitListener;
import com.damai.widget.SubmitButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 
 * @author renxueliang
 *
 */
public class LoginActivity extends DMActivity implements OnSubmitListener {
	
	private static final String LOGIN_API = "passport/login";
	
	@Res
	private SubmitButton login_btn;
	
	private boolean succeed;


	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_access_login);
		setTitle("登录");
		login_btn.setOnSubmitListener(this);
	}
	/**
	 * 登陆成功
	 * @param data
	 */
	@JobSuccess(LOGIN_API)
	public void onLoginSuccess(JSONObject data) throws JSONException {
		succeed = true;
		NewJavaServerHandler.runningInstance().onGetToken( data.getJSONObject("token") );
		ECardUserInfo account = DMAccount.get();
		Map<String, Object> params = login_btn.getJob().getParams();
		account.fromJson(data,(String)params.get("phone"),(String)params.get("pwd"));
		LoginListener listener = LoginListenerWrap.getInstance().getListener();
		if(listener!=null){
			listener.onLoginSuccess();
		}
		Push.onLogin(account);
		AccountModule.onLoginSuccess(account);

		finish();
	}
	
	private static final int REQUEST_REGISTER = 101;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(requestCode == REQUEST_REGISTER){
				finish();
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		onCancel();
		super.onBackPressed();
	}
	
	void onCancel(){
		if(!succeed){
			LoginListener listener = LoginListenerWrap.getInstance().getListener();
			if(listener!=null){
				listener.onLoginCancel();
			}
		}
	}

	@Override
	public boolean shouldSubmit(Form formView, Map<String, Object> data) {
		data.put("platform", "android");
		data.put("version", PackageUtil.getVersionCode(this));
		data.put("pushID", Push.getPushId());
		
		return true;
	}

	
	@Event
	public void onForgetPass(){
		startActivity(new Intent(this,ForgetPassActivity.class));
	}
	
	@Event
	public void onRegister(){
		startActivityForResult(new Intent(this,RegisterActivity.class),REQUEST_REGISTER);
	}
}
