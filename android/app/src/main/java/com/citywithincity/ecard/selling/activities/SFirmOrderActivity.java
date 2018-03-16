package com.citywithincity.ecard.selling.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.ItemEventHandler;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.selling.models.OrderBModel;
import com.citywithincity.ecard.selling.models.OrderModel;
import com.citywithincity.ecard.selling.models.vos.SCartListVo;
import com.citywithincity.models.ListDataProvider;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ViewUtil;
import com.damai.helper.OnSelectDataListener;
import com.damai.helper.a.Res;
import com.damai.widget.DefAddressView;
import com.damai.widget.vos.AddressVo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@Observer
@EventHandler
@ItemEventHandler
public class SFirmOrderActivity extends BaseActivity implements OnSelectDataListener<AddressVo>{

	public static int SET_ADDR = 1;
	@Res
	private DefAddressView _address_container;
	
	private ListView listView;
	private ListDataProvider<SCartListVo> dataProvider;
	private List<SCartListVo> list;
	private CheckBox checkBox;
	private TextView postCost,totalPayPrice;
	
	public static boolean isFromAddrList = false;
	
	private View btnPay;
	@Override
	protected void onDestroy() {
		listView = null;
		dataProvider.destroy();
		listView = null;
		super.onDestroy();
	}

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_s_firm_order);
		setTitle("订单确认");
		OrderBModel model  =ModelHelper.getModel(OrderBModel.class);
		list = model.getList();
		listView = (ListView) findViewById(R.id._list_view);
		dataProvider = new ListDataProvider<SCartListVo>(this,
				R.layout.s_item_order_detail_card, null);
		listView.setAdapter(dataProvider);
		dataProvider.setData(list, false);
		ViewUtil.setTextFieldValue(this, R.id.count, String.format( "共%d件商品",list.size()));
		ViewUtil.setTextFieldValue(this, R.id.total_price, model.getTotalPay());
		checkBox = (CheckBox) findViewById(R.id.checkbox);
		checkBox.setChecked(false);
		checkBox.setVisibility(View.GONE);
		//findViewById(R.id.invoce_container).setVisibility(View.GONE);
		postCost = (TextView) findViewById(R.id.price);
		totalPayPrice = (TextView) findViewById(R.id.total_pay_price);
		btnPay= findViewById(R.id._id_ok);
		btnPay.setEnabled(false);
		
		_address_container.setOnSelectDataListener(this);
	}


	@EventHandlerId(id=R.id._id_ok)
	public void onBtnPay() throws JSONException {
		
		ModelHelper.getModel(OrderBModel.class).submit(this,this.checkBox.isChecked(),addressVo.getTyId() );
	}
	
	/**
	 * 提交成功
	 * @param json
	 * @throws JSONException 
	 */
	@NotificationMethod(OrderModel.SUBMIT)
	public void onSubmitOrderSuccess(JSONObject json){
		startActivity(new Intent(this,SCashierActivity.class));
		finish();
	}
	
	
	@NotificationMethod(OrderModel.ORDER_TRANS_FEE)
	public void onGetTransFee(Integer value){
		btnPay.setEnabled(true);
		//计算所有价格
		OrderBModel model= ModelHelper.getModel(OrderBModel.class);
		model.setTransFee(value);
		postCost.setText(model.getTransFee());
		//总价格
		totalPayPrice.setText(model.getRealPay());
	}
	private int lastId;
	private AddressVo addressVo;
	@Override
	public void onSelectData(AddressVo data) {
		addressVo  =data;
		if(lastId!=data.getShiId()){
			//邮费
			btnPay.setEnabled(false);
			
			//加载邮费
			ModelHelper.getModel(OrderModel.class).getTransFee(addressVo.getTyId());
			
			lastId = data.getShiId();
		}
	}
	


}
