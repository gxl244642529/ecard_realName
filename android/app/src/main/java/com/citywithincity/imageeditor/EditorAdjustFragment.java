package com.citywithincity.imageeditor;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.citywithincity.ecard.R;
import com.citywithincity.imageeditor.utils.BitmapUtils;
import com.citywithincity.interfaces.IOnTabChangeListener;
import com.citywithincity.pattern.wrapper.TabWrap;

@SuppressLint("ValidFragment")
class EditorAdjustFragment extends AbsEditorFragment implements
		OnSeekBarChangeListener, IOnTabChangeListener {
	public static final int DEF = 0;
	public static final int MAX = 1;

	int sat;
	int bright;
	int con;
	SeekBar seekBarSat;
	SeekBar seekBarBright;
	SeekBar seekBarCon;
	private Bitmap bitmap;
	private int index;
	TabWrap tabWrap ;
	ViewGroup toolContainer;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.editor_fragment_adjust, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setTitle("调整颜色");
		seekBarSat = (SeekBar) view.findViewById(R.id.seek_bar1);
		seekBarSat.setMax(200);
		seekBarSat.setProgress(100);
		seekBarBright = (SeekBar) view.findViewById(R.id.seek_bar2);
		seekBarBright.setMax(255);
		seekBarBright.setProgress(127);
		seekBarCon = (SeekBar) view.findViewById(R.id.seek_bar3);
		seekBarCon.setMax(127);
		seekBarCon.setProgress(63);
		seekBarSat.setOnSeekBarChangeListener(this);
		seekBarBright.setOnSeekBarChangeListener(this);
		seekBarCon.setOnSeekBarChangeListener(this);

		sat = 100;
		bright = 127;
		con = 63;
		// 拷贝
		bitmap = Bitmap.createBitmap(workingBitmap);
		
		ViewGroup tools = (ViewGroup) view.findViewById(R.id.editor_tools);
		
		tabWrap = new TabWrap( tools, this);
		
		toolContainer = (ViewGroup)view.findViewById(R.id.editor_tools_container);
		onTabChange(0);
		tabWrap.setSelectedIndex(0);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser) {
			Bitmap bmp = bitmap;

			if (seekBar == seekBarSat) {
				sat = progress;
			} else if (seekBar == seekBarBright) {
				bright = progress;
			} else if (seekBar == seekBarCon) {
				con = progress;
			}

			bmp = BitmapUtils.createSat(bmp, (float) sat / 100);
			bmp = BitmapUtils.createBright(bmp, bright - 127);
			bmp = BitmapUtils.createContrast(bmp, (float) ((con + 64) / 128.0));

			if (bmp != null) {
				thisImageView.setImageBitmap(bmp);
			}
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Bitmap executeImage(Bitmap bm) throws OutOfMemoryError {
		Bitmap bmp = null;
		switch (index) {
		case 0: {
			bmp = BitmapUtils.createSat(bm, (float) sat / 100);
		}
			break;
		case 1: {
			int brightness = bright - 127;
			bmp = BitmapUtils.createBright(bm, brightness);
		}
			break;
		case 2: {
			float contrast = (float) ((con + 64) / 128.0);
			bmp = BitmapUtils.createContrast(bm, contrast);
		}
			break;
		}
		return bmp;
	}

	@Override
	public void onDestroy() {
		bitmap = null;
		tabWrap.destroy();
		super.onDestroy();
	}

	@Override
	public void onTabChange(int index) {
		for(int i=0 , count = toolContainer.getChildCount(); i < count; ++i){
			View view = toolContainer.getChildAt(i);
			if(i==index){
				view.setVisibility(View.VISIBLE);
			}else{
				view.setVisibility(View.GONE);
			}
		}
	}
	


}
