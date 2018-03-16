package com.citywithincity.ecard.models;

import com.citywithincity.auto.ApiArray;
import com.citywithincity.auto.ApiDetail;
import com.citywithincity.auto.ApiValue;
import com.citywithincity.ecard.interfaces.IECardPlatform.MyCouponInfo;
import com.citywithincity.ecard.models.vos.ActionListVo;
import com.citywithincity.ecard.models.vos.BusinessDetail;
import com.citywithincity.ecard.models.vos.CouponDetail;
import com.citywithincity.ecard.models.vos.CouponInfo;
import com.citywithincity.ecard.models.vos.NewsInfo;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.models.cache.CachePolicy;

/**
 * e通卡相关内容管理
 * 
 * @author Randy
 * 
 */
public interface ECardContentModel {

	String ADD_FAV = "add_fav";

	String MY_COUPON_LIST = "fcoupon_list";
	String COUPON_DETAIL = "coupon_detail";
	// 优惠券
	String COUPON_LIST = "coupon_list";

	// 新闻列表
	String NEWS_LIST = "news_list";

	String BUSINESS_DETAIL = "business_detail2";
	
	/**
	 * 活动专区
	 */
	String ACTION_LIST = "action_list";

	/**
	 * 添加收藏
	 */

	/**
	 * 
	 * @param bsID
	 * @param type
	 *            1:优惠券 2:商家
	 * @param add
	 *            1:增加 2:减少
	 * @param listener
	 */
	@ApiValue(api = ADD_FAV, params = { "id" ,"type" ,"action" })
	void addBusinessCollection(int bsID, int type, int add);

	@ApiValue(api = ADD_FAV, params = { "id" })
	void addCouponCollection(CouponInfo info, boolean add);

	/**
	 * 我的优惠券，收藏
	 * 
	 * @param position
	 * @param last
	 * @param listener
	 */
	@ApiArray(api = MY_COUPON_LIST, isPrivate = true, clazz = MyCouponInfo.class)
	void getMyCouponList(int position);

	/**
	 * 获取新闻列表
	 */
	@ApiArray(api = NEWS_LIST, clazz = NewsInfo.class)
	IArrayJsonTask<NewsInfo> getNewsList(int position);

	/**
	 * 优惠券列表
	 */
	@ApiArray(api = COUPON_LIST, clazz = CouponInfo.class, params = { "type",
			"area" })
	IArrayJsonTask<CouponInfo> getCouponList(int position, int type, int area);

	@ApiDetail(api = COUPON_DETAIL, clazz = CouponDetail.class,params = {
			"id", "userID" })
	void getCouponDetail(int couponID, int userID);

	@ApiDetail(api = BUSINESS_DETAIL, clazz = BusinessDetail.class, params = {
			"id", "shop", "userID","last" })
	void getBusinessDetail(int id, int shop, int userID,int last);
	
	/**
	 * 活动专区
	 */
	@ApiArray(api = ACTION_LIST, clazz = ActionListVo.class,cachePolicy = CachePolicy.CachePolicy_TimeLimit)
	IArrayJsonTask<ActionListVo> getActionList(int position);

}
