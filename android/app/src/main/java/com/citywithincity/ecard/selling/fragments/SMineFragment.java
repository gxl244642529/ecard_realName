package com.citywithincity.ecard.selling.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.SellingConfig;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.selling.activities.SAllMyOrderActivity;
import com.citywithincity.ecard.selling.activities.SMyCollectionActivity;
import com.citywithincity.ecard.selling.activities.SMyDiyOnLineActivity;
import com.citywithincity.ecard.selling.activities.SOrderListActivity;
import com.citywithincity.ecard.selling.models.OrderModel;
import com.citywithincity.ecard.selling.models.vos.SOrderListVo;
import com.citywithincity.ecard.widget.UserInfoView;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.widget.TabFragment;

import java.util.List;

@Observer
public class SMineFragment extends TabFragment implements OnClickListener {

	public static int SET_ADDR = 1;

	private TextView noPayView, noPostView, noRecieveView, booView, doneView,
			refundView;
	
	private UserInfoView userInfoView;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.s_fragment_mine, null);
		view.findViewById(R.id._title_left).setOnClickListener(this);

		view.findViewById(R.id.my_collection).setOnClickListener(this);
		view.findViewById(R.id.my_diy).setOnClickListener(this);
		view.findViewById(R.id.all_order_list).setOnClickListener(this);
		view.findViewById(R.id.order_unpaied).setOnClickListener(this);
		view.findViewById(R.id.order_undispatched).setOnClickListener(this);
		view.findViewById(R.id.order_unfetched).setOnClickListener(this);
		view.findViewById(R.id.order_book).setOnClickListener(this);
		view.findViewById(R.id.order_back).setOnClickListener(this);
		view.findViewById(R.id.order_refund).setOnClickListener(this);

		noPayView = (TextView) view.findViewById(R.id.wait_for_pay_num);
		noPostView = (TextView) view.findViewById(R.id.wait_for_post_num);
		noRecieveView = (TextView) view.findViewById(R.id.wait_for_recieve_num);
		booView = (TextView) view.findViewById(R.id.book_num);
		doneView = (TextView) view.findViewById(R.id.done_num);
		refundView = (TextView) view.findViewById(R.id.refund_num);

		noPayView.setVisibility(View.INVISIBLE);
		noPostView.setVisibility(View.INVISIBLE);
		noRecieveView.setVisibility(View.INVISIBLE);
		booView.setVisibility(View.INVISIBLE);
		doneView.setVisibility(View.INVISIBLE);
		refundView.setVisibility(View.INVISIBLE);
		
		
		
		userInfoView = (UserInfoView) view.findViewById(R.id._container);
		userInfoView.onResume( (IViewContainer)getActivity());
		
		
		return view;
	}
	
	@Override
	protected void onInitFragment(Context context) {
		setTitle(R.string.mine);
	}

	@Override
	public void onResume() {
		if (!ECardJsonManager.getInstance().isLogin()) {
			
			noPayView.setVisibility(View.INVISIBLE);
			noPostView.setVisibility(View.INVISIBLE);
			noRecieveView.setVisibility(View.INVISIBLE);
		} else {


		}
		super.onResume();
	}
	
	@NotificationMethod(OrderModel.LIST)
	public void onGetOrder(List<SOrderListVo> result,boolean isNetworkError) {
		int orderNoPayl = 0, orderPaied = 0, orderDelivered = 0;

		for (SOrderListVo sOrderListVo : result) {
			if (sOrderListVo.state == SellingConfig.ORDER_NOPAY) {
				orderNoPayl++;
			} else if (sOrderListVo.state == SellingConfig.ORDER_PAYED) {
				orderPaied++;
			} else if (sOrderListVo.state == SellingConfig.ORDER_DELIVERED) {
				orderDelivered++;
			}
		}

		if (orderNoPayl != 0) {
			 noPayView.setText(String
						 .valueOf(orderNoPayl));
			 noPayView.setVisibility(View.VISIBLE);
		} else if(orderNoPayl == 0) {
			noPayView.setVisibility(View.GONE);
		}
		
		if (orderPaied != 0) {
			noPostView.setText(String
					 .valueOf(orderPaied));
			noPostView.setVisibility(View.VISIBLE);
		}else if(orderPaied == 0) {
			noPostView.setVisibility(View.GONE);
		}
		if (orderDelivered != 0) {
			noRecieveView.setText(String
					 .valueOf(orderDelivered));
			noRecieveView.setVisibility(View.VISIBLE);
		} else if(orderDelivered == 0) {
			noRecieveView.setVisibility(View.GONE);
		}
	}
	

	@Override
	public void onDestroy() {
		ECardJsonManager.getInstance().onDestroy(getActivity());
		noPayView = null;
		noPostView = null;
		noRecieveView = null;
		booView = null;
		doneView = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		switch (v.getId()) {
//		case R.id.id_address:
//			if (ECardJsonManager.getInstance().isLogin()) {
//				ActivityUtil.startActivity(getActivity(), UserInfoActivity.class);
//			} else {
//				ECardJsonManager.getInstance().requestLogin(getActivity());
//			}
//			break;
		case R.id.my_collection:
			if (ECardJsonManager.getInstance().isLogin()) {
				ActivityUtil.startActivity(getActivity(),
						SMyCollectionActivity.class);
			} else {
				ECardJsonManager.getInstance().requestLogin(getActivity());
			}

			break;
		case R.id.my_diy:
			if (ECardJsonManager.getInstance().isLogin()) {
				ActivityUtil.startActivity(getActivity(),
						SMyDiyOnLineActivity.class);
			} else {
				ECardJsonManager.getInstance().requestLogin(getActivity());
			}

			break;
		case R.id.all_order_list:
			if (ECardJsonManager.getInstance().isLogin()) {
				ActivityUtil.startActivity(getActivity(),
						SAllMyOrderActivity.class);
			} else {
				ECardJsonManager.getInstance().requestLogin(getActivity());
			}

			break;
		case R.id.order_unpaied:
			if (ECardJsonManager.getInstance().isLogin()) {
				bundle.putInt("state", SellingConfig.ORDER_NOPAY);
				ActivityUtil.startActivity(getActivity(),
						SOrderListActivity.class, bundle);
			} else {
				ECardJsonManager.getInstance().requestLogin(getActivity());
			}

			break;
		case R.id.order_undispatched:
			if (ECardJsonManager.getInstance().isLogin()) {
				bundle.putInt("state", SellingConfig.ORDER_PAYED);
				ActivityUtil.startActivity(getActivity(),
						SOrderListActivity.class, bundle);
			} else {
				ECardJsonManager.getInstance().requestLogin(getActivity());
			}

			break;
		case R.id.order_unfetched:
			if (ECardJsonManager.getInstance().isLogin()) {
				bundle.putInt("state", SellingConfig.ORDER_DELIVERED);
				ActivityUtil.startActivity(getActivity(),
						SOrderListActivity.class, bundle);
			} else {
				ECardJsonManager.getInstance().requestLogin(getActivity());
			}

			break;
		case R.id.order_book:
			bundle.putInt("state", SellingConfig.ORDER_UNAUDITED);
			if (ECardJsonManager.getInstance().isLogin()) {
				ActivityUtil.startActivity(getActivity(),
						SOrderListActivity.class, bundle);
			} else {
				ECardJsonManager.getInstance().requestLogin(getActivity());
			}

			break;
		case R.id.order_back:
			bundle.putInt("state", SellingConfig.ORDER_RECEIVED);
			if (ECardJsonManager.getInstance().isLogin()) {
				ActivityUtil.startActivity(getActivity(),
						SOrderListActivity.class, bundle);
			} else {
				ECardJsonManager.getInstance().requestLogin(getActivity());
			}
			break;

//		case R.id.login_container:
//			ECardJsonManager.getInstance().requestLogin(getActivity());
//			break;

		case R.id.order_refund:
			bundle.putInt("state", SellingConfig.ORDER_REFUND);
			if (ECardJsonManager.getInstance().isLogin()) {
				ActivityUtil.startActivity(getActivity(),
						SOrderListActivity.class, bundle);
			} else {
				ECardJsonManager.getInstance().requestLogin(getActivity());
			}
			break;
		case R.id._title_left:
			getActivity().finish();
			break;

		default:
			break;
		}
	}


	
}
