package com.citywithincity.ecard.access;

import android.os.Bundle;
import android.widget.EditText;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.citywithincity.utils.PackageUtil;
import com.damai.auto.DMActivity;
import com.damai.core.DMAccount;
import com.damai.core.DMLib;
import com.damai.core.LoginListener;
import com.damai.core.LoginListenerWrap;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.push.Push;
import com.damai.widget.Form;
import com.damai.widget.OnSubmitListener;
import com.damai.widget.SubmitButton;

import org.json.JSONObject;

import java.util.Map;


public class RegisterDirectActivity extends DMActivity implements OnSubmitListener {
	@Res
	private SubmitButton submit;
	@Res
	private EditText pass1;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_access_register_dir);
		setTitle("注册新用户");
		submit.setOnSubmitListener(this);
	}


	@Override
	public boolean shouldSubmit(Form formView, Map<String, Object> data) {
		if(!data.get("pass1").equals(data.get("pwd"))){
			pass1.setError("重复密码必须和密码一致");
			return false;
		}
		data.put("pushID", DMLib.getJobManager().getPushId());
		data.put("platform", "android");
		data.put("version", PackageUtil.getVersionCode(this));
		
		data.remove("pass1");
		return true;
	}
	@JobSuccess("pass_api/register")
	public void onRegisterSuccess(JSONObject data){
		ECardUserInfo account = DMAccount.get();
		Map<String, Object> params = submit.getJob().getParams();
		String phone = (String) params.get("username");
		account.fromJson(data,phone,(String)params.get("pwd"));
		LoginListener listener = LoginListenerWrap.getInstance().getListener();
		if(listener!=null){
			listener.onLoginSuccess();
		}
		Push.onLogin(account);
		setResult(RESULT_OK);
		finish();
	}

}
