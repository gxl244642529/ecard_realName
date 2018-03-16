package com.citywithincity.models.vos;

import com.citywithincity.auto.Databind;

@Databind
public class AddressVo {
	/**
	* 经纬度
	*/
	public double lat;
	/**
	* 经纬度
	*/
	public double lng;

	
	public String distance;
	
	public String title;
	
	public String address;
}
