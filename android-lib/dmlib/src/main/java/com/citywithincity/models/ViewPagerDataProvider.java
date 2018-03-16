package com.citywithincity.models;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IViewPagerDataProviderListener;

/**
 * 
 * @author randy
 * 
 */
public class ViewPagerDataProvider<T> extends PagerAdapter implements IDestroyable,  OnPageChangeListener  {
	private IViewPagerDataProviderListener<T> listener;
	private LayoutInflater inflater;
	private View[] contentViews;
	private int viewID;


	/**
	 * 对于分页比较少的，可以,只支持同一个布局,bi
	 * 
	 * @param inflater
	 * @param viewID
	 * @param count
	 * @param listener
	 */
	public ViewPagerDataProvider(LayoutInflater inflater, int viewID, IViewPagerDataProviderListener<T> listener) {
		this.inflater = inflater;
		this.viewID = viewID;
		this.listener = listener;
	}
	
	private List<T> dataList;
	public void setData(List<T> dataList){
		this.dataList = dataList;
		this.contentViews = new View[dataList.size()];
		notifyDataSetChanged();
	}

	public View getContentView(int index) {
		if (index < 0 || index > contentViews.length - 1)
			return null;
		return contentViews[index];
	}

	@Override
	public int getCount() {
		if(dataList!=null){
			return dataList.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view = (View)object;
		if (listener != null)
			listener.onViewPagerDestroyView(view, position);
		container.removeView(view);
		contentViews[position] = null;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = contentViews[position];
		if (view == null) {
			view = inflater.inflate(viewID, null);
			if (listener != null){
				if(dataList==null){
					listener.onViewPagerCreateView(view, position,null);
				}else{
					listener.onViewPagerCreateView(view, position,dataList.get(position));
				}
				
			}
				
			contentViews[position] = view;
		}
		container.addView(view);
		return view;
	}

	@Override
	public void destroy() {
		if (listener != null && contentViews!=null){
			//把每一个view都destroy掉
			for (int i=0; i < contentViews.length; ++i) {
				if(contentViews[i]!=null){
					listener.onViewPagerDestroyView(contentViews[i], i);
				}
			}
		}
		
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
			if(dataList==null){
				listener.onViewPagerPageSelect(contentViews[position],position,null);
			}else{
				listener.onViewPagerPageSelect(contentViews[position],position,dataList.get(position));
			}
			
		}
	}
}
