package com.citywithincity.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citywithincity.utils.MemoryUtil;
import com.damai.lib.R;


/**
 * 底部tab
 * @author Randy
 *
 */
public class TabHostView extends RelativeLayout {

	/**
	 * tab改变
	 * @author Randy
	 *
	 */
	public static interface OnTabChangeListener{
		/**
		 *
		 * @param view
		 * @param index
		 * @param fromUser
		 */
		void onTabChange(TabHostView view,int index,boolean fromUser);
	}

	/**
	 * 点击事件
	 * @author Randy
	 *
	 */
	public static interface OnTabClickListener{
		/**
		 *
		 * @param view
		 * @param index
		 */
		void onTabClick(TabHostView view,int index);
	}

	private TextView[] textViews;
	private TabFragment[] subFragments;
	private View[] subViews;
	private ViewGroup container;
	private int currentSelected = -1;

	private OnTabChangeListener changeListener;
	private OnTabClickListener clickListener;


	public TabHostView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void registerFragment(int index,TabFragment fragment){
		/**
		 *
		 */
		subFragments[index] = fragment;
	}

	@Override
	protected void onFinishInflate() {
		if(isInEditMode())return;
		ViewGroup viewGroup = (ViewGroup)findViewById(R.id._id_tabs);
		int count = viewGroup.getChildCount();
		textViews = new TextView[count];
		subFragments = new TabFragment[count];
		subViews= new View[count];
		for (int i = 0; i < count; ++i) {
			View view = viewGroup.getChildAt(i);
			view.setTag(i);
			view.setOnClickListener(childClickListener);
			textViews[i] = (TextView)view.findViewById(R.id._text_view);
		}

		container = (ViewGroup)findViewById(R.id._container);
	}


	@Override
	protected void onDetachedFromWindow() {
		for (int i = 0, count = subFragments.length; i < count; ++i) {
			TabFragment subView  =subFragments[i];
			if(subView!=null){
				subView.onDeatch();
				subView.onDestroy();
				subFragments[i] = null;
			}
		}
		MemoryUtil.clearArray(textViews);
		MemoryUtil.clearArray(subViews);
		subFragments = null;
		subViews = null;
		textViews = null;
		changeListener =null;
		clickListener = null;
		super.onDetachedFromWindow();
	}

	public int getCurrent() {
		return currentSelected;
	}

	private boolean first = true;

	public void setCurrent(int index){

		if(subFragments[index]!=null){
			if(currentSelected>=0){
				//上一次的
				textViews[currentSelected].setSelected(false);
				subFragments[currentSelected].onPause();
				subViews[currentSelected].setVisibility(View.GONE);
			}

			textViews[index].setSelected(true);


			if(subViews[index]==null){
				subFragments[index].onAttach(getContext());
				subViews[index]=subFragments[index].createView(getContext());
				container.addView(subViews[index]);
			}else{
				subViews[index].setVisibility(View.VISIBLE);
			}
			if(first){
				first = false;
			}else{
				subFragments[index].onResume();
			}

			currentSelected = index;
			if(changeListener!=null){
				changeListener.onTabChange(TabHostView.this, index, true);
			}
		}
	}

	public void onResume(){
		if(currentSelected>=0 && currentSelected < subFragments.length)
			subFragments[currentSelected].onResume();
	}


	public void onStop(){
		first = false;
		if(currentSelected>=0 && currentSelected < subFragments.length)
			subFragments[currentSelected].onStop();
	}

	protected OnClickListener childClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			//如果本位置有tab，切换tab
			int index = (Integer)v.getTag();
			if(index!=currentSelected){
				setCurrent(index);
			}

			if(clickListener!=null){
				clickListener.onTabClick(TabHostView.this, index);
			}
		}
	};


	public void setOnTabChangeListener(OnTabChangeListener listener){
		changeListener = listener;
	}


	public void setOnTabClickListener(OnTabClickListener listener){
		clickListener = listener;
	}



}
