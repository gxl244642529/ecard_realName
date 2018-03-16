package com.citywithincity.ecard.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.damai.auto.DMWebActivity;
import com.damai.map.LocationInfo;
import com.damai.map.LocationUtil;

import java.util.List;

public class MapUtil {

	public static final String MINE="我的位置";

	
	//开始位置
	public static String startPosition;
	//结束位置
	public static String endPosition;

	public static int seletedIndex;

	/**
	 * 打开路线搜索
	 * @param address
	 * @param lat
	 * @param lng
	 */
	public static void openRoutePlanActivity(Context context,String address,double lat,double lng){
		/*RouteSearchInfo info = new RouteSearchInfo();
		info.destName = address;
		info.destlat = lat;
		info.destlng = lng;
		ActivityUtil.startActivity(context, RoutePlanActivity.class,info);
*/
		LocationInfo location = LocationUtil.getCachedLocation();


        String url = String.format("http://m.amap.com/navi/?start=%f,%f&dest=%f,%f&destName=%s&naviBy=car&key=7ffc0743945d569cc8501e757af68d25",
              location.getLng(),
				location.getLat(),
				lng,lat,
				address
				);

		DMWebActivity.openUrl( (Activity) context,url,"路线");

	}
	
	
	
	public static String getDistance(int dis)
	{
		int km=dis /1000;
		int m=dis % 1000;
		if(km>0)
		{
			return km + "." + (m/100) + "公里";
		}
		return dis + "米";
	}



}
