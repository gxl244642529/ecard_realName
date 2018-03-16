
import Api from './network/Api'
import Account from './network/Account'
import {RoleTaxi,RoleBusiness} from './Roles'
import A from './Alert'

export const loginSuccess = "loginSuccess";
export const logoutSuccess = "logoutSuccess";//登出之后
export const loginCancel = "loginCancel";

/**
loginSuccess
loginCancel
*/

const AccountModule = require('react-native').NativeModules.AccountModule;


export function requireAuth(nextState, replaceState){
  if (!isLogin()) {
  	replaceState('/login');
  }
}

export function login(callback){
	AccountModule.login(callback);
}

export function onLoginSuccess(data){
	AccountModule.onLoginSuccess(data,()=>{
		Account.user = data;
	});
}

export function onCheckLoginPress(callback){
	return (
		()=>{
			if(!isLogin()){
				login((result)=>{});
			}else{
				callback();
			}
		}
	);
}

/**
 * 如果没有登录，则调出登录界面
 * 登录成功之后回调
 *
 * 如果已经登录，则直接回调
 * 
 * @param  {Function} callback [description]
 * @return {[type]}            [description]
 */
export function onRequireLoginPress(callback){
	return (
		()=>{
			if(isLogin()){
				callback(1);
			}else{
				login((result)=>{
					if(result){
						callback();
					}
				});
			}
		}
	);
}


export function isLogin(){
	return Account.user;
}

export function logout(){
	AccountModule.logout();
	Account.user = null;

}