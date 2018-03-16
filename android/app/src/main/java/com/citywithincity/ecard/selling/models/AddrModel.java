package com.citywithincity.ecard.selling.models;

import com.citywithincity.auto.ApiArray;
import com.citywithincity.auto.ApiValue;
import com.citywithincity.ecard.selling.models.vos.SAddrListVo;
import com.citywithincity.interfaces.IArrayJsonTask;

import java.util.Map;

/**
 * 我的收货地址
 * @author Randy
 *
 */
public interface AddrModel {
	

	String LIST = "s_addr_list3";
	String ADD = "s_addr_add4";
	String EDIT = "s_addr_edit3";
	String UPDATE = "s_addr_edit3";
	
	String DELETE = "s_addr_del3";
	
	@ApiArray(api=LIST,clazz=SAddrListVo.class,isPrivate=true,waitingMessage="请稍后，正在加载地址……")
	IArrayJsonTask<SAddrListVo> getList(int position);
	
	@ApiValue(api=DELETE,params={"id"})
	void delete(int id);
	
	@ApiValue(api=ADD,params={""})
	void add(Map<String, Object> data);
	
	@ApiValue(api=EDIT,params={"id",""})
	void update(int id,Map<String, Object> data);
	
}
