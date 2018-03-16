
const OrderStatus={
	/**
	 * 没有支付
	 */
	OrderStatus_NoPay : 0,
	/**
	 * 交易成功
	 */
	OrderStatus_Success : 1,
	/**
	 * 已经退款
	 */
	OrderStatus_Refund : 2,
	
	
	/**
	 * 正在提交，表示系统已经收到通知，进入提交状态
	 */
	OrderStatus_Submiting : 11,
	/**
	 * 由于用户信息提交失败导致的错误(系统错误或者用户提交信息错误，不可恢复）
	 */
	OrderStatus_SubmitError : 12,
	/**
	 * 进入退款,表示用户有提交对款
	 */
	OrderStatus_Refunding : 13,
	/**
	 * 取消任务失败(系统错误导致，不可恢复）
	 */
	OrderStatus_CancelOrderError : 14,
	/**
	 * 取消任务成功
	 */
	OrderStatus_CancelOrderSuccess : 15,
	/**
	 * 退款失败
	 */
	OrderStatus_RefundError : 16,
};

module.exports = OrderStatus;
	
	