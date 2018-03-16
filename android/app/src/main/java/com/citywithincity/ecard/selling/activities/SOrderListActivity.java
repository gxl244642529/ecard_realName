package com.citywithincity.ecard.selling.activities;

import android.os.Bundle;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.ItemEventHandler;
import com.citywithincity.auto.ItemEventHandler.ItemEventId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.SellingConfig;
import com.citywithincity.ecard.selling.models.OrderBModel;
import com.citywithincity.ecard.selling.models.OrderModel;
import com.citywithincity.ecard.selling.models.vos.SOrderListVo;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.ListDataProvider;
import com.citywithincity.models.OnItemClickListenerOpenActivity;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.widget.StateListView;

import java.util.ArrayList;
import java.util.List;

@Observer
@EventHandler
@ItemEventHandler
public class SOrderListActivity extends BaseActivity {

	private int state = 0;

	private StateListView<SOrderListVo> listView;
	private ListDataProvider<SOrderListVo> dataProvider;

	@SuppressWarnings("unchecked")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_sstate_order);

		state = getIntent().getIntExtra("state", 0);
		if (state == SellingConfig.ORDER_NOPAY) {
			setTitle("待付款");
		} else if (state == SellingConfig.ORDER_PAYED) {
			setTitle("待发货");
		} else if (state == SellingConfig.ORDER_DELIVERED) {
			setTitle("待收货");
		}

		listView = (StateListView<SOrderListVo>) findViewById(R.id._list_view);
//		dataProvider = new ListDataProvider<SOrderListVo>(this,
//				R.layout.s_item_order_list, null);
//		listView.setAdapter(dataProvider);
		listView.setItemRes(R.layout.s_item_order_list);
		listView.setOnItemClickListener(new OnItemClickListenerOpenActivity<SOrderListVo>(
				SOrderDetailActivity.class));

		listView.setTask(ModelHelper.getModel(OrderModel.class).getList(
				LibConfig.StartPosition));
	}

	@NotificationMethod(OrderModel.LIST)
	public void onGetListSuccess(List<SOrderListVo> result, boolean isLastPage) {
		List<SOrderListVo> list = new ArrayList<SOrderListVo>();
		for (SOrderListVo sOrderListVo : result) {
			if (sOrderListVo.state == state) {
				list.add(sOrderListVo);
			}
		}
		listView.onRequestSuccess(list, false);
	}

	@NotificationMethod(OrderModel.LIST_ERROR)
	public void onGetListError(String error) {
		Alert.showShortToast(error);
		listView.onRequestError(error, false);
	}

	@ItemEventId(id = R.id.order_confirm)
	public void onOrderConfirm(final SOrderListVo data) {
		Alert.confirm(this, "温馨提示", "您确认已经收到货了吗？", new DialogListener() {

			@Override
			public void onDialogButton(int id) {
				ModelHelper.getModel(OrderModel.class).confirm(data.id);
			}
		});
	}

	@ItemEventId(id = R.id.order_pay)
	public void onOrderPay(SOrderListVo data) {
		ModelHelper.getModel(OrderBModel.class).buyFromOrderList(data);
		ActivityUtil.startActivity(this, SCashierActivity.class);
	}

	/**
	 * 收获成功、取消成功
	 */
	public void onConfirmSuccess(Object value){
		Alert.showShortToast("收货成功");
		listView.reloadWithState();
	}
	
	/**
	 * 收获成功、取消成功
	 */
	public void onCancelOrderSuccess(Object value){
		Alert.showShortToast("取消订单成功");
		listView.reloadWithState();
	}
	
	
	
}
