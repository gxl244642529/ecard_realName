package com.citywithincity.ecard.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.interfaces.IECardJsonTaskManager;
import com.citywithincity.ecard.interfaces.IECardPlatform.MyCouponInfo;
import com.citywithincity.ecard.models.vos.BusinessInfo;
import com.citywithincity.ecard.models.vos.CouponInfo;
import com.citywithincity.ecard.models.vos.ShopInfo;
import com.citywithincity.interfaces.IJsonTaskManager;
import com.damai.map.LocationInfo;
import com.damai.map.LocationUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JsonUtil {
	
	public static final int  Status_Online = 3;
	public static final int  Status_New = 1;
	public static final int  Status_Locked = 4;
	
	public static String getString(JSONObject json,String name) throws JSONException{
		String str = json.getString(name);
		if(str.equals("null")){
			return "";
		}
		if(str==null)return "";
		return str;
	}
	
	public static String parseTime(String strTime) {
		StringBuilder sb = new StringBuilder();
		sb.append(strTime.substring(0, 4));
		sb.append("年");
		sb.append(strTime.substring(4, 6));
		sb.append("月");
		sb.append(strTime.substring(6, 8));
		sb.append("日 ");
		sb.append(strTime.substring(8, 10));
		sb.append("点");
		sb.append(strTime.substring(10, 12));
		sb.append("分");
		return sb.toString();
	}
	
	

	
	public static void setShopItemData(View view,ShopInfo data){
		TextView name=(TextView)view.findViewById(R.id.coupondetail_shop1_name);
		TextView addr=(TextView)view.findViewById(R.id.coupondetail_shop1_addr);
		TextView distance=(TextView)view.findViewById(R.id.shop_distance);
		name.setText(data.title.substring(0,Math.min(14,data.title.length())));
		addr.setText(data.address.substring(0, Math.min(14,data.address.length())));
		distance.setText(data.disString);
		if(TextUtils.isEmpty(data.phone))
		{
			view.findViewById(R.id.shop_phone).setVisibility(View.GONE);
			view.findViewById(R.id.seprator).setVisibility(View.GONE);
		}else{
			view.findViewById(R.id.shop_phone).setVisibility(View.VISIBLE);
			view.findViewById(R.id.seprator).setVisibility(View.VISIBLE);
		}
	}
	
	public static void setShopItemData(Activity view,ShopInfo data){
		TextView name=(TextView)view.findViewById(R.id.coupondetail_shop1_name);
		TextView addr=(TextView)view.findViewById(R.id.coupondetail_shop1_addr);
		TextView distance=(TextView)view.findViewById(R.id.shop_distance);
		name.setText(data.title.substring(0,Math.min(14,data.title.length())));
		addr.setText(data.address.substring(0, Math.min(14,data.address.length())));
		distance.setText(data.disString);
		if(TextUtils.isEmpty(data.phone))
		{
			view.findViewById(R.id.shop_phone).setVisibility(View.GONE);
			view.findViewById(R.id.seprator).setVisibility(View.GONE);
		}else{
			view.findViewById(R.id.shop_phone).setVisibility(View.VISIBLE);
			view.findViewById(R.id.seprator).setVisibility(View.VISIBLE);
		}
	}
	
	
	
	
	public static void setBusinessData(IECardJsonTaskManager platform,View view, BusinessInfo data){
		ImageView image=(ImageView)view.findViewById(R.id.business_image);
		platform.setImageSrc(data.img, image);
		((TextView)view.findViewById(R.id.business_title)).setText(data.title);
	}   
	
	public static void setMyCouponData(IECardJsonTaskManager platform,View view, MyCouponInfo data){
		ImageView couponImg = (ImageView)view.findViewById(R.id.coupon_img);
		TextView introTextView=(TextView)view.findViewById(R.id.coupon_intro);
		platform.setImageSrc(data.img, couponImg);
		introTextView.setText(data.title);
		
		((TextView)view.findViewById(R.id.coupon_title)).setText(data.bsname);
		
		((TextView)view.findViewById(R.id.coupon_purch)).setText("有效期至"+data.etime);
	}
	
	
	
	
	public static void setCouponData(IJsonTaskManager platform,View view,CouponInfo data){
		ImageView couponImg = (ImageView)view.findViewById(R.id.coupon_img);
		TextView titleTextView = (TextView)view.findViewById(R.id.coupon_title);
		TextView introTextView=(TextView)view.findViewById(R.id.coupon_intro);
		TextView priceTextView = (TextView)view.findViewById(R.id.coupon_purch);
		platform.setImageSrc(data.img, couponImg);
		introTextView.setText(data.title);
		titleTextView.setText(data.bsname);
		switch (data.type) {
		case CouponType_Limit:
			priceTextView.setText("剩余: "+(data.total-data.used));
			break;
		case CouponType_Qr:
			priceTextView.setText("验证有效，不限次数");
			break;
		case CouponType_Show:
			priceTextView.setText("出示即可使用");
			break;
		case CouponType_ECard:
			priceTextView.setText("刷e通卡享受优惠");
			break;
		default:
			break;
		}
	}




	// /////////////////
	public static String getImageUrl(String img) {
		return ECardConfig.BASE_URL + img;
	}
	
	public static String getJavaImageUrl(String img) {
		if(img.startsWith("http")){
			return img;
		}
		if (img.startsWith("/")) {
			return ECardConfig.JAVA_SERVER + img.substring(1);
		}
		return ECardConfig.JAVA_SERVER + img;
	}
	
	
	
	public static class ComparatorDistance implements Comparator<ShopInfo> {
		@Override
		public int compare(ShopInfo arg0, ShopInfo arg1) {
			return arg0.distance.compareTo(arg1.distance);
		}
	}
	
	
	public static String getDistance(double lat,double lng){
		LocationInfo info = LocationUtil.getCachedLocation();
		return MapUtil.getDistance((int)  LocationUtil.getDistance(
				info.getLat(),info.getLng(),lat,lng
		) );
	}
	
	
	public static ArrayList<ShopInfo> parseShops(JSONArray jshops) throws JSONException {
		ArrayList<ShopInfo> group = new ArrayList<ShopInfo>();

		LocationInfo locationInfo = LocationUtil.getCachedLocation();


		int count = jshops.length();
		
		for (int i = 0; i < count; i++) {

			JSONObject jshop = jshops.getJSONObject(i);
			ShopInfo info = new ShopInfo();
			// info.id=jshop.getInt("ID");
			info.title = jshop.getString("TITLE");
			info.phone = jshop.getString("PHONE");
			info.address = jshop.getString("ADDRESS");
			info.lat = jshop.getDouble("LAT");
			info.lng = jshop.getDouble("LNG");
			info.distance = LocationUtil.getDistance(locationInfo.getLat(),locationInfo.getLng(),
					info.lat, info.lng);
			info.disString = MapUtil.getDistance((int)(double)info.distance);
			
			group.add(info);
		}
		Collections.sort(group, new JsonUtil.ComparatorDistance());
		return group;

	}




	public static boolean getBooean(JSONObject json, String name) throws JSONException {
		return json.getString(name).equals("1");
	}
	public static int getInt(JSONObject json, String name) {
		int ret = 0;
		try {
			ret = json.getInt(name);
		} catch (JSONException e) {
			
		}
		return ret;
	}
}
