package com.citywithincity.paylib;


/**
 * 支付类型信息
 * @author Randy
 *
 */
 public class PayTypeInfo{
	public PayTypeInfo(PayType type,String text,int iconResID){
		this.type = type;
		this.text = text;
		this.iconResID = iconResID;
	}
	
	//支付类型
	public PayType type;
	//支付类型说明
	public String text;
	//图标
	public int iconResID;
	//选中
	public boolean selected;
}