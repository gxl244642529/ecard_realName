
import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  CommonStyle,
  Api,
  A,
  TextInput
} from '../lib/Common';

import MyWebView from '../lib/MyWebView'
const SysModule = require('react-native').NativeModules.SysModule;
export default class MainView extends Component {


	componentDidMount() {
		SysModule.onStartup();
	}

	render(){
		return (
			<View style={CommonStyle.container}>
				<MyWebView source={{uri:'http://www.163.com'}} style={{width:300,height:300}} />
			</View>
		);
	}

}