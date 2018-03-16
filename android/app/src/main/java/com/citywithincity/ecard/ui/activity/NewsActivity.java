package com.citywithincity.ecard.ui.activity;

import android.os.Bundle;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.ECardContentModel;
import com.citywithincity.ecard.models.vos.NewsInfo;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.OnItemClickListenerOpenActivity;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.widget.StateListView;


/**
 * 
 * @author Randy
 *
 */
public class NewsActivity extends BaseActivity{

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.base_lv);
		setTitle("新闻");
		@SuppressWarnings("unchecked")
		StateListView<NewsInfo> listView = (StateListView<NewsInfo>)findViewById(R.id._list_view);
		listView.setItemRes(R.layout.news_item);
		listView.setOnItemClickListener(new OnItemClickListenerOpenActivity<NewsInfo>(NewsDetailActivity.class));
		listView.setTask(ModelHelper.getModel(ECardContentModel.class).getNewsList(LibConfig.StartPosition));
	}
	

}
