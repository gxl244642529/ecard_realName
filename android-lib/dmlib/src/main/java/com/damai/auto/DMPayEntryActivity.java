package com.damai.auto;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.core.DMLib;
import com.damai.lib.R;
import com.damai.pay.platform.WXPay;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class DMPayEntryActivity extends DMActivity implements IMessageListener,IWXAPIEventHandler {
	protected IWXAPI api;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout._dialog_wait);
		TextView textView = (TextView) findViewById(R.id._text_view);
		textView.setText("正在拉取订单信息...");
		if(DMLib.getPayModel()!=null){
			WXPay pay = DMLib.getPayModel().getPay();
			pay.handleIntent(getIntent(),this);
		}else{
			initPay(getIntent());
			
		}
	}
	
	protected void initPay(Intent intent){
		
	}

	
	@Override
	public void onBackPressed() {

	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		if(DMLib.getPayModel()!=null){
			WXPay pay = DMLib.getPayModel().getPay();
			pay.handleIntent(getIntent(),this);
		}else{
			initPay(intent);
		}
		
	}

	private int errCode;

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			errCode = resp.errCode;
			MessageUtil.sendMessage(this);
		}
	}

	@Override
	public void onMessage(int message) {
		
		if(DMLib.getPayModel()!=null){
			WXPay pay = DMLib.getPayModel().getPay();
			if(errCode==0){
				//去拉取信息
				pay.onPaySuccess();
			}else if(errCode == -1){
				pay.onPayError("支付失败");
				finish();
			}else{
				pay.onPayCancel();
				finish();
			}
			
		}else{
			handlerPayResult(errCode);
		}
	}


	protected void handlerPayResult(int errCode2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}
}
