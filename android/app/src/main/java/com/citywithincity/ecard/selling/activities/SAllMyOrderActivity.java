package com.citywithincity.ecard.selling.activities;

import android.os.Bundle;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.ItemEventHandler;
import com.citywithincity.auto.ItemEventHandler.ItemEventId;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.selling.models.OrderBModel;
import com.citywithincity.ecard.selling.models.OrderModel;
import com.citywithincity.ecard.selling.models.vos.SOrderListVo;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.OnItemClickListenerOpenActivity;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.widget.StateListView;

/**
 * 我的订单列表(所有)
 * 
 * @author randy
 * 
 */
@Observer
@EventHandler
@ItemEventHandler
public class SAllMyOrderActivity extends BaseActivity {

	private StateListView<SOrderListVo> listView;
	
	@Override
	protected void onDestroy() {
		listView = null;
		super.onDestroy();
	}

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.s_activity_all_my_order);
		setTitle("我的订单");
		init();

	}

	@Override
	protected void onResume() {
		listView.reloadWithState();
		super.onResume();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		
		listView = (StateListView<SOrderListVo>) findViewById(R.id._list_view);
		listView.setItemRes(R.layout.s_item_order_list);
		listView.setOnItemClickListener(new OnItemClickListenerOpenActivity<SOrderListVo>(
				SOrderDetailActivity.class));
		listView.setTask(ModelHelper.getModel(OrderModel.class).getList(LibConfig.StartPosition));
		
	}

	
	@ItemEventId(id=R.id.order_confirm)
	public void onOrderConfirm(final SOrderListVo data) {
		Alert.confirm(this, "温馨提示", "您确认已经收到货了吗？", new DialogListener() {

			@Override
			public void onDialogButton(int id) {
				ModelHelper.getModel(OrderModel.class).confirm(
						data.id);
			}
		});
	}
	
	@ItemEventId(id=R.id.order_pay)
	public void onOrderPay(SOrderListVo data) {
		ModelHelper.getModel(OrderBModel.class).buyFromOrderList(data);
		ActivityUtil.startActivity(SAllMyOrderActivity.this,
				SCashierActivity.class);
	}
	
	@ItemEventId(id=R.id.order_confirm)
	public void onOrderConfirmSuccess(boolean result) {
		if (result) {
			listView.reloadWithState();
			Alert.showShortToast("收货成功");
		}
	}
	
	@ItemEventId(id=R.id.order_confirm)
	public void onOrderConfirmError(String errMsg, boolean isNetworkError) {
		Alert.showShortToast(errMsg);
	}

}
