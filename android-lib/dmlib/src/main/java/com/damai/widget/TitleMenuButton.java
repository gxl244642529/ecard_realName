package com.damai.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow.OnDismissListener;

import com.citywithincity.widget.Popups;
import com.damai.lib.R;

public class TitleMenuButton extends ImageButton implements OnDismissListener {
	
	/**
	 * 顶部menu
	 * @author renxueliang
	 *
	 */
	public static interface TitleMenuListener{
		void onTitleMenu(int index);
		void onInitView(View menuView);
	}

	
	private TitleMenuListener listener;
	
	
	public void setTitleMenuListener(TitleMenuListener listener){
		this.listener = listener;
	}
	
	
	@Override
	protected void onDetachedFromWindow() {
		listener = null;
		super.onDetachedFromWindow();
	}
	
	public TitleMenuButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,attrs);
	}

	public TitleMenuButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}

	public TitleMenuButton(Context context) {
		super(context);
	}

	private int itemRes;
	
	private void init(Context context, AttributeSet attrs){
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable._group_view);
		itemRes = a.getResourceId(R.styleable._group_view_item_layout, 0);
		a.recycle();
		setOnClickListener(clickMenuClickListener);
	}
	
	private OnClickListener clickMenuClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			View menuView = ((Activity)getContext()).getLayoutInflater().inflate(itemRes,null);
			listener.onInitView(menuView);
			setSelected(true);
			Popups.addPopup(menuView, TitleMenuButton.this, true, TitleMenuButton.this);
			ViewGroup viewGroup = (ViewGroup) menuView.findViewById(R.id._tab_group);
			for(int i=0 , count = viewGroup.getChildCount(); i < count; ++i){
				View child = viewGroup.getChildAt(i);
				if(child instanceof Button){
					child.setId(1000+i);
					child.setOnClickListener(onClickListener);
				}
			}
		}
	};
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int index = v.getId()-1000;
			listener.onTitleMenu(index);
			Popups.hide();
		}
	};

	@Override
	public void onDismiss() {
		setSelected(false);
	}
}
