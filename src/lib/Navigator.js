
import PayUtil from '../../lib/PayUtil'
import {Api} from '../../lib/Common'

const SysModule = require('react-native').NativeModules.SysModule;


export const PAY_ALIPAY = 1;     //支付宝
export const PAY_WEIXIN = 2;         //微信
export const PAY_ETONGKA = 3;        //e通卡
export const PAY_UNION = 4;          //银行卡
export const PAY_CMB = 5;            //招商银行




const Navigator = {

	pay:(orderId,fee,moduleId,types,success)=>{
		PayUtil.pay(orderId,fee,moduleId,types,success);
	},

	setBlackStyle:()=>{
		SysModule.setBlackStyle();
	},

	setWhiteStyle:()=>{
		SysModule.setWhiteStyle();
	},

	create:(moduleId,types)=>{
		PayUtil.create(moduleId,types);
	},

	setIndex:(index)=>{
		PayUtil.setIndex(index);
	},

	call:(api,data,callback)=>{
		PayUtil.call(api,data,callback);
	},

	destroy:()=>{
		PayUtil.destroy();
	},
	onStartup:()=>{
		SysModule.onStartup();
	},
	setFirstRead:()=>{
		SysModule.setFirstRead();
	},
	setCallback:(callback)=>{
		PayUtil.setCallback(callback);
	}
};

export default Navigator;