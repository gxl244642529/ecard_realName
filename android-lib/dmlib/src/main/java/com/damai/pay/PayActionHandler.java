package com.damai.pay;

import android.app.Activity;
import android.content.Intent;

import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.utils.Alert;
import com.damai.auto.LifeManager;
import com.damai.core.DMLib;
import com.damai.core.ILife;
import com.damai.helper.DataHolder;


/**
 * 一般在需要支付的界面，一般为订单确认界面,或者详情页面初始化本Handle，根据实际需要，配置参数
 * @author renxueliang
 *
 */


public class PayActionHandler implements DMPayListener, ILife {
	
	public static interface IPayActionListener{
		void onPaySuccess(Object data);
		void onPayUserCancel();
		boolean onPayGetPayInfoError(String error,boolean isNetworkError);
	}
	
	
	private DMPayModel model;
	protected Activity context;
	
	public static enum PaySuccessAction{
		/**
		 * 关闭当前
		 */
		PaySuccessAction_PopupToPrevious, //CloseCurrent
		/**
		 *关闭到当前
		 */
		PaySuccessAction_PopupToCurrent,
	}
	
	/**
	 * 在收银台做出关闭操作,即为退出收银台的时候
	 * @author renxueliang
	 *
	 */
	public static enum PayCancelAction{
		/**
		 * 什么也不做
		 */
		PayCancelAction_None,
		/**
		 * 关闭当前
		 */
		PayCancelAction_PopupToPrevious, //CloseCurrent
		/**
		 *关闭到当前
		 */
		PayCancelAction_PopupToCurrent,
	}
	
	private PaySuccessAction paySuccessAction;
	
	private PayCancelAction payCancelAction;
	
	private IPayActionListener listener;
	/**
	 * 成功界面
	 */
	private Class<? extends  Activity> activityClass;
	
	public PayActionHandler(Activity activity,String moduleName,int[] supportPayTypes,Class<?> entityClass,Class<? extends  Activity> resultActivityClass){
		model = DMLib.getJobManager().createPayModel(moduleName,supportPayTypes);
		context = activity;
		model.setPayAction(new BasePayAction(moduleName,entityClass) );
		model.setListener(this);
		paySuccessAction = PaySuccessAction.PaySuccessAction_PopupToPrevious;
		payCancelAction = PayCancelAction.PayCancelAction_PopupToCurrent;
		activityClass = resultActivityClass;
		if(activity instanceof IViewContainer){
			((IViewContainer)activity).addLife(this);
		}
	}
	public PayActionHandler(Activity activity,String moduleName,int[] supportPayTypes){
		this(activity, moduleName, supportPayTypes, null);
	}
	public PayActionHandler(Activity activity,String moduleName,int[] supportPayTypes,Class<?> entityClass){
		this(activity, moduleName, supportPayTypes,entityClass, null);
	}
	
	public void setPaySuccessAction(PaySuccessAction successAction){
		this.paySuccessAction = successAction;
	}
	
	public void setPayCancelAction(PayCancelAction cancelAction){
		this.payCancelAction = cancelAction;
	}
	
	public void startup(String orderId,int fee){
		model.setOrderId(orderId);
		model.setFee(fee);
	}
	

	@Override
	public void onPaySuccess(DMPayModel model, Object data) {
		
		
		if(paySuccessAction==PaySuccessAction.PaySuccessAction_PopupToCurrent){
			LifeManager.popupTo(context);
		}else{
			Activity activity = LifeManager.findPrevious(context);
			LifeManager.popupTo(activity);
			context = activity;
		}
		

		//然后
		if(activityClass!=null){
			DataHolder.set(activityClass,data);
			context.startActivity(new Intent(context,activityClass));
		}
		
		if(listener!=null){
			listener.onPaySuccess(data);
		}
	}

	/**
	 * 这里需要拉取订单
	 * @param model
	 * @param type
     * @return
     */
	@Override
	public boolean onClientPaySuccess(DMPayModel model, int type, Object data) {
		return false;
	}

	@Override
	public void onPayCancel(DMPayModel model) {
		if(payCancelAction==PayCancelAction.PayCancelAction_PopupToCurrent){
			LifeManager.popupTo(context);
		}else if(payCancelAction == PayCancelAction.PayCancelAction_PopupToPrevious){
			Activity activity = LifeManager.findPrevious(context);
			LifeManager.popupTo(activity);
		}
		
		if(listener!=null){
			listener.onPayUserCancel();
		}
	}



	@Override
	public boolean onGetPayInfoError(DMPayModel model, String error,boolean isNetworkError) {
		if(listener!=null){
			return listener.onPayGetPayInfoError(error, isNetworkError);
		}else{
			Alert.alert(context,error);
		}
		return false;
	}

	@Override
	public boolean onPrePayError(String reason, boolean isNetworkError) {
		return false;
	}

    @Override
    public void onPayError(int type, String error, boolean isNetworkError) {

    }

    public IPayActionListener getListener() {
		return listener;
	}
	public void setListener(IPayActionListener listener) {
		this.listener = listener;
	}
	@Override
	public void onResume(IViewContainer container) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPause(IViewContainer container) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNewIntent(Intent intent, IViewContainer container) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDestroy(IViewContainer container) {
		DMLib.setPayModel(null);
	}
}
