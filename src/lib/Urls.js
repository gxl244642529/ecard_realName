
import React, { Component } from 'react';
const NativeModules = require('react-native').NativeModules;
const WebModule = NativeModules.WebModule;

console.log('BrowserModule'+WebModule)

export default class Urls {

	static openUrl(url,title){
		WebModule.open(url,title);
	}

}

