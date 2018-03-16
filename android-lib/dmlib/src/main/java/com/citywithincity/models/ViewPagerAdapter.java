package com.citywithincity.models;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citywithincity.interfaces.IDestroyable;

/**
 * 对于不同的视图,可以采用本adapter
 * @author randy
 *
 */
public class ViewPagerAdapter extends PagerAdapter implements IDestroyable, OnPageChangeListener  {
	
	
	public interface OnViewPagerSwitchListener{
		void onSwitchView(View view,int position);
	}
	
	public interface OnViewPagerCreateLiserner{
		void onViewCreated(View view,int position);
	}
	
	
	private final LayoutInflater inflater;
	private final int[] viewIDs;
	private final View[] views;
	private OnViewPagerSwitchListener switchListener;
	private boolean isFirstSelected = true;
	private OnViewPagerCreateLiserner onViewPagerCreateLiserner;
	
	public ViewPagerAdapter(LayoutInflater inflater, int[] viewIDs){
		this.viewIDs = viewIDs;
		this.inflater = inflater;
		views = new View[viewIDs.length];
	}
	
	
	public void setOnViewPagerSwitchListener(OnViewPagerSwitchListener listener){
		switchListener = listener;
	}

	@Override
	public int getCount() {
		return viewIDs.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = views[position];
		if(view==null){
			view = inflater.inflate(viewIDs[position], null);
			views[position] = view;
			container.addView(view);
			if(position==currentIndex){
				onPageSelected(currentIndex);
			}
			if(onViewPagerCreateLiserner!=null){
				onViewPagerCreateLiserner.onViewCreated(view, position);
			}
		}
		return view;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view = (View)object;
		container.removeView(view);
		views[position] = null;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		
		if (isFirstSelected) {
			if(switchListener!=null){
				switchListener.onSwitchView(views[position], position);
				isFirstSelected = false;
			}
			return;
		}
		
		if (position != currentIndex) {
			if(switchListener!=null){
				currentIndex = position;
				switchListener.onSwitchView(views[position], position);
			}
		}
	}

	@Override
	public void destroy() {
		switchListener = null;
	}

	public View getView(int index) {
		return views[index];
	}
	private int currentIndex = -1;
	public void setCurrent(int index) {
		currentIndex = index;
	}


	public OnViewPagerCreateLiserner getOnViewPagerCreateLiserner() {
		return onViewPagerCreateLiserner;
	}


	public void setOnViewPagerCreateLiserner(OnViewPagerCreateLiserner onViewPagerCreateLiserner) {
		this.onViewPagerCreateLiserner = onViewPagerCreateLiserner;
	}

}
