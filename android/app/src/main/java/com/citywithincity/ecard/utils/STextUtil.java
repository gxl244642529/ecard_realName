package com.citywithincity.ecard.utils;

import android.graphics.Color;
import android.widget.TextView;

import com.citywithincity.ecard.SellingConfig;

public class STextUtil {

	public static void setTextStateBg(TextView view, int state) {
		switch (state) {
		case SellingConfig.ORDER_NOPAY:
//			view.setBackgroundColor(Color.parseColor("#f86c19"));
//			view.setTextColor(Color.parseColor("#f86c19"));
			view.setText("待付款");
			break;

		case SellingConfig.ORDER_PAYED:
//			view.setBackgroundColor(Color.parseColor("#66b0f1"));
//			view.setTextColor(Color.parseColor("#66b0f1"));
			view.setText("待发货");
			break;

		case SellingConfig.ORDER_DELIVERED:
//			view.setBackgroundColor(Color.parseColor("#84c302"));
//			view.setTextColor(Color.parseColor("#84c302"));
			view.setText("待收货");
			break;

//		case SellingConfig.:
////			view.setBackgroundColor(Color.parseColor("#9b9bf1"));
////			view.setTextColor(Color.parseColor("#9b9bf1"));
//			view.setText("预约");
//			break;
		case SellingConfig.ORDER_REFUND:
//			view.setBackgroundColor(Color.parseColor("#9b9bf1"));
//			view.setTextColor(Color.parseColor("#9b9bf1"));
			view.setText("订单已完成退款");
			break;
			
		case SellingConfig.ORDER_CLOSED:
//			view.setBackgroundColor(Color.parseColor("#9b9bf1"));
//			view.setTextColor(Color.parseColor("#9b9bf1"));
			view.setText("订单已关闭");
			break;

		case SellingConfig.ORDER_RECEIVED:
//			view.setBackgroundColor(Color.parseColor("#ff3d2c"));
//			view.setTextColor(Color.parseColor("#ff3d2c"));
			view.setText("已完成");
			break;

		default:
			break;
		}
	}
	
	public static void setTextColorState(TextView view, int state) {
		switch (state) {
		case SellingConfig.ORDER_NOPAY:
			view.setTextColor(Color.parseColor("#f86c19"));
			view.setText("待付款");
			break;

		case SellingConfig.ORDER_PAYED:
			view.setTextColor(Color.parseColor("#66b0f1"));
			view.setText("待发货");
			break;

		case SellingConfig.ORDER_DELIVERED:
			view.setTextColor(Color.parseColor("#66b0f1"));
			view.setText("待收货");
			break;

//		case SellingConfig.:
//			view.setTextColor(Color.parseColor("#9b9bf1"));
//			view.setText("预约");
//			break;

		case SellingConfig.ORDER_RECEIVED:
			view.setTextColor(Color.parseColor("#ff3d2c"));
			view.setText("已完成");
			break;
		case SellingConfig.ORDER_REFUND:
			view.setTextColor(Color.parseColor("#9b9bf1"));
			view.setText("已退款");
			break;
			
		case SellingConfig.ORDER_CLOSED:
//			view.setBackgroundColor(Color.parseColor("#9b9bf1"));
			view.setTextColor(Color.parseColor("#7a706f"));
			view.setText("订单已关闭");
			break;

		default:
			break;
		}
	}
}
