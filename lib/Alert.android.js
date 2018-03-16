
import React, { Component } from 'react';

const AlertModule = require('react-native').NativeModules.AlertModule;

//ok为0

export default class A {

	static alert(info,callback){
		callback = callback || function(){}
		AlertModule.alert(info, callback );

	}

	static confirm(info,callback){
		AlertModule.confirm(info,callback || function(){});
	}

	static toast(message){
		AlertModule.toast(message);
	}

	static wait(message){
		AlertModule.wait(message);
	}
	static cancelWait(){
		AlertModule.cancelWait();
	}

}
