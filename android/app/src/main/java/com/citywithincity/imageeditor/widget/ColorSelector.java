package com.citywithincity.imageeditor.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class ColorSelector extends LinearLayout {

	public interface IColorChangeListener {
		void onColorChange(int color);
	}

	private static final String[] colors = new String[] {

	"#ffffff", "#000000", "#3C3C3C", "#999999", "#60608C", "#FEFF9D",
			"#FDFF2B", "#FFB939", "#FF6E3F", "#E93F2D", "#BB0000", "#E73F57",
			"#FF6C97", "#FF9EFF", "#D181FF", "#965AFF", "#4C4EFF", "#00238C",
			"#0035C3", "#087AED", "#0078F2", "#00B3F5", "#00F3D4", "#00D47F",
			"#00B300", "#77E118", "#C4FF30", };

	public ColorSelector(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private IColorChangeListener onColorChangeListener;

	@Override
	protected void onFinishInflate() {
		if (isInEditMode())
			return;
		// 这里增加
		Context context = getContext();
		for (int i = 0, count = colors.length; i < count; ++i) {
			View view = new View(context);
			LayoutParams lp = new LayoutParams(0, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			lp.weight = 1;
			addView(view, lp);
			view.setBackgroundColor(Color.parseColor(colors[i]));
			view.setClickable(false);
		}

		setOnTouchListener(onTouchListener);
	}

	@Override
	protected void onDetachedFromWindow() {
		onColorChangeListener = null;
		super.onDetachedFromWindow();
	}

	public void setOnColorChangeListener(
			IColorChangeListener onColorChangeListener) {
		this.onColorChangeListener = onColorChangeListener;
	}

	private OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE: {
				float x = event.getX();
				float width = getMeasuredWidth();
				if (x >= 0 && x <= width) {
					
					float rate = x * colors.length / width;
					int index = (int) rate;
					System.out.println(index + ":" + x);
					onColorChangeListener.onColorChange(Color
							.parseColor(colors[index]));
				}
			}

				break;

			default:
				break;
			}

			return false;
		}
	};
}
