package com.citywithincity.ecard.insurance.activities;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.activities.others.InsuranceOffLineActivity;
import com.citywithincity.ecard.insurance.activities.others.InsuranceOthersActivity;
import com.citywithincity.ecard.insurance.activities.others.InsuranceTravelDetailActivity;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.vos.InsuranceListVo;
import com.citywithincity.ecard.insurance.models.vos.InsuranceTypeVo;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.interfaces.IOnItemClickListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.widget.StateListView;

@Observer
@EventHandler
public class InsuranceProductListActivity extends BaseActivity implements
		IOnItemClickListener<InsuranceListVo>,
		IListDataProviderListener<InsuranceListVo> {

	private StateListView<InsuranceListVo> listView;

	@SuppressWarnings("unchecked")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_product_list);

		InsuranceTypeVo type = (InsuranceTypeVo) getIntent().getExtras().get(
				LibConfig.DATA_NAME);

		if(type==null){
			Alert.alert(this, "温馨提示", "数据为空，请重新进入", new DialogListener() {
				@Override
				public void onDialogButton(int id) {
					finish();
				}
			});
			return;
		}

		setTitle(type.title);

		listView = (StateListView<InsuranceListVo>) findViewById(R.id._list_view);
		listView.setItemRes(R.layout.item_insurance_home);
		listView.setOnItemClickListener(this);
		listView.setDataListener(this);
		listView.setSelector(R.drawable.new_item_selector);
		listView.setTask(ModelHelper.getModel(InsuranceModel.class).getList(
				type.type_id));

	}

	@Override
	public void onItemClick(Activity activity, InsuranceListVo data,
			int position) {
		if ("2".equals(data.type_id) || "3".equals(data.type_id)) {
			if ("1".equals(data.on_sale)) {
				if ("5801".equals(data.insurance_id)
						|| "5802".equals(data.insurance_id)
						|| "5803".equals(data.insurance_id)) {
					Bundle bundle = new Bundle();
					bundle.putString("insurance_id", data.insurance_id);
					bundle.putString("type_id", data.type_id);
					ActivityUtil.startActivity(this,
							InsuranceTravelDetailActivity.class, bundle);
				} else {
					Bundle bundle = new Bundle();
					bundle.putString("insurance_id", data.insurance_id);
					bundle.putString("type_id", data.type_id);
					ActivityUtil.startActivity(this,
							InsuranceOthersActivity.class, bundle);
				}
			} else {
				Bundle bundle = new Bundle();
				bundle.putString("insurance_id", data.insurance_id);
				bundle.putString("type_id", data.type_id);
				bundle.putString("icon_url", data.icon_url);
				bundle.putString("title", data.title);
				ActivityUtil.startActivity(this,
						InsuranceOffLineActivity.class, bundle);
			}
		} else if ("5".equals(data.type_id)) {// 寿险
			if ("1".equals(data.on_sale)) {
				Bundle bundle = new Bundle();
				bundle.putString("insurance_id", data.insurance_id);
				bundle.putString("type_id", data.type_id);
				ActivityUtil.startActivity(this, InsuranceOthersActivity.class,
						bundle);
			} else {
				Bundle bundle = new Bundle();
				bundle.putString("insurance_id", data.insurance_id);
				bundle.putString("type_id", data.type_id);
				bundle.putString("icon_url", data.icon_url);
				bundle.putString("title", data.title);
				ActivityUtil.startActivity(this,
						InsuranceOffLineActivity.class, bundle);
			}
		} else if ("6".equals(data.type_id)) {// 车险
			if ("1".equals(data.on_sale)) {
				Bundle bundle = new Bundle();
				bundle.putString("insurance_id", data.insurance_id);
				bundle.putString("type_id", data.type_id);
				ActivityUtil.startActivity(this, InsuranceOthersActivity.class,
						bundle);
			} else {
				Bundle bundle = new Bundle();
				bundle.putString("insurance_id", data.insurance_id);
				bundle.putString("type_id", data.type_id);
				bundle.putString("icon_url", data.icon_url);
				bundle.putString("title", data.title);
				ActivityUtil.startActivity(this,
						InsuranceOffLineActivity.class, bundle);
			}
		} else if ("4".equals(data.type_id)) {// 金融
			if ("1".equals(data.on_sale)) {
				Bundle bundle = new Bundle();
				bundle.putString("insurance_id", data.insurance_id);
				bundle.putString("type_id", data.type_id);
				ActivityUtil.startActivity(this, InsuranceOthersActivity.class,
						bundle);
			} else {
				Bundle bundle = new Bundle();
				bundle.putString("insurance_id", data.insurance_id);
				bundle.putString("type_id", data.type_id);
				bundle.putString("icon_url", data.icon_url);
				bundle.putString("title", data.title);
				ActivityUtil.startActivity(this,
						InsuranceOffLineActivity.class, bundle);
			}
		} else {
			ActivityUtil.startActivity(this,
					InsuranceProductDetailActivity.class, data.insurance_id);
		}
		// if ("1".equals(data.type_id)) {
		// ActivityUtil.startActivity(this,
		// InsuranceProductDetailActivity.class,data.insurance_id);
		// } else {
		// if ("1".equals(data.on_sale)) {
		// if ("5801".equals(data.insurance_id) ||
		// "5802".equals(data.insurance_id) || "5803".equals(data.insurance_id))
		// {
		// Bundle bundle = new Bundle();
		// bundle.putString("insurance_id", data.insurance_id);
		// bundle.putString("type_id", data.type_id);
		// ActivityUtil.startActivity(this,
		// InsuranceTravelDetailActivity.class,bundle);
		// } else {
		// Bundle bundle = new Bundle();
		// bundle.putString("insurance_id", data.insurance_id);
		// bundle.putString("type_id", data.type_id);
		// ActivityUtil.startActivity(this,
		// InsuranceOthersActivity.class,bundle);
		// }
		// } else {
		//
		// }
		// }
	}

	@EventHandlerId(id = R.id._title_right, checkLogin = true)
	public void onBtnMyInsurance() {
		ActivityUtil.startActivity(this, InsuranceMyPolicyActivity.class);
	}

	@Override
	public void onInitializeView(View view, InsuranceListVo data, int position) {
		TextView textView = (TextView) view.findViewById(R.id.original_price);
		if (data.type_id.equals("1")) {
			textView.setText("¥" + data.ori_price);
			textView.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 中划线
		} else {
			textView.setVisibility(View.GONE);
		}

		ImageView flag = (ImageView) view.findViewById(R.id.flag);
		if ("1".equals(data.promotion_type)) {
			flag.setVisibility(View.VISIBLE);
			flag.setBackgroundResource(R.drawable.ic_insurance_flag_hot);
		} else if ("2".equals(data.promotion_type)) {
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

}
