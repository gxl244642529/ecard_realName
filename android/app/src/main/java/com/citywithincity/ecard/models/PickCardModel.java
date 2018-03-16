package com.citywithincity.ecard.models;

import com.citywithincity.auto.ApiArray;
import com.citywithincity.auto.ApiObject;
import com.citywithincity.auto.ApiValue;
import com.citywithincity.ecard.interfaces.IECardPlatform.PickCardQueryResult;
import com.citywithincity.ecard.models.vos.LostCardDetailInfo;
import com.citywithincity.ecard.models.vos.MyLostCardDetailInfo;
import com.citywithincity.ecard.models.vos.PickCardDetailInfo;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.interfaces.IObjectJsonTask;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.interfaces.IValueJsonTask;

import java.util.Map;

/**
 * 是卡不美
 * 
 * @author Randy
 * 
 */
public interface PickCardModel {
	
	String my_lost_card_info = "my_lost_card_info";
	
	String lost_revocation = "lost_revocation";
	String lost_publish = "lost_publish";
	String lost_save = "lost_save";
	
	String query_lost_card = "query_lost_card";
	
	String pick_save = "pick_save";
	/**
	 * 捡到卡以后的查询
	 * 
	 * @param card_number
	 * @param listener
	 */
	void pickupCardQuery(String card_number,
			IRequestResult<PickCardQueryResult> listener);

	/**
	 * 
	 * @param cardNumber
	 * @param listener
	 */
	@ApiObject(api=query_lost_card,params={"cardid","pushID"},clazz=MyLostCardDetailInfo.class)
	IObjectJsonTask<MyLostCardDetailInfo> queryLostCard(String cardNumber,String pushID);

	/**
	 * 
	 * @param cardID
	 * @param phoneNumber
	 * @param strAddr
	 * @param strTime
	 * @param listener
	 */
	@ApiValue(api=pick_save,params={"cardid","phone","address","time","pushID"},waitingMessage="请稍等...")
	void pulishPickCard(String cardID, String phoneNumber, String strAddr,
			String strTime,String pushID);

	/**
	 * 
	 * @param position
	 * @param listener
	 */
	@ApiArray(api="lost_card_list",clazz=LostCardDetailInfo.class)
	IArrayJsonTask<LostCardDetailInfo> getLostCardList(int position);

	/**
	 * 
	 * @param position
	 * @param listener
	 */
	@ApiArray(api="pick_card_list",clazz=PickCardDetailInfo.class)
	IArrayJsonTask<PickCardDetailInfo> getPickCardList(int position);

	/**
	 * 撤销,已经找回
	 * 
	 * @param listener
	 */
	@ApiValue(api=lost_revocation,params={})
	IValueJsonTask<Object> lostCardRevocation();

	/**
	 * 
	 * @param cardNumber
	 * @param listener
	 */
	@ApiValue(api=lost_publish,params={"cardid"},waitingMessage="正在发布..")
	IValueJsonTask<Object> lostCardPublish(String cardNumber);

	// 保存
	@ApiValue(api=lost_save,params={""},waitingMessage="正在保存...")
	IValueJsonTask<Object> saveLostCardInfo(Map<String, Object> values);

	@ApiObject(clazz=MyLostCardDetailInfo.class,api=my_lost_card_info,params={})
	IObjectJsonTask<MyLostCardDetailInfo> getMyLostCardInfo();

}
