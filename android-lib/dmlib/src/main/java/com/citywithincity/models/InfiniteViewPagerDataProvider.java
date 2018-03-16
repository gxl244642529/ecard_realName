package com.citywithincity.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IViewPagerDataProviderListener;

/**
 * 无限循环的viewPager
 * @author randy
 *
 */
public class InfiniteViewPagerDataProvider<T> extends PagerAdapter implements IDestroyable,  OnPageChangeListener   {
	private IViewPagerDataProviderListener<T> listener;
	private LayoutInflater inflater;
	private int viewResID;
	private Map<Integer, View> contentViews;
	private List<T> dataList;

	/**
	 * 对于分页比较少的，可以
	 * 
	 * @param inflater
	 * @param viewID
	 * @param count
	 * @param listener
	 */
	@SuppressLint("UseSparseArrays")
	public InfiniteViewPagerDataProvider(LayoutInflater inflater, 
			int viewID,
			IViewPagerDataProviderListener<T> listener) {
		this.inflater = inflater;
		this.contentViews = new HashMap<Integer, View>();
		this.viewResID = viewID;
		this.listener = listener;
	}
	
	public View getContentView(int index) {
		return contentViews.get(index);
	}

	@Override
	public int getCount() {
		return dataList==null ? 0 :Integer.MAX_VALUE;
	}
	
	
	public void setData(List<T> dataList){
		this.dataList = dataList;
		notifyDataSetChanged();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}

	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		View view = inflater.inflate(viewResID, null);
		contentViews.put(position, view);
		if (listener != null)
			listener.onViewPagerCreateView(view, position,dataList.get(position % dataList.size()));
		container.addView(view);
		return view;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view = contentViews.get(position);
		if (listener != null)
			listener.onViewPagerDestroyView(view, position);
		container.removeView(view);
		contentViews.remove(position);
	}

	@Override
	public void destroy() {
		if (listener != null){
			//把每一个view都destroy掉
			for (Integer iterable_element : contentViews.keySet()) {
				View view = contentViews.get(iterable_element);
				listener.onViewPagerDestroyView(view, 0);
			}
		}
		dataList.clear();
		dataList = null;
		inflater = null;
		listener = null;
		contentViews = null;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		if(listener!=null){
			View view = contentViews.get(position);
			listener.onViewPagerPageSelect(view,position,dataList.get(position % dataList.size()));
		}
	}

}
