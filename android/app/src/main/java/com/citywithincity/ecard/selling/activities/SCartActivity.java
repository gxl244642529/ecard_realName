package com.citywithincity.ecard.selling.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.ItemEventHandler;
import com.citywithincity.auto.ItemEventHandler.ItemEventId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.selling.models.CartBModel;
import com.citywithincity.ecard.selling.models.CartBModel.IOnCartItemChangeListener;
import com.citywithincity.ecard.selling.models.CartModel;
import com.citywithincity.ecard.selling.models.OrderBModel;
import com.citywithincity.ecard.selling.models.vos.SAddrListVo;
import com.citywithincity.ecard.selling.models.vos.SCartListVo;
import com.citywithincity.ecard.selling.views.AddToCartView;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;
import com.citywithincity.widget.StateListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 * 
 * @author randy
 * 
 */

@Observer
@EventHandler
@ItemEventHandler
public class SCartActivity extends BaseActivity{
	private TextView totalPriceText;
	StateListView<SCartListVo> listView;
	CartBModel model;
	@Override
	protected void onDestroy() {
		totalPriceText = null;
		super.onDestroy();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.s_activity_cart);
		setTitle("购物车");
		
		totalPriceText = (TextView) findViewById(R.id.total_pay_price);
		
		listView = (StateListView<SCartListVo>)findViewById(R.id._list_view);
		listView.setItemRes(R.layout.s_item_cart);
		
		//ModelHelper.getModel(CartBModel.class);
		
		
		model = new CartBModel();
		model.getList();
		listView.setTask(model);
		model.setListener(listView);
	}
	
	@NotificationMethod(CartModel.LIST)
	public void onList(List<SAddrListVo> result,boolean isLastPage){
		findViewById(R.id._title_right).setVisibility(View.VISIBLE);
		model.notifyPrice();
	}
	
	@EventHandlerId(id=R.id._title_right)
	public void onBtnSelectAll(){
		model.toggleSelectAll();
	}
	
	/**
	 * 改变
	 * @param data
	 * @param checked
	 */
	@ItemEventId(id=R.id.s_card_select)
	public void onItemSelect(SCartListVo data,boolean checked){
		model.selectItem(data,checked);
	}
	@ItemEventId(id=R.id.update_cart)
	public void onItemUpdateCart(final SCartListVo data){
		new AddToCartView(this).setCount(data.count).
		setRecharge(data.recharge).setMaxCount(data.stock).show().setListener(new IOnCartItemChangeListener() {
			@Override
			public void onChange(int count, int recharge) {
				ModelHelper.getModel(CartModel.class).update(data.cartId, count, recharge);
			}
		});
	}
	
	@EventHandlerId(id=R.id.btn_delete)
	public void onBtnDelete(){
		Alert.confirm(this,  "确定从购物车删除吗",new DialogListener() {
			
			@Override
			public void onDialogButton(int id) {
				if(id==R.id._id_ok){
					//删除
					model.deleteSeleted();
					
				}
			}
		});
	}
	/**
	 * 这里也可以直接刷新task
	 * @param value
	 */
	@NotificationMethod(CartModel.UPDATE)
	public void onUpdateSuccess(Object value){
		Alert.showShortToast("修改成功");
		listView.reloadWithState();
	}
	/**
	 * 这里也可以直接刷新task
	 * @param value
	 */
	@NotificationMethod(CartModel.DELETE)
	public void onDeleteSuccess(Object value){
		Alert.showShortToast("删除成功");
		listView.reloadWithState();
	}
	@EventHandlerId(id=R.id.id_btn)
	public void onBtnBuy(){
		ArrayList<SCartListVo> list = model.getSelected();
		if(list.size()==0){
			Alert.showShortToast("请选择要购买的商品");
			return;
		}
		//加入购买请求
			ModelHelper.getModel(OrderBModel.class).buyFromCart(this,list);
		
	}
	
	@NotificationMethod(CartBModel.PRICE_HAS_CHANGED)
	public void onPriceHasChange(float value){
		totalPriceText.setText(String.format("%.2f",value));
	}
	
}
