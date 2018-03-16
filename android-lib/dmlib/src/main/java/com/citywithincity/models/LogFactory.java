package com.citywithincity.models;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.citywithincity.interfaces.ILog;
import com.citywithincity.interfaces.ILogImpl;

/**
 * 
 * @author Randy
 * 
 */
public class LogFactory {
	public static class LogConfig {
		private int level;
		private int type;
		// private String format = "[yy-MM-dd HH:mm:ss]";
		private String path;
	}

	private static int level;
	private static int type;
	private static Context context;

	public static ILog getLog(Class<?> clazz) {
		// (type & ILog.Type_Standard)
		return getLog(clazz.getSimpleName());
	}

	public static ILog getLog(String name) {
		List<ILogImpl> logs = new ArrayList<ILogImpl>();
		if ((type & ILog.Type_Standard) > 0) {
			logs.add(new AndroidLog(name));
		}
		if ((type & ILog.Type_File) > 0) {
			logs.add(new FileLog(name, context));
		}
		if ((type & ILog.Type_StdOut) > 0) {
			logs.add(new StdOutLog());
		}
		return new LogProxy(logs,level);
	}

	public static void setConfig(int l, int t,Context context) {
		level = l;
		type = t;
		LogFactory.context = context;
	}
}
