package com.citywithincity.ecard.models;

import com.citywithincity.auto.ApiArray;
import com.citywithincity.auto.ApiDetail;
import com.citywithincity.auto.ApiObject;
import com.citywithincity.auto.ApiValue;
import com.citywithincity.auto.Crypt;
import com.citywithincity.ecard.models.vos.ECardDetail;
import com.citywithincity.ecard.models.vos.ECardInfo;
import com.citywithincity.ecard.models.vos.RealInfoVo;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.interfaces.IDetailJsonTask;
import com.citywithincity.interfaces.IObjectJsonTask;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.models.vos.Null;

public interface MyECardModel {

	String ECARD_BARCODE = "ecard_barcode";
	
	String ECARD_BARCODE_ERROR = "ecard_barcode_error";
	
	String ECARD_LIST = "ecard_list2";
	String ECARD_DETAIL = "ecard_detail";
	String ECARD_UNBIND = "ecard_unbind";
	String ECARD_BIND = "ecard_bind2";
	String ECARD_RENAME = "ecard_rename";
	
	String REAL_INFO = "user/realInfo";

	/**
	 * 新名称
	 */
	String NAME_HAS_CHANGED = "ecardNameHasChanged";
	
	
	

	
	/**
	 * 绑定e通卡
	 * @param cardNumber
	 * @param name
	 * @param pretypey
	 */
	@ApiValue(api=ECARD_BIND,params={"cardid","name","pretype","last"},waitingMessage="正在绑卡...")
	void bindECard(String cardNumber, String name, int pretypey,int last);

	@ApiValue(api=ECARD_UNBIND,params={"card"})
	void unbindECard(String cardNumber);

	@ApiValue(api=ECARD_RENAME,params={"card","name"},waitingMessage="请稍等...")
	void renameCard(String cardNumber, String cardName);

	
	@ApiValue(api=ECARD_BARCODE,params={"code"})
	void bindByBarcode(String code);
	
	
	@ApiArray(api=ECARD_LIST,clazz=ECardInfo.class,isPrivate=true,paged=false)
	IArrayJsonTask<ECardInfo> getList();
	
	@ApiObject(api="ecard_bind_member",clazz=Null.class,params={"card","name","sex","phone","idcode","address"})
	void updateMemberInfo(String cardId,String name, int sex, String phone,
				String idCode,String address);

	@ApiDetail(api=ECARD_DETAIL,clazz=ECardDetail.class,params={"cardid"})
	IDetailJsonTask<ECardDetail> getDetail(String cardNumber);
	
	/**
	 * 获取用户实名信息
	 * @return
	 */
	@Crypt(value=Crypt.DOWNLOAD)
	@ApiObject(api=REAL_INFO,clazz=RealInfoVo.class,params={},cachePolicy=CachePolicy.CachePolity_NoCache,factory=1)
	IObjectJsonTask<RealInfoVo> getRealInfo();

}
