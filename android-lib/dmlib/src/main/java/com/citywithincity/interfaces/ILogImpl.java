package com.citywithincity.interfaces;

public interface ILogImpl {
	void info(String message,Throwable error);
	void error(String message,Throwable error);
}
