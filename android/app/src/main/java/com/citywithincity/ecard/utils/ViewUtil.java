package com.citywithincity.ecard.utils;

import android.app.Activity;

import com.citywithincity.ecard.models.vos.ShopInfo;
public class ViewUtil {
	
	public static void openRouteActivity(Activity context,
			String addr, double lat,double lng) {
		MapUtil.openRoutePlanActivity(context, addr, lat, lng);//(context,shop.lat,shop.lng,shop.address);		
	}
	
	public static void openRouteActivity(Activity context,
			ShopInfo shop) {
		MapUtil.openRoutePlanActivity(context, shop.address, shop.lat, shop.lng);//(context,shop.lat,shop.lng,shop.address);		
	}
	
	
	
	


}
