package com.citywithincity.ecard.selling.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.selling.models.vos.SMyDiyListVo;
import com.citywithincity.interfaces.IListRequsetResult;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.ListGroup.IListGroupListener;
import com.citywithincity.models.OnItemClickListenerOpenActivity;
import com.citywithincity.models.PullToRefreshListGroup;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;

import java.util.List;

public class SMyDiyOnLineActivity extends BaseActivity implements IListGroupListener<SMyDiyListVo>,IListRequsetResult<List<SMyDiyListVo>>{
	
	private PullToRefreshListGroup<SMyDiyListVo, GridView> dataGroup;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.s_activity_my_diy_online);
		setTitle("我的DIY");
		@SuppressWarnings("unchecked")
		PullToRefreshAdapterViewBase<GridView> listViewBase  = (PullToRefreshAdapterViewBase<GridView>)findViewById(R.id.my_diy_online_grid_view);
		dataGroup = new PullToRefreshListGroup<SMyDiyListVo, GridView>(SMyDiyOnLineActivity.this,R.layout.s_item_my_diy_online, listViewBase,this);
		onLoadData(LibConfig.StartPosition);
		dataGroup.setOnItemClickListener(new OnItemClickListenerOpenActivity<SMyDiyListVo>(SMyDiyDetailActivity.class));
	}

	@Override
	public void onInitializeView(View view, SMyDiyListVo data, int position) {
		ImageView imageView = (ImageView)view.findViewById(R.id.card_my_diy_onlineimage);
		ECardJsonManager.getInstance().setImageSrc(data.thumb, imageView);
		TextView textView = (TextView)view.findViewById(R.id.card_my_diy_date_text);
		textView.setText(data.diyDay);
	}

	@Override
	public void onLoadData(int position) {
//		SellingModel.getInstance().getMyDiyList(position, this);
		dataGroup.showLoadingState();
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		dataGroup.onRequestError(errMsg, isNetworkError);
	}

	@Override
	public void onRequestSuccess(List<SMyDiyListVo> result, boolean isLastPage) {
		// TODO Auto-generated method stub
		dataGroup.onRequestSuccess(result, isLastPage);
	}

}
