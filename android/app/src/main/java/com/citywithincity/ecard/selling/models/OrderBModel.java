package com.citywithincity.ecard.selling.models;

import android.app.Activity;

import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.selling.activities.SFirmOrderActivity;
import com.citywithincity.ecard.selling.diy.models.DiyCard;
import com.citywithincity.ecard.selling.models.vos.SCardListVo;
import com.citywithincity.ecard.selling.models.vos.SCartListVo;
import com.citywithincity.ecard.selling.models.vos.SOrderListVo;
import com.citywithincity.mvc.Model;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Observer
public class OrderBModel extends Model {

	private List<SCartListVo> buyList;
	private float transFee;
	/**
	 * 购买
	 * @param data
	 */
	public void buy(Activity context,SCardListVo data,float rawPrice,int count,int recharge){
		//转成SCartListVo
		SCartListVo cart = new SCartListVo();
		cart.price = rawPrice;
		cart.id = data.getId();
		cart.count = count;
		cart.recharge = recharge;
		cart.title = data.getTitle();
		cart.thumb = data.getThumb();
		buyList = new ArrayList<SCartListVo>();
		buyList.add(cart);
		ActivityUtil.startActivity(context, SFirmOrderActivity.class);
	}
	
	/**
	 * 购买
	 * @param data
	 */
	public void buyDIY(Activity context,DiyCard data,int count,int recharge){
		//转成SCartListVo
		SCartListVo cart = new SCartListVo();
		cart.id = data.bgIdS;
		cart.thumb = data.imgPathCard;
		cart.title = data.nameCard;// 卡名
		cart.price = data.price;// 单价
		//data.cartId = String.valueOf(vo.bgIdS);
		cart.count = count;
		cart.recharge = recharge;
		cart.cardType = 1;
		buyList = new ArrayList<SCartListVo>();
		buyList.add(cart);
		ActivityUtil.startActivity(context, SFirmOrderActivity.class);
	}
	
	/**
	 * 从购物车购买购买
	 * @param data
	 */
	public void buyFromCart(Activity context,List<SCartListVo> data){
		
		buyList = new ArrayList<SCartListVo>();
		buyList = data;
		
		ActivityUtil.startActivity(context, SFirmOrderActivity.class);
	}
	
	public List<SCartListVo> getList(){
		return buyList;
	}

	public void setTransFee(int value) {
		transFee = (float)value / 100;
	}

	public String getTransFee() {
		return String.format("%.2f", transFee);
	}
	
	private float getTotalPrice(){
		float total = 0;
		for (SCartListVo data : buyList) {
			total += data.getTotalPrice();
		}
		return total;
	}

	public float getRealPrice(){
		return transFee + getTotalPrice();
	}
	
	public String getTotalPay(){
		return String.format("%.2f",  getTotalPrice());
	}

	public String getRealPay() {
		return String.format("%.2f", transFee + getTotalPrice());
	}

	public void submit(Activity context,boolean invoice,int addressID) throws JSONException {
		//这里组成json
		List<Map<String,Object>> arr = new ArrayList<Map<String,Object>>();
		for (SCartListVo data : buyList) {
			arr.add(data.toJson(context));
		}
		
		ModelHelper.getModel(OrderModel.class).submit(addressID, buyList.get(0).title, arr, invoice?1:0);
	}
	float realPrice;
	String orderId;
	
	@NotificationMethod(OrderModel.SUBMIT)
	public void onSubmitOrderSuccess(JSONObject json) throws JSONException{
		
		//购物车里面也要删除掉
		List<Integer> deleteList = new ArrayList<Integer>();
		for (SCartListVo data : buyList) {
			if(data.cartId>0){
				//要删除的
				deleteList.add(data.cartId);
			}
		}
		if(deleteList.size()>0){
			ModelHelper.getModel(CartBModel.class).deleteFromCart(deleteList);
		}
		
		buyList = null;
		realPrice = ((float) json.getInt("real_price")) / 100;
		orderId = json.getString("order_id");
	}
	/**
	 * 用于收银台
	 * @return
	 */
	public String getRealPayPay(){
		return String.format("%.2f", (float)realPrice);
	}
	
	public void pay(int type) {
		ModelHelper.getModel(OrderModel.class).pay(orderId, type);
	}
	
	public void buyFromOrderList(SOrderListVo data){
		realPrice =  data.realPrice / 100;
		orderId = data.id;
	}

	
	//进入收银台
	
}
