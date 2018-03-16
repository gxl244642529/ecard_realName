package com.citywithincity.ecard.selling.models;

import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.selling.models.vos.SCardListVo;
import com.citywithincity.ecard.selling.models.vos.SCartListVo;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.interfaces.IJsonTaskListener;
import com.citywithincity.interfaces.IListRequsetResult;
import com.citywithincity.interfaces.IListTask;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.mvc.Model;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.widget.StateListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Observer
public class CartBModel extends Model implements IListTask,IListRequsetResult<List<SCartListVo>>, IJsonTaskListener<SCartListVo> {
	
	

	public static final String PRICE_HAS_CHANGED = "PRICE_HAS_CHANGED";
	public interface IOnCartItemChangeListener{
		void onChange(int count,int recharge);
	}



	public static String CART_COUNT = "CartBModel.cart_count";



	public static String ENTER_CART = "CartBModel.enter_cart";
	
	
	
	private boolean selected;
	
	
	public CartBModel(){
	}
	
	private IArrayJsonTask<SCartListVo> task;
	
	public void getList(){
		task=ModelHelper.getModel(CartModel.class).getList(LibConfig.StartPosition)
				.setOnParseListener(this).setListener(this);
		selectedList.clear();
	}
	
	private Set<Integer> selectedList = new HashSet<Integer>();
	
	
	public void toggleSelectAll() {
		selectedList.clear();
		selected = !selected;
		float totalPrice = 0;
		for (SCartListVo data : listView.getAdapter().getData()) {
			data.isSeleted = selected;
			if (selected) {
				totalPrice += data.getTotalPrice();
				selectedList.add(data.cartId);
			}
		}
		listView.getAdapter().notifyDataSetChanged();
		sendNotification(PRICE_HAS_CHANGED, totalPrice);
	}
	
	public void notifyPrice(){
		float totalPrice = 0;
		for (SCartListVo data : listView.getAdapter().getData()) {
			if (data.isSeleted) {
				totalPrice += data.getTotalPrice();
			}
		}
		sendNotification(PRICE_HAS_CHANGED, totalPrice);
	}

	@Override
	public Object beforeParseData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afterParseData(List<SCartListVo> data, Object result) {
		
	}

	public void selectItem(SCartListVo data, boolean checked) {
		if(checked){
			selectedList.add(data.cartId);
		}else{
			selectedList.remove(data.cartId);
		}
		data.isSeleted = checked;
		listView.getAdapter().notifyDataSetChanged();
		notifyPrice();
	}
	
	/**
	 * 增加到购物车
	 * @param data
	 */
	public void addToCart(SCardListVo data){
		
	}
	
	
	
	/**
	 * 选中的
	 * @return
	 */
	public ArrayList<SCartListVo> getSelected(){
		ArrayList<SCartListVo> list = new ArrayList<SCartListVo>();
		for (SCartListVo data : listView.getAdapter().getData()) {
			if (data.isSeleted) {
				list.add(data);
			}
		}
		return list;
	}

	
	public void deleteSeleted() {
		ArrayList<SCartListVo> list = getSelected();
		List<Integer> arr = new ArrayList<Integer>();
		for (SCartListVo sCartListVo : list) {
			arr.add(sCartListVo.cartId);
		}
		ModelHelper.getModel(CartModel.class).delete(arr);
	}

	/**
	 * 购物车删除
	 * @param cartIDS
	 */
	public void deleteFromCart(List<Integer> cartIDS) {
		
	//	sendNotification(CartModel.CART_NUMBER, args)
		
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		// TODO Auto-generated method stub
		listView.onRequestError(errMsg, isNetworkError);
	}

	@Override
	public void onRequestSuccess(List<SCartListVo> result, boolean isLastPage) {
		// TODO Auto-generated method stub
		for (SCartListVo sCartListVo : result) {
			if(selectedList.contains( sCartListVo.cartId)){
				sCartListVo.isSeleted = true;
			}
		}
		listView.onRequestSuccess(result, isLastPage);
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		task.reload();
	}

	@Override
	public void loadMore(int position) {
		// TODO Auto-generated method stub
		task.loadMore(position);
	}

	
	private StateListView<SCartListVo> listView;
	public void setListener(StateListView<SCartListVo> listView) {
		this.listView = listView;
	}

	
}
