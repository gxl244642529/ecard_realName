package com.citywithincity.ecard.selling.models;

import com.citywithincity.auto.ApiArray;
import com.citywithincity.auto.ApiDetail;
import com.citywithincity.auto.ApiValue;
import com.citywithincity.ecard.selling.models.vos.SCardDetailVo;
import com.citywithincity.ecard.selling.models.vos.SCardListVo;
import com.citywithincity.ecard.selling.models.vos.SCardTypeVo;
import com.citywithincity.ecard.selling.models.vos.SDiyListVo;
import com.citywithincity.ecard.selling.models.vos.SDiyPagesVo;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.interfaces.IDetailJsonTask;
import com.citywithincity.models.cache.CachePolicy;

/**
 * 售卡相关的模型
 * @author Randy
 *
 */
public interface ShopModel {
	/**
	 * 售卡类别
	 */
	String CARD_TYPE = "s_card_type2";
	String CARD_LIST = "s_card_list2";
	String CARD_DETAIL = "s_card_detail2";
	//优秀作品列表
	String DIY_EXCELLENT_LIST = "s_diy_list";
	//卡面
	String DIY_PAGES = "s_diy_pages2";
	//收藏
	String COLLECTION = "s_fav_add3";
	//取消收藏
	String CANCLE_COLLECTION = "s_fav_del3";
	
	//收藏列表
	String COLLECTION_LIST = "s_fav_list3";
	
	
	/**
	 * 售卡分类列表
	 */
	@ApiArray(api=CARD_TYPE,clazz=SCardTypeVo.class,paged=false)
	void getSCardType();
	
	/**
	 * 售卡列表
	 * @param position
	 * @param type
	 * @param order
	 */
	@ApiArray(api=CARD_LIST,clazz=SCardListVo.class,params={"type","order"})
	IArrayJsonTask<SCardListVo> getSCardList(int position, int type, String order);
	
	/**
	 * 售卡详情
	 * @param id
	 */
	@ApiDetail(api=CARD_DETAIL,clazz=SCardDetailVo.class,params={"id"},cachePolicy=CachePolicy.CachePolity_NoCache)
	IDetailJsonTask<SCardDetailVo> getSCardDetail(int id);
	
	/**
	 * 在线优秀作品列表
	 * @param position
	 */
	@ApiArray(api=DIY_EXCELLENT_LIST,clazz=SDiyListVo.class)
	IArrayJsonTask<SDiyListVo> getSDiyList(int position);
	
	/**
	 * diy卡背面列表
	 */
	@ApiArray(api=DIY_PAGES,clazz=SDiyPagesVo.class)
	void getSDiyPages(int position);
	
	
	/**
	 * 收藏
	 * @param id
	 */
	@ApiValue(api=COLLECTION,params={"id"})
	void collection(int id);
	
	/**
	 * 取消收藏
	 * @param id
	 */
	@ApiValue(api=CANCLE_COLLECTION,params={"id"})
	void cancleCollection(int id);
	
	/**
	 * 收藏列表 
	 * @param position
	 * @return
	 */
	@ApiArray(api=COLLECTION_LIST,clazz=SCardListVo.class)
	IArrayJsonTask<SCardListVo> getCollectionList(int position);
	
	
	
}
