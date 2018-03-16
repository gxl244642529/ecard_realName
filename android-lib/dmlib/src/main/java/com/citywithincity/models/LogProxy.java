package com.citywithincity.models;

import java.util.List;

import com.citywithincity.interfaces.ILog;
import com.citywithincity.interfaces.ILogImpl;

/**
 * log的配置
 * @author Randy
 *
 */
public class LogProxy implements ILog {
	private ILogImpl[] logs;
	private int level;
	public LogProxy(List<ILogImpl> list,int level){
		logs = new ILogImpl[list.size()];
		list.toArray(logs);
		this.level = level;
	}
	
	@Override
	public void info(String message) {
		info(message,null);
	}
	@Override
	public void info(Throwable error) {
		info(null,error);
	}

	@Override
	public void info(String message, Throwable error) {
		if(level >= ILog.Level_Info){
			for (ILogImpl log : logs) {
				log.info(message, error);
			}
		}
	}
	@Override
	public void error(String error) {
		error(error,null);
	}

	@Override
	public void error(Throwable error) {
		error(null,error);
	}

	@Override
	public void error(String message, Throwable error) {
		if(level >= ILog.Level_Error){
			for (ILogImpl log : logs) {
				log.error(message, error);
			}
		}
	}

	

	
}
