package com.citywithincity.imageeditor;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.imageeditor.utils.BitmapUtils;
import com.citywithincity.utils.ThreadUtil;
import com.damai.util.ViewUtil;

@SuppressLint("ValidFragment")
class EditorFilterFragment extends AbsEditorFragment implements OnClickListener, Runnable {

	//LOMO
	float colormatrix_lomo[] = {
	    1.7f,  0.1f, 0.1f, 0, -73.1f,
	    0,  1.7f, 0.1f, 0, -73.1f,
	    0,  0.1f, 1.6f, 0, -73.1f,
	    0,  0, 0, 1.0f, 0 };

	//黑白
	float colormatrix_heibai[] = {
	    0.8f,  1.6f, 0.2f, 0, -163.9f,
	    0.8f,  1.6f, 0.2f, 0, -163.9f,
	    0.8f,  1.6f, 0.2f, 0, -163.9f,
	    0,  0, 0, 1.0f, 0 };
	//复古
	float colormatrix_huajiu[] = {
	    0.2f,0.5f, 0.1f, 0, 40.8f,
	    0.2f, 0.5f, 0.1f, 0, 40.8f,
	    0.2f,0.5f, 0.1f, 0, 40.8f,
	    0, 0, 0, 1, 0 };

	//哥特
	float colormatrix_gete[] = {
	    1.9f,-0.3f, -0.2f, 0,-87.0f,
	    -0.2f, 1.7f, -0.1f, 0, -87.0f,
	    -0.1f,-0.6f, 2.0f, 0, -87.0f,
	    0, 0, 0, 1.0f, 0 };

	//锐化
	 float colormatrix_ruise[] = {
	    4.8f,-1.0f, -0.1f, 0,-388.4f,
	    -0.5f,4.4f, -0.1f, 0,-388.4f,
	    -0.5f,-1.0f, 5.2f, 0,-388.4f,
	    0, 0, 0, 1.0f, 0 };

	//淡雅
	 float colormatrix_danya[] = {
	    0.6f,0.3f, 0.1f, 0,73.3f,
	    0.2f,0.7f, 0.1f, 0,73.3f,
	    0.2f,0.3f, 0.4f, 0,73.3f,
	    0, 0, 0, 1.0f, 0 };


	//酒红
	 float colormatrix_jiuhong[] = {
	    1.2f,0.0f, 0.0f, 0.0f,0.0f,
	    0.0f,0.9f, 0.0f, 0.0f,0.0f,
	    0.0f,0.0f, 0.8f, 0.0f,0.0f,
	    0, 0, 0, 1.0f, 0 };

	//清宁
	 float colormatrix_qingning[] = {
	    0.9f, 0, 0, 0, 0,
	    0, 1.1f,0, 0, 0,
	    0, 0, 0.9f, 0, 0,
	    0, 0, 0, 1.0f, 0 };

	//浪漫
	 float colormatrix_langman[] = {
	    0.9f, 0, 0, 0, 63.0f,
	    0, 0.9f,0, 0, 63.0f,
	    0, 0, 0.9f, 0, 63.0f,
	    0, 0, 0, 1.0f, 0 };

	//光晕
	 float colormatrix_guangyun[] = {
	    0.9f, 0, 0,  0, 64.9f,
	    0, 0.9f,0,  0, 64.9f,
	    0, 0, 0.9f,  0, 64.9f,
	    0, 0, 0, 1.0f, 0 };

	//蓝调
	 float colormatrix_landiao[] = {
	    2.1f, -1.4f, 0.6f, 0.0f, -31.0f,
	    -0.3f, 2.0f, -0.3f, 0.0f, -31.0f,
	    -1.1f, -0.2f, 2.6f, 0.0f, -31.0f,
	    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
	};

	//梦幻
	 float colormatrix_menghuan[] = {
	    0.8f, 0.3f, 0.1f, 0.0f, 46.5f,
	    0.1f, 0.9f, 0.0f, 0.0f, 46.5f,
	    0.1f, 0.3f, 0.7f, 0.0f, 46.5f,
	    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
	};



	//夜色
	 float colormatrix_yese[] = {
	    1.0f, 0.0f, 0.0f, 0.0f, -66.6f,
	    0.0f, 1.1f, 0.0f, 0.0f, -66.6f,
	    0.0f, 0.0f, 1.0f, 0.0f, -66.6f,
	    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
	};


	 float image_filters[][] = {
	    colormatrix_lomo,
	    colormatrix_heibai,
	    colormatrix_huajiu,
	    colormatrix_gete,
	    colormatrix_ruise,
	    colormatrix_danya,
	    colormatrix_jiuhong,
	    colormatrix_qingning,
	    colormatrix_langman,
	    colormatrix_guangyun,
	    colormatrix_landiao,
	    colormatrix_menghuan,
	    colormatrix_yese
	};

	 String g_filter_names[]={
	    "原图",
	    "LOMO",
	    "黑白",
	    "复古",
	    "哥特",
	    "锐化",
	    "淡雅",
	    "酒红",
	    "清宁",
	    "浪漫",
	    "光晕",
	    "蓝调",
	    "梦幻",
	    "夜色"
	};
	 
	
	 private Handler handler;
	private volatile boolean exit;
	private ViewGroup viewGroup;

	private int lastIndex;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return  inflater.inflate(R.layout.editor_fragment_filter, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setTitle("滤镜");
		lastIndex = -1;
		viewGroup = (ViewGroup)view.findViewById(R.id.editor_tools);
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		for (int i = 0,count= image_filters.length+1; i < count; i++) {
			View item = inflater.inflate(R.layout.item_filter_tool, null);
			item.setTag(i);
			viewGroup.addView(item);
			item.setOnClickListener(this);
			TextView textView = (TextView)item.findViewById(R.id._text_view);
			textView.setText(g_filter_names[i]);
			if(i==0){
				item.setSelected(true);
				lastIndex = 0;
			}
		}
		exit = false;
		handler = new MyHandler(Looper.myLooper());
		ThreadUtil.run(this);
	}
	
protected class MyHandler extends Handler{
		
		public MyHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			View view = viewGroup.getChildAt(msg.what);
			ImageView imageView = (ImageView)view.findViewById(R.id._image_view);
			imageView.setImageBitmap((Bitmap)msg.obj);
		}
	}

	@Override
	public void run() {
		if(exit){
			return;
		}
		Bitmap bitmap = createSmallImage();
		if(exit){
			return;
		}
		Message msg = Message.obtain();
		msg.what = 0;
		msg.obj = bitmap;
		handler.sendMessage(msg);
		
		for(int i=0; i < image_filters.length; ++i){
			if(exit){
				return;
			}
			msg = Message.obtain();
			msg.what = i+1;
			bitmap = createFilterBitmap(bitmap, image_filters[i]);
			if(exit){
				bitmap.recycle();
				return;
			}
			msg.obj = bitmap;
			handler.sendMessage(msg);
		}
	}
	
	
	
	protected Bitmap createSmallImage(){
		int filterWidth = (int) getResources().getDimension(R.dimen.item_filter_width);
		int filterHeight = (int) getResources().getDimension(R.dimen.item_filter_height);
		filterWidth = (int) ViewUtil.dipToPx(filterWidth);
		filterHeight = (int) ViewUtil.dipToPx(filterHeight);
		return BitmapUtils.smallImage(workingBitmap, filterWidth, filterHeight);
	}
	
	/**
	 * 
	 * @param src
	 * @param filter
	 * @return
	 */
	protected Bitmap createFilterBitmap(Bitmap src,float[] filter){
		Bitmap bmp = Bitmap.createBitmap(src.getWidth(),
				src.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.set(filter);
		paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
		canvas.drawBitmap(src, 0, 0, paint);
		return bmp;
	}
	
	protected void applyFilter(int index){
		if(index==0){
			thisImageView.setImageBitmap(workingBitmap);
		}else{
			Bitmap bmp = createFilterBitmap(workingBitmap,image_filters[index-1]);
			thisImageView.setImageBitmap(bmp);
		}
	}

	
	@Override
	protected Bitmap executeImage(Bitmap bm) throws OutOfMemoryError {
		if(lastIndex==0){
			return null;
		}else{
			Bitmap bmp = createFilterBitmap(bm,image_filters[lastIndex-1]);
			return bmp;
		}
	}

	@Override
	public void onDestroy() {
		exit = true;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		int index = (Integer)v.getTag();
		if(index==lastIndex)return;
		applyFilter(index);
		if(lastIndex>=0){
			viewGroup.getChildAt(lastIndex).setSelected(false);
		}
		lastIndex = index;
		viewGroup.getChildAt(lastIndex).setSelected(true);
	}
}
