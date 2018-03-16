package com.citywithincity.ecard.selling.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.selling.diy.models.DiyCard;
import com.citywithincity.ecard.selling.diy.models.dao.CardManager;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ImageUtil;
import com.citywithincity.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class DiyAdapter extends BaseAdapter {

	private List<DiyCard> list;
	private final Context context;
	private LayoutInflater layoutInflater;

	public DiyAdapter(Context context) {
		list = new ArrayList<DiyCard>();
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}
	
	public void reload(){
		list = ModelHelper.getModel(CardManager.class).getList();
		notifyDataSetChanged();
	}
	

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
		if (0 == position) {
			view = layoutInflater
					.inflate(R.layout.s_item_diy_onlocal_add, null);

		} else {
			view = layoutInflater.inflate(R.layout.s_item_diy_onlocal, null);
			ImageView localImage = (ImageView) view
					.findViewById(R.id.card_diy_onlocalimage);
			Bitmap imBitmap = ImageUtil.decodeBitmap(
					list.get(position - 1).thumb, ViewUtil.screenWidth,
					ViewUtil.screenHeight);
			localImage.setImageBitmap(imBitmap);
			TextView cardDiyTime = (TextView) view
					.findViewById(R.id.card_diy_time);
			String timeString = String
					.valueOf(list.get(position - 1).createTime);

			cardDiyTime.setText(timeString);

			TextView cardDiyDelete = (TextView) view
					.findViewById(R.id.card_diy_delete);
			cardDiyDelete.setOnClickListener(new OnClickListener() {// 删除监听
						@Override
						public void onClick(View paramView) {

							Alert.confirm(context, "温馨提示", "确定删除吗？删除后不可恢复",
									new DialogListener() {

										@Override
										public void onDialogButton(int id) {
											if (id == R.id._id_ok) {
												if (position > 0) {
													DiyCard cardVo = list
															.get(position - 1);
													ModelHelper.getModel(CardManager.class).remove(cardVo);
													list.remove(cardVo);
													notifyDataSetChanged();
												}
											}
										}
									});

						}

					});
		}

		return view;
	}

	@Override
	public int getCount() {
		return list.size() + 1;
	}

	@Override
	public DiyCard getItem(int index) {
		if (index == 0) {
			return null;
		}
		return list.get(index - 1);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

}
