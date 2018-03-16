package com.citywithincity.ecard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IOnItemClickListener;
import com.damai.auto.DMActivity;
import com.damai.core.ApiJob;
import com.damai.helper.ItemEventHelper;
import com.damai.helper.ItemEventHelper.ItemEventSetter;
import com.damai.helper.a.Event;
import com.damai.helper.a.ItemEvent;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.widget.StateListView;
import com.damai.widget.vos.AddressVo;


/**
 * 
 * @author renxueliang
 *
 */
public class AddressActivity extends DMActivity implements IOnItemClickListener<AddressVo> {

	@Res
	private StateListView<AddressVo> _list_view;
	
	private boolean selectMode;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_address);
		String action = getPackageName() + ".action.SELECT_ADDRESS";
		if(action.equals(getIntent().getAction())){
			setTitle("选择收货地址");
			selectMode =true;
		}else{
			setTitle("收货地址管理");
		}
		_list_view.setOnItemClickListener(this);
	}

	@Event
	public void _title_right(){
		//增加
		startActivity(new Intent(this,AddressEditActivity.class));
	}
	
	@ItemEvent
	public void onItemEdit(AddressVo addressVo){
		startActivity(AddressEditActivity.class,addressVo);
	}
	
	@ItemEvent
	public void onItemDelete(AddressVo addressVo){
		ItemEventHelper.onItemEvent(addressVo, "address/delete", "真的要删除本地址吗?", new ItemEventSetter<AddressVo>(){

			@Override
			public void setParam(ApiJob job, AddressVo data) {
				job.put("id", data.getTyId());
			}
		});
	}
	
	@JobSuccess({"address/delete","address/save"})
	public void onDelete(Object value){
		_list_view.refreshWithState();
	}

	@Override
	public void onItemClick(Activity activity, AddressVo data, int position) {
		if(selectMode){
			Intent intent = new Intent();
			intent.putExtra("data", data);
			setResult(RESULT_OK,intent);
			finish();
		}else{
			onItemEdit(data);
		}
	}
}
