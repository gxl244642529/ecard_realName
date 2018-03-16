package com.citywithincity.models;

import com.citywithincity.interfaces.ILogImpl;

/**
 * 标准输出log
 * @author Randy
 *
 */
public class StdOutLog implements ILogImpl {

	@Override
	public void info(String message, Throwable error) {
		if(message!=null){
			System.out.println(message);
		}
		if(error!=null){
			error.printStackTrace(System.out);
		}
		
	}

	@Override
	public void error(String message, Throwable error) {
		if(message!=null){
			System.err.println(message);
		}
		if(error!=null){
			error.printStackTrace(System.err);
		}
	}

	

}
