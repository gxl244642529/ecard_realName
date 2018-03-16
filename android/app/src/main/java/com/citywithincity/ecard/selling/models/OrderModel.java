package com.citywithincity.ecard.selling.models;

import com.citywithincity.auto.ApiArray;
import com.citywithincity.auto.ApiDetail;
import com.citywithincity.auto.ApiValue;
import com.citywithincity.ecard.selling.models.vos.SOrderDetailVo;
import com.citywithincity.ecard.selling.models.vos.SOrderListVo;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.interfaces.IDetailJsonTask;
import com.citywithincity.interfaces.IValueJsonTask;
import com.citywithincity.models.cache.CachePolicy;

import java.util.List;
import java.util.Map;

/**
 * 订单相关的模型
 * @author Randy
 *
 */
public interface OrderModel {
	//订单列表
	String LIST = "s_order_list3";
	//订单列表错误
	String LIST_ERROR = "s_order_list3_error";
	//订单分类列表
	String LIST_STATES = "s_order_state3";
	//订单详情
	String DETAIL = "s_order_detail3";
	//提交订单
	String SUBMIT = "s_order_submit3";
	//确认收货
	String CONFIRM = "s_order_confirm3";
	//支付订单
	String PAY = "s_order_pay3";
	//取消订单
	String CLOSE = "s_order_cancel3";
	//通知服务器成功
	String NOFITY_SERVER = "s_pay_notify";
	
	//邮费查询接口
	String ORDER_TRANS_FEE = "s_order_post";
	
	//支付确认成功
	String PAY_NOTITY = "s_pay_notify";
	
	String PAY_NOTITY_ERROR = "s_pay_notify_error";
	
	@ApiArray(api=LIST,clazz=SOrderListVo.class,isPrivate=true)
	IArrayJsonTask<SOrderListVo> getList(int position);
	
	@ApiDetail(api=DETAIL,params={"id"},clazz=SOrderDetailVo.class,cachePolicy=CachePolicy.CachePolity_NoCache)
	IDetailJsonTask<SOrderDetailVo> getDetail(String id);
	
	@ApiValue(api=SUBMIT,waitingMessage="正在提交订单,请稍候...",params={"address_id","title","list","invoice"})
	void submit(int addrId, String title, List<Map<String,Object>> array, int invoice);
	
	/**
	 * 
	 * @param id
	 * @param type	PayModel.PayType
	 */
	@ApiValue(api=PAY,params={"id","type"})
	void pay(String id,int type);
	
	@ApiValue(api=CLOSE,params={"id"})
	void close(String id);
	
	@ApiValue(api=CONFIRM,params={"id"})
	void confirm(String id);
	
	@ApiValue(api=ORDER_TRANS_FEE,waitingMessage="正在计算邮费...",params={"address_id"},cachePolicy=CachePolicy.CachePolity_Permanent)
	void getTransFee(int address_id);
	
	@ApiValue(api=NOFITY_SERVER,params={"verify"})
	void nofityServer(String verify);
	
	@ApiArray(api=LIST_STATES,clazz=SOrderListVo.class,params={"state"})
	IArrayJsonTask<SOrderListVo> getListState(int position,int state);
	
	/**
	 * 支付成功
	 * @param payResult
	 * @return
	 */
	@ApiValue(api=PAY_NOTITY,params={"verify"})
	IValueJsonTask<Boolean> payNotify(String payResult);
}
