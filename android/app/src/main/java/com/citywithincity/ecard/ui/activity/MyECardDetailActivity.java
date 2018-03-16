package com.citywithincity.ecard.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.MyECardModel;
import com.citywithincity.ecard.models.vos.ECardDetail;
import com.citywithincity.ecard.models.vos.ECardHistroyInfo;
import com.citywithincity.ecard.models.vos.ECardInfo;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.IDetailJsonTask;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.interfaces.IListTask;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.widget.Popups;
import com.citywithincity.widget.StateListView;

@Observer
@EventHandler
public class MyECardDetailActivity extends BaseActivity implements IListTask,
		IListDataProviderListener<ECardHistroyInfo>, OnDismissListener,
		OnClickListener {

	private ECardInfo ecardInfo;
	private ImageView titleRight;

	StateListView<ECardHistroyInfo> listView;
	IDetailJsonTask<ECardDetail> task;
	private View headerView;

	@SuppressWarnings("unchecked")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.my_ecard_detail);
		titleRight = (ImageView) findViewById(R.id._title_right);
		titleRight.setImageResource(R.drawable.ic_ecard_top_menu_down);

		listView = (StateListView<ECardHistroyInfo>) findViewById(R.id._list_view);
		listView.setItemRes(R.layout.my_ecard_detail_item);
		headerView = View.inflate(this, R.layout.my_ecard_detail_head, null);
		headerView.setFocusable(false);
		headerView.setClickable(false);
		listView.addHeaderView(headerView);
		listView.setDataListener(this);

		listView.setTask(this);
		// 查询
		ecardInfo = (ECardInfo) getIntent().getExtras().get("data");
		com.citywithincity.utils.ViewUtil.setBinddataViewValues(ecardInfo, this);
		setTitle(TextUtils.isEmpty(ecardInfo.name) ? ecardInfo.id : ecardInfo.name);
		
		task = ModelHelper.getModel(MyECardModel.class).getDetail(ecardInfo.id);
	}
	
	@NotificationMethod(MyECardModel.NAME_HAS_CHANGED)
	public void onRenameSuccess(String name){
		setTitle(name);
	}

	@NotificationMethod(MyECardModel.ECARD_DETAIL)
	public void onGetDetail(ECardDetail result) {
		listView.onRequestSuccess(result.histroy, true);
		com.citywithincity.utils.ViewUtil.setBinddataViewValues(result,
				headerView, R.layout.my_ecard_detail_head);
	}

	@Override
	protected void onDestroy() {
		Alert.cancelWait();
		titleRight = null;
		ecardInfo = null;
		super.onDestroy();
	}

	@NotificationMethod(MyECardModel.ECARD_UNBIND)
	public void onUnBindSuccess(Object value) {
		finish();
	}

	@NotificationMethod(MyECardModel.ECARD_BARCODE)
	public void onScanSuccess(Object value) {
		finish();
	}

	@SuppressLint("InflateParams")
	@EventHandlerId(id = R.id._title_right)
	public void onBtnTitleRight() {
		View menuView = getLayoutInflater().inflate(R.layout.top_popup_ecard,
				null);
		titleRight.setImageResource(R.drawable.ic_ecard_top_menu_up);
		Popups.addPopup(menuView, titleRight, true, this);

		menuView.findViewById(R.id.ecard_unbind).setOnClickListener(this);
		menuView.findViewById(R.id.ecard_edit).setOnClickListener(this);
		menuView.findViewById(R.id.ecard_member).setOnClickListener(this);
		menuView.findViewById(R.id.ecard_scan).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Popups.hide();
		switch (v.getId()) {
		case R.id.ecard_unbind:
			Alert.confirm(this,"温馨提示", "确实要解绑吗?", new DialogListener() {
				
				@Override
				public void onDialogButton(int id) {
					if(id==R.id._id_ok){
						
						ModelHelper.getModel(MyECardModel.class).unbindECard(ecardInfo.id);
					}
					
				}
			});
			break;
		case R.id.ecard_edit:
		{
			ActivityUtil.startActivity(this, RenameECardActivity.class, this.ecardInfo);//(this, RenameECardActivity.class);
		}
			break;
		case R.id.ecard_member:
			if (TextUtils.isEmpty(ecardInfo.barCode)) {
				Alert.showShortToast("本卡非会员卡");
			} else {
				MemberInfoActivity.call(this, ecardInfo);
			}
			break;
		case R.id.ecard_scan:
		{
			startActivity(new Intent(this, MyECardScanActivity.class));
		}
			break;

		default:
			break;
		}

	}

	@Override
	public void reload() {
		task.reload();
	}

	@Override
	public void loadMore(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitializeView(View view, ECardHistroyInfo data, int position) {
		TextView shopName = (TextView) view
				.findViewById(R.id.ecard_item_shopname);
		TextView value = (TextView) view.findViewById(R.id.ecard_item_value);
		TextView date = (TextView) view.findViewById(R.id.ecard_item_date);
		TextView type = (TextView) view.findViewById(R.id.ecard_item_type);
		if (data.type.equals("充值")) {
			type.setBackgroundResource(R.drawable.ecard_consume_type_bg);
		} else {
			type.setBackgroundResource(R.drawable.ecard_item_type_bg);
		}
		type.setText(data.type);
		shopName.setText(data.shopName);
		value.setText(data.value);
		date.setText(data.hisTime);
	}

	@Override
	public void onDismiss() {
		if(titleRight!=null){
			titleRight.setImageResource(R.drawable.ic_ecard_top_menu_down);
		}
		
	}

}
