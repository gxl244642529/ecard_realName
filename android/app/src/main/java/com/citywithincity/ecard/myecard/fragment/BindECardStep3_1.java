package com.citywithincity.ecard.myecard.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citywithincity.ecard.R;
import com.damai.auto.DMFragment;
import com.damai.helper.a.Res;
import com.damai.widget.Form;
import com.damai.widget.OnSubmitListener;
import com.damai.widget.SubmitButton;

import java.util.Map;


public class BindECardStep3_1 extends DMFragment implements OnSubmitListener{

	@Res
	private SubmitButton btn_bind_ecard;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_bind_step3_1, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		btn_bind_ecard.setOnSubmitListener(this);
	}

	@Override
	public boolean shouldSubmit(Form formView, Map<String, Object> data) {
		data.put("pretype", 1);
		data.put("last", 0);
		return true;
	}
	

}
