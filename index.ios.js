/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import {
  AppRegistry
} from 'react-native';

global.console={
	log:()=>{

	},
	warn:()=>{

	},
	error:()=>{

	},
	info:()=>{
		
	}
}


import App from './src/App'
//import App from './src/recharge/RechargeResult'
//import App from './src/recharge/RechargeSuccess'
//import TestApp from './src/TestApp'
//import App from './src/_home'
//import App from './src/AppShop'
//import App from './src/ble/BleTest'

//import App from './src/busqr/_Home'

//import App from './src/busqr/_Home'
AppRegistry.registerComponent('ecard', () => App);
