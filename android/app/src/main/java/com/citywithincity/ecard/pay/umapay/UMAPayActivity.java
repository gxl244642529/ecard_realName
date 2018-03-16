package com.citywithincity.ecard.pay.umapay;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.models.LocalData;
import com.damai.auto.DMActivity;
import com.damai.helper.a.Event;
import com.damai.helper.a.Model;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.widget.Form;
import com.damai.widget.OnSubmitListener;
import com.damai.widget.VerifyButton;

import java.util.Map;

public class UMAPayActivity extends DMActivity implements OnSubmitListener {

	private static final String PHONE_KEY = "uma_phone";
	
	
	@Res
	private TextView title;
	
	@Res
	private VerifyButton btnVerify;
	
	@Res
	private Button btnPay;
	
	@Res
	private EditText code;
	
	@Res
	private EditText phone;

    @Res
    private TextView ec_fee;

	@Model
	private UmaModel model;
	private String orderId;
	private int fee;
	private int businessId;
	private int type;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_uma_pay);
		setTitle("移动话费支付");
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String title = bundle.getString("title");
		orderId = bundle.getString("orderId");
		fee = bundle.getInt("fee");
        ec_fee.setText(String.format("%.02f",  (double) fee/100));
		businessId = bundle.getInt("businessId");
		type = bundle.getInt("type");
		this.title.setText(title);
		btnPay.setEnabled(false);
		
		btnVerify.setOnSubmitListener(this);

		String strPhone = LocalData.read().getString(PHONE_KEY, "");

		phone.setText(strPhone);
		
		code.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				btnPay.setEnabled(s.length()>0);
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
	
	@Event
	public void onBtnPay(){
		String phone = this.phone.getText().toString();

		model.pay(String.valueOf( btnVerify.getVerifyId()), phone,code.getText().toString(),orderId, businessId, type, fee,btnPay);

	}
	
	@JobSuccess(UmaModel.verify)
	public void onVerifySuccess(Object object){
        LocalData.write().putString(PHONE_KEY, phone.getText().toString()).save();
	}

	@JobSuccess(UmaModel.pay)
	public void onPaySuccess(Object object){
		this.setResult(RESULT_OK);
		finish();
	}

	@Override
	public boolean shouldSubmit(Form formView, Map<String, Object> data) {
		data.put("fee", fee);
		return true;
	}

}
