package com.citywithincity.ecard.insurance.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.vos.InsuranceListVo;
import com.citywithincity.ecard.insurance.models.vos.InsuranceTypeVo;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.interfaces.IOnItemClickListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.widget.StateListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

@Observer
@EventHandler
public class InsuranceHomeActivity extends BaseActivity implements IOnItemClickListener<InsuranceListVo>, IListDataProviderListener<InsuranceListVo>, OnClickListener{

	private StateListView<InsuranceListVo> listView;
//	private List<InsuranceTypeVo> typeList;
//	private GridView gridView;
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_home);
		setTitle("保险");
		
		listView = (StateListView<InsuranceListVo>)findViewById(R.id._list_view);
		listView.setItemRes(R.layout.item_insurance_home);
		listView.setSelector(R.drawable.new_item_selector);
		listView.setDataListener(this);
		listView.setOnItemClickListener(this);
//		typeList = new ArrayList<InsuranceTypeVo>();
		
		//卡保类型
//		ModelHelper.getModel(InsuranceModel.class).getTypeList();
		
		ModelHelper.getModel(InsuranceModel.class).getAdvList(LibConfig.StartPosition);
		
		
		initListViewHeaderView();
	}
	
	@SuppressLint("InflateParams")
	private void initListViewHeaderView() {
		View view = getLayoutInflater().inflate(R.layout.insurance_home_listview_header, null);
		view.findViewById(R.id.safe_accident).setOnClickListener(this);
		view.findViewById(R.id.safe_age).setOnClickListener(this);
		view.findViewById(R.id.safe_car).setOnClickListener(this);
		view.findViewById(R.id.safe_cashier).setOnClickListener(this);
		view.findViewById(R.id.safe_ecard).setOnClickListener(this);
		view.findViewById(R.id.safe_family).setOnClickListener(this);
		((PullToRefreshListView)listView.getListView()).getRefreshableView().addHeaderView(view, null, false);

		listView.setTask(ModelHelper.getModel(InsuranceModel.class).getList("1"));
	}
	
//	@SuppressLint("InflateParams")
//	@NotificationMethod(InsuranceModel.TYPES)
//	public void onGetTypeListSuccess(List<InsuranceTypeVo> result,boolean isLastPage) {
//		List<InsuranceTypeVo> list = result;
//		if (typeList.isEmpty()) {
//			typeList = result;
//			View view = getLayoutInflater().inflate(R.layout.insurance_home_listview_header, null);
//			gridView = (GridView) view.findViewById(R.id._grid_view);
//			InsuranceHomeTabAdapter gridAdapter = new InsuranceHomeTabAdapter(this, result);
//			gridView.setAdapter(gridAdapter);
//			InsuranceUtil.setGridViewHeightBasedOnChildren(gridView);
//			gridView.setOnItemClickListener(this);
//			listView.addHeaderView(view);
//			gridView.setSelector(R.drawable.new_item_selector);
//			if(result.size()>0){
//				listView.setTask(ModelHelper.getModel(InsuranceModel.class).getList(result.get(0).type_id));
//			}
//		}
//		
//	}
	
	@EventHandlerId(id=R.id._title_right,checkLogin=true)
	public void onBtnMyInsurance(){
		
		ActivityUtil.startActivity(this, InsuranceMyPolicyActivity.class);
		
	}

	//listview
	@Override
	public void onItemClick(Activity activity, InsuranceListVo data,
			int position) {
		if (data != null) {
			ActivityUtil.startActivity(this, InsuranceProductDetailActivity.class,data.insurance_id);
		}
	}

	//gridView
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		InsuranceTypeVo data = (InsuranceTypeVo) parent.getAdapter().getItem(position);
//		ActivityUtil.startActivity(this, InsuranceProductListActivity.class,data);
//		
//	}
	

	@Override
	public void onInitializeView(View view, InsuranceListVo data, int position) {
		TextView textView = (TextView) view.findViewById(R.id.original_price);
		if (data.type_id.equals("1")) {
			textView.setText("¥" + data.ori_price);
			textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); //中划线
		} else {
			textView.setVisibility(View.GONE);
		}
		
		ImageView flag =  (ImageView) view.findViewById(R.id.flag);
		if ("1".equals(data.promotion_type)) {
			flag.setVisibility(View.VISIBLE);
			flag.setBackgroundResource(R.drawable.ic_insurance_flag_hot);
		} else if("2".equals(data.promotion_type)) {
			flag.setVisibility(View.VISIBLE);
			flag.setBackgroundResource(R.drawable.ic_insurance_flag_new);
		} else {
			flag.setVisibility(View.GONE);
		}
		
		if ("1".equals(data.on_sale)) {
			view.findViewById(R.id.price).setVisibility(View.VISIBLE);
		} else {
			view.findViewById(R.id.price).setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			super.onRestoreInstanceState(savedInstanceState);
		}catch (Exception e) {
			
		}
		savedInstanceState=null;
	}

	@Override
	public void onClick(View v) {
		
		InsuranceTypeVo typeVo = new InsuranceTypeVo();
		switch (v.getId()) {
		case R.id.safe_ecard:
			typeVo.title = getResources().getString(R.string.safe_ecard);
			typeVo.type_id = "1";//卡保
			break;
		case R.id.safe_family:
			typeVo.title = getResources().getString(R.string.safe_family);
			typeVo.type_id = "2";//居家小窝
			break;
		case R.id.safe_accident:
			typeVo.title = getResources().getString(R.string.safe_accident);
			typeVo.type_id = "3";//背包乐游
			break;
		case R.id.safe_age:
			typeVo.title = getResources().getString(R.string.safe_age);
			typeVo.type_id = "5";//爱车一族
			break;
		case R.id.safe_car:
			typeVo.title = getResources().getString(R.string.safe_car);
			typeVo.type_id = "6";//汽车金融
			break;
		case R.id.safe_cashier:
			typeVo.title = getResources().getString(R.string.safe_cashier);
			typeVo.type_id = "4";//个人金融
			break;
			
		default:
			break;
		}
		
		ActivityUtil.startActivity(this, InsuranceProductListActivity.class,typeVo);
	}

}
