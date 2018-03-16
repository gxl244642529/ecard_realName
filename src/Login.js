



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

import Button from './widget/Button'
import MD5 from 'crypto-js/md5'
import TitleBar from './widget/TitleBar'
import {getPushId} from '../lib/PushUtil'

import {onLoginSuccess} from '../lib/LoginUtil'


export default class Login extends Component{

    constructor(props) {
        super(props);
        this.state = {account:'18659210057',pwd:'123456'};
    }

    _onLoginSuccess=(data)=>{
    	onLoginSuccess({...data,...{account:this.state.account,pwd:MD5(this.state.pwd).toString()}});
    }

    _login=()=>{
      getPushId((pushId)=>{
        Api.api({
                api:"login/login",
                crypt:3,
                data:{
                  account:this.state.account,
                  pwd:MD5(this.state.pwd).toString(),
                  platform:'web',
                  version:Api.VERSION,
                  pushID:pushId
                },
                success:this._onLoginSuccess
              });
      });
    	
    }
    

    render(){
    	return (
    		<View style={CommonStyle.container}>
    			<TitleBar title="登录" />
    			<TextInput onChangeText={(account)=>{this.setState({account})}} value={this.state.account} style={{margin:10,height:30,backgroundColor:'#fff'}} placeholder="请输入用户名" />
				<TextInput onChangeText={(pwd)=>{this.setState({pwd})}} value={this.state.pwd} style={{margin:10,height:30,backgroundColor:'#fff'}} placeholder="请输入密码" />

    			<Button text="登录" style={{margin:20,backgroundColor:'#ea9'}} onPress={this._login} />
    		</View>
    	);
    }

}