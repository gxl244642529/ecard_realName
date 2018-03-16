package com.citywithincity.ecard.insurance.activities.others;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.citywithincity.ecard.insurance.models.vos.RelationshipVo;
import com.citywithincity.ecard.pay.PayIds;
import com.citywithincity.ecard.utils.DateTimeUtil_M;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.ListDataProvider;
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
public class InsuranceDomesticInfoActivity extends BaseActivity implements
		OnCheckedChangeListener, IListDataProviderListener<ContactVos> {

	public static int MAX_INSURED_PERSON = 1;

	public static int SET_ADDR = 1;
	private com.damai.widget.AddressPickerView pickerView;
	private String addr, street, name, phone, ID;
	private EditText nameView, IDView, phoneView, addrView;
	private CheckBox checkBox;
	private View containerView;
	private List<RelationshipVo> list;

	private InsuranceDetailVo data;
	private Button purchase;

	private List<ContactVos> contactList;
	private ListDataProvider<ContactVos> listDataProvider;
	private ListView contactListView;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_domestic_info);
		setTitle("投保信息");

		data = (InsuranceDetailVo) getIntent().getExtras().get(
				LibConfig.DATA_NAME);

		initView();
	}

	@Override
	protected void onResume() {
		purchase.setClickable(true);
		super.onResume();
	}

	private void initView() {

		pickerView = (com.damai.widget.AddressPickerView) findViewById(R.id._area_picker);
//		pickerView.setDataProvider(new ECardAreaDataProvider());
//		pickerView.setProvinceEnable(false);

		nameView = (EditText) findViewById(R.id.id_name);
		IDView = (EditText) findViewById(R.id.id_idcard_no);
		phoneView = (EditText) findViewById(R.id.id_phone);
		addrView = (EditText) findViewById(R.id.id_street);

		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		checkBox.setOnCheckedChangeListener(this);

		containerView = findViewById(R.id._container1);

		if (this.data.insurance_id.equals("2003")) {
			findViewById(R.id.btn_add).setVisibility(View.VISIBLE);
			MAX_INSURED_PERSON = 5;
		} else {
			findViewById(R.id.btn_add).setVisibility(View.GONE);
			MAX_INSURED_PERSON = 1;
		}

		purchase = (Button) findViewById(R.id.id_purchase);

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
				for (ContactVos data : contactList) {
					Map<String,Object> json = new HashMap<String, Object>();
					json.put("name", data.name);// 姓名
					json.put("idCard", data.idCard);// 身份证号
					if (this.data.insurance_id.equals("2003")) {
						json.put("relation", data.relation);// 关系
					}
					array.add(json);
				}

			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("addr", addr);

			MyInsuranceModel.getInstance().submitInsured(data.insurance_id,
					data.typeId, phone, name, ID, data.count, array, map);
		}
	}

	private boolean validate() {

		name = nameView.getText().toString();
		phone = phoneView.getText().toString();
		ID = IDView.getText().toString();
		addr = pickerView.getAddress();
		street = addrView.getText().toString();

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

		if (!ValidateUtil.is18Idcard(ID)) {
			Alert.showShortToast("你输入身份证号格式有误，请重新输入");
			return false;
		}

		if (DateTimeUtil_M.getAge(ID) < 18) {
			Alert.showShortToast("投保人应该年满十八周岁");
			return false;
		}

		if (!checkBox.isChecked()) {

			if (contactList.isEmpty()) {
				Alert.showShortToast("请添加联系人");
				return false;
			}

			int contactListSize = contactList.size();
			for (int i = 0; i < contactListSize; i++) {
				if (TextUtils.isEmpty(contactList.get(i).name)) {
					Alert.showShortToast("请输入第" + (i + 1) + "个被保险人姓名");
					return false;
				} else if (contactList.get(i).idCard.length() != 18) {
					if (contactList.get(i).idCard.length() == 0) {
						Alert.showShortToast("请输入第" + (i + 1) + "个被保险人身份证号");
						return false;
					}
					Alert.showShortToast("你输入第" + (i + 1)
							+ "个被保险人身份证号格式有误，请重新输入");
					return false;
				}

				if (!ValidateUtil.is18Idcard(contactList.get(i).idCard)) {
					Alert.showShortToast("你输入第" + (i + 1) + "个身份证号格式有误，请重新输入");
					return false;
				}

				if (!validateAge(contactList.get(i).idCard)) {
					Alert.showShortToast("你输入第" + (i + 1)
							+ "个被保险人年龄应该在2周岁-85周岁");
					return false;
				}
			}
		}

		if (TextUtils.isEmpty(addr)) {
			Alert.showShortToast("请输入收货地址");
			return false;
		}

		if (TextUtils.isEmpty(street)) {
			Alert.showShortToast("请输入街道地址");
			return false;
		}
		addr = addr + street;

		return true;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			containerView.setVisibility(View.GONE);
		} else {
			initListView();
		}
	}

	private void initRelationSpinner(View view, final int index) {
		// 被保人和投保人关系。 1- 父母 2-配偶 3-子女
		list = new ArrayList<RelationshipVo>();
		RelationshipVo data = new RelationshipVo();
		data.relation = "父母";
		data.id = 1;

		RelationshipVo data1 = new RelationshipVo();
		data1.relation = "配偶";
		data1.id = 2;

		RelationshipVo data2 = new RelationshipVo();
		data2.relation = "子女";
		data2.id = 3;

		list.add(data);
		list.add(data1);
		list.add(data2);

		Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				RelationshipVo relation = (RelationshipVo) parent.getAdapter()
						.getItem(position);
				contactList.get(index).relation = relation.id;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Alert.showShortToast("请选择与投保人关系");
			}
		});

		ArrayAdapter<RelationshipVo> adapter = new ArrayAdapter<RelationshipVo>(
				this, R.layout.relation_picker_spinner_item, R.id.id_name, list);
		adapter.setDropDownViewResource(R.layout.area_picker_spinner_item);
		spinner.setAdapter(adapter);

		if (contactList.get(index).relation != 0
				&& contactList.get(index).relation != 4) {
			spinner.setSelection(contactList.get(index).relation - 1);
		}

	}

	@Override
	public void onInitializeView(View view, final ContactVos data, final int position) {
		EditText nameView = (EditText) view.findViewById(R.id.id_name1);

		EditText IDView = (EditText) view.findViewById(R.id.id_idcard_no1);

		TextView textView = (TextView) view.findViewById(R.id.count);
		textView.setText("" + (position + 1));

		ImageButton button = (ImageButton) view.findViewById(R.id.sub);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (position < contactList.size()) {
					if (contactList.get(position) != null) {
						contactList.remove(position);
						listDataProvider.notifyDataSetChanged();
						if (contactList.isEmpty()) {
//							containerView.setVisibility(View.GONE);
							checkBox.setChecked(true);
						}
					}
				}
			}
		});

		if (!TextUtils.isEmpty(data.name)) {
			nameView.setText(data.name);
		} else {
			nameView.setText("");
			nameView.setHint("请输入被保险人姓名");
		}

		if (!TextUtils.isEmpty(data.idCard)) {
			IDView.setText(data.idCard);
		} else {
			IDView.setText("");
			IDView.setHint("请输入被保险人身份证号");
		}

		if (this.data.insurance_id.equals("2003")) {
			initRelationSpinner(view, position);
		} else {
			view.findViewById(R.id._container).setVisibility(View.GONE);
		}

		nameView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				contactList.get(position).name = ((EditText) v).getText()
						.toString().trim();

			}
		});

		IDView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				contactList.get(position).idCard = ((EditText) v).getText()
						.toString().trim();
			}
		});

	}

	@EventHandlerId(id = R.id.btn_add)
	public void onBtnAddInsuredPerson() {

		initListView();
		if (contactList.size() < MAX_INSURED_PERSON) {
			ContactVos vos;
			if (!contactList.isEmpty()) {
				vos = contactList.get(contactList.size() - 1);
			} else {
				vos = new ContactVos();
				contactList.add(vos);
				listDataProvider.notifyDataSetChanged();
				return;
			}
				if (!TextUtils.isEmpty(vos.idCard)
						|| !TextUtils.isEmpty(vos.name)) {
					ContactVos addVos = new ContactVos();
					contactList.add(addVos);
					listDataProvider.notifyDataSetChanged();
				} else {
						Alert.alert(this, "提示", "请您先填写完整已添加的被保险人资料，然后再添加被保险人");
				}
			
		} else {
			Alert.alert(this,"提示", "你选择的被保险人已经达到上限，不能再添加了");
		}
	}

	@EventHandlerId(id = R.id.btn_group)
	public void onBtnSelectInsuredPerson() {
		initListView();
		int optionalNum;
		ContactVos vos;
		if (!contactList.isEmpty()) {
			vos = contactList.get(contactList.size() - 1);
		} else {
			vos = new ContactVos();
			contactList.add(vos);
		}
		if (!TextUtils.isEmpty(vos.idCard) || !TextUtils.isEmpty(vos.name)) {
			optionalNum = MAX_INSURED_PERSON - contactList.size();
		} else {
			optionalNum = MAX_INSURED_PERSON - contactList.size() + 1;
		}

		Bundle bundle = new Bundle();
		bundle.putInt("insured", optionalNum);
		bundle.putString("insurance_id", data.insurance_id);

		ActivityUtil.startActivityForResult(this,
				InsuranceContactListActivity.class, bundle, MAX_INSURED_PERSON);
	}

	private void initListView() {
		containerView.setVisibility(View.VISIBLE);
		checkBox.setChecked(false);
		if (null == contactList) {
			contactList = new ArrayList<ContactVos>();
			ContactVos data = new ContactVos();
			contactList.add(data);
			listDataProvider = new ListDataProvider<ContactVos>(this,
					R.layout.item_insured_person, this);
			contactListView = (ListView) findViewById(R.id._list_view);
			contactListView.setAdapter(listDataProvider);
			listDataProvider.setData(contactList, false);
			listDataProvider.notifyDataSetChanged();

			if (this.data.insurance_id.equals("2003")) {
				findViewById(R.id.tip).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.tip).setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == MAX_INSURED_PERSON) {
				List<ContactVos> selectList = ModelHelper.getListData();
				if (!selectList.isEmpty()) {
					ContactVos vos;
//					if (!contactList.isEmpty()) {
						vos = contactList.get(contactList.size() - 1);
//					} else {
//						vos = new ContactVos();
//						contactList.add(vos);
//					}
					if (!TextUtils.isEmpty(vos.idCard)
							|| !TextUtils.isEmpty(vos.name)) {
						//
					} else {
						contactList.remove(contactList.size() - 1);
					}

					contactList.addAll(selectList);
					listDataProvider.notifyDataSetChanged();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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

	private boolean validateAge(String ID) {
		if (data.insurance_id.equals("2003")) {// 全家宝
			return DateTimeUtil_M.getAge(ID) <= 85
					&& DateTimeUtil_M.getAge(ID) >= 2;
		}
		return true;

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
