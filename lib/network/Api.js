
import MD5 from 'crypto-js/md5'
import Account from './Account';
import A from '../Alert';
import keyMirror from 'key-mirror';
import Notifier from '../Notifier'
import Log from '../Log'

import {InteractionManager} from 'react-native'



const ApiModule = require('react-native').NativeModules.ApiModule;

const CommonApi = require('react-native').NativeModules.CommonApi;
const SysModule = require('react-native').NativeModules.SysModule;






const TIME_OUT = 5000;

export const CRYPT_NONE = 0;
export const CRYPT_UPLOAD = 1;
export const CRYPT_DOWNLOAD = 2;
export const CRYPT_BOTH = 3;

export const ERROR_NETWORK = 0;
export const ERROR_SERVER = 1;
export const ERROR_ALERT = 2;
export const ERROR_TOAST = 3;

///缓存策略
export const CachePolicy_NoCache 		= 0;
export const CachePolicy_TimeLimit		= 1;
export const CachePolicy_Permanent		= 2;
export const CachePolicy_UseCacheFirst	= 3;
export const CachePolicy_Offline		= 4;

//任务取消等级
export const CancelLevel_Task  	=0;		//取消本次任务
export const CancelLevel_Page 		=1;		//关闭本页面		默认
export const CancelLevel_NoCancel 	=2;		//不能取消（除非完成或发生错误，否则一直在本页面loading)

//const
export const ApiObject = 0;
export const ApiPage = 1;




const Page = function(json) {
	this.page = json.page;
	this.total = json.total;
	this.list = json.result;
	this.isFirst = function() {
		return this.page <= 1;
	};
	this.isLast = function() {
		return this.page >= this.total;
	};
	this.isEmpty = function() {
		return this.isFirst() && this.list.length <= 0;
	};
};



function defaultShowError(error,errorType){
	A.toast(error);
}

function defaultShowMessage(error,errorType){
	if(errorType == ERROR_TOAST){
		A.toast(error);
	}else{
		A.alert(error);
	}
}


export default class Api{

	static routeCount(){
		return Api.navigator.getCurrentRoutes().length;
	}

	static getRoutes(){
		return Api.navigator.getCurrentRoutes();
	}

	static detail(component,api){
		var success=(data)=>{
			InteractionManager.runAfterInteractions(()=>{
				if(api.name){
					let d = {};
					d[api.name] = data;
					component.setState(d);
				}else{
					component.setState(data);
				}

				api.success && api.success(data);
			});
		}
		Api.api({...api,...{success}});
	}

	/**
	 * 判断指定的url是否存在，如果存在，则跳转至指定的url，
	 * 如果不存在，则先跳转至首页，然后在push到url
	 * @param  {[type]} url [description]
	 * @return {[type]}     [description]
	 */
	static returnTo(url){
		let arr = Api.navigator.getCurrentRoutes();

		if(url == '/' || url === undefined){
			Api.go(-  (arr.length-1) );
			return;
		}

		for(let i= arr.length-1; i >=0; --i){
			let vo = arr[i];
			if(vo.location===undefined){
				break;
			}

			if(vo.location.pathname==url){
				//回到这里
				let index = i-arr.length+1;
				if(index==0){
					return;
				}
				Api.go(index);
				return;
			}
		}
		//没有,将退出到首页，然后在push
		//Api.go(-  (arr.length-1) );
		Api.push(url);
	}

	static exit(){
		ApiModule.exit();
	}

	static push(str,param={}){

		if(str.substring(0,1)=='/'){
			Api.router.push(str);
		}else{
			//调用模块
			SysModule.callModule(str,param);
		}
	}


	static finish(){
		ApiModule.finish();
	}

	static goBack(){
		if(Api.router.canGo(-1)){
			Api.router.go(-1);
		}else{
			ApiModule.finish();
		}
	}

	/**
	 * 返回上一级
	 * @return {[type]} [description]
	 */
	static pop(){
		Api.navigator.pop();
	}

	/**
	 * 替换，尽量少用
	 * @param  {[type]} str [description]
	 * @return {[type]}     [description]
	 */
	static replace(str){
		Api.router.replace(str);
	}

	/**
	 * 跳转
	 * @param  {[type]} n [description]
	 * @return {[type]}   [description]
	 */
	static go(n){
		Api.router.go(n);
	}

	/**
	 * 指定跳转层数，（必须是负数）
	 * 并指定转到到的url
	 * @return {[type]} [description]
	 */
	static goBackAndPush(n,url){
		if(n>0){
			console.warn('层数必须小于0');
			return;
		}
		Api.go(n);
		Api.push(n);
	}

	//waitingMessage
	static api(api){
		let data =  {
			timeoutMs : api.timeoutMs || TIME_OUT,
			api : api.api,
			data:api.data || {},
			cachePolicy:api.cachePolicy || CachePolicy_NoCache,
			cancelLevel: api.cancelLevel || CancelLevel_Page,
			type:api.type || 0,
			crypt: api.crypt || CRYPT_NONE,
			files:api.files || undefined,
			waitingMessage:api.waitingMessage
		}
		let successCallback = api.success;
		console.log("开始调用",api.api,api.data);
		//Log.info("开始调用");
		ApiModule.api(data,(result)=>{
			if(data.type==1){
				result = new Page(result);
			}
			Log.info("收到数据",result);
			Notifier.notifyObservers(api.api,result);
			successCallback && successCallback(result);
		},(error,errorType)=>{
			console.log(error,errorType);
			if(errorType == ERROR_TOAST || errorType == ERROR_ALERT){
				//这个是否是message
				if(api.message && api.message(error,errorType)===true){
					return;
				}
				defaultShowMessage(error,errorType);
			}else{
				console.log(api.error);
				if(api.error && api.error(error,errorType==ERROR_NETWORK)===true){
					return;
				}
				defaultShowError(error,errorType);
			}

		});
	}



}

Api.Page = Page;
