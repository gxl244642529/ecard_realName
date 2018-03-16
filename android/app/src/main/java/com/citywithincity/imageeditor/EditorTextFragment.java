package com.citywithincity.imageeditor;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.imageeditor.widget.ColorSelector;
import com.citywithincity.imageeditor.widget.ColorSelector.IColorChangeListener;
import com.citywithincity.interfaces.IOnTabChangeListener;
import com.citywithincity.pattern.wrapper.TabWrap;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.Alert.IInputListener;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.citywithincity.utils.SizeUtil;
import com.damai.util.ViewUtil;

@SuppressLint("ValidFragment")
class EditorTextFragment extends AbsEditorFragment implements
		OnSeekBarChangeListener, OnTouchListener, OnPreDrawListener,
		OnGestureListener, IOnTabChangeListener, IColorChangeListener {
	public static final int PADDING = (int) ViewUtil.dipToPx(10);
	private Rect maxRect;
	private View imageContainer;
	private TextView textView;
	private GestureDetector gesture;
	private int containerHeight;
	private int containerWidth;

	private SeekBar textDirectionBar, textSizeBar;
	TabWrap tabWrap;
	ViewGroup toolContainer;
	ColorSelector colorSelector;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.editor_fragment_text, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setTitle("编辑文字");
		imageContainer = view.findViewById(R.id.image_container);
		maxRect = new Rect();
		textView = (TextView) view.findViewById(R.id._text_view);
		drawTextView = (TextView) view.findViewById(R.id.editor_draw_text);
		
		colorSelector = (ColorSelector) view.findViewById(R.id.editor_color_selector);
		colorSelector.setOnColorChangeListener(this);

		textDirectionBar = (SeekBar) view.findViewById(R.id.seek_bar1);
		textSizeBar = (SeekBar) view.findViewById(R.id.seek_bar2);
		textSizeBar.setOnSeekBarChangeListener(this);
		textDirectionBar.setOnSeekBarChangeListener(this);
		textDirectionBar.setMax(360);
		// 文字为progress+5
		textSizeBar.setMax(25);
		textSizeBar.setProgress(9);

		// 获取图片范围
		ImageView imageView = (ImageView) view.findViewById(R.id._image_view);
		imageView.setImageBitmap(workingBitmap);
		ViewTreeObserver vto = imageContainer.getViewTreeObserver();
		vto.addOnPreDrawListener(this);
		gesture = new GestureDetector(getActivity(), this);

		ViewGroup tools = (ViewGroup) view.findViewById(R.id.editor_tools);
		tabWrap = new TabWrap(tools, this);

		toolContainer = (ViewGroup) view
				.findViewById(R.id.editor_tools_container);
		onTabChange(0);
		tabWrap.setSelectedIndex(0);
	}

	@Override
	public boolean onPreDraw() {
		int height = imageContainer.getMeasuredHeight();
		int width = imageContainer.getMeasuredWidth();

		containerHeight = height;
		containerWidth = width;

		ViewTreeObserver vto = imageContainer.getViewTreeObserver();
		vto.removeOnPreDrawListener(this);

		// 这里
		calcImageRect(workingBitmap);
		textView.setOnTouchListener(this);
		return true;
	}

	private void calcImageRect(Bitmap bitmap) {

		int destWidth = containerWidth - PADDING * 2;
		int destHeight = containerHeight - PADDING * 2;

		int targetWidth = bitmap.getWidth();
		int targetHeight = bitmap.getHeight();

		// 璁＄畻澶у皬
		float dx = (float) destWidth / targetWidth;
		float dy = (float) destHeight / targetHeight;
		dx = Math.min(dx, dy);

		int w = (int) (targetWidth * dx);
		int h = (int) (targetHeight * dx);

		int left = (destWidth - w) / 2 + PADDING;
		int top = (destHeight - h) / 2 + PADDING;

		AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) thisImageView
				.getLayoutParams();
		lp.x = left;
		lp.y = top;
		lp.width = w;
		lp.height = h;

		thisImageView.setLayoutParams(lp);

		maxRect.set(left, top, left + w, top + h);

		Point pt = SizeUtil.measureView(textView);
		lp = (AbsoluteLayout.LayoutParams) textView.getLayoutParams();
		lp.x = maxRect.left + (maxRect.width() - pt.x) / 2;
		lp.y = maxRect.top + (maxRect.height() - pt.y) / 2;

		textView.setLayoutParams(lp);

		calcTextSize();
	}

	private void calcTextSize() {
		float ax = ((float) maxRect.width()) / ((float) imageSize.x);
		float realSize = (textSizeBar.getProgress() + 5) / ax;
		drawTextView.setTextSize(realSize);
	}

	private TextView drawTextView;
	private volatile boolean isProccessed;

	private Bitmap drawingBitmap;

	@Override
	protected Bitmap executeImage(Bitmap bm) throws OutOfMemoryError {

		float ax = ((float) maxRect.width()) / ((float) bm.getWidth());
		float l = textView.getLeft() - maxRect.left;
		float t = textView.getTop() - maxRect.top;

		synchronized (drawTextView) {
			isProccessed = false;
		}

		MessageUtil.sendMessage(0, new IMessageListener() {

			@Override
			public void onMessage(int message) {
				drawTextView.setDrawingCacheEnabled(true);
				drawTextView.buildDrawingCache();
				drawingBitmap = drawTextView.getDrawingCache();
				synchronized (drawTextView) {
					isProccessed = true;
					drawTextView.notify();
				}
			}
		});
		synchronized (drawTextView) {
			if (!isProccessed) {
				try {
					drawTextView.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		Bitmap nBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
				Config.RGB_565);
		// Bitmap nBitmap = Bitmap.createBitmap(bm);
		Canvas canvas = new Canvas(nBitmap);
		canvas.drawBitmap(bm, 0, 0, null);
		canvas.drawBitmap(drawingBitmap, l / ax, t / ax, null);
		MessageUtil.sendMessage(0, new IMessageListener() {

			@Override
			public void onMessage(int message) {
				drawTextView.destroyDrawingCache();
			}
		});

		return nBitmap;
	}

	private float offsetX;
	private float offsetY;

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			AbsoluteLayout.LayoutParams lParams = (AbsoluteLayout.LayoutParams) textView
					.getLayoutParams();
			offsetX = event.getRawX() - lParams.x;
			offsetY = event.getRawY() - lParams.y;
		}
			break;
		case MotionEvent.ACTION_MOVE: {

			AbsoluteLayout.LayoutParams lParams = (AbsoluteLayout.LayoutParams) textView
					.getLayoutParams();
			lParams.x = (int) (event.getRawX() - offsetX);
			lParams.y = (int) (event.getRawY() - offsetY);

			int minX = maxRect.left - textView.getMeasuredWidth() / 2;
			int maxX = maxRect.left + maxRect.width()
					- textView.getMeasuredWidth() / 2;
			int minY = maxRect.top - textView.getMeasuredHeight() / 2;
			int maxY = maxRect.top + maxRect.height()
					- textView.getMeasuredHeight() / 2;

			lParams.x = Math.min(Math.max(minX, lParams.x), maxX);
			lParams.y = Math.min(Math.max(minY, lParams.y), maxY);

			textView.setLayoutParams(lParams);
		}
			break;
		default:
			break;
		}
		gesture.onTouchEvent(event);
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		System.out.println(e1);
		return false;
	}

	@SuppressLint("NewApi")
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (seekBar == textDirectionBar) {
			textView.setRotation(progress);
		} else if (seekBar == textSizeBar) {
			textView.setTextSize(progress + 5);

			AbsoluteLayout.LayoutParams lParams = (AbsoluteLayout.LayoutParams) textView
					.getLayoutParams();

			int minX = maxRect.left - textView.getMeasuredWidth() / 2;
			int maxX = maxRect.left + maxRect.width()
					- textView.getMeasuredWidth() / 2;
			int minY = maxRect.top - textView.getMeasuredHeight() / 2;
			int maxY = maxRect.top + maxRect.height()
					- textView.getMeasuredHeight() / 2;

			lParams.x = Math.min(Math.max(minX, lParams.x), maxX);
			lParams.y = Math.min(Math.max(minY, lParams.y), maxY);

			textView.setLayoutParams(lParams);

			calcTextSize();
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
	public void onTabChange(int index) {
		for (int i = 0, count = toolContainer.getChildCount(); i < count; ++i) {
			View view = toolContainer.getChildAt(i);
			if (i == index) {
				view.setVisibility(View.VISIBLE);
			} else {
				view.setVisibility(View.GONE);
			}
		}

		if (index == 0) {

			Alert.input(getActivity(), "文字", Alert.OK_CANCEL, textView.getText()
					.toString(), new IInputListener() {

				@Override
				public void onInput(String text) {
					if(TextUtils.isEmpty(text)){
						Alert.showShortToast("请输入文字");
						return;
					}
					textView.setText(text);
					drawTextView.setText(text);
					onTabChange(1);
					tabWrap.setSelectedIndex(1);
				}

				@Override
				public void onInputCancel() {
					if(textView.getText().toString().equals("")){
						onCancelEditImage();
					}else{
						onTabChange(1);
						tabWrap.setSelectedIndex(1);
					}
				}
			});

		}
	}

	@Override
	public void onColorChange(int color) {
		drawTextView.setTextColor(color);
		textView.setTextColor(color);
	}
}
