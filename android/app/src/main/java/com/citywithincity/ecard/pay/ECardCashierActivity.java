package com.citywithincity.ecard.pay;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.damai.auto.DMActivity;
import com.damai.core.DMLib;
import com.damai.interfaces.IAdapterListener;
import com.damai.pay.DMPay;
import com.damai.pay.DMPayModel;

public class ECardCashierActivity extends DMActivity implements IAdapterListener<DMPay>, OnClickListener {

	private ListView listView;
	private View btnPay;
	private DMPayModel payModel;
	/**
	 * 
	 */
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_ecard_cashier);
		setTitle("收银台");
		
		payModel = DMLib.getPayModel();//.getFormatFee();
		TextView textView = (TextView) findViewById(R.id.total_pay_price);
		textView.setText(payModel.getFormatFee());
		listView = (ListView) findViewById(R.id._list_view);
		payModel.setListView(listView, R.layout.item_ecard_cashier, this);
		btnPay = findViewById(R.id._id_ok);
		btnPay.setOnClickListener(this);
		
	}
	
	
	@Override
	protected void backToPrevious() {
		Alert.confirm(this, "确认要取消付款吗?", new DialogListener() {
			
			@Override
			public void onDialogButton(int id) {
				if(id==R.id._id_ok){
					DMLib.getPayModel().onUserCancel();
				}
			}
		});
		
	}
	


	@Override
	public void onInitializeView(View view, DMPay data, int position) {
		ECardCashierItem b = (ECardCashierItem) view;
		b.setData(data);
	}

	@Override
	public void onClick(View v) {
		//去支付
		payModel.prePay(v);
	}
}
