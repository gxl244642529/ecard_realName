package com.citywithincity.ecard.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.citywithincity.ecard.interfaces.IECardJsonTaskManager;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

public class QrUtil {
	
	
	
	public static final String Qr_Coupon = "coupon";			//优惠券
	public static final String Qr_User = "user";						//用户二维码
	public static final String Qr_Business = "business";         //商家二维码
	
	public static final String Qr_Coupon2 = "coupon2";			//优惠券
	
	/**
	 * 用字符串生成二维码
	 * 
	 * @param str
	 * @author zhouzhe@lenovo-cw.com
	 * @return
	 * @throws WriterException
	 */
	public static Bitmap createRrCode(String str) throws WriterException {
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		int size = SystemUtil.dipToPx(150);
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, size, size);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}

			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	public static void createPersonalInfo(ImageView imageView,int userID){
		try {
			imageView.setImageBitmap(QrUtil.createRrCode(String.valueOf(userID)));
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public static String convertQrCodeForTextView(String code) {
		// 对code惊醒分析
		if (code.length() == 16) {
			StringBuilder sb = new StringBuilder();
			sb.append(code.substring(0, 4));
			sb.append("-");
			sb.append(code.substring(4, 8));
			sb.append("-");
			sb.append(code.substring(8, 12));
			sb.append("-");
			sb.append(code.substring(12, 16));
			String ncode = sb.toString();
			return ncode;
		}
		return code;
	}
	
	
	public static String generateQrCode(String type,String code){
		JSONObject obj = new JSONObject();
		try {
			obj.put("code", code);
			IECardJsonTaskManager taskManager = ECardJsonManager.getInstance();
			if(taskManager.isLogin())
			{
				obj.put("account", taskManager.getUserInfo().getId());
			}
			obj.put("deviceID", taskManager.getDeviceID());
			obj.put("type", type);
			return obj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void showQrView(Context context,String type, String code) {
		/*
		MyDialog dlg = new MyDialog(context);
		dlg.setTitle("提示");
		dlg.setContentView(R.layout.qr_layout);
		dlg.setCanceledOnTouchOutside(true);
		View contentView = dlg.getContentView();
		try {
			((ImageView)contentView.findViewById(R.id.qr_image)).setImageBitmap(createRrCode(generateQrCode(type, code)));
			((TextView)contentView.findViewById(R.id.qr_code)).setText(convertQrCodeForTextView(code));
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dlg.show();
		return dlg;
		*/

	}
}
