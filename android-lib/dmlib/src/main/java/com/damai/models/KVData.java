package com.damai.models;

/**
 * 表示一个数据
 * @author renxueliang
 *
 * @param <T>
 */
public class KVData {
	
	private String label;
	private Object data;
	
	public KVData(Object data,String label){
		this.data = data;
		this.label = label;
	}
	
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

}
