package com.damai.auto;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.content.Context;

import com.citywithincity.models.LocalData;
import com.citywithincity.utils.PackageUtil;
import com.damai.core.DMAccount;
import com.damai.core.DMLib;
import com.damai.core.DMServers;
import com.damai.core.JavaServerHandler;
import com.damai.core.JobManagerImpl;
import com.damai.core.PhpServerHandler;
import com.damai.pay.DMPayFactory;
import com.damai.push.IPush;
import com.damai.push.Push;

public abstract class DMApplication  extends Application implements DMPayFactory, UncaughtExceptionHandler {
	private JobManagerImpl jobManager;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate() {
		super.onCreate();

		Push.setPush(createPush());

		Thread.setDefaultUncaughtExceptionHandler(this);


		jobManager = new JobManagerImpl();
		DMLib.setJobManager(jobManager);
		jobManager.setPayFactory(this);
		Context context = getApplicationContext();

		if(!registerApiServerHandler(jobManager,context)){
			jobManager.registerApiServerHandler(0, new PhpServerHandler(context));
			jobManager.registerApiServerHandler(1, new JavaServerHandler(context));

			//这里加载url
			DMServers.setUrl(0,PackageUtil.getMetaValue(context, "PHP_URL"));
			DMServers.setUrl(1,PackageUtil.getMetaValue(context, "JAVA_URL"));
		}



		String clazzString = PackageUtil.getMetaValue(context, "UserClass");
		Class<? extends DMAccount> clazz;
		try {
			clazz = (Class<? extends DMAccount>) Class.forName(clazzString);
			DMAccount.setClass(clazz);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		jobManager.setWXId(PackageUtil.getMetaValue(context, "WXID"));
		initApplication();
		jobManager.startup(getApplicationContext());
		LocalData.setContext(getApplicationContext());

	}

    protected abstract IPush createPush();

    protected abstract boolean registerApiServerHandler(JobManagerImpl jobManager,Context context);

	protected abstract void initApplication() ;





}
