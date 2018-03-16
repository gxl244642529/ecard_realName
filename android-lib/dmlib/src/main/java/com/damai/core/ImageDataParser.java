package com.damai.core;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

import com.damai.dmlib.DMParseException;

@SuppressWarnings("rawtypes")
public class ImageDataParser implements DataParser {
	
	private Config defaultConfig;
	
	public static final Object lock = new Object();
	
	public ImageDataParser(){
		defaultConfig = Config.RGB_565;
	}
	
	@Override
	public Object parseData(HttpJobImpl api, byte[] data) throws DMParseException {
		synchronized (lock) {
			try{
				/**
				 * 这里假设服务器下载的图片都是经过计算的
				 */
				BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
				decodeOptions.inPreferredConfig = defaultConfig;
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,decodeOptions);
				if(bitmap==null){
					throw new DMParseException("Data is not a bitmap");
				}
				return bitmap;
			}catch(OutOfMemoryError e){
				e.printStackTrace();
				throw new DMParseException(e);
			}
		}
		
	}

}
