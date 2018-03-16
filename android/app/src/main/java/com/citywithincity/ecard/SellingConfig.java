package com.citywithincity.ecard;


/**
 * 配置类 
 * @author randy
 *
 */
public class SellingConfig {
	
//	public static final String BASE_URL="http://192.168.1.253:8887";
//	
//	public static final String API_URL = BASE_URL + "/api2/";
	
	/**
	 * 敬老卡购物车本地数据文件名
	 */
	public static final String OLD = "old";
	/**
	 * 学生卡购物车本地数据文件名
	 */
	public static final String STUDENT = "student";
	/**
	 * DIY卡购物车本地数据文件名
	 */
	public static final String DIY = "DIY";
	/**
	 * 普通卡购物车本地数据文件名
	 */
	public static final String COMMON = "common";
	
	/**
	 * 待付款
	 */
	public static final int noPay = R.color.s_state_wait_for_pay_color;

	/**
	 * 待发货
	 */
	public static final int noPost = R.color.s_state_wait_for_post_color;
	/**
	 * 待收货
	 */
	public static final int noRecieve = R.color.s_state_wait_for_recieve_color;
	/**
	 * 预约
	 */
	public static final int bookOrder = R.color.s_state_book_color;
	/**
	 * 已完成   5
	 */
	public static final int orderBack = R.color.s_state_order_back_color;
	
	/**
	 * 没有付款
	 */
	public static final int ORDER_NOPAY = 0;
	/**
	 * 没有发货
	 */
	public static final int ORDER_PAYED = 2;
	/**
	 * 已经发货
	 */
	public static final int ORDER_DELIVERED = 12;
	/**
	 * 未审核
	 */
	public static final int ORDER_UNAUDITED = 4;
	/**
	 * 退款中
	 */
	public static final int ORDER_REFUND = 6;
	/**
	 * 关闭
	 */
	public static final int ORDER_CLOSED = 9;
	
	/**
	 * 收货
	 */
	public static final int ORDER_RECEIVED = 13;

}


