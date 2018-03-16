package com.citywithincity.ecard.insurance.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.vos.InsurancePaySuccessNotifyVo;
import com.citywithincity.ecard.pay.PayCashierActivity;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.ListDataProvider;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.paylib.ECardPayModel;
import com.citywithincity.paylib.IPay;
import com.citywithincity.paylib.PayTypeInfo;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ViewUtil;

@Observer
@EventHandler
public class InsuranceCashierActivity extends BaseActivity implements IListDataProviderListener<PayTypeInfo>, OnItemClickListener {

	private ListDataProvider<PayTypeInfo> dataProvider;
	private InsurancePaySuccessNotifyVo data;
	
	/* (non-Javadoc)
	 * @see com.citywithincity.activities.BaseActivity#onSetContent(android.os.Bundle)
	 */
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_cashier);
		setTitle("支付");
		
		data = (InsurancePaySuccessNotifyVo) getIntent().getExtras().get(LibConfig.DATA_NAME);

		dataProvider = new ListDataProvider<PayTypeInfo>(this,
				R.layout.item_cashier, this);
		final ListView listView = (ListView) findViewById(R.id._list_view);
		listView.setAdapter(dataProvider);
		
		ECardPayModel model = ModelHelper.getModel(ECardPayModel.class);
		dataProvider.setData(model.getSupportPayTypes(PayCashierActivity.PAY_INFOS), false);
		
		listView.setOnItemClickListener(this);
		
		((TextView) findViewById(R.id.total_pay_price)).setText(String.format("%.2f", (float)data.fee/100));
		
		initSummary(data.summary);
		ViewUtil.setBinddataViewValues(data, this);
		
		if (TextUtils.isEmpty(data.cardId)) {
			findViewById(R.id._container).setVisibility(View.GONE);
		}
		
	}
	
	private void initSummary(String summary) {
		
		String[] summaries = new String[3];
		
		int index = summary.indexOf("^");
		summaries = new String[2];
		summaries[0] = summary.substring(0, index);
		summaries[1] = summary.substring(++index, summary.length());
		
		ViewUtil.setTextFieldValue(this, R.id.id_safeguard1, summaries[0]);
		ViewUtil.setTextFieldValue(this, R.id.id_safeguard2, summaries[1]);
	}
	
	@EventHandlerId(id=R.id._id_ok)
	public void onBtnPay() {
		ModelHelper.getModel(ECardPayModel.class).prePay(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(ModelHelper.getModel(ECardPayModel.class).setCurrentPayIndex(position)){
			dataProvider.notifyDataSetChanged();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onInitializeView(View view, PayTypeInfo data, int position) {
		ImageView select = (ImageView) view.findViewById(R.id._radio_button);
		ImageView icon = (ImageView) view.findViewById(R.id._image_view);
		TextView textView = (TextView) view.findViewById(R.id._text_view);

		select.setSelected(data.selected);
		icon.setImageDrawable(getResources().getDrawable(data.iconResID));
		textView.setText(data.text);
	}
	
	@NotificationMethod(IPay.PAY_SUCCESS)
	public void onPaySuccess(Object result) {
		ActivityUtil.startActivity(this,InsurancePaySuccessActivity.class);
		finish();
	}
	
	@NotificationMethod(IPay.PAY_ERROR)
	public void onPayError(Object result) {
//		ActivityUtil.startActivity(this,InsurancePayErrorActivity.class,data);
		ActivityUtil.startActivity(this,InsuranceMyPolicyActivity.class);
		finish();
	}
	
	@NotificationMethod(IPay.PAY_CANCEL)
	public void onPayCancel(Object result) {
		finish();
	}
	
	@NotificationMethod(InsuranceModel.CLIENT_NOTIFY_ERROR)
	public void onPiccNotifyFailed(String error,boolean isNetworkError) {
		Alert.cancelWait();
		if (isNetworkError) {
			error = "由于网络的原因，您的投保资料提交失败，请到我的保单重新提交。";
		} else {
			error = error + "请到我的保单重新提交。";
		}
		Alert.alert(this, "温馨提示", error, new DialogListener() {
			@Override
			public void onDialogButton(int id) {
				ActivityUtil.startActivity(InsuranceCashierActivity.this,InsuranceMyPolicyActivity.class);
				finish();
			}
		});
	}

}
