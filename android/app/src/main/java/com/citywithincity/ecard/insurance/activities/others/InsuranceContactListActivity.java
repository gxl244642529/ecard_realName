package com.citywithincity.ecard.insurance.activities.others;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.ItemEventHandler;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.vos.ContactVos;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;
import com.citywithincity.widget.StateListView;

import java.util.ArrayList;
import java.util.List;

@Observer
@EventHandler
@ItemEventHandler
public class InsuranceContactListActivity extends BaseActivity implements
		IListDataProviderListener<ContactVos> {

	private StateListView<ContactVos> listView;

	private int maxNumSelectEnable;
	private TextView num;

	private List<ContactVos> contactList, selectList;

	@SuppressWarnings("unchecked")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_contact_list);

		setTitle("常用联系人");

		Bundle bundle = getIntent().getExtras();
		maxNumSelectEnable = bundle.getInt("insured");
		String insuranceId = bundle.getString("insurance_id");
		if (insuranceId.equals("2003")) {
			((TextView) findViewById(R.id.text)).setText("您已作为默认被保险人，您还可以选择");
		} else {
			((TextView) findViewById(R.id.text)).setText("您还可以选择");
		}

		listView = (StateListView<ContactVos>) findViewById(R.id._list_view);
		listView.setItemRes(R.layout.item_insurance_contact);
		listView.setSelector(R.drawable.new_item_selector);
		listView.setTask(ModelHelper.getModel(InsuranceModel.class)
				.getContactList());
		listView.setDataListener(this);

		num = (TextView) findViewById(R.id.num);
		num.setText(String.valueOf(maxNumSelectEnable));
		contactList = new ArrayList<ContactVos>();
		selectList = new ArrayList<ContactVos>();
	}

	// @ItemEventId(id=R.id.id_delete)
	// public void onBtnItemDelete(final ContactVos data) {
	// Alert.alert(this, "提示", "您确认要删除联系人", new DialogListener() {
	//
	// @Override
	// public void onDialogButton(int id) {
	// if (id == R.id._id_ok) {
	// ModelHelper.getModel(InsuranceModel.class).deleteContace(data.id);
	// }
	// }
	// });
	// }

	@NotificationMethod(InsuranceModel.INSURANCE_CONTACT_DELETE)
	public void onDeleteContactSuccess(boolean result) {
		Alert.showShortToast("联系人删除成功");
		listView.reloadWithState();
	}

	@EventHandlerId(id = R.id._id_ok)
	public void onBtnOk() {

		ModelHelper.setListData(selectList);
		setResult(RESULT_OK);

		finish();

	}

	// @Override
	// public void onItemClick(Activity activity, ContactVos data, int position)
	// {
	// if (contactList.size() > maxNumSelectEnable) {
	// if (data.selected) {
	// data.selected = false;
	// } else {
	// data.selected = true;
	// }
	// listView.getDataProvider().notifyDataSetChanged();
	// num.setText(String.valueOf(maxNumSelectEnable - contactList.size()));
	// } else {
	// Alert.alert(this, "你选择的被保险人数已经达到上限，不能再选择了");
	// }
	// }

	@Override
	public void onInitializeView(View view, final ContactVos data, int position) {
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
		checkBox.setChecked(data.selected);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (contactList.isEmpty()) {
					contactList = listView.getDataProvider().getData();
				}

				if (data.selected) {
					data.selected = false;
				} else {
					if (selectList.size() < maxNumSelectEnable) {
						data.selected = true;
					} else {
						Alert.alert(InsuranceContactListActivity.this, "提示",
								"你选择的被保险人数已经达到上限，不能再选择了");
					}
				}

				selectList.clear();
				for (ContactVos contactVos : listView.getDataProvider()
						.getData()) {
					if (contactVos.selected) {
						selectList.add(contactVos);
					}
				}
				listView.getDataProvider().notifyDataSetChanged();
				num.setText(String.valueOf(maxNumSelectEnable
						- selectList.size()));
			}
		});
	}

}
