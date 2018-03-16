package com.citywithincity.ecard.insurance.activities.others;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.MyInsuranceModel;
import com.citywithincity.ecard.insurance.models.PICCPayAction;
import com.citywithincity.ecard.insurance.models.vos.ContactVos;
import com.citywithincity.ecard.insurance.models.vos.InsuranceDetailVo;
import com.citywithincity.ecard.insurance.models.vos.InsuranceOtherPurchaseSuccessVo;
import com.citywithincity.ecard.pay.PayIds;
import com.citywithincity.ecard.utils.DateTimeUtil_M;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.paylib.ECardPayModel;
import com.citywithincity.paylib.IPay;
import com.citywithincity.paylib.IPay.CashierInfo;
import com.citywithincity.paylib.PayType;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ValidateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Observer
@EventHandler
public class InsuranceAutoInfoActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private final static int SELECT_INSURED_PERSON = 2;

	private CheckBox checkBox;
	private View containerView;
	private InsuranceDetailVo data;
	private EditText nameView, idCardView, phoneView, insuredPersonNameView,
			insuredPersonIDCardView, carNoView, carFrameView;
	private String name, ID, phone, insuredName, insuredPersonID, carNo,
			carFrame;
	private Button purchase;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_auto_info);
		setTitle("投保信息");

		data = (InsuranceDetailVo) getIntent().getExtras().get(
				LibConfig.DATA_NAME);

		init();
	}

	@Override
	protected void onResume() {
		purchase.setClickable(true);
		super.onResume();
	}

	private void init() {
		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		checkBox.setOnCheckedChangeListener(this);

		containerView = findViewById(R.id._container1);

		nameView = (EditText) findViewById(R.id.id_name);
		idCardView = (EditText) findViewById(R.id.id_idcard_no);
		phoneView = (EditText) findViewById(R.id.id_phone);
		carNoView = (EditText) findViewById(R.id.car_no);
		carFrameView = (EditText) findViewById(R.id.car_frame);

		purchase = (Button) findViewById(R.id.id_purchase);

		/*Alert.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				purchase.setClickable(true);

			}
		});*/
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			containerView.setVisibility(View.GONE);
		} else {
			initInsuredPersonView();
		}
	}

	private void initInsuredPersonView() {
		containerView.setVisibility(View.VISIBLE);
		checkBox.setChecked(false);
		if (null == insuredPersonNameView || null == insuredPersonIDCardView) {
			insuredPersonNameView = (EditText) findViewById(R.id.id_name1);
			insuredPersonIDCardView = (EditText) findViewById(R.id.id_idcard_no1);
		}
	}

	@EventHandlerId(id = R.id.btn_group)
	public void onBtnSelectInsuredPerson() {
		initInsuredPersonView();
		int optionalNum = 1;

		Bundle bundle = new Bundle();
		bundle.putInt("insured", optionalNum);
		bundle.putString("insurance_id", data.insurance_id);

		ActivityUtil.startActivityForResult(this,
				InsuranceContactListActivity.class, bundle,
				SELECT_INSURED_PERSON);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_INSURED_PERSON) {
				List<ContactVos> selectList = ModelHelper.getListData();
				if (!selectList.isEmpty()) {
					ContactVos contactVos = selectList.get(0);
					insuredPersonNameView.setText(contactVos.name);
					insuredPersonIDCardView.setText(contactVos.idCard);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@EventHandlerId(id = R.id.id_purchase)
	public void onBtnOk() {
		if (validate()) {
			checkData();
			purchase.setClickable(false);
		}
	}

	/**
	 * 检测是否可以购买
	 * 
	 * @param cardId
	 */
	private void checkData() {
		ModelHelper.getModel(InsuranceModel.class).checkDataInsured(ID,
				data.insurance_id, data.count);
	}

	@NotificationMethod(InsuranceModel.CHECK_DATA_INSURED)
	public void onCheckDataInsured(boolean result) {
		if (result) {
			List<Map<String,Object>> array = null;
			if (!checkBox.isChecked()) {
				array = new ArrayList<Map<String,Object>>();
				Map<String,Object> json = new HashMap<String,Object>();
				json.put("name", insuredName);// 姓名
				json.put("idCard", insuredPersonID);// 身份证号
				array.add(json);
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("carNo", carNo);// 车牌号
			map.put("carFrame", carFrame);// 车架号

			MyInsuranceModel.getInstance().submitInsured(data.insurance_id,
					data.typeId, phone, name, ID, data.count, array, map);
			purchase.setClickable(true);
		}
	}

	private boolean validate() {

		name = nameView.getText().toString().trim();
		phone = phoneView.getText().toString().trim();
		ID = idCardView.getText().toString().trim();
		carNo = carNoView.getText().toString().trim();
		carFrame = carFrameView.getText().toString().trim();

		if (TextUtils.isEmpty(name)) {
			Alert.showShortToast("请输入投保人姓名");
			return false;
		} else if (ID.length() != 18) {
			if (ID.length() == 0) {
				Alert.showShortToast("请输入投保人身份证号");
				return false;
			}
			Alert.showShortToast("你输入身份证号格式有误，请重新输入");
			return false;
		} else if (phone.length() != 7 && phone.length() != 11) {
			if (phone.length() == 0) {
				Alert.showShortToast("请输入投保人电话号码");
				return false;
			}
			Alert.showShortToast("你输入电话号码格式有误，请重新输入");
			return false;
		}
		if (TextUtils.isEmpty(carNo)) {
			Alert.showShortToast("请输入车牌号");
			return false;
		} else if (TextUtils.isEmpty(carFrame)) {
			Alert.showShortToast("请输入车架号");
			return false;
		}

		if (!ValidateUtil.is18Idcard(ID)) {
			Alert.showShortToast("你输入身份证号格式有误，请重新输入");
			return false;
		}

		if (DateTimeUtil_M.getAge(ID) < 18) {
			Alert.showShortToast("投保人应该年满十八周岁");
			return false;
		}

		if (!checkBox.isChecked()) {

			insuredName = insuredPersonNameView.getText().toString().trim();
			insuredPersonID = insuredPersonIDCardView.getText().toString()
					.trim();

			if (TextUtils.isEmpty(insuredName)) {
				Alert.showShortToast("请输入被保人姓名");
				return false;
			} else if (insuredPersonID.length() != 18) {
				if (insuredPersonID.length() == 0) {
					Alert.showShortToast("请输入被保人身份证号");
					return false;
				}
				Alert.showShortToast("你输入身份证号格式有误，请重新输入");
				return false;
			}

			if (!ValidateUtil.is18Idcard(insuredPersonID)) {
				Alert.showShortToast("你输入身份证号格式有误，请重新输入");
				return false;
			}
		}

		if (carNo.startsWith("港") || carNo.startsWith("澳")) {
			Alert.showShortToast("当前版本暂不支持港澳车辆投保");
			return false;
		}

		if (carFrame.length() != 17) {
			Alert.showShortToast("您输入的车架号有误，请重新核实");
			return false;
		}

		return true;
	}

	@NotificationMethod(MyInsuranceModel.SUBMIT_INSURED)
	public void onPurchaseSuccess(InsuranceOtherPurchaseSuccessVo result) {
		purchase.setClickable(true);
		IPay model = ModelHelper.getModel(ECardPayModel.class);
		model.setId(PayIds.PAY_ID_PICC);
		model.setCashierInfo(new CashierInfo(result.id, result.fee));
		model.setPayAction(new PICCPayAction());
		model.setPayTypes(new PayType[] { PayType.PAY_WEIXIN });
		ActivityUtil.startActivity(this, InsuranceOthersCashierActivity.class,
				result);
		finish();
	}

	@NotificationMethod(IPay.PAY_SUCCESS)
	public void onPaySuccess(Object result) {
		finish();
	}

	@NotificationMethod(IPay.PAY_ERROR)
	public void onPayError(Object result) {
		finish();
	}

	@NotificationMethod(IPay.PAY_CANCEL)
	public void onPayCancel(Object result) {
		finish();
	}

	@NotificationMethod(InsuranceModel.CLIENT_NOTIFY_ERROR)
	public void onPiccNotifyFailed(String error, boolean isNetworkError) {
		Alert.cancelWait();
		finish();
	}

	@NotificationMethod(InsuranceModel.CLIENT_NOTIFY)
	public void onPiccNotifySuccess() {
		finish();
	}

	@NotificationMethod(InsuranceModel.CHECK_DATA_INSURED_ERROR)
	public void onCheckIdCardSuccess(String error, boolean isNetworkError) {
		Alert.cancelWait();
		purchase.setClickable(true);
		if (isNetworkError) {
			Alert.alert(this, "温馨提示", "网络错误，请重试", new DialogListener() {

				@Override
				public void onDialogButton(int id) {

				}
			});
		} else {
			Alert.alert(this, "温馨提示", error, new DialogListener() {

				@Override
				public void onDialogButton(int id) {

				}
			});
		}
	}

	@NotificationMethod(MyInsuranceModel.SUBMIT_INSURED_ERROR)
	public void onSubmit(String err, boolean isNetwork) {
		purchase.setClickable(true);
		Alert.cancelWait();
		Alert.alert(this, "提示", err, new DialogListener() {

			@Override
			public void onDialogButton(int id) {

			}
		});
	}

}
