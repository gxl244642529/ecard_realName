package com.citywithincity.interfaces;

public interface ILog extends ILogImpl{
	/**
	 * 级别
	 */
	public static final int Level_Info = 0;
	public static final int Level_Error = 1;
	
	/**
	 * Type_Standard | Type_StdOut
	 */
	public static final int Type_Standard=		0x1;
	public static final int Type_StdOut=		0x2;
	public static final int Type_File=			0x4;
	

	void info(String message);
	void info(Throwable error);
	void error(String message);
	void error(Throwable error);
	
}
