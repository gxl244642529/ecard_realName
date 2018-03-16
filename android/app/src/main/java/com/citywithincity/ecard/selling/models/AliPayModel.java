package com.citywithincity.ecard.selling.models;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.AlixId;

public class AliPayModel {

	private static AliPayModel instance;

	public static AliPayModel getInstance() {
		if (instance == null) {
			instance = new AliPayModel();
		}
		return instance;
	}

	public void aliPay(final Context context, final String info, final Handler handler) {
//		// 构造PayTask 对象
//		PayTask alipay = new PayTask((Activity) context);
//		
//		// 调用支付接口，获取支付结果
//		String result = alipay.pay(info);
//		
//		System.out.println("PayTask is run");
//		System.out.println(result);
//		
//		Message msg = new Message();
//		msg.what = AlixId.RQF_PAY;
//		msg.obj = result;
//		handler.sendMessage(msg);
		
		
		 Runnable payRunnable = new Runnable() {
		 @Override
		 public void run() {
			// 构造PayTask 对象
				PayTask alipay = new PayTask((Activity) context);
				
				// 调用支付接口，获取支付结果
				String result = alipay.pay(info,true);
				
				System.out.println("PayTask is run");
				System.out.println(result);
				
				Message msg = new Message();
				msg.what = AlixId.RQF_PAY;
				msg.obj = result;
				handler.sendMessage(msg);
		 }
		 };
		 // 必须异步调用
		 Thread payThread = new Thread(payRunnable);
		 payThread.start();
	}

}
