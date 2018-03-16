package com.citywithincity.ecard.access;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.citywithincity.ecard.R;
import com.citywithincity.utils.Alert;
import com.damai.auto.DMActivity;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.widget.Form;
import com.damai.widget.OnSubmitListener;
import com.damai.widget.SubmitButton;
import com.damai.widget.VerifyButton;

import java.util.Map;

public class ForgetPassActivity extends DMActivity implements OnSubmitListener {

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
		setContentView(R.layout.activity_access_forget_pass);
		setTitle("找回密码");
		submit.setEnabled(false);
		submit.setOnSubmitListener(this);

		code.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0 && verifyButton.getVerifyId() != null) {
					submit.setEnabled(true);
				} else {
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
		if (!data.get("pass1").equals(data.get("pass"))) {
			pass1.setError("重复密码必须和密码一致");
			return false;
		}
		data.put("verify_id", verifyButton.getVerifyId());

		data.remove("pass1");
		return true;
	}

	@JobSuccess("security_api/cp_submit")
	public void onGetPassSuccess(Object data) {
		Alert.showShortToast("找回密码成功");
		finish();
	}
}
