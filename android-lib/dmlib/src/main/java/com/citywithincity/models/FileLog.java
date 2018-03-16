package com.citywithincity.models;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

import com.citywithincity.interfaces.ILogImpl;
import com.citywithincity.utils.FileLogUtil;
import com.citywithincity.utils.IoUtil;

public class FileLog implements ILogImpl {
	
	private String tag;
	private SimpleDateFormat df;
	private Context context;
	
	public FileLog(String tag,Context context) {
		this.tag = tag;
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		this.context = context;
	}

	@Override
	public void info(String message, Throwable error) {
		if (message != null) {
			String content = String.format("[%s]   %s   %s", df.format(new Date()),tag,message);
			try {
				IoUtil.writeToFile(content, FileLogUtil.getInfoLogFile(context));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (error != null) {
			try {
				IoUtil.writeToFile(error.toString(), FileLogUtil.getInfoLogFile(context));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void error(String message, Throwable error) {
		if (message != null) {
			String content = String.format("[%s]   %s   %s", df.format(new Date()),tag,message);
			try {
				IoUtil.writeToFile(content, FileLogUtil.getErrLogFile(context));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (error != null) {
			try {
				IoUtil.writeToFile(error.toString(), FileLogUtil.getErrLogFile(context));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
}
