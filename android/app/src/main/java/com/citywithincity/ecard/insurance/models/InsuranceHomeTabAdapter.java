package com.citywithincity.ecard.insurance.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.vos.InsuranceTypeVo;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.interfaces.ImageRequestLisener;

import java.util.List;

public class InsuranceHomeTabAdapter extends BaseAdapter{

	private List<InsuranceTypeVo> list;
	private Context context;
	private LayoutInflater layoutInflater;
	private TextView[] textViews;
	
	public InsuranceHomeTabAdapter(Context context,List<InsuranceTypeVo> list) {
		this.list = list;
		this.context = context;
		textViews = new TextView[list.size()];
		layoutInflater = LayoutInflater.from(this.context);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Integer.parseInt(list.get(position).type_id);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder viewHolder;
		
		if (null == view) {
			view = layoutInflater.inflate(R.layout.item_insurance_home_tab, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) view.findViewById(R.id.id_name);
			textViews[position] = viewHolder.name;
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		InsuranceTypeVo data = list.get(position);
		if (data != null) {
			viewHolder.name.setText(data.title);
		}
		
		ECardJsonManager.getInstance().loadImage(data.icon_url, new ImageRequestLisener() {
			
			@Override
			public void onRequestError(String errMsg, boolean isNetworkError) {
				// TODO Auto-generated method stub
				
			}
			
			@SuppressWarnings("deprecation")
			@Override
			public void onImageSuccess(String url, Bitmap bitmap) {
				Drawable top = new BitmapDrawable(bitmap);
				top.setBounds(0, 0, 89, 89); 
				textViews[position].setCompoundDrawables(null, top, null, null);
			}
			
			@Override
			public void onImageProgress(String url, int total, int progress) {
				
			}
			
			@Override
			public void onImageError(String url, boolean networkError) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return view;
	}
	
	private class ViewHolder{
		public TextView name;
	}
	
}
