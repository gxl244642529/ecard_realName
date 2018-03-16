package com.citywithincity.ecard.discard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.discard.models.DiscardModel;
import com.citywithincity.ecard.discard.pay.DiscardPayActionHandler;
import com.citywithincity.ecard.discard.vos.BookInfo;
import com.damai.auto.DMFragmentActivity;
import com.damai.helper.DataHolder;
import com.damai.helper.OnSelectDataListener;
import com.damai.helper.a.Event;
import com.damai.helper.a.Model;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.pay.PayActionHandler;
import com.damai.widget.DefAddressView;
import com.damai.widget.vos.AddressVo;

import org.json.JSONException;
import org.json.JSONObject;
public class DiscardBuyActivity extends DMFragmentActivity implements
		OnCheckedChangeListener, OnSelectDataListener<AddressVo> {

	private PayActionHandler handler;

	@Model
	private DiscardModel model;
	@Res
	private TextView total_pay_price;
	@Res
	private TextView price;

	@Res
	private ImageView thumb;
	@Res
	private Button submit;

	private BookInfo bookInfo;

	private AddressVo address;
	
	@Res
	private CheckBox checkBox;

	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_discard_buy);
		setTitle("买卡");



		submit.setEnabled(false);
		bookInfo = DataHolder.get(getClass());
		// 总价格
		total_pay_price.setText(bookInfo.getFormatTotalPrice(false));

		DefAddressView addressView = (DefAddressView) findViewById(R.id._address_container);
		addressView.setOnSelectDataListener(this);
		handler = new DiscardPayActionHandler(this);

		thumb.setImageResource(bookInfo.getThumb());
		price.setText(bookInfo.formatCardPrice());
		
		checkBox.setOnCheckedChangeListener(this);
	}

	@JobSuccess(DiscardModel.SUBMIT)
	public void onSubmitSuccess(JSONObject json) throws JSONException {
		handler.startup(String.valueOf(json.get("order_id")),
				json.getInt("fee"));
		startActivity(new Intent( getPackageName() + ".action.PAYCASHIER"));
	}

	@Event
	public void submit() {
		// 去付款,提交订单
		if (model == null) {
			model = new DiscardModel();
		}
		model.submit(checkBox.isChecked(), bookInfo, address, submit);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		total_pay_price.setText(bookInfo.getFormatTotalPrice(isChecked));
	}

	@Override
	public void onSelectData(AddressVo data) {
		submit.setEnabled(true);
		address = data;
	}

}
