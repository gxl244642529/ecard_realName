
import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  TextInput,
  View,
  Image,
  TouchableOpacity,
  Dimensions,
  TouchableHighlight,
  Api,
  A,
  Account,
  ActivityIndicator,
  CommonStyle,
  PixelRatio,
  LocalData,
  Log,
  ScrollView,
  Form,
  Platform
} from '../../lib/Common';
 let BusQrModule = require('react-native').NativeModules.BusQrModule;
 let SysModule = require('react-native').NativeModules.SysModule;
const SCREEN_WIDTH = Dimensions.get('window').width;

var { requireNativeComponent } = require('react-native');
let RCTBusQrView;
if(Platform.OS=='android'){
  RCTBusQrView = requireNativeComponent('RCTBusQrView', null);
}else{
  RCTBusQrView = requireNativeComponent('RCTQrView', null);
}



export class BusQrView extends Component {
  static propTypes = {
    /**
     * 指定内容
     *
     * @type {[type]}
     */
     content: React.PropTypes.string.isRequired
  };


  render() {
    return <View>
      <RCTBusQrView style={this.props.style} content={this.props.content} />
      <Image source={require('./images/ecard_pay.png')} style={styles.ecardIcon}/>

    </View>;
  }
}

const QR_ACCOUNT_KEY = "qrAccountKey3";


export const  OK = 0;//密码验证成功
export const ERROR = 100;//验证失败
export const EXTEND = 2;//超过次数限制

export default class QrUtils{
  static BusQrView = BusQrView;

  static data = {};

  static getAccount(callback){
    let qrAccount = Account.user.qrAccount;
    if(!qrAccount){
      Api.api({
        api:"qr_acc/account",
        success:(arAccount)=>{
          Account.append({arAccount});
          callback(arAccount);
        }
      });
    }else{
        callback(qrAccount);
    }
  }


/**
 *
 * @return {[type]} [description]
 */
  static clear(){
    QrUtils.data = {};
  }

	static getQr(callback){
    QrUtils.getAccount((account)=>{
      //如果没有激活，那么去激活，否则拉取token
      let key = QR_ACCOUNT_KEY+account;
      if(QrUtils.data[key]){
         BusQrModule.getQr(account,callback);
      }else{
          BusQrModule.activate(account,(ret)=>{
            QrUtils.data[key] = account;
            BusQrModule.getQr(account,callback);
          });
      }

    });
	}


  static activate(callback){
    QrUtils.getAccount((account)=>{
       BusQrModule.activate(account,callback);
    });
  }
  //禁止截屏
  static enableCapture(param){
    SysModule.enableCapture(param);
  }



  static disableToken(){
    QrUtils.clear();
    BusQrModule.disableToken();

  }
  static inactiveDevice(){
    BusQrModule.inactiveDevice();
  }
}


export function checkedPwd(data,callbackError,callbacksucceed){
	console.log("正在调用checkedPwd");
	Api.api({
		api:"qr_pwd/check",
		data:data,
		crypt:3,
		success:(result)=>{
			console.log("check返回"+result);
			if (result==OK) {
        callbacksucceed();
			}else if (result>ERROR) {
				// A.alert("密码错误，请重新输入",callbackError);
        console.log(result);
        callbackError(result);

			}else if (result==EXTEND) {
				A.alert("账号锁定，1个自然日后将重新开启");
			}

		}
	});

}
const styles = StyleSheet.create({
  ecardIcon:{
    height:50,
    width:50,
    top:SCREEN_WIDTH*0.8/2-20,
    right:SCREEN_WIDTH*0.8/2-20,
    position:'absolute'
  }
});
