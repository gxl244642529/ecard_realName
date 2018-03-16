package com.citywithincity.ecard.selling.models.vos;

import android.view.View;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.SellingConfig;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 全部订单
 * 
 */
@Databind
public class SOrderListVo implements IJsonValueObject, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 应付价
	 */
	public float realPrice;
	/**
	 * 状态
	 */
	public int state;
	/**
	 * 订单时间
	 */
	public String time;
	/**
	 * 编号
	 */
	public String id;
	/**
	 * 标题
	 */
	@ViewId(id=R.id.title)
	public String title;
	/**
	 * 购买商品总数
	 */
	@ViewId(id=R.id.goods_count)
	public int count;
	/**
	 * 图片
	 */
	@ViewId(id=R.id.img)
	public String img;

	/**
		 * 
		 */
	public String payTime;
	/**
		 * 
		 */
	public int attach;
	/**
		 * 
		 */
	public float totalPrice;
	/**
		 * 
		 */
	public String code;

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		realPrice = json.getInt("REAL_PRICE");
		state = json.getInt("STATE");
		time = json.getString("TIME");
		id = json.getString("ID");
		title = json.getString("TITLE");
		count = json.getInt("COUNT");
		// img=JsonUtil.getImageUrl(json.getString("IMG"));
		img = json.getString("IMG");
		payTime = json.getString("PAY_TIME");
		attach = json.getInt("ATTACH");
		totalPrice = json.getInt("TOTAL_PRICE");
		code = json.getString("CODE");

	}
	
	@ViewId(id=R.id.total_price)
	public String realPriceString(){
		return String.format("%.2f", realPrice/100);
	}
	
	/**
	 * 
	 * @return
	 */
	@ViewId(id=R.id.order_confirm,type=ViewId.TYPE_VISIBILITY)
	public int getConfimVisible() {
		if(state == SellingConfig.ORDER_DELIVERED){
			return View.VISIBLE;
		}
		return View.GONE;
	}
	
	@ViewId(id=R.id.order_pay,type=ViewId.TYPE_VISIBILITY)
	public int getPayVisible() {
		if(state == SellingConfig.ORDER_NOPAY){
			return View.VISIBLE;
		}
		return View.GONE;
	}
	
	@ViewId(id=R.id.s_order_state)
	public String getOrderState(){
		
		String string = null;
		
		switch (state) {
		case SellingConfig.ORDER_NOPAY:
			string = "待付款";
			break;

		case SellingConfig.ORDER_PAYED:
			string = "待发货";
			break;

		case SellingConfig.ORDER_DELIVERED:
			string = "待收货";
			break;

		case SellingConfig.ORDER_REFUND:
			string = "订单已完成退款";
			break;
			
		case SellingConfig.ORDER_CLOSED:
			string = "订单已关闭";
			break;

		case SellingConfig.ORDER_RECEIVED:
//			view.setBackgroundColor(Color.parseColor("#ff3d2c"));
//			view.setTextColor(Color.parseColor("#ff3d2c"));
			string = "已完成";
			break;

		default:
			break;
		}
		
		return string;
	}
}
