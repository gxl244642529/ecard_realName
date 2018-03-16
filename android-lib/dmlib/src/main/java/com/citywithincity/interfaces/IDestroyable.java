package com.citywithincity.interfaces;

/**
 * 可销毁
 * @author Randy
 *
 */
public interface IDestroyable {
	
	/**
	 * 销毁对象,释放内存
	 * 如果是图形，移除所有必要子对象，清空引用
	 * 如果是数据处理，取消所有异步处理过程，清空引用
	 */
	void destroy();
}
