package com.citywithincity.ecard.selling.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.selling.models.vos.SMyDiyListVo;
import com.citywithincity.interfaces.IViewPagerDataProviderListener;
import com.citywithincity.models.ViewPagerDataProvider;

import java.util.ArrayList;
import java.util.List;

public class SMyDiyDetailActivity extends BaseActivity implements IViewPagerDataProviderListener<String>{

	private ViewPager viewPager;
	private ViewPagerDataProvider<String> dataProvider;
	
	private SMyDiyListVo sMyDiyListVo;
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.s_my_diy_detail);
		setTitle(R.string.s_my_diy_detail);
		onLoadData();
	}
	
	public void onLoadData(){
		sMyDiyListVo = (SMyDiyListVo)getIntent().getExtras().get("data");
		
		TextView workDate = (TextView)findViewById(R.id.diy_work_date); 
		
		workDate.setText(sMyDiyListVo.diyDay);
		viewPager = (ViewPager)findViewById(R.id.diy_view_pager);
		dataProvider = new ViewPagerDataProvider<String>(getLayoutInflater(), R.layout.diy_detail_pages_bg_view, this);
		onLoadViewData();
		viewPager.setAdapter(dataProvider);
		viewPager.setOnPageChangeListener(dataProvider);
	}
	
	public void onLoadViewData(){
		
		List<String> data = new ArrayList<String>();
		data.add(sMyDiyListVo.imageZ);
		data.add(sMyDiyListVo.imageF);
		dataProvider.setData(data);
	}

	@Override
	public void onViewPagerDestroyView(View view, int index) {
	}

	@Override
	public void onViewPagerCreateView(View view, int index, String data) {
		ImageView imageView = (ImageView) view.findViewById(R.id.diy_diydetail_bg_image);
		ECardJsonManager.getInstance().setImageSrc(data, imageView);
	}

	@Override
	public void onViewPagerPageSelect(View view, int index, String data) {
		View view1 = findViewById(R.id.diy_detail_view1); 
		View view2 = findViewById(R.id.diy_detail_view2); 
		switch (index) {
		case 0:
			view1.setBackgroundColor(getResources().getColor(R.color.title_color));
			view2.setBackgroundColor(getResources().getColor(R.color.s_title_bg_color));
			break;
		case 1:

			view1.setBackgroundColor(getResources().getColor(R.color.s_title_bg_color));
			view2.setBackgroundColor(getResources().getColor(R.color.title_color));
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		dataProvider.destroy();
		dataProvider = null;
		super.onDestroy();
	}


}
