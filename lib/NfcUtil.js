
const NfcModule = require('react-native').NativeModules.NfcModule;

import Api from './network/Api'

export default class NfcUtil{

	static handleSelf(){
		NfcModule.handleSelf();
	}

	static readCard(callback){
		NfcModule.readCard(callback);
	}


	static hasNfc(){
		return Api.nfc !== undefined;
	}
	static isAvailable(callback){
		return NfcModule.isAvailable(callback);
	}

	/**
	 * 吥噔
	 * @param  {[type]} order [description]
	 * @return {[type]}       [description]
	 */
	static recharge(order){
		NfcModule.recharge(order.tyId,order.cardId,order.fee,order.cmAcc);
	}

}
