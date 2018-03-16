package com.citywithincity.ecard.insurance.models;

import com.citywithincity.auto.ApiArray;
import com.citywithincity.auto.ApiDetail;
import com.citywithincity.auto.ApiObject;
import com.citywithincity.auto.ApiValue;
import com.citywithincity.auto.Crypt;
import com.citywithincity.ecard.insurance.models.vos.ContactVos;
import com.citywithincity.ecard.insurance.models.vos.InsuranceAdvVo;
import com.citywithincity.ecard.insurance.models.vos.InsuranceClientNotifySuccessVo;
import com.citywithincity.ecard.insurance.models.vos.InsuranceDetailVo;
import com.citywithincity.ecard.insurance.models.vos.InsuranceListVo;
import com.citywithincity.ecard.insurance.models.vos.InsurancePaySuccessNotifyVo;
import com.citywithincity.ecard.insurance.models.vos.InsurancePolicyVo;
import com.citywithincity.ecard.insurance.models.vos.InsuranceRecieverInfoVo;
import com.citywithincity.ecard.insurance.models.vos.InsuranceTypeVo;
import com.citywithincity.ecard.insurance.models.vos.SafeAddressVos;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.interfaces.IDetailJsonTask;
import com.citywithincity.interfaces.IObjectJsonTask;
import com.citywithincity.interfaces.IValueJsonTask;
import com.citywithincity.models.cache.CachePolicy;

public interface InsuranceModel {
	
	//轮播
	String ADV = "i_adv/list";
	
	String LOGIN = "i_safe/login";
	String TYPES = "i_safe/types";
	String LIST = "i_safe/list";
	String DETAIL = "i_safe/detail";
	
	String DETAIL_ERROR = "i_safe/detail_error";
//	保险下单
	
	String PREPAY = "i_safe/prePay";
	
	String MY_LIST = "i_m_safe/list";
	//报失
	String LOST = "i_m_safe/lost";
	
	//检测身份证是否可以购买
	String CHECK_ID_CARD = "i_safe/checkIdCard";
	
	//检测e通卡好是否可以购买
	String CHECK_ECARD = "i_safe/checkECard";
	
	//获取收货人信息
	String RECIEVER_INFO = "i_m_safe/detail";
	
	//资料提交失败，重新提交
	String CLIENT_NOTIFY = "i_safe/clientNotify";
	
	/**
	 * 资料提交失败，重新提交 
	 * error
	 */
	String CLIENT_NOTIFY_ERROR = "i_safe/clientNotify_error";
	
	/**
	 * 验证身份证、e通卡、验证码
	 */
	String CHECK_DATA = "i_safe/checkData";
	
	String CHECK_DATA_ERROR = "i_safe/checkData_error";
	
	//检查其他保险是否可以购买
	String CHECK_DATA_INSURED = "i_safe/checkDataInsured";
	
	String CHECK_DATA_INSURED_ERROR = "i_safe/checkDataInsured_error";
	
	//获取我的联系人
	String INSURANCE_CONTACT_LIST = "i_contact/list";
	
	//删除联系人
	String INSURANCE_CONTACT_DELETE = "i_contact/delete";
	
	//收货人地址
	String INSURANCE_ADDR_LIST = "i_addr/list";
	
	// 删除地址
	String INSURANCE_ADDR_DELETE = "i_addr/delete";
	
	// 增加地址
	String INSURANCE_ADDR_ADD = "i_addr/add";
	
	//未付款订单，支付
	String INSURANCE_PAY_INFO = "i_safe/payInfo";
	
	/**
	 * 获取保险类型
	 */
	@ApiArray(api=TYPES,paged=false,cachePolicy=CachePolicy.CachePolity_NoCache,clazz=InsuranceTypeVo.class,factory=1)
	IArrayJsonTask<InsuranceTypeVo> getTypeList();
	
	/**
	 * 获取列表
	 */
	@ApiArray(api=LIST,params={"typeId"},paged=false,cachePolicy=CachePolicy.CachePolity_NoCache,clazz=InsuranceListVo.class,factory=1)
	IArrayJsonTask<InsuranceListVo> getList(String typeId);
	
	/**
	 * 保险详情
	 * @param id
	 * @return
	 */
	@ApiObject(api=DETAIL,params={"id"},clazz=InsuranceDetailVo.class,cachePolicy=CachePolicy.CachePolity_NoCache,factory=1)
	IObjectJsonTask<InsuranceDetailVo> getDetail(String id);
	
	/**
	 * 我的保单
	 */
	@ApiArray(api=MY_LIST,paged=false,cachePolicy=CachePolicy.CachePolity_NoCache,clazz=InsurancePolicyVo.class,factory=1,isPrivate=true)
	IArrayJsonTask<InsurancePolicyVo> getMyList();

	/**
	 * 报失
	 * @param orderID
	 * @param idCard
	 * @param addr
	 * @param name
	 * @param phone
	 * @return
	 */
	@Crypt(Crypt.UPLOAD)
	@ApiValue(api=LOST,params={"orderId","idCard","addr","name","phone"},factory=1,waitingMessage="请稍后……")
	IValueJsonTask<Boolean> lost(String orderID,String idCard,String addr,String name,String phone);

	/**
	 * 检测身份证是否可以购买
	 */
	@Crypt(Crypt.UPLOAD)
	@ApiValue(api=CHECK_ID_CARD,params={"idCard"},factory=1)
	IValueJsonTask<Boolean> checkIdCard(String idCard);

	/**
	 * 检测e通卡好是否可以购买
	 */
	@ApiValue(api=CHECK_ECARD,params={"cardId"},factory=1)
	IValueJsonTask<Boolean> checkECard(String cardId);
	
	/**
	 * 
	 */
	@Crypt(Crypt.BOTH)
	@ApiDetail(api=CLIENT_NOTIFY,clazz=InsuranceClientNotifySuccessVo.class,params={"id","id_card","phone","addr","name"},factory=1,waitingMessage="请稍后……")
	IDetailJsonTask<InsuranceClientNotifySuccessVo> clientNotify(String id,String idCard,String phone, String addr, String name);
	
	@ApiDetail(api=RECIEVER_INFO,params={"orderId"},clazz=InsuranceRecieverInfoVo.class,factory=1,cachePolicy=CachePolicy.CachePolity_NoCache)
	IDetailJsonTask<InsuranceRecieverInfoVo> getRecieverInfo(String orderId);

	/**
	 * 获取保险广告
	 * @param position
	 * @return
	 */
	@ApiArray(api=ADV,clazz=InsuranceAdvVo.class,paged=false,factory=1)
	IArrayJsonTask<InsuranceAdvVo> getAdvList(int position);
	
	/**
	 * 验证身份证、e通卡、验证码
	 * @param cardId
	 * @param ticket
	 * @param idCard
	 * @return
	 */
	@Crypt(Crypt.UPLOAD)
	@ApiValue(api=CHECK_DATA,params={"cardId","ticket","idCard"},factory=1)
	IValueJsonTask<Boolean> checkData(String cardId,String ticket,String idCard);
	
	/**
	 * 检查是否可以购买
	 * @param idCard	身份证
	 * @param inId		保险id
	 * @param count		购买份数
	 * @return
	 */
	@Crypt(Crypt.BOTH)
	@ApiValue(api=CHECK_DATA_INSURED,params={"idCard","inId","count"},factory=1)
	IValueJsonTask<Boolean> checkDataInsured(String idCard,String inId,int count);
	
	/**
	 * 获取我的联系人
	 */
	@ApiArray(api=INSURANCE_CONTACT_LIST,paged=false,cachePolicy=CachePolicy.CachePolity_NoCache,clazz=ContactVos.class,factory=1,isPrivate=true)
	IArrayJsonTask<ContactVos> getContactList();
	
	/**
	 * 删除联系人
	 */
	@ApiValue(api=INSURANCE_CONTACT_DELETE,params={"id"},factory=1)
	IValueJsonTask<Boolean> deleteContace(long id);
	
	/**
	 * 收货人地址
	 */
	@ApiArray(api=INSURANCE_ADDR_LIST,paged=false,cachePolicy=CachePolicy.CachePolity_NoCache,clazz=SafeAddressVos.class,factory=1,isPrivate=true)
	IArrayJsonTask<SafeAddressVos> getAddrList();
	
	/**
	 * 删除地址
	 */
	@ApiValue(api=INSURANCE_ADDR_DELETE,params={"id"},factory=1)
	IValueJsonTask<Boolean> deleteInsuranceId(long id);

	/**
	 * 增加地址
	 */
	@ApiValue(api=INSURANCE_ADDR_DELETE,params={"sheng","shengId","shi","shiId","qu","quId","addr"},factory=1)
	IValueJsonTask<Long> addInsuranceId(String sheng,int shengId,String shi, int shiId,String qu,int quId,String addr);
	
	/**
	 * 未付款订单，支付
	 * @param orderId
	 * @return
	 */
	@Crypt(Crypt.BOTH)
	@ApiDetail(api=INSURANCE_PAY_INFO,params={"piccOrderId"},clazz=InsurancePaySuccessNotifyVo.class,factory=1,cachePolicy=CachePolicy.CachePolity_NoCache)
	IDetailJsonTask<InsurancePaySuccessNotifyVo> getPayInfo(String orderId);
	
}
