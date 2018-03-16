
import React, { Component } from 'react';

import {
  Alert
} from 'react-native';

const AlertApi = require('react-native').NativeModules.CommonApi;
//ok为0

export default class A {

	static alert(info,callback){
		let message;
		if(typeof info === 'string'){
			message = info;
			let title = info.title || '温馨提示';
			Alert.alert(
            title,
            message,
            [
              {text: '确定',onPress:()=>{callback && callback(0)}},
            ]
           );
		}else{
			
		}
		
	}

	static confirm(info,callback){
		if(typeof info==="string"){
			Alert.alert(
	            "温馨提示",
	            info,
	            [
	              {text: '取消',onPress:()=>{console.log('cancel');callback(1)}},
	              {text: '确定',onPress:()=>{callback(0)}},
	            ]
	        );
		}else{
			Alert.alert(
	            "温馨提示",
	            info.message,
	            [
	              {text: info.cancel ? info.cancel : '取消',onPress:()=>{callback && callback(1)}},
	              {text: info.ok ? info.ok : '确定',onPress:()=>{callback && callback(0)}},
	            ]
	        );
		}
		
	}

	static toast(message){
		AlertApi.toast(message);

	}

	static wait(message){
		AlertApi.wait(message);
	}
	static cancelWait(){
		AlertApi.cancelWait();
	}

}