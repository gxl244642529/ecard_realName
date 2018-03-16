package com.damai.note;

import java.lang.reflect.Method;
import java.util.List;

import com.citywithincity.interfaces.IViewContainer;

/**
 * 
 * @author renxueliang
 *
 */
public interface IMethodParser {
	/**
	 * 
	 * @param method
	 * @return
	 */
	void getMethodInfo(Method method,IViewContainer container,List<ClsInfo> list);

	void beginParse(IViewContainer observer);

	void endParse(IViewContainer observer);
	
	
	
}
