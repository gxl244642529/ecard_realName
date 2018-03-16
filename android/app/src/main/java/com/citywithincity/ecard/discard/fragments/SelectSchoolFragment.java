package com.citywithincity.ecard.discard.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.discard.vos.SchoolVo;
import com.citywithincity.interfaces.IOnItemClickListener;
import com.damai.dl.PluginFragment;
import com.damai.helper.DMAdapter;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.widget.SubmitButton;

import java.util.List;
public class SelectSchoolFragment extends PluginFragment implements IOnItemClickListener<SchoolVo>, OnItemClickListener{
	
	public interface SelectSchoolListener{
		void onSelectSchool(String schoolName,String schoolCode);
	}
	private EditText editText;
	private SelectSchoolListener listener;
	
	
	@Res
	private SubmitButton btnSearch;
	
	@Res
	private ListView listView;
	
	
	
	public SelectSchoolFragment(){
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		listener = null;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_select_school, container,false);
	}
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		/**
		 * 这里初始化
		 */
		listView.setOnItemClickListener(this);
		editText = (EditText) view.findViewById(R.id._edit_text);
		view.findViewById(R.id._title_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				//FragmentUtil.removeFragment(getActivity(), SelectSchoolFragment.this, true);	
			}
		});
		
	}
	
	@JobSuccess("school/list")
	public void onGetList(List<SchoolVo> list) {
		@SuppressWarnings("unchecked")
		final DMAdapter<SchoolVo> adapter = (DMAdapter<SchoolVo>) listView
				.getAdapter();
		adapter.setData(list);
	}
	
	@Override
	public void onItemClick(Activity activity, SchoolVo data, int position) {
		listener.onSelectSchool(data.getName(), data.getCode());
		finish();
		//FragmentUtil.removeFragment(getActivity(), SelectSchoolFragment.this, true);	
	}

	public SelectSchoolListener getListener() {
		return listener;
	}

	public void setListener(SelectSchoolListener listener) {
		this.listener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		onItemClick(getActivity(), (SchoolVo) parent.getAdapter().getItem(position),position);
	}
	
}
