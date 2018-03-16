package com.citywithincity.ecard.pay;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.models.ListDataProvider;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.paylib.ECardPayModel;
import com.citywithincity.paylib.IPay;
import com.citywithincity.paylib.IPay.CashierInfo;
import com.citywithincity.paylib.PayType;
import com.citywithincity.paylib.PayTypeInfo;

@Observer
public class PayCashierActivity extends BaseActivity implements
		IListDataProviderListener<PayTypeInfo>, OnItemClickListener,
		OnClickListener {

	public static final PayTypeInfo[] PAY_INFOS = new PayTypeInfo[] {
			new PayTypeInfo(PayType.PAY_ALIPAY, "支付宝", R.drawable.ali_pay),
			new PayTypeInfo(PayType.PAY_WEIXIN, "微信支付", R.drawable.weixin_pay),
			new PayTypeInfo(PayType.PAY_ETONGKA, "e通卡后台账户",
					R.drawable.ecard_pay),
			new PayTypeInfo(PayType.PAY_UNION, "银联", R.drawable.union_pay), };

	private ListDataProvider<PayTypeInfo> dataProvider;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_pay_cashier);
		setTitle("收银台");
		dataProvider = new ListDataProvider<PayTypeInfo>(this,
				R.layout.item_cashier, this);
		final ListView listView = (ListView) findViewById(R.id._list_view);
		listView.setAdapter(dataProvider);

		ECardPayModel model = ModelHelper.getModel(ECardPayModel.class);
		dataProvider.setData(model.getSupportPayTypes(PAY_INFOS), false);

		listView.setOnItemClickListener(this);

		findViewById(R.id._id_ok).setOnClickListener(this);

		CashierInfo cashierInfo = ModelHelper.getModel(ECardPayModel.class)
				.getCashierInfo();
		((TextView) findViewById(R.id.total_pay_price)).setText(cashierInfo
				.getFormatedPrice());
	}

	@Override
	public void onInitializeView(View view, PayTypeInfo data, int position) {
		ImageView select = (ImageView) view.findViewById(R.id._radio_button);
		ImageView icon = (ImageView) view.findViewById(R.id._image_view);
		TextView textView = (TextView) view.findViewById(R.id._text_view);

		select.setSelected(data.selected);
		icon.setImageDrawable(getResources().getDrawable(data.iconResID));
		textView.setText(data.text);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(ModelHelper.getModel(ECardPayModel.class).setCurrentPayIndex(position)){
			dataProvider.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id._id_ok) {
			ModelHelper.getModel(ECardPayModel.class).prePay(this);
		}
	}

	@NotificationMethod(IPay.PAY_SUCCESS)
	public void onPaySuccess(Object result) {
		finish();
	}

}
