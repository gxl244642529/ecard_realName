package com.citywithincity.ecard.insurance.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.activities.others.InsurancePolicyAutoActivity;
import com.citywithincity.ecard.insurance.activities.others.InsurancePolicyDomesticActivity;
import com.citywithincity.ecard.insurance.activities.others.InsurancePolicyTravelActivity;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.PICCPayAction;
import com.citywithincity.ecard.insurance.models.vos.InsurancePaySuccessNotifyVo;
import com.citywithincity.ecard.insurance.models.vos.InsurancePolicyVo;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.pay.PayIds;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.interfaces.IOnItemClickListener;
import com.citywithincity.models.ViewPagerAdapter.OnViewPagerCreateLiserner;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.paylib.ECardPayModel;
import com.citywithincity.paylib.IPay;
import com.citywithincity.paylib.IPay.CashierInfo;
import com.citywithincity.paylib.PayType;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.widget.StateListView;
import com.citywithincity.widget.TabPaneView;
import com.citywithincity.widget.TabPaneView.OnTabPaneViewSwitchListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

import java.util.ArrayList;
import java.util.List;

@Observer
public class InsuranceMyPolicyActivity extends BaseActivity implements
		IOnItemClickListener<InsurancePolicyVo>, OnTabPaneViewSwitchListener,
		OnViewPagerCreateLiserner, IListDataProviderListener<InsurancePolicyVo> {

	private static final int NORMAL = 0;
	private static final int EXPIRES = 1;

	// private StateListView<InsurancePolicyVo> listView;
	private TabPaneView _tabPaneView;
	private List<InsurancePolicyVo>[] lists;
	private StateListView<InsurancePolicyVo>[] listViews;
	private int currentIndex;

	private boolean hasFailureOrder = false;

	@SuppressWarnings("unchecked")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_my_policy);
		setTitle("我的保单");

		_tabPaneView = (TabPaneView) findViewById(R.id._tab_pane_view);
		_tabPaneView.setListener(this);
		_tabPaneView.setCurrent(0);
		_tabPaneView.setOnViewPagerCreateLiserner(this);

		listViews = new StateListView[2];
		lists = new List[2];

		ModelHelper.getModel(InsuranceModel.class).getMyList();
	}

	@NotificationMethod(InsuranceModel.MY_LIST)
	public void onRechargeQuerySuccess(List<InsurancePolicyVo> result,
			boolean isLastPage) {

		for (int i = 0; i < 2; i++) {
			lists[i] = new ArrayList<InsurancePolicyVo>();
		}

		for (InsurancePolicyVo data : result) {

			if (data.status == -1) {
				hasFailureOrder = true;
			}

			if (data.status == 3 || data.status == 4 || data.status == 5) {
				lists[EXPIRES].add(data);
			} else {
				lists[NORMAL].add(data);
			}
		}
		listViews[currentIndex].onRequestSuccess(lists[currentIndex],
				isLastPage);

		for (int i = 0; i < lists.length; i++) {
			if (currentIndex != i) {
				if (listViews[i] != null) {
					listViews[i].onRequestSuccess(lists[i], isLastPage);
				}
			}
		}

		if (hasFailureOrder) {
			Alert.alert(this, "提示", "你有提交失败的保单，请重新提交", new DialogListener() {

				@Override
				public void onDialogButton(int id) {

				}
			});
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onViewCreated(View view, int position) {
		listViews[position] = (StateListView<InsurancePolicyVo>) view;
		StateListView<InsurancePolicyVo> listView = listViews[position];
		listView.setItemRes(R.layout.item_insurance_my_policy);
		listView.setSelector(R.drawable.item_selector);
		listView.setDataListener(this);
		listView.setOnItemClickListener(this);
		listView.setMode(Mode.DISABLED);
	}

	@Override
	public void onTabPaneViewSwitch(View view, int index, boolean firstSwitch) {
		currentIndex = index;
		if (firstSwitch) {
			if (lists[currentIndex] != null) {
				listViews[currentIndex].onRequestSuccess(lists[currentIndex],
						true);
			}
		}
	}

	@Override
	public void onItemClick(Activity activity, InsurancePolicyVo data,
			int position) {

			if (data.status == 0 || data.status == 4 || data.status == 6) {
				// 保障中 // 已过期 // 未生效保单
				if (TextUtils.isEmpty(data.e_card_id)) {
					if (data.category_code.equals("2")) {//居家小屋
						ActivityUtil.startActivity(this, InsurancePolicyDomesticActivity.class, data);
					} else if (data.category_code.equals("3")) {//背包乐游
						ActivityUtil.startActivity(this, InsurancePolicyTravelActivity.class, data);
					} else if (data.category_code.equals("5")) {//爱车一族
						ActivityUtil.startActivity(this, InsurancePolicyAutoActivity.class, data);
					}
				} else {
					ActivityUtil.startActivity(this,
							InsuranceMyPolicyDetailActivity.class, data);
				}
				
			}

			if (data.status == 1 || data.status == 2 || data.status == 3) {
				// 理赔中 // 已理赔
				if (TextUtils.isEmpty(data.e_card_id)) {
					//
				} else {
					ActivityUtil.startActivity(this,
							InsuranceInClaimActivity.class, data);
				}
			}

			if (data.status == -1) {
				// 保单未提交
				ActivityUtil.startActivity(this,
						InsuranceValidateSubmitInfoActivity.class, data);
				
//				ActivityUtil.startActivity(this, InsurancePolicyAutoActivity.class, data);
			}
			
			if (data.status == 7 && !data.category_code.equals("1")) {
				// 保单未付款
				ModelHelper.getModel(InsuranceModel.class).getPayInfo(data.order_id);
			}
			

	}

	@NotificationMethod(InsuranceModel.CLIENT_NOTIFY)
	public void onBtnNotifySuccess() {
		finish();
	}

	@NotificationMethod(InsuranceModel.LOST)
	public void onLostSuccess(boolean result) {
		finish();
	}

	@Override
	public void onInitializeView(View view, InsurancePolicyVo data, int position) {
		ImageView image = (ImageView) view.findViewById(R.id.thumb);
		// if (data.status < 0) {
		// image.setBackgroundResource(R.drawable.ic_insurance_no_order_thumb);
		ECardJsonManager.getInstance().setImageSrc(data.thumb_url, image,
				R.drawable.ic_insurance_no_order_thumb);
		// } else {
		// ECardJsonManager.getInstance().setImageSrc(data.thumb_url, image);
		// }
		if (TextUtils.isEmpty(data.e_card_id)) {
			view.findViewById(R.id.ecard_member).setVisibility(View.GONE);
		}

	}
	
	@NotificationMethod(InsuranceModel.INSURANCE_PAY_INFO)
	public void onGetPayInfoSuccess(InsurancePaySuccessNotifyVo result) {
		IPay model = ModelHelper.getModel(ECardPayModel.class);
		model.setId(PayIds.PAY_ID_PICC);
		model.setCashierInfo(new CashierInfo(result.id, result.fee));
		model.setPayAction(new PICCPayAction());
		model.setPayTypes(new PayType[] { PayType.PAY_WEIXIN });
		ActivityUtil.startActivity(this,
				InsuranceCashierActivity.class, result);
		finish();
	}

}
