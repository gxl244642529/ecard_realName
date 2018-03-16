package com.damai.core;

import com.citywithincity.utils.Alert;
import com.damai.auto.LifeManager;
import com.damai.note.NotificationParser;



@SuppressWarnings({ "unchecked", "rawtypes" })
public class ApiDelivery extends DefaultJobDelivery<ApiJobImpl> implements OnApiMessageListener {
	 NotificationParser notificationParser;
	public ApiDelivery(IJobLife life,NotificationParser notificationParser) {
		super(life);
		this.notificationParser = notificationParser;
	}

	@Override
	public void onJobSuccess(ApiJobImpl job) {
		if(job.button!=null){
			job.button.setEnabled(true);
		}
		if(job.getWaitingMessage()!=null){
			Alert.cancelWait();
		}
		OnJobSuccessListener listener = job.onJobSuccessListener;
		if(listener!=null){
			listener.onJobSuccess(job);
		}
		notificationParser.notifyObservers(
				NotificationParser.makeJobSuccessNotification(job.getId()),
				job.getData());
		
		
	}
	
	@Override
	public void onJobProgress(ApiJobImpl job) {
		
	}

	@Override
	public boolean onJobError(ApiJobImpl job) {
		if(job.button!=null){
			job.button.setEnabled(true);
		}
		if(job.getWaitingMessage()!=null){
			Alert.cancelWait();
		}
		boolean deliveried = false;
		OnJobErrorListener listener = job.onJobErrorListener;
		if(listener!=null){
			deliveried = listener.onJobError(job);
		}
		deliveried = deliveried || notificationParser.notifyObservers(NotificationParser.makeJobErrorNotification(job.getId()),
						job.getError());
		if(!deliveried){
			//这里如果没有投递
			Alert.showShortToast(job.getError().getReason());
		}
		
		
		return true;
		//life.onRemoveJob(job);
	}

	@Override
	public boolean onApiMessage(ApiJob ijob) {
		ApiJobImpl job = (ApiJobImpl) ijob;
		if(job.button!=null){
			job.button.setEnabled(true);
		}
		if(job.getWaitingMessage()!=null){
			Alert.cancelWait();
		}
		boolean deliveried = false;
		OnApiMessageListener listener = job.onJobMessageListener;
		if(listener!=null){
			deliveried = listener.onApiMessage(job);
		}
		deliveried = deliveried || notificationParser.notifyObservers(NotificationParser.makeJobMessageNotification(job.getId()),job);
		if(!deliveried){
			//这里如果没有投递
			DMMessage message = job.getMessage();
			if(message.getType() == DMMessage.ALERT){
				if(message.getTitle()==null){
					Alert.alert(LifeManager.getActivity(), message.getMessage());
				}else{
					Alert.alert(LifeManager.getActivity(),message.getTitle(), message.getMessage());
				}
			}else{
				Alert.showShortToast(message.getMessage());
			}
		}
		return true;
	}

	
	

}
