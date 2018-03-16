package com.citywithincity.ecard.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.BusinessMenuDataProvider;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.models.ECardModel;
import com.citywithincity.ecard.models.vos.BusinessInfo;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.interfaces.IOnItemClickListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.widget.StateListView;
import com.citywithincity.widget.TopMenu;
import com.citywithincity.widget.TopMenu.OnMenuSelectListener;
import com.citywithincity.widget.data.MenuData;

public class BusinessActivity extends BaseActivity implements OnMenuSelectListener,IListDataProviderListener<BusinessInfo>, IOnItemClickListener<BusinessInfo> {

	private Integer selectedDistance;
	private String selectedPath;
	
	StateListView<BusinessInfo> listView;
	
	public BusinessActivity() {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.business_grid);
		
		setTitle("联盟商家");
		
		TopMenu topMenu = (TopMenu) findViewById(R.id._top_menu);
		topMenu.setOnMenuSelectListener(this);
		BusinessMenuDataProvider dataProvider = new BusinessMenuDataProvider();
		
		
		listView = (StateListView<BusinessInfo>) findViewById(R.id._list_view);
		listView.setItemRes(R.layout.business_grid_item);
		listView.setDataListener(this);
		listView.setOnItemClickListener(this);
		
		Integer selectedDistance;
		String selectedPath;
		if(getIntent()!=null){
			Bundle bundle = getIntent().getExtras();
			selectedDistance = bundle.getInt("distance");
			selectedPath = bundle.getString("defaultData");
		}else{
			selectedDistance = 0;
			selectedPath = "";
		}
		
		dataProvider.setDefaultData(selectedPath,selectedDistance);
		topMenu.setDataProvider(dataProvider);
		
	}
	
	@Override
	public void onInitializeView(View view, BusinessInfo data, int position) {
		ImageView imageView = (ImageView) view
				.findViewById(R.id.business_image);
		ECardJsonManager.getInstance().setImageSrc(data.img, imageView);
		TextView titleTextView = (TextView) view
				.findViewById(R.id.business_title);
		titleTextView.setText(data.title);
	}

	@Override
	public void onMenuSelected(int index, MenuData data, boolean fromUser) {
		if(index==0){
			selectedPath = (String)data.data;
		}else{
			selectedDistance = (Integer)data.data;
		}
		if(selectedDistance!=null && selectedDistance!=null){
			//加载
			listView.setStateLoading();
			
			listView.setTask(ECardModel.getInstance().getBusinessAssortmentList(LibConfig.StartPosition, selectedPath, selectedDistance)
							);
		}
	}

	@Override
	public void onItemClick(Activity activity, BusinessInfo data, int position) {
		// TODO Auto-generated method stub
		ActivityUtil.startActivity(this, BusinessDetailActivity.class, data);
	}


}
