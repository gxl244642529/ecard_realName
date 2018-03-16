package com.citywithincity.ecard.models.vos;

import java.io.Serializable;

/**
 * 分店信息
 * @author Randy
 *
 */
public class ShopInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;//分店id
	public String phone;//电话
	public String title;//分店名字
	public String address;//分店地址
	public Double distance;
	public String disString;
	public double lat;
	public double lng;
}