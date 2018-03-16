package com.citywithincity.ecard.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.enums.Actions;
import com.citywithincity.ecard.models.MyECardModel;
import com.citywithincity.ecard.models.vos.ECardInfo;
import com.citywithincity.interfaces.IOnItemClickListener;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.widget.StateListView;

@Observer
@EventHandler
public class MyECardActivity extends BaseActivity implements IOnItemClickListener<ECardInfo>{
	
	private static final String REQUEST = "selectECard";
	
	private boolean selectMode;
	StateListView<ECardInfo> listView;
	@SuppressWarnings("unchecked")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.my_ecard_list);
		
		listView = (StateListView<ECardInfo>)findViewById(R.id._list_view);
		listView.setItemRes(R.layout.my_ecard_list_item);
		listView.setOnItemClickListener(this);
		
		listView.setTask(ModelHelper.getModel(MyECardModel.class).getList());
		
		
		if(getIntent()!=null && REQUEST.equals( getIntent().getAction())){
			setTitle("选择e通卡");
			selectMode = true;
		}else{
			setTitle("我的e通卡");
			selectMode = false;
		}
		
	}

	
	public static void selectECart(Activity context) {
		Intent intent = new Intent(context,MyECardActivity.class);
		intent.setAction(REQUEST);
		context.startActivity(intent);
	}
	
	
	@EventHandlerId(id=R.id.id_bind)
	public void onBtnBind(){
		startActivity(new Intent(this,BindECardActivity.class));
	}
	
	

	
	@EventHandlerId(id=R.id._title_right)
	public void onBtnScan(){
		
		startActivity(new Intent(this,MyECardScanActivity.class));
	}

	@Override
	public void onItemClick(Activity activity, ECardInfo data, int position) {
		if(selectMode){
			Notifier.notifyObservers(Actions.SELECT_ECARD, data);
			finish();
		}else{
			ActivityUtil.startActivity(this, MyECardDetailActivity.class, data);
		}
		
	}
	
	@NotificationMethod(MyECardModel.ECARD_UNBIND)
	public void onUnBindSuccess(Object value){
		listView.reloadWithState();
	}
	
	@NotificationMethod(MyECardModel.NAME_HAS_CHANGED)
	public void onRenameSuccess(String name){
		listView.reloadWithState();
	}
	
	@NotificationMethod(MyECardModel.ECARD_BARCODE)
	public void onScanSuccess(Object value){
		listView.reloadWithState();
	}
	
	@NotificationMethod(MyECardModel.ECARD_BIND)
	public void onEcardBindSuccess(Object result) {
		listView.reloadWithState();
	}

}
