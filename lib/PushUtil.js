import A from './Alert'
import Api from './network/Api'
import Notifier from './Notifier'
import Account from './network/Account'


const PushModule = require('react-native').NativeModules.PushModule;
const AccountModule = require('react-native').NativeModules.AccountModule;


//单点登录
const TYPE_SINGLE_LOGIN = 1;
//实名
const REAL_VERIFY_OK = 101;//app实名审核通过
const REAL_VERIFY_ERROR = 102;//app实名审核失败

const REAL_SINGLE_PAY_SUCCESS = 107;//银行卡打款成功
const  REAL_SINGLE_PAY_ERROR  = 108;//银行卡打款失败

const LOST_FUND_ERROR = 103;//挂失打款失败
const LOST_FUND_OK = 104;	//挂失打款成功
const LOST_FUND_ERROR_LOCK = 105;//挂失打款失败，余额不足
const LOST_FUNDING = 106;//挂失打款中

//实名化二期
const  DB_ORDER_SUCCESS  = 109;//补登验证绑卡成功
const DB_ORDER_CANEL = 110; //补登验证绑卡订单取消
const DB_ORDER_ERROR = 111; //卟噔绑卡失败

//扫码
const BUS_OK = 201;				//上车成功
const OUT_FUND_OK = 202;			//出金成功
const OUT_FUND_ERROR = 203;		//出金失败

//电子发票

const TICKET_SUCCESS = 302;//开票成功
const TICKET_RED_SUCCEED = 303;//红冲成功
const TICKET_ERROR = 304;//开票失败

//电子发票推送链接地址
// const TICKET_BASE_URL = "http://www.cczcc.net/index.php/test_ticket/index?userid=";
// const  GO_URL= "billHistory"



export const getPushId=(success)=>{
	PushModule.getPushId(success);
}


export const openMessage = (data,isReturn)=>{
	console.log(data.type);
	let url = null;
	// let ticketData = {base_url:TICKET_BASE_URL,go_url:GO_URL};
	try{
		let type = data.type;
		switch(type){
		case TYPE_SINGLE_LOGIN:
			A.alert("您的账号在其他设备登录，如果不是您的设备，请及时更换密码");
			AccountModule.logout();
			url = "/login";
			break;
		case REAL_VERIFY_OK:
			console.log("REAL_VERIFY_OK");
			Notifier.notifyObservers('verify');
			url = ('/realName/overTrans');
			break;
		case REAL_VERIFY_ERROR:
			Notifier.notifyObservers('verify');
			url = ('/realName/overTrans');
			break;
		case REAL_SINGLE_PAY_SUCCESS:
			Notifier.notifyObservers('verify');
			url = ('/realName/overTrans');
			break;
		case REAL_SINGLE_PAY_ERROR:
			Notifier.notifyObservers('verify');
			url = ('/realName/overTrans');
			break;
		case LOST_FUND_ERROR:
			Notifier.notifyObservers('verifyInfo');
			// url = ('/realName/rCardDetail');//0513
			url = ('/realName/rCardDetail/'+data.extras.cardId);
			break;
		case LOST_FUND_OK:
			Notifier.notifyObservers('verifyInfo');
			console.log(data.cardId);
			// url = ('/realName/rCardDetail');
			url = ('/realName/rCardDetail/'+data.extras.cardId);
			break;
		case LOST_FUNDING:
			Notifier.notifyObservers('verifyInfo');
			// console.log(data.cardId+ data);
			// url = ('/realName/rCardDetail');
			url = ('/realName/rCardDetail/'+data.extras.cardId);
			break;
		case LOST_FUND_ERROR_LOCK:
			Notifier.notifyObservers('delivery');
			// url = ('/repair/detail/'+data.id);
			url = ('/realName/rCardDetail/'+data.extras.cardId);
			break;
		case DB_ORDER_SUCCESS:
			Notifier.notifyObservers('delivery');
			// url = ('/repair/detail/'+data.id);
			// url = ('/realName/rCardList');
			url = ('/realName/rCard')
			break;
		case DB_ORDER_CANEL:
			Notifier.notifyObservers('delivery');
			// url = ('/repair/detail/'+data.id);
			// url = ('/realName/rCardList');
			url = ('/realName/rCard')
			break;
		case DB_ORDER_ERROR:
			Notifier.notifyObservers('delivery');
			// url = ('/repair/detail/'+data.id);
			// url = ('/realName/rCardList');
				url = ('/realName/rCard')
			break;
		case OUT_FUND_OK:

			url = ('/busqr/qrReturnUrl/0');
			break;
		case OUT_FUND_ERROR:

			url = ('/busqr/qrReturnUrl/0');
			break;
		case BUS_OK:

			url = ('/busqr/bus_record');
			break;

		case TICKET_SUCCESS:

			url = ('/ticketurl');
			console.log(url)
			break;
		case TICKET_RED_SUCCEED:

			url = ('/ticketurl');
			break;
		case TICKET_ERROR:
			console.log("开票失败")
			url = ('/ticketurl');
			console.log(url);
			break;

		}


		if(isReturn){
			Api.returnTo(url);
		}else{
			Api.push(url);
		}
	}catch(e){

	}
}
