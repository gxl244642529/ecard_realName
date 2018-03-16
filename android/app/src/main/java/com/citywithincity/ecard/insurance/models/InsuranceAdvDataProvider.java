package com.citywithincity.ecard.insurance.models;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.ecard.insurance.activities.InsuranceActionActivity;
import com.citywithincity.ecard.insurance.models.vos.InsuranceAdvVo;
import com.citywithincity.ecard.ui.activity.WebViewActivity;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.interfaces.IListRequsetResult;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.AbsDataProvider;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.widget.AdvView.OnClickAdvViewListener;
import com.citywithincity.widget.LoadingImageView;

import java.util.List;

public class InsuranceAdvDataProvider  extends AbsDataProvider<List<InsuranceAdvVo>> implements IListDataProviderListener<InsuranceAdvVo>, OnClickAdvViewListener<InsuranceAdvVo>,IListRequsetResult<List<InsuranceAdvVo>> {

	private final Context context;
	public InsuranceAdvDataProvider(Context context){
		this.context = context;
	}
	
	@Override
	public void load() {
		IArrayJsonTask<InsuranceAdvVo>  task = ModelHelper.getModel(InsuranceModel.class).getAdvList(LibConfig.StartPosition);
		task.setListener(this);
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		listener.onError(errMsg, isNetworkError);
	}

	@Override
	public void onRequestSuccess(List<InsuranceAdvVo> result, boolean isLastPage) {
		listener.onReceive(result);
	}

	@Override
	public void onInitializeView(View view, InsuranceAdvVo data, int position) {
		LoadingImageView imageView = (LoadingImageView) view;
		imageView.load(data.img);
	}
	
	@Override
	public void onClickAdvView(View view, int index, InsuranceAdvVo data) {
		if (data.flag == 1) {
			if (data.type == 1) {
				WebViewActivity.open(context, ECardConfig.BASE_URL + "/index.php/api2/i_adv_detail/index/"+data.id, data.title);
			} else {
				Intent intent = new Intent(context,InsuranceActionActivity.class);
				intent.putExtra("url", data.url);
				intent.putExtra("title", data.title);
				context.startActivity(intent);
//				WebViewActivity.open(context, data.url, data.title);
			}
		}
		
	}

}
