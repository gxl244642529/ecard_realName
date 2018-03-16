package com.citywithincity.widget.data;

import java.util.List;


public class MenuData {

	/**
	 * 在下拉菜单中的下标
	 */
	public int index;
	
	/**
	 * 是否被选中
	 */
	public boolean selected;
	
	/**
	 * 服务器id
	 */
	public int id;
	
	/**
	 * 菜单上面显示的
	 */
	public String label;
	
	/**
	 * 顶部菜单显示的
	 * 如果为空，则显示label
	 */
	public String topLabel;
	
	/**
	 * 实际数据,一般用于接口
	 */
	public Object data;
	
	/**
	 * 如果大于0，表示图标id
	 */
	public int imageRes;

	
	
	
	public List<MenuData> children;
	
}
