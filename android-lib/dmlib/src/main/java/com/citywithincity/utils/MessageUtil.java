package com.citywithincity.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;


/**
 * 本类用于在其他线程中向ui线程发送消息
 * @author Randy
 *
 */
public class MessageUtil {
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	// private
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	private static MyHandler hander = new MyHandler(Looper.getMainLooper());

	public static interface IMessageListener {
		void onMessage(int message);
	}

	public static void sendMessage(int message, IMessageListener listener) {
		sendMessage(message, listener, 0);
	}
	public static void sendMessage(IMessageListener listener) {
		sendMessage(0, listener, 0);
	}
	

	/**
	 * 发送消息
	 * @param message		消息id
	 * @param listener			监听，这里的代码在ui线程运行
	 * @param delayed		延迟 毫秒
	 */
	public static void sendMessage(int message, IMessageListener listener,
			int delayed) {
		Message msg = Message.obtain();
		msg.obj = listener;
		msg.what = message;
		hander.sendMessageDelayed(msg, delayed);
	}

	

	protected static class MyHandler extends Handler {
		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			IMessageListener listener = (IMessageListener) msg.obj;
			if(listener instanceof Activity){
				if(((Activity)listener).isFinishing()){
					return;
				}
			}
			listener.onMessage(msg.what);
		}
	}
}
