package com.citywithincity.ecard.selling.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.citywithincity.ecard.selling.models.CartModel;
import com.citywithincity.ecard.selling.models.ShopMenuDataProvider;
import com.citywithincity.ecard.selling.models.vos.SCardListVo;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.widget.TabFragment;
import com.citywithincity.widget.TopMenu;
import com.citywithincity.widget.TopMenu.OnMenuSelectListener;
import com.citywithincity.widget.data.MenuData;
import com.damai.widget.StateListView;

/**
 * 售卡列表
 * 
 * @author randy
 * 
 */
@Observer
public class SCardListFragment extends TabFragment implements OnMenuSelectListener{
	/**
	 * 排序
	 * 
	 * price 价格 update 更新时间 server 推荐
	 * 
	 */
	private String order;

	/**
	 * 分类
	 */
	private Integer type;

	private StateListView<SCardListVo> listView;
	
	@Override
	public View onCreateView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.s_fragment_card_list, null);
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void onInitFragment(Context context) {
		setTitle(R.string.card_store);
		// 获取menu 
		TopMenu topMenu = (TopMenu)findViewById(R.id._top_menu);
		topMenu.setOnMenuSelectListener(this);
		topMenu.setDataProvider(new ShopMenuDataProvider());

		listView = (StateListView<SCardListVo>)findViewById(R.id._list_view);
	}

	@Override
	public void onResume() {
		if(JsonTaskManager.getInstance().isLogin()){
			ModelHelper.getModel(CartModel.class).getList(LibConfig.StartPosition);
		}
		super.onResume();
	}

	/*
	@NotificationMethod(CartModel.LIST)
	public void onGetList(List<SCartListVo> list,boolean isLastPage){
		Notifier.notifyObservers(CartModel.CART_NUMBER, list.size());
	}
	*/
	@NotificationMethod(ECardJsonManager.LOGIN_SUCCESS)
	public void onLoginSuccess(ECardUserInfo userInfo){
		ModelHelper.getModel(CartModel.class).getList(LibConfig.StartPosition);
	}

	@Override
	public void onMenuSelected(int index, MenuData data,boolean fromUser) {
		// TODO Auto-generated method stub
		if(index==0){
			type = (Integer)data.data;
		}else{
			order = (String)data.data;
		}
		if(type!=null && order!=null){
        //    ShopModel;
		/*	listView.setStateLoading();
			//获取
			listView.setTask(ModelHelper.getModel(ShopModel.class).getSCardList(LibConfig.StartPosition, type, order));
			*/

			listView.getJob().put("type",type).put("order",order);

			listView.refreshWithState();
		}
	}
	



	

	
}
