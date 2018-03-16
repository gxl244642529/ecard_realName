

import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  CommonStyle,
  Api,
  A
} from '../../lib/Common';

import Button from '../widget/Button'

import TitleBar from '../widget/TitleBar'


export default class BusinessCenter extends Component{

    constructor(props) {
        super(props);
        this.state = {};
    }

    _logout=()=>{
    	A.confirm("确定要退出吗?",(index)=>{
			if(index==0){
				Api.logout();
    			Api.goBack();
			}
		});
    	
    }

    render(){
    	return (
    		<View style={CommonStyle.container}>
    			<TitleBar title="商户中心" />
    			<Button text="退出登录" onPress={this._logout} />
    		</View>
    	);
    }

}