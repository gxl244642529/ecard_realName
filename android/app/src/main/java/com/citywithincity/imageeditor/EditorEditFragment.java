package com.citywithincity.imageeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.citywithincity.utils.SizeUtil;
import com.citywithincity.utils.SizeUtil.Size;
import com.damai.util.ViewUtil;

@SuppressLint("ValidFragment")
class EditorEditFragment extends AbsEditorFragment implements
		OnPreDrawListener, OnSeekBarChangeListener, OnTouchListener,
		OnClickListener {
	/**
	 * 放图片容器的宽度、高度
	 */
	protected int containerWidth;
	protected int containerHeight;

	public static final int PADDING = (int) ViewUtil.dipToPx(10);

	private SeekBar seekBar;
	// 鐢ㄤ簬鎴彇鍥剧墖鐨勫ぇ灏�
	private Rect rect;
	private MyEditView myEditView;
	private Paint paint;
	private MyPointView[] pointViews;
	// 30dip
	private final int pointSize;
	private Rect maxRect;

	// 水平翻转
	private float scaleX = 1;
	// 垂直翻转
	private float scaleY = 1;

	// 楂樺姣�
	private float rate;// 楂樺害/瀹藉害
	// 图片容器
	protected ViewGroup imageContainer;

	// 像素
	private TextView pixelView;

	private ImageView warning;

	class MyPointView extends View {

		private int mPosition;

		public MyPointView(Context context, int position) {
			super(context);
			paint = new Paint();
			mPosition = position;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			paint.setColor(Color.WHITE);
			if (mPosition == 0) {// LT
				canvas.drawRect(pointSize / 4, pointSize / 4, pointSize,
						pointSize / 2, paint);
				canvas.drawRect(pointSize / 4, pointSize / 4, pointSize / 2,
						pointSize, paint);
			} else if (mPosition == 1) {// RT
				canvas.drawRect(0, pointSize / 4, pointSize / 4 * 3,
						pointSize / 2, paint);
				canvas.drawRect(pointSize / 2, pointSize / 4,
						pointSize / 4 * 3, pointSize, paint);
			} else if (mPosition == 2) {// LB
				canvas.drawRect(pointSize / 4, 0, pointSize / 2,
						pointSize / 4 * 3, paint);
				canvas.drawRect(pointSize / 4, pointSize / 2, pointSize,
						pointSize / 4 * 3, paint);
			} else if (mPosition == 3) {// RB
				canvas.drawRect(0, pointSize / 2, pointSize / 4 * 3,
						pointSize / 4 * 3, paint);
				canvas.drawRect(pointSize / 2, 0, pointSize / 4 * 3,
						pointSize / 4 * 3, paint);
			}
		}

		@SuppressWarnings("deprecation")
		public int getCenterX() {
			return ((AbsoluteLayout.LayoutParams) getLayoutParams()).x
					+ pointSize / 2;
		}

		@SuppressWarnings("deprecation")
		public int getCenterY() {
			return ((AbsoluteLayout.LayoutParams) getLayoutParams()).y
					+ pointSize / 2;
		}

		@SuppressWarnings("deprecation")
		public void moveCenter(int x, int y) {
			AbsoluteLayout.LayoutParams lp = ((AbsoluteLayout.LayoutParams) getLayoutParams());
			lp.x = x - lp.width / 2;
			lp.y = y - lp.height / 2;
			setLayoutParams(lp);
		}

	}

	class MyEditView extends View {

		private Paint paint;
		/**
		 * 闃村奖棰滆壊
		 */
		private int maskColor;

		private Rect rect;

		public MyEditView(Context context) {
			super(context);
			// 涓嶆帴鍙椾簨浠�
			setFocusable(true);
			paint = new Paint();
			maskColor = Color.parseColor("#a0000000");
		}

		public void setRect(Rect rect) {
			this.rect = rect;
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			if (rect != null) {
				paint.setColor(maskColor);
				int width = containerWidth;
				int height = containerHeight;
				canvas.drawRect(0, 0, width, rect.top, paint);
				canvas.drawRect(0, rect.top, rect.left, rect.bottom, paint);
				canvas.drawRect(rect.right, rect.top, width, rect.bottom, paint);
				canvas.drawRect(0, rect.bottom, width, height, paint);

				paint.setColor(Color.WHITE);
				canvas.drawLine(rect.left, rect.top
						+ ((rect.bottom - rect.top) / 3), rect.right, rect.top
						+ +((rect.bottom - rect.top) / 3), paint);
				canvas.drawLine(rect.left, rect.top
						+ ((rect.bottom - rect.top) / 3 * 2), rect.right,
						rect.top + +((rect.bottom - rect.top) / 3 * 2), paint);
				canvas.drawLine(rect.left + ((rect.right - rect.left) / 3),
						rect.top, rect.left + ((rect.right - rect.left) / 3),
						rect.bottom, paint);
				canvas.drawLine(rect.left + ((rect.right - rect.left) / 3 * 2),
						rect.top, rect.left
								+ ((rect.right - rect.left) / 3 * 2),
						rect.bottom, paint);

			}

		}

	}

	public EditorEditFragment() {
		pointSize = (int) ViewUtil.dipToPx(20);
		rate = EditorUtil.rate;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.editor_fragment_edit, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setTitle("编辑图片");
		seekBar = (SeekBar) view.findViewById(R.id._seek_bar);
		seekBar.setMax(360);
		seekBar.setOnSeekBarChangeListener(this);
		rect = new Rect();
		maxRect = new Rect();

		// 计算
		ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.editor_tools);
		for (int i = 0, count = viewGroup.getChildCount(); i < count; ++i) {
			View child = viewGroup.getChildAt(i);
			child.setTag(i);
			child.setOnClickListener(this);
		}

		// 设置图片大小
		imageContainer = (ViewGroup) view.findViewById(R.id.image_container);
		ViewTreeObserver vto = imageContainer.getViewTreeObserver();
		vto.addOnPreDrawListener(this);
	}

	@Override
	public void onClick(View v) {
		int tag = (Integer) v.getTag();
		onTool(tag);
	}

	// 鍩哄噯鐐�

	protected OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int index = (Integer) v.getTag();
			MyPointView dragView = pointViews[index];
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				offsetX = event.getRawX() - dragView.getCenterX();
				offsetY = event.getRawY() - dragView.getCenterY();
				break;
			case MotionEvent.ACTION_MOVE: {
				int px = (int) (event.getRawX() - offsetX);
				switch (index) {
				case LT: {
					int x = rect.left + rect.width();
					int y = rect.top + rect.height();

					int x0 = x - (int) ((y - maxRect.top) / rate);
					int minX = Math.max(x0, maxRect.left);
					int maxX = x - pointSize;

					rect.left = Math.min(Math.max(minX, px), maxX);
					rect.top = (int) ((rect.left - rect.right) * rate + rect.bottom);
				}
					break;
				case RT: {

					int x = rect.left;
					int y = rect.bottom;
					int x0 = (int) (x + (y - maxRect.top) / rate);

					int minX = maxRect.left + pointSize;
					int maxX = Math.min(maxRect.right, x0);

					rect.right = Math.min(Math.max(minX, px), maxX);
					rect.top = (int) (rect.bottom - rate
							* (rect.right - rect.left));
				}
					break;
				case LB: {
					// 右上
					int x = rect.left + rect.width();
					int y = rect.top;

					int x0 = (int) (x - (maxRect.bottom - y) / rate);
					int minX = Math.max(x0, maxRect.left);
					int maxX = rect.right - pointSize;

					rect.left = Math.min(Math.max(minX, px), maxX);
					rect.bottom = (int) (rect.top - (rect.left - rect.right)
							* rate);
				}
					break;
				case RB: {
					// 坐上
					int x = rect.left;
					int y = rect.top;

					int x0 = (int) (x + (maxRect.bottom - y) / rate);
					int maxX = Math.min(x0, maxRect.right);
					int minX = rect.left + pointSize;

					rect.right = Math.min(Math.max(minX, px), maxX);
					rect.bottom = (int) (rect.top + rate
							* (rect.right - rect.left));

				}
					break;
				}

				refreshRect();
			}
				break;
			default:
				break;
			}
			return true;
		}
	};

	protected void refreshRect() {
		pointViews[LT].moveCenter(rect.left, rect.top);
		pointViews[LB].moveCenter(rect.left, rect.bottom);
		pointViews[RT].moveCenter(rect.right, rect.top);
		pointViews[RB].moveCenter(rect.right, rect.bottom);

		myEditView.setRect(rect);

		AbsoluteLayout.LayoutParams lp = (LayoutParams) pixelView
				.getLayoutParams();
		if (textSize != null) {
			lp.x = rect.centerX() - textSize.x / 2;
			lp.y = rect.centerY() - textSize.y / 2;
			textSize = null;
		} else {
			lp.x = rect.centerX() - pixelView.getWidth() / 2;
			lp.y = rect.centerY() - pixelView.getHeight() / 2;
		}
		pixelView.setLayoutParams(lp);

		lp = (LayoutParams) warning.getLayoutParams();
		if (warningSize != null) {
			lp.x = rect.centerX() - warningSize.x / 2;
			lp.y = rect.centerY() - warningSize.y / 2;
			warningSize = null;
		} else {
			lp.x = rect.centerX() - warning.getWidth() / 2;
			lp.y = rect.centerY() - warning.getHeight() / 2;
		}

		warning.setLayoutParams(lp);
		showSize();

	}

	protected void showSize() {

		/**
		 * 这里计算大小
		 */

		// 计算rect\maxrect的比例,
		float dx = rect.width() / (float) maxRect.width();
		// 现在图片大小
		float imageWidth = currentImageWidth * dx;
		// 计算和实际图片大小的比例
		// 这个时候的图片大小为
		//

		float readImageWidth = currentImageWidth * imageSize.x
				/ (float) workingBitmap.getWidth();

		float dy = currentImageWidth / readImageWidth;

		// 实际大小
		showPixel((int) (imageWidth / dy), 0);
	}

	public static final int LT = 0;
	public static final int RT = 1;
	public static final int LB = 2;
	public static final int RB = 3;

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		Bitmap bitmap = rotateBitmap(progress, workingBitmap);
		thisImageView.setImageBitmap(bitmap);
		calcImageRect(bitmap);
		showSize();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	/**
	 * 鏃嬭浆
	 * 
	 * @param degree
	 * @param bitmap
	 * @return
	 */
	public Bitmap rotateBitmap(int degree, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);
		matrix.postRotate(degree);

		Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return bm;
	}

	protected void onTool(int index) {

		switch (index) {
		case 0: {
			int progress = (seekBar.getProgress() / 90 * 90 + 90) % 360;
			seekBar.setProgress(progress);
		}
			break;
		case 1: {

			scaleX = -scaleX;
			onProgressChanged(seekBar, seekBar.getProgress(), true);
			break;
		}
		case 2: {
			scaleY = -scaleY;
			onProgressChanged(seekBar, seekBar.getProgress(), true);
			break;
		}
		default:
			break;
		}
	}

	private float offsetX;
	private float offsetY;
	private boolean drag = false;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			if (rect.contains((int) event.getX(), (int) event.getY())) {
				drag = true;
				offsetX = event.getRawX() - rect.left;
				offsetY = event.getRawY() - rect.top;
			}

		}
			break;
		case MotionEvent.ACTION_MOVE: {
			if (drag) {
				// 最大，最小
				int px = (int) (event.getRawX() - offsetX);
				int py = (int) (event.getRawY() - offsetY);
				px = Math.max(maxRect.left, px);
				py = Math.max(maxRect.top, py);
				px = Math.min(maxRect.right - rect.width(), px);
				py = Math.min(maxRect.bottom - rect.height(), py);
				rect.offsetTo(px, py);
				refreshRect();
			}

		}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			drag = false;
			break;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	protected void onInit() {

		myEditView = new MyEditView(getActivity());
		myEditView.setOnTouchListener(this);
		imageContainer.addView(myEditView, new AbsoluteLayout.LayoutParams(
				containerWidth, containerHeight, 0, 0));

		pointViews = new MyPointView[4];
		for (int i = 0; i < 4; ++i) {
			pointViews[i] = new MyPointView(getActivity(), i);
			pointViews[i].setTag(i);
			pointViews[i].setFocusable(true);
			pointViews[i].setOnTouchListener(onTouchListener);
			imageContainer
					.addView(pointViews[i], new AbsoluteLayout.LayoutParams(
							pointSize, pointSize, 0, 0));
		}

		pixelView = new TextView(getActivity());
		pixelView.setTextColor(Color.rgb(255, 255, 255));
		imageContainer.addView(pixelView);

		warning = new ImageView(getActivity());
		imageContainer.addView(warning);
		warning.setImageResource(R.drawable.e_ic_warning);

		calcImageRect(workingBitmap);

		int w = maxRect.width() - PADDING * 2;
		int h = maxRect.height() - PADDING * 2;

		Size size = SizeUtil.getCenterInsideSize(w, h,
				EditorUtil.MIN_CARD_WIDTH, EditorUtil.MIN_CARD_HEIGHT);

		rect.left = maxRect.centerX() - size.width / 2;
		rect.top = maxRect.centerY() - size.height / 2;
		rect.right = maxRect.centerX() + size.width / 2;
		rect.bottom = maxRect.centerY() + size.height / 2;
		currentImageWidth = workingBitmap.getWidth();
		showSize();
		textSize = SizeUtil.measureView(pixelView);
		warningSize = SizeUtil.measureView(warning);
		warning.setVisibility(View.GONE);
		refreshRect();
	}

	private Point textSize;
	private Point warningSize;

	private int currentImageWidth;

	@SuppressWarnings("deprecation")
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

		// 璁剧疆鍥剧墖
		AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) thisImageView
				.getLayoutParams();
		lp.x = left;
		lp.y = top;
		lp.width = w;
		lp.height = h;
		thisImageView.setLayoutParams(lp);

		maxRect.set(left, top, left + w, top + h);
		currentImageWidth = bitmap.getWidth();

	}

	@Override
	public boolean onPreDraw() {
		int height = imageContainer.getMeasuredHeight();
		int width = imageContainer.getMeasuredWidth();

		containerHeight = height;
		containerWidth = width;
		MessageUtil.sendMessage(0, new IMessageListener() {

			@Override
			public void onMessage(int message) {
				onInit();
			}
		});
		View imageContainer = getView().findViewById(R.id.image_container);
		ViewTreeObserver vto = imageContainer.getViewTreeObserver();
		vto.removeOnPreDrawListener(this);
		return true;
	}

	private void showPixel(int width, int height) {
		if (EditorUtil.MIN_CARD_WIDTH > 0) {
			if (width < EditorUtil.MIN_CARD_WIDTH) {
				warning.setVisibility(View.VISIBLE);
			} else {
				warning.setVisibility(View.GONE);
			}
		}

		pixelView
				.setText(width
						+ "X"
						+ ((int) (width * EditorUtil.MIN_CARD_HEIGHT / (float) EditorUtil.MIN_CARD_WIDTH)));
	}

	@Override
	protected Bitmap executeImage(Bitmap bm) throws OutOfMemoryError {
		Rect rect = this.rect;
		Rect maxRect = this.maxRect;
		double dx;
		if (scaleX < 0 || scaleY < 0 || seekBar.getProgress() > 0) {
			bm = rotateBitmap(seekBar.getProgress(), bm);
		}
		dx = (double) bm.getWidth() / maxRect.width();
		int left = (int) ((rect.left - maxRect.left) * dx);
		int top = (int) ((rect.top - maxRect.top) * dx);
		int width = (int) (rect.width() * dx);
		int height = (int) (rect.height() * dx);

		if (width > bm.getWidth()) {
			width = bm.getWidth();
		}

		if (height > bm.getHeight()) {
			height = bm.getHeight();
		}

		if (left + width > bm.getWidth()) {
			left = bm.getWidth() - width;
		}
		if (top + height > bm.getHeight()) {
			top = bm.getHeight() - height;
		}
		try{
			Bitmap dest = Bitmap.createBitmap(bm, left, top, width, height);
			return dest;
		}catch (Throwable t){
			return  null;
		}

	}

	@Override
	protected void processImage() {
		if (EditorUtil.ErrorMinSize !=null
				&& warning.getVisibility() == View.VISIBLE) {
			Alert.alert(getActivity(), "温馨提示", EditorUtil.ErrorMinSize);
			return;
		}
		super.processImage();
	}

	@Override
	protected void onCancelEditImage() {
		if (!model.isCutted()) {

			Alert.confirm(getActivity(), "温馨提示", "您还没有对图片进行剪裁，继续退出吗？",
					new DialogListener() {

						@Override
						public void onDialogButton(int id) {
							if (id == R.id._id_ok) {
								//EditorEditFragment.super.onCancelEditImage();
								getActivity().finish();
							}
						}
					});

		} else {
			super.onCancelEditImage();
		}

	}

}
