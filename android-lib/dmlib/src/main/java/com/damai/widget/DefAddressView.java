package com.damai.widget;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.citywithincity.models.cache.CachePolicy;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.core.DMLib;
import com.damai.helper.ActivityResult;
import com.damai.helper.ActivityResultContainer;
import com.damai.helper.OnSelectDataListener;
import com.damai.lib.R;
import com.damai.widget.vos.AddressVo;
/**
 *地址选择
 * @author renxueliang
 *
 */
public class DefAddressView extends FrameLayout implements ApiListener,ActivityResult{

	private ApiJob job;
	
	private OnSelectDataListener<AddressVo> listener;
	
	private View noAddress;
	private View loadView;
	private View addressView;
	
	private TextView txtAddress;
	private TextView txtName;
	private TextView txtPhone;
	private AddressVo address;
	
	public DefAddressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	public void setOnSelectDataListener(OnSelectDataListener<AddressVo> listener){
		this.listener = listener;
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if(job==null){
			job.cancel();
			job = null;
		}
		listener = null;
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if(isInEditMode()){
			return;
		}
		
		
		
		
		job = DMLib.getJobManager().createObjectApi("address/def");
		job.setCachePolicy(CachePolicy.CachePolity_NoCache);
		job.setEntity(AddressVo.class);
		job.setApiListener(this);
		job.setServer(1);
		job.execute();
		
		
		addressView = findViewById(R.id._id_address);
		noAddress = findViewById(R.id._id_no_address);
		loadView = findViewById(R.id._load_state_loading);
		
		addressView.setVisibility(View.GONE);
		addressView.setVisibility(View.GONE);
		
		txtAddress = (TextView) findViewById(R.id.address);
		txtPhone = (TextView) findViewById(R.id.phone);
		txtName = (TextView) findViewById(R.id.name);
		
		addressView.setOnClickListener(addressListener);
		noAddress.setOnClickListener(addressListener);
	}
	
	
	public DefAddressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public DefAddressView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	private OnClickListener addressListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//编辑地址
			String packageName = getContext().getPackageName();
			//startActivity
			((ActivityResultContainer)getContext()).startActivityForResult(DefAddressView.this, new Intent(packageName+".action.SELECT_ADDRESS"), 0);
		}
	};
	
	
	
	
	/**
	 * 获取详细信息
	 */
	public String getAddress(){
		if(address==null)return null;
		return AddressVo.formatAddress(address);
	}
	
	/**
	 * 获取地址信息
	 * @return
	 */
	public AddressVo getAddressVo(){
		return address;
	}

	@Override
	public void onJobSuccess(ApiJob job) {
		address = job.getData();
		loadView.setVisibility(View.GONE);
        onRecvData();
	}


    void onRecvData(){
        if(address!=null){
            noAddress.setVisibility(View.GONE);
            addressView.setVisibility(View.VISIBLE);
            updateAddress(address);
            if(listener!=null){
                listener.onSelectData(address);
            }
        }else{
            noAddress.setVisibility(View.VISIBLE);
            addressView.setVisibility(View.GONE);
        }
    }

	/**
	 * 当选中一个地址的时候
	 */
	
	/**
	 * 地址
	 * @param address
	 */
	private void updateAddress(AddressVo address){

		txtAddress.setText(AddressVo.formatAddress(address));
		txtName.setText(address.getName());
		txtPhone.setText(address.getPhone());
	}



	@Override
	public boolean onJobError(ApiJob job) {
		
		return false;
	}

	@Override
	public boolean onApiMessage(ApiJob job) {
		
		return false;
	}


	@Override
	public void onActivityResult(Intent intent, int resultCode, int requestCode) {
		if(resultCode== Activity.RESULT_OK){

			AddressVo vo = (AddressVo)intent.getExtras().get("data");
            address = vo;
            onRecvData();
		}
	}
}
