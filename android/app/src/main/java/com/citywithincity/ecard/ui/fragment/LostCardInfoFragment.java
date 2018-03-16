package com.citywithincity.ecard.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.vos.LostCardDetailInfo;
import com.citywithincity.utils.ActivityUtil;

public class LostCardInfoFragment extends Fragment implements OnClickListener {
	
	private View view;
	private LostCardDetailInfo info;
	
	
	public void setData(LostCardDetailInfo info){
		
		this.info=info;
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_lost_card_info, null);
		view.findViewById(R.id._id_ok).setOnClickListener(this);
		((TextView)view.findViewById(R.id.lost_card_info)).setText(info.message);
		TextView txtSex = (TextView)view.findViewById(R.id.id_sex);
		String sex = "";
		switch (info.sex) {
		case 0:
			sex = "保密";
			break;
		case 1:
			sex = "男";
			break;
		case 2:
			sex = "女";
			break;
		default:
			break;
		}
		txtSex.setText(sex);
		return view;
	}

	@Override
	public void onClick(View v) {
		ActivityUtil.makeCall(getActivity(), info.phone);
	}

}
