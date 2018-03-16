package com.citywithincity.ecard.selling.models;

import com.citywithincity.auto.ApiArray;
import com.citywithincity.ecard.selling.models.vos.SDiyPagesVo;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.models.cache.CachePolicy;


/**
 * diy模型 
 * @author Randy
 *
 */
public interface DiyModel {

	int MIN_CARD_WIDTH = 1016;
	int MIN_CARD_HEIGHT = 638;
	
	//背面图
	String LIST = "s_diy_pages2";
	
	@ApiArray(api=LIST,clazz=SDiyPagesVo.class,cachePolicy=CachePolicy.CachePolicy_TimeLimit,waitingMessage="正在加载背面图片……")
	IArrayJsonTask<SDiyPagesVo> getList(int position);
}
