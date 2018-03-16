package com.damai.core;


/**
 * 下载后的数据格式,影响的是调用哪个解析器进行解析
 * @author Randy
 *
 */
public class DataTypes {
	public static final int DataType_Image = 0;
	public static final int DataType_Json = 1;
	public static final int DataType_Text = 2;
	public static final int DataType_File = 3;
	
	//////////////////////////
	public static final int DataType_ApiArray = 4;
	public static final int DataType_ApiObject = 5;
	public static final int DataType_ApiPage = 6;
	
	
	public static final int DataType_Max = 7;
}
