package com.citywithincity.ecard.insurance.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.vos.InsurancePolicyVo;
import com.citywithincity.ecard.models.MyECardModel;
import com.citywithincity.ecard.models.vos.RealInfoVo;
import com.citywithincity.ecard.ui.activity.WebViewActivity;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ViewUtil;
import com.damai.widget.AddressPickerView;

@Observer
@EventHandler
public class InsuranceSettleClaimActivity extends BaseActivity{

	public static int SET_ADDR = 1;
	private TextView iDCardView,nameView,phoneView,streetView;
	private String orderID,idCard,addr,name,phone,street;
	private InsurancePolicyVo data;
	private AddressPickerView pickerView;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_settle_claim);
		setTitle("产品报失");
		
		data = (InsurancePolicyVo) getIntent().getExtras().get(LibConfig.DATA_NAME);
		orderID = data.order_id;
		
		ViewUtil.setBinddataViewValues(data, this);
		initSummary(data.summary);
		
		initView();
		ModelHelper.getModel(MyECardModel.class).getRealInfo();
		
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
	
	@NotificationMethod(MyECardModel.REAL_INFO)
	public void onGetRealInfo(RealInfoVo result) {
		nameView.setText(result.name);
	}
	
	private void initView() {
		
		pickerView = (AddressPickerView) findViewById(R.id._area_picker);
		
		iDCardView = (TextView) findViewById(R.id.id_idcard_no);
		nameView = (TextView) findViewById(R.id.id_name);
		phoneView = (TextView) findViewById(R.id.id_phone);
		streetView = (TextView) findViewById(R.id.id_street);
		
	}
	
//	@EventHandlerId(id=R.id._container1)
//	public void onBtnSelectAddr() {
//		SAddrListActivity.selectAddress(this, SET_ADDR);
//	}
	
	@EventHandlerId(id=R.id._id_ok)
	public void onBtnLost() {
		if (validate()) {
			ModelHelper.getModel(InsuranceModel.class).lost(orderID, idCard, addr, name, phone);
		}
	}
	
	@NotificationMethod(InsuranceModel.LOST)
	public void onLostSuccess(boolean result){
		
		Alert.alert(this, "提示", "您的报失已受理，保险公司将在三个工作日核实处理", new DialogListener() {
			
			@Override
			public void onDialogButton(int id) {
				ActivityUtil.startActivity(InsuranceSettleClaimActivity.this,InsuranceMyPolicyActivity.class);
				finish();
			}
		});

	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if(resultCode == RESULT_OK){
//			if(requestCode == SET_ADDR){
//				SAddrListVo sAddrListVo = (SAddrListVo)data.getExtras().get("data");
//				setReceiverInfo(sAddrListVo);
//			}
//		}
//	}
	
//	private void setReceiverInfo(SAddrListVo data) {
////		nameView.setText(data.name);
//		phoneView.setText(data.phone);
//		addrView.setText(data.getAddr());
//	}
	
	private boolean validate() {
		idCard = iDCardView.getText().toString().trim();
//		addr = addrView.getText().toString().trim();
		addr = pickerView.getAddress();
		name = nameView.getText().toString().trim();
		phone = phoneView.getText().toString().trim();
		street = streetView.getText().toString().trim();
		
		if (TextUtils.isEmpty(idCard)) {
			Alert.showShortToast("请输入身份证号");
			return false;
		}
		if (TextUtils.isEmpty(addr)) {
			Alert.showShortToast("请输入收货地址");
			return false;
		}
		if (TextUtils.isEmpty(name)) {
			Alert.showShortToast("请输入收货人姓名");
			return false;
		}
		if (phone.length() != 7 && phone.length() != 11) {
			if (phone.length() == 0) {
				Alert.showShortToast("请输入电话号码");
				return false;
			}
			Alert.showShortToast("你输入电话号码格式有误，请重新输入");
			return false;
		}
		if (TextUtils.isEmpty(street)) {
			Alert.showShortToast("请输入街道地址");
			return false;
		}
		addr = addr + street;
		return true;
	}
	
	@EventHandlerId(id=R.id.ecard_info_card)
	public void onBtnShowClaimCard() {
		WebViewActivity.open(this,data.sample_url, "");
	}

}
