package com.citywithincity.ecard.selling.activities;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.utils.ActivityUtil;

public class SPayResultActivity extends BaseActivity implements OnClickListener {
	
	private View container;
	private TextView faildText,successtText,tip;
	private Button goShopping,goOrderDetail;
	@SuppressWarnings("unused")
	private String orderId;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_spay_result);
		setTitle("支付");
		
		init();
		
		Bundle bundle = getIntent().getExtras();
		orderId = bundle.getString("orderId");
//		int price = bundle.getInt("price");
		int result = bundle.getInt("result");
		boolean isSuccess = false;
		if (result == 9000) {
			isSuccess = true;
		} else {
			switch (result) {
			case 8000:
				faildText.setText("正在处理中");
				break;
			case 6001:
				faildText.setText("用户中途取消");
				break;
			case 6002:
				faildText.setText("网络连接出错");
				break;
			case 4000:
				faildText.setText("订单支付失败");
				break;

			default:
				break;
			}
			isSuccess = false;
		}
		showState(isSuccess);
	}
	
	private void init() {
		container = findViewById(R.id._container);
		faildText = (TextView) findViewById(R.id.faild_text);
		successtText = (TextView) findViewById(R.id.success_text);
		tip = (TextView) findViewById(R.id.tip);
		goShopping = (Button) findViewById(R.id.go_shopping);
		goOrderDetail = (Button) findViewById(R.id.go_order);
		
		container.setOnClickListener(this);
		goOrderDetail.setOnClickListener(this);
		goShopping.setOnClickListener(this);
	}
	
	private void showState(boolean isSuccess) {
		if (isSuccess) {
			faildText.setVisibility(View.GONE);
			tip.setVisibility(View.INVISIBLE);
			successtText.setVisibility(View.VISIBLE);
			container.setClickable(false);
		} else {
			faildText.setVisibility(View.VISIBLE);
			tip.setVisibility(View.VISIBLE);
			successtText.setVisibility(View.GONE);
			container.setClickable(true);
		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.go_order:
				ActivityUtil.startActivity(SPayResultActivity.this, SAllMyOrderActivity.class);
				finish();
			
			break;
			
		case R.id.go_shopping:
			ActivityUtil.startActivity(SPayResultActivity.this, SMainActivity.class);
			finish();
			break;
		
		case R.id._container:
			finish();
			break;

		default:
			break;
		}
		
	}

}
