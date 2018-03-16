package com.citywithincity.ecard.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.MyECardModel;
import com.citywithincity.ecard.ui.base.BaseFragment;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.KeyboardUtil;

@Observer
public class BindECardStep3_1 extends BaseFragment implements OnClickListener, OnEditorActionListener {

	private EditText _txtECard;
	private EditText _txtRemark;

	// private Button btnBind;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bind_step3_1, null);
		view.findViewById(R.id.btn_bind_ecard).setOnClickListener(this);

		_txtECard = (EditText) view.findViewById(R.id.txt_bind_ecard);
		_txtRemark = (EditText) view.findViewById(R.id.txt_ecard_remark);
		_txtRemark.setOnEditorActionListener(this);
		KeyboardUtil.showSoftKeyboard(getActivity(),_txtECard,500);
		//view.findViewById(R.id.btn_bind_ecard).setBackgroundResource(SystemUtil.getCurrentSelect(getActivity()) == 1 ? R.drawable.common_button_bg : R.drawable.travel_button_selector);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		// 绑定e通卡
		String cardNumber = _txtECard.getText().toString().trim();
		String remark = _txtRemark.getText().toString().trim();
		if (TextUtils.isEmpty(cardNumber)) {
			// _txtMessage.setText(R.string.input_ecard);
			Alert.alert(getActivity(), "提示", getResources().getString(R.string.input_ecard));
			return;
		}
		
		// btnBind.setEnabled(false);
		Alert.wait(getActivity(), R.string.waiting_bind);
		
		ModelHelper.getModel(MyECardModel.class).bindECard(cardNumber, remark, 1 , 0);
		//MyECardModel.getInstance().bindECard(cardNumber, remark, true, getActivity());
	}
	
	@NotificationMethod(MyECardModel.ECARD_BIND)
	public void onBindSuccess() {
		Alert.cancelWait();
	}


	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId==EditorInfo.IME_ACTION_DONE)
		{
			onClick(null);
			return true;
		}
		return false;
	}

	@Override
	public void onDestroy() {
		Alert.cancelWait();
		super.onDestroy();
	}

}
