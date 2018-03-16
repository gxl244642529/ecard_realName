package com.citywithincity.ecard.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IViewPagerDataProviderListener;
import com.citywithincity.models.ViewPagerDataProvider;
import com.citywithincity.utils.CollectionUtil;
import com.citywithincity.widget.LoadingImageView;

public class ImageActivity extends BaseActivity implements IViewPagerDataProviderListener<String> {
	
	private String[] _photos;
	private ViewPagerDataProvider<String> _dataDataProvider;
	private TextView _pageTextView;
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.image_view_layout);
		_photos=(String[])getIntent().getExtras().get("data");
		int _currentIndex = getIntent().getIntExtra("index", 0);
		
		ViewPager viewPager = (ViewPager)findViewById(R.id._view_pager);
//		int [] views = new int[_photos.length];
//		for(int i=0; i < _photos.length; ++i)
//		{
//			views[i]=R.layout.base_loading_image_view;
//		}
		//显示图片
		_dataDataProvider = new ViewPagerDataProvider<String>(
				getLayoutInflater(), 
				R.layout.base_loading_detail_image_view,
				this);
		viewPager.setOnPageChangeListener(_dataDataProvider);
		_dataDataProvider.setData(CollectionUtil.createListFromArray(_photos));
		viewPager.setAdapter(_dataDataProvider);
		_pageTextView=(TextView)findViewById(R.id._text_view);
		viewPager.setCurrentItem(_currentIndex);
		
		_pageTextView.setText((_currentIndex + 1) + "/" + _photos.length);

	}


	@Override
	protected void onDestroy() {
		_photos=null;
		_pageTextView=null;
		_dataDataProvider.destroy();
		_dataDataProvider=null;
		super.onDestroy();
	}
	
	
	@Override
	public void onViewPagerDestroyView(View view, int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onViewPagerCreateView(View view, int index,String data) {
		LoadingImageView loadingView = (LoadingImageView)view;
		loadingView.load(data);
	}

	@Override
	public void onViewPagerPageSelect(View view, int index,String data) {
		_pageTextView.setText((index + 1) + "/" + _photos.length);
	}

	
	

	
}
