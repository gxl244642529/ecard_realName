package com.citywithincity.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citywithincity.models.ViewPagerAdapter;
import com.citywithincity.models.ViewPagerAdapter.OnViewPagerCreateLiserner;
import com.citywithincity.models.ViewPagerAdapter.OnViewPagerSwitchListener;
import com.citywithincity.utils.MemoryUtil;
import com.damai.lib.R;

/**
 * 顶部、任意、tab,viewpager
 * @author Randy
 *
 */
public class TabPaneView extends RelativeLayout implements OnViewPagerSwitchListener{
	
	public interface OnTabPaneViewSwitchListener{
		/**
		 * 
		 * @param view
		 * @param index
		 * @param firstSwitch
		 */
		void onTabPaneViewSwitch(View view,int index,boolean firstSwitch);
	}
	
	
	ViewPager viewPager;
	private int[] viewResIDS;
	private int currentIndex = -1;
	private TextView[] textViews;
	private View[] views;
	ViewPagerAdapter dataProvider;
	private boolean isFirstSelected[];
	private OnTabPaneViewSwitchListener listener;
	
	@Override
	protected void onDetachedFromWindow() {
		dataProvider.destroy();
		dataProvider = null;
		MemoryUtil.clearArray(textViews);
		textViews=null;
		viewPager= null;
		isFirstSelected  =null;
		super.onDetachedFromWindow();
	}
	
	public TabPaneView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isInEditMode())return;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable._tab_host);
		//需要加载的视图
		int tabSubs = a.getResourceId(R.styleable._tab_host_tab_subs, 0);
		if(tabSubs>0){
			TypedArray layouts = context.getResources().obtainTypedArray(tabSubs); 
			int count = layouts.length();
			isFirstSelected = new boolean[count];
			viewResIDS = new int[count];
			for(int i=0 ; i < count ; ++i){
				int res = layouts.getResourceId(i, 0);
				viewResIDS[i] = res;
				isFirstSelected[i] = true;
			}
			layouts.recycle();
		}
		a.recycle();
	}
	
	
	@Override
	protected void onFinishInflate() {
		if(isInEditMode())return;
		ViewGroup viewGroup = (ViewGroup)findViewById(R.id._id_tabs);
		int count = viewGroup.getChildCount();
		views = new View[count];
		textViews = new TextView[count];
		for (int i = 0; i < count; ++i) {
			View view = viewGroup.getChildAt(i);
			views[i] = view;
			view.setTag(i);
			view.setOnClickListener(childClickListener);
			textViews[i] = (TextView)view.findViewById(R.id._text_view);
		}
		//每一个child都是relativelayout,每一个relativelayout包含textview(id=text_view)
		viewPager = (ViewPager)findViewById(R.id._view_pager);
		dataProvider = new ViewPagerAdapter(LayoutInflater.from(getContext()), viewResIDS);
		dataProvider.setOnViewPagerSwitchListener(this);
		viewPager.setOnPageChangeListener(dataProvider);
		viewPager.setAdapter(dataProvider);
	}
	
	
	protected OnClickListener childClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//切换到本tag
			int index = (Integer)v.getTag();
			if(index!=currentIndex){
				viewPager.setCurrentItem(index, true);
				onSelectIndex(index,dataProvider.getView(index));
			}
		}
	};
	
	private void onSelectIndex(int index,View view){
		if(currentIndex>=0){
			textViews[currentIndex].setSelected(false);
			views[currentIndex].setSelected(false);
		}
		textViews[index].setSelected(true);
		views[index].setSelected(true);
		currentIndex = index;
		if(listener!=null){
			listener.onTabPaneViewSwitch(view, index, isFirstSelected[index]);
		}
		isFirstSelected[index]=false;
	}

	@Override
	public void onSwitchView(View view, int position) {
		onSelectIndex(position,view);
	}

	
	public void setCurrent(int index){
		viewPager.setCurrentItem(index);
		if(dataProvider.getView(index)!=null){
			onSwitchView(dataProvider.getView(index), index);
		}else{
			dataProvider.setCurrent(index);
		}
	}

	public OnTabPaneViewSwitchListener getListener() {
		return listener;
	}

	public void setListener(OnTabPaneViewSwitchListener listener) {
		this.listener = listener;
	}
	
	public ViewPager getViewPager() {
		return viewPager;
	}
	
	public OnViewPagerCreateLiserner getOnViewPagerCreateLiserner() {
		return dataProvider.getOnViewPagerCreateLiserner();
	}


	public void setOnViewPagerCreateLiserner(OnViewPagerCreateLiserner onViewPagerCreateLiserner) {
		dataProvider.setOnViewPagerCreateLiserner(onViewPagerCreateLiserner);
	}

	public ViewPagerAdapter getAdapter(){
		return dataProvider;
	}
}
