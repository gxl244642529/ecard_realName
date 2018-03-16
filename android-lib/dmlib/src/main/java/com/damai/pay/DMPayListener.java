package com.damai.pay;


public interface DMPayListener {
	/**
	 * 拉取订单成功
	 * @param model
	 * @param data
     */
	void onPaySuccess(DMPayModel model,Object data);

    /**
     * 客户端支付成功
     * 如果返回true，则不做处理
     * @param model
     */
    boolean onClientPaySuccess(DMPayModel model,int type,Object data);

    /**
     * 支付取消
     * @param model
     */
	void onPayCancel(DMPayModel model);


    /**
     * 拉取订单失败
     * @param model
     * @param error
     * @param isNetworkError
     * @return
     */
	boolean onGetPayInfoError(DMPayModel model,String error,boolean isNetworkError);

    /**
     * 预支付失败
     * @param reason
     * @param isNetworkError
     * @return
     */
	boolean onPrePayError(String reason, boolean isNetworkError);

    /**
     * 支付失败
     * @param type
     * @param error
     * @param isNetworkError
     */
    void onPayError(int type, String error, boolean isNetworkError);
}
