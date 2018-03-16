package com.damai.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citywithincity.models.cache.CachePolicy;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.core.DMLib;
import com.damai.helper.IValue;
import com.damai.interfaces.PopupListener;
import com.damai.lib.R;
import com.damai.widget.vos.AddressVo;
import com.damai.widget.vos.Ssq;

/**
 * 地址选择视图
 * 
 * @author renxueliang
 * 
 */
public class AddressPickerView extends RelativeLayout implements ApiListener,
		OnWheelChangedListener, IValue, FormElement {
	private TextView hintView;
	private ApiJob job;

	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;

	public AddressPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(listener);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable._address_picker);

		shengId = a.getInt(R.styleable._address_picker_provinceId, 350000);
		shiId = a.getInt(R.styleable._address_picker_cityId, 350200);
		quId = a.getInt(R.styleable._address_picker_areaId, 350203);
		fixShengId = a.getInt(R.styleable._address_picker_fixProvinceId, 0);

		a.recycle();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mProvinceList = null;
		mCityList = null;
		mViewCity = null;
		mViewDistrict = null;
		mViewProvince = null;
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mProvinceList != null) {
				initData();
				return;
			}
			// 首先加载数据
			if (job == null) {
				job = DMLib.getJobManager().createArrayApi("ssq/ssq");
				job.setServer(1);
				job.setCachePolicy(CachePolicy.CachePolity_Permanent);
				job.setEntity(Ssq.class);
				job.setWaitingMessage("正在加载省市区...");
				job.setCancelable(false);
				job.setApiListener(AddressPickerView.this);
			}
			job.execute();

		}
	};

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		hintView = (TextView) findViewById(R.id._select_hint);

	}

	private static class SsqAdapter extends AbstractWheelTextAdapter {

		private List<Ssq> list;

		public SsqAdapter(Context context, List<Ssq> list) {
			super(context);
			this.list = list;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			Ssq data = list.get(index);
			return data.name;
		}

	}

	/**
	 * 默认值
	 */

	private int shengId;
	private int shiId;
	private int quId;

	/**
	 * 固定省
	 */
	private int fixShengId;

	private String shengName;
	private String shiName;
	private String quName;

	int oldShengId;
	int oldShiId;
	int oldQuId;

	private List<Ssq> mProvinceList;
	private List<Ssq> mCityList;

	private void initData() {

		oldShengId = shengId;
		oldQuId = quId;
		oldShiId = shiId;

		Activity activity = (Activity) getContext();
		ViewGroup addressView = (ViewGroup) activity.getLayoutInflater()
				.inflate(R.layout._address_picker_view, null);
		mViewProvince = (WheelView) addressView.getChildAt(0);
		mViewCity = (WheelView) addressView.getChildAt(1);
		mViewDistrict = (WheelView) addressView.getChildAt(2);
		mViewProvince.addChangingListener(this);
		mViewCity.addChangingListener(this);
		mViewDistrict.addChangingListener(this);

		List<Ssq> list = mProvinceList;
		if (fixShengId > 0) {
			// 搜索
			for (Ssq ssq : list) {
				if (ssq.id == fixShengId) {
					if (list.size() > 0) {
						mProvinceList = new ArrayList<Ssq>();
						mProvinceList.add(ssq);
					}
					mViewProvince.setViewAdapter(new SsqAdapter(getContext(),
							mProvinceList));
					break;
				}
			}
			mViewProvince.setCurrentItem(0, WheelView.UserMode);

		} else {
			mViewProvince.setViewAdapter(new SsqAdapter(getContext(), list));
			if (shengId > 0) {
				// 搜索
				int index = 0;
				for (Ssq ssq : list) {
					if (ssq.id == shengId) {
						mViewProvince.setCurrentItem(index, false,
								WheelView.UserMode);
						break;
					}
					++index;
				}
			} else {
				mViewProvince.setCurrentItem(0, false, WheelView.UserMode);
			}
		}

		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();

		Dialogs.createBottomPopup(activity, addressView, "选择地区",
				new PopupListener<ViewGroup>() {

					@Override
					public void onPopup(int id, ViewGroup contentView) {
						if (id == R.id._id_ok) {
							updateDatas();
						} else {
							restoreDatas();
						}
					}

				});
	}

	@Override
	public void onJobSuccess(ApiJob job) {
		List<Ssq> list = job.getData();
		mProvinceList = list;
		initData();
	}

	private void restoreDatas() {
		shengId = oldShengId;
		shiId = oldShiId;
		quId = oldQuId;
	}

	private void updateDatas() {
		int p = mViewProvince.getCurrentItem();
		Ssq province = mProvinceList.get(p);
		shengId = province.id;
		shengName = province.name;
		int c = mViewCity.getCurrentItem();
		Ssq city = province.list.get(c);
		shiId = city.id;
		shiName = city.name;
		int a = mViewDistrict.getCurrentItem();
		if (city.list != null) {
			Ssq area = city.list.get(a);
			quId = area.id;
			quName = area.name;
		} else {
			quId = 0;
			quName = null;
		}

		hintView.setText(getAddress());
	}

	public String getAddress() {
		if (quId == 0) {
			return shengName + shiName;
		}
		return shengName + shiName + quName;
	}

	@SuppressWarnings("unchecked")
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		Ssq city = mCityList.get(pCurrent);
		List<Ssq> list = city.list;
		if (list == null) {
			list = Collections.EMPTY_LIST;
		}
		mViewDistrict.setViewAdapter(new SsqAdapter(getContext(), list));
		if (quId > 0) {
			// 搜索
			int index = 0;
			for (Ssq ssq : city.list) {
				if (ssq.id == quId) {
					mViewDistrict.setCurrentItem(index, false,
							WheelView.UserMode);
					break;
				}
				++index;
			}

		} else {
			mViewDistrict.setCurrentItem(0, false, WheelView.UserMode);
		}

	}

	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		Ssq province = mProvinceList.get(pCurrent);
		mCityList = province.list;
		mViewCity.setViewAdapter(new SsqAdapter(getContext(), province.list));

		if (shiId > 0) {
			// 搜索
			int index = 0;
			for (Ssq ssq : province.list) {
				if (ssq.id == shiId) {
					mViewCity.setCurrentItem(index, false, WheelView.UserMode);
					break;
				}
				++index;
			}

		} else {
			mViewCity.setCurrentItem(0, false, WheelView.UserMode);
		}

		updateAreas();
	}

	@Override
	public boolean onJobError(ApiJob job) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onApiMessage(ApiJob job) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue, int mode) {
		if (WheelView.UserMode == mode) {
			return;
		}
		if (wheel == mViewProvince) {
			updateCities();
			shiId = 0;
			quId = 0;
		} else if (wheel == mViewCity) {
			updateAreas();
			quId = 0;
		}
	}

	@Override
	public void setValue(Object value) {
		AddressVo data = (AddressVo) value;
		shengId = data.getShengId();
		shiId = data.getShiId();
		quId = data.getQuId();

		shengName = data.getSheng();
		shiName = data.getShi();
		quName = data.getQu();

		hintView.setText(getAddress());
	}

	@Override
	public String validate(Map<String, Object> data) {
		if (shengName == null) {
			return "请选择地区";
		}

		data.put("shengId", shengId);
		data.put("shiId", shiId);
		data.put("quId", quId);

		data.put("sheng", shengName);
		data.put("shi", shiName);
		data.put("qu", quName);

		return null;
	}

}
