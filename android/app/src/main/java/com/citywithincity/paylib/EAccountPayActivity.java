package com.citywithincity.paylib;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.pay.ECardAccountPayModel;
import com.citywithincity.ecard.recharge.activities.RechargeECardActivity;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.models.LocalData;
import com.citywithincity.utils.Alert;
import com.damai.auto.DMActivity;
import com.damai.auto.LifeManager;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.helper.a.Model;
import com.damai.http.api.a.JobSuccess;


public class EAccountPayActivity extends DMActivity implements
		OnClickListener, ApiListener {
	
	@Model
	private ECardAccountPayModel model;
	
	
	private EditText txtAccount;
	private EditText txtPwd;
	private TextView txtFee;
	private String platId;
	private Button btnPay;
	private String key;
	
	public final String E_ACCOUNT = "e_account_recored";

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_ecard_account_pay);
		setTitle("e通卡账户支付");
		// 获取信息
		txtAccount = (EditText) findViewById(R.id.ec_account);
		String eAccount = LocalData.read().getString(E_ACCOUNT, "");
		if (!TextUtils.isEmpty(eAccount)) {
			txtAccount.setText(eAccount);
		}
		txtPwd = (EditText) findViewById(R.id.ec_pwd);
		txtFee = (TextView) findViewById(R.id.ec_fee);
		btnPay = (Button) findViewById(R.id._id_ok);
		Bundle bundle = getIntent().getExtras();
		String fee = bundle.getString("fee");
		platId = bundle.getString("platId");
		key = bundle.getString("sign");
		txtFee.setText(fee);

		btnPay.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// 支付
		String account = txtAccount.getText().toString();
		if (TextUtils.isEmpty(account)) {
			Alert.showShortToast("请输入账号");
			return;
		}

		String pwd = txtPwd.getText().toString();
		if (TextUtils.isEmpty(pwd)) {
			Alert.showShortToast("请输入密码");
			return;
		}
		LocalData.write().putString(E_ACCOUNT, account).destroy();
		
		model.pay(account, pwd,key,platId,btnPay,this);
		
	}


    @Override
    public void onBackPressed() {
        backToPrevious();
    }

    @Override
	protected void backToPrevious() {
		Alert.confirm(this, "温馨提示", "取消付款吗?", new DialogListener() {

			@Override
			public void onDialogButton(int id) {
				if (id == R.id._id_ok) {
                    setResult(RESULT_CANCELED);
                    finish();
				}
			}
		});
	}


	public void onError(final String error, final boolean isNetworkError){
        Alert.alert(this,"温馨提示",error,new DialogListener(){
            @Override
            public void onDialogButton(int i) {
                Intent intent = new Intent();
                intent.putExtra("error",error);
                intent.putExtra("isNetworkError",isNetworkError);

                setResult(ECardAccountPayModel.RESULT_ERROR,intent);
                finish();
            }
        });
	}

	@Override
	public boolean onApiMessage(ApiJob apiJob) {
		onError(apiJob.getMessage().getMessage(),false);
		return true;
	}

	@Override
	public boolean onJobError(ApiJob apiJob) {
		onError(apiJob.getError().getReason(),apiJob.getError().isNetworkError());
		return true;
	}

	@Override
	public void onJobSuccess(ApiJob apiJob) {
		setResult(RESULT_OK);
		finish();
	}
}
