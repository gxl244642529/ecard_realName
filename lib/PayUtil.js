
const PayModule = require('react-native').NativeModules.PayModule;




export default class PayUtil{


	static PAY_ALIPAY = 1;     //支付宝
	static PAY_WEIXIN = 2;         //微信
	static PAY_ETONGKA = 3;        //e通卡
	static PAY_UNION = 4;          //银行卡
	static PAY_CMB = 5;            //招商银行


	static GetOrderInfoSuccess = 0;
	static PayCancel = 1;
	static GetOrderInfoError = 2;
	static PrePayError = 3;
	static PrePaySuccess = 4;
	static ClinetPaySuccess = 5;
	static ClientPayError = 6;
	

	/**
	 *   GetOrderInfoSuccess,      //拉取订单成功
  PayCancel,                //支付取消
  GetOrderInfoError,        //支付错误
  PrePayError,              //预支付错误
  PrePaySuccess,            //预支付成功
  ClinetPaySuccess,         //客户端支付成功
  ClientPayError            //客户端支付失败
	 * 					   
	 * 
	 * @param  {[type]}   orderId  [description]
	 * @param  {[type]}   fee      [description]
	 * @param  {[type]}   moduleId [description]
	 * @param  {[type]}   types    [description]
	 * @param  {Function} callback [description]
	 * @return {[type]}            [description]
	 */
	static pay(orderId,fee,moduleId,types,callback){
		PayModule.pay(orderId,fee,moduleId,types,callback);
	}


	static getPayInfo(callback){
		PayModule.getPayInfo(callback);
	}	

	static create(moduleId,types){
		PayModule.create(moduleId,types);
	}

	static setIndex(index){
		PayModule.setIndex(index);
	}

	/**
	 *   GetOrderInfoSuccess,      //拉取订单成功
  PayCancel,                //支付取消
  GetOrderInfoError,        //支付错误
  PrePayError,              //预支付错误
  PrePaySuccess,            //预支付成功
  ClinetPaySuccess,         //客户端支付成功
  ClientPayError            //客户端支付失败
	 * @param  {[type]}   api      [description]
	 * @param  {[type]}   data     [description]
	 * @param  {Function} callback [description]
	 * @return {[type]}            [description]
	 */
	static call(api,data,callback){
		PayModule.call(api,data,callback);
	}
	
	static destroy(){
		PayModule.destroy();
	}

	static setCallback(callback){
		PayModule.setCallback(callback);
	}
}