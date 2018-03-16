package com.citywithincity.ecard.selling.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.citywithincity.auto.tools.FormError;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.selling.models.CartBModel.IOnCartItemChangeListener;
import com.citywithincity.interfaces.IWidget;
import com.citywithincity.interfaces.IWidget.OnWidgetValueChangeListener;
import com.citywithincity.models.Observable;
import com.citywithincity.utils.ViewUtil;
import com.citywithincity.widget.AddAndSubEditText;
import com.citywithincity.widget.Dialogs;
import com.citywithincity.widget.RadioGroup;
@SuppressLint("InflateParams")
public class AddToCartView extends Observable<IOnCartItemChangeListener> implements OnClickListener, OnWidgetValueChangeListener<Integer>{
	
	
	private final Context context;
	private final View view ;
	private final RadioGroup group;
	private final AddAndSubEditText edit;
	private final TextView stockView;
	
	public AddToCartView(Context context){
		this.context = context;
		view = LayoutInflater.from(context).inflate(R.layout.add_to_cart_view, null);
		view.findViewById(R.id._id_ok).setOnClickListener(this);
		group = (RadioGroup) view.findViewById(R.id.radio_group1);
		edit = (AddAndSubEditText) view.findViewById(R.id.num_edit);
		edit.setOnClickListener(this);
		edit.setOnWidgetValueChangeListener(this);
		
		stockView = (TextView) view.findViewById(R.id.stock);
		
		group.setValue(0, 0);
		group.setValue(1, 10);
		group.setValue(2, 20);
		group.setValue(3, 30);
	}
	
	
	public AddToCartView show(){
		Dialogs.addPopup(context, view, Dialogs.BOTTOM, ViewUtil.screenWidth, LayoutParams.WRAP_CONTENT);
		return this;
	}


	public AddToCartView setCount(int count) {
		edit.setValue(count);
		return this;
	}

	public AddToCartView setRecharge(int recharge) {
		group.setValue(recharge);
		return this;
	}

	public int getCount(){
		try {
			return edit.getValue();
		} catch (FormError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int getRecharge(){
		try {
			return (Integer)group.getValue();
		} catch (FormError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public void onClick(View v) {
		if(listener!=null){
			listener.onChange(getCount(), getRecharge());
		}
		Dialogs.hide();
	}


	@Override
	public void onWidgetValueChange(IWidget<Integer> widget) {
		
		
	}

	public AddToCartView setMaxCount(int stock) {
		edit.setMaxCount(stock);
		this.stockView.setText(String.valueOf(stock));
		return this;
	}
}
