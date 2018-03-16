package com.citywithincity.ecard.selling.models;

import com.citywithincity.auto.ApiArray;
import com.citywithincity.auto.ApiValue;
import com.citywithincity.ecard.selling.models.vos.SCartListVo;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.models.cache.CachePolicy;

import java.util.List;

/**
 * 购物车相关的模型
 * @author Randy
 *
 */
public interface CartModel {

	String LIST = "s_cart_list";
	
	String LIST_ERROR = "s_cart_list_error";
	
	String ADD = "s_cart_add";
	String UPDATE = "s_cart_update";
	
	String DELETE = "s_cart_del";
	

	@ApiArray(api=LIST,clazz=SCartListVo.class,cachePolicy=CachePolicy.CachePolity_NoCache)
	IArrayJsonTask<SCartListVo> getList(int position);
	
	@ApiValue(api=DELETE,params={"list"})
	void delete(List<Integer> list);
	
	@ApiValue(api=ADD,params={"id","count","recharge","file"},waitingMessage = "正在提交...")
	void add(long id, int count, int recharge, byte[] file);
	
	@ApiValue(api=UPDATE,params={"id","count","recharge"})
	void update(int id,int count,int recharge);
}
