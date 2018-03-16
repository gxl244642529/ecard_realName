package com.citywithincity.ecard.selling.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.SellingConfig;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.selling.models.OrderModel;
import com.citywithincity.ecard.selling.models.vos.SOrderDetailVo;
import com.citywithincity.ecard.selling.models.vos.SOrderDetailVo.Card;
import com.citywithincity.ecard.selling.models.vos.SOrderListVo;
import com.citywithincity.ecard.utils.STextUtil;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.models.ListDataProvider;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ViewUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 订单详情
 * 
 * @author randy
 * 
 */

@EventHandler
@Observer
public class SOrderDetailActivity extends BaseActivity implements
		OnClickListener, IListDataProviderListener<Card> {

	private SOrderListVo sOrderListVo;
	private SOrderDetailVo sOrderDetailVo;
	private TextView ordrState;// 订单状态
	private TextView goodsCount;// 商品数量
	private TextView totalPrice;// 总价
	private TextView name;// 收货人
	private TextView phone;
	private TextView address;// 收货地址
	private TextView orderId;// 订单号
	private TextView dealId;// 交易号
	private TextView date;// 日期
	private TextView realPrice;// 实付价格
	private Button btnPay;
	private Button btnRecive;
	private View btnContact;
	private TextView deliver;
	private TextView deliverCode;

	private ListView listView;
	private ListDataProvider<Card> cardDataProvider;
	private View closeBtn;

	@Override
	protected void onDestroy() {
		ordrState = null;
		goodsCount = null;
		totalPrice = null;
		name = null;
		phone = null;
		address = null;
		orderId = null;
		dealId = null;
		date = null;
		realPrice = null;
		btnPay = null;
		btnRecive = null;
		btnContact = null;
		super.onDestroy();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.s_activity_order_detail);
		setTitle("订单详情");

		sOrderListVo = (SOrderListVo) getIntent().getExtras().get("data");

	
		onLoadData(sOrderListVo.id);
		init();
		setData();

	}

	@NotificationMethod(OrderModel.DETAIL)
	public void onDataLoadComplete(SOrderDetailVo result) {
		Alert.cancelWait();
		sOrderDetailVo = result;

		cardDataProvider.setData(sOrderDetailVo.cards, false);

		name.setText(sOrderDetailVo.name);
		phone.setText(sOrderDetailVo.phone);
		address.setText(sOrderDetailVo.addr);
		orderId.setText(sOrderDetailVo.id);
		dealId.setText(sOrderDetailVo.code);
		goodsCount.setText("共" + sOrderDetailVo.cards.size() + "件商品");

		deliver.setText(result.deliver);
		deliverCode.setText(result.deliverCode);
	}

	private void init() {
		ordrState = (TextView) findViewById(R.id.order_state);
		goodsCount = (TextView) findViewById(R.id.goods_count);
		totalPrice = (TextView) findViewById(R.id.total_price);
		name = (TextView) findViewById(R.id.name);
		phone = (TextView) findViewById(R.id.phone);
		address = (TextView) findViewById(R.id.address);
		orderId = (TextView) findViewById(R.id.order_id);
		dealId = (TextView) findViewById(R.id.deal_id);
		date = (TextView) findViewById(R.id.date);
		realPrice = (TextView) findViewById(R.id.real_price);
		deliver = (TextView) findViewById(R.id.deliver);
		deliverCode = (TextView) findViewById(R.id.deliver_code);
		btnPay = (Button) findViewById(R.id.s_pay_btn);
		btnRecive = (Button) findViewById(R.id.order_confirm);
		btnContact = findViewById(R.id.s_contact);
		btnContact.setOnClickListener(this);
		closeBtn = findViewById(R.id._title_right);
		closeBtn.setOnClickListener(this);
		if (sOrderListVo.state == 0) {
			closeBtn.setVisibility(View.VISIBLE);
		} else {
			closeBtn.setVisibility(View.GONE);
		}

		listView = (ListView) findViewById(R.id._list_view);
		cardDataProvider = new ListDataProvider<SOrderDetailVo.Card>(this,
				R.layout.s_item_order_detail_card, this);
		listView.setAdapter(cardDataProvider);

		btnPay.setOnClickListener(this);
		if (sOrderListVo.state != 0) {
			btnPay.setVisibility(View.GONE);
		}

		if (sOrderListVo.state == SellingConfig.ORDER_DELIVERED) {
			btnRecive.setVisibility(View.VISIBLE);
		} else {
			btnRecive.setVisibility(View.GONE);
		}
		btnRecive.setOnClickListener(this);

	}

	private void setData() {
		STextUtil.setTextColorState(ordrState, sOrderListVo.state);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		String dateString = simpleDateFormat.format(Long
				.parseLong(sOrderListVo.time));
		date.setText(dateString);
		totalPrice.setText(sOrderListVo.realPriceString());
		realPrice.setText(sOrderListVo.realPriceString());

	}

	private void onLoadData(String orderId) {
		Alert.wait(this, "正在加载……");
		// SellingModel.getInstance().getSOrderDetail(orderId, this);
		ModelHelper.getModel(OrderModel.class).getDetail(
				orderId);
	}

	@NotificationMethod(OrderModel.DETAIL)
	public void onRequestError(String errMsg, boolean isNetworkError) {
		Alert.cancelWait();
		Alert.showShortToast(errMsg);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.s_pay_btn:
			Bundle bundle = new Bundle();
			bundle.putString("id", sOrderListVo.id);
			bundle.putFloat("price", sOrderListVo.realPrice);
			ActivityUtil.startActivity(SOrderDetailActivity.this,
					SCashierActivity.class, bundle);
			break;

		case R.id.s_contact:
			ActivityUtil.makeCall(this, "05925195866");
			break;

		case R.id._title_right: {
			Alert.confirm(this, "温馨提示", "您确定要取消订单吗？", new DialogListener() {

				@Override
				public void onDialogButton(int id) {
					closeOrder();
				}
			});

		}
			break;

		case R.id.order_confirm: {
			Alert.confirm(this, "温馨提示", "您确认已经收到货了吗？", new DialogListener() {

				@Override
				public void onDialogButton(int id) {
					ModelHelper.getModel(OrderModel.class).confirm(
							sOrderListVo.id);
				}
			});

		}
			break;

		default:
			break;
		}

	}

	@NotificationMethod(OrderModel.CONFIRM)
	public void onConfirmSuccess(boolean result) {
		Alert.cancelWait();
		if (result) {
			onLoadData(sOrderListVo.id);
			btnRecive.setVisibility(View.GONE);
			Alert.showShortToast("收货成功");
		}
	}

	@NotificationMethod(OrderModel.CONFIRM)
	public void onConfirmError(String result) {
		Alert.cancelWait();
		Alert.showShortToast(result);
	}

	private void closeOrder() {
		ModelHelper.getModel(OrderModel.class).close(sOrderListVo.id);
	}

	@NotificationMethod(OrderModel.CLOSE)
	public void onCloseSuccess(boolean result) {
		Alert.cancelWait();
		if (result) {
			Alert.showShortToast("订单取消成功");
			finish();
		}
	}

	@NotificationMethod(OrderModel.CONFIRM)
	public void onCloseError(String result) {
		Alert.cancelWait();
		Alert.showShortToast(result);
	}

	@Override
	public void onInitializeView(View view, Card data, int position) {
		ImageView thumb = (ImageView) view.findViewById(R.id.s_card_img);

		ViewUtil.setTextFieldValue(view, R.id.id_title, data.title);
		ViewUtil.setTextFieldValue(view, R.id.unit_price,
				String.format("¥%.2f", data.price));
		ViewUtil.setTextFieldValue(view, R.id.pre_charge,
				String.format("%.2f", data.preCharge));
		ViewUtil.setTextFieldValue(view, R.id.count,
				"×" + String.valueOf(data.count));
		ECardJsonManager.getInstance().setImageSrc(data.thumb, thumb);

	}

}
