package com.citywithincity.models;

import java.io.Serializable;

 public class FileObject implements Comparable<FileObject>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -542931038414580068L;

	/**
	 * 路径
	 */
	public String path;
	
	/**
	 * 最后更新时间
	 */
	public long lastModified;
	
	//创建时间
	public long createTime;

	@Override
	public int compareTo(FileObject another) {
		/**
		 * 最新的排在前面
		 */
		if(this.lastModified > another.lastModified)return 1;
		return -1;
	}
	
	
}