package com.citywithincity.ecard.utils;

import android.graphics.Point;
import android.view.View;

import com.citywithincity.ecard.enums.ImageEnum;
import com.citywithincity.utils.SizeUtil;

public class Utils {

	public static void setViewSize(View view) {
		// 获取长宽
		Point pt = SizeUtil.measureView(view);
		SizeUtil.setViewSize(view, ImageEnum.IMAGE_WIDTH,
				ImageEnum.IMAGE_HEIGHT, pt.x);
	}

}
