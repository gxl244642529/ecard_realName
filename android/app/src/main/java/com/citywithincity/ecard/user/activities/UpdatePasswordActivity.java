package com.citywithincity.ecard.user.activities;


import android.os.Bundle;
import android.widget.EditText;

import com.citywithincity.ecard.R;
import com.citywithincity.utils.Alert;
import com.damai.auto.DMActivity;
import com.damai.core.DMAccount;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.widget.Form;
import com.damai.widget.OnSubmitListener;
import com.damai.widget.SubmitButton;

import java.util.Map;

/**
 * 更新密码
 * @author Randy
 *
 */
public class UpdatePasswordActivity extends DMActivity implements OnSubmitListener {

	@Res
	private SubmitButton submit;
	
	@Res
	private EditText newPwd1;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView( R.layout.activity_user_update_password);
		setTitle("修改密码");
		submit.setOnSubmitListener(this);
	}
	
	
	@JobSuccess("user/updatePassword")
	public void onUpdatePass(Object value){
		Alert.showShortToast("修改密码成功");
		DMAccount account = DMAccount.get();
		account.setPassword((String)submit.getJob().getParams().get("newPwd"));
		account.save();
		finish();
	}


	@Override
	public boolean shouldSubmit(Form formView, Map<String, Object> data) {
		if(!data.get("newPwd").equals(data.get("newPwd1"))){
			newPwd1.setError("确认密码不一致");
			return false;
		}
		return true;
	}
	
}
