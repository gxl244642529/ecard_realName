package com.damai.core;

import com.damai.pay.DMPayModel;

public class DMLib {
	
	/**
	 * Action
	 */
	public static final String ACTION_PAY_CASHIER="com.damai.action.PAYCASHIER";
	
	
	private static JobManager gJobManager;
	private static DMPayModel gPayModel;
	
	public static DMPayModel getPayModel(){
		return gPayModel;
	}
	
	public static void setPayModel(DMPayModel payModel){
		gPayModel = payModel;
	}
	
	public static JobManager getJobManager(){
		return gJobManager;
	}
	
	public static void setJobManager(JobManager jobManager){
		gJobManager = jobManager;
	}
}
