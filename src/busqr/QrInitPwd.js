
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
  Form
} from '../../lib/Common';

import {
  TitleBar,
  LoadingButton
} from '../Global'

import {FormSelect,FormSwitch,FormItem} from '../../lib/TemplateForm'
const imageHolder = require('../images/s_ic_add_diy.png');


import FormInput from '../widget/StandardFormInput'
import StandardStyle from '../lib/StandardStyle'


import {
  radioButton,
  loadingButton,
  loadingButtonDisabled
} from '../GlobalStyle'





export default class QrInitPwd extends Component{
	constructor(props) {
	    super(props);
	    this.state={};
	    this.form = new Form();
	  }
	  onSuccess=(result)=>{
	    console.log(result);
	  }
	  onSubmit=()=>{
	    this.form.submit(this,{
	      api:'qr_pwd/setInit',
	      crypt:0,
	      success:this.onSuccess
	    });
	  }
  
	render(){
			return (
				<View style={StandardStyle.container}>
					<TitleBar title="初始化密码" />
					<ScrollView style={styles.container}>
 						<FormInput 
 							label="初始化密码" 
 							value={this.state.initPwd} 
 							placeholder="请输入初始化密码" 
 							ref="initPwd" 
 							onChange={(initPwd)=>{this.setState({initPwd})}} />
						 <LoadingButton 
				            loading={this.state.submiting}
				            text="提交" 
				            onPress={this.onSubmit}
				            disabled={this.state.submiting}
				            style={this.state.submiting ? loadingButtonDisabled : loadingButton.loadingButton} 
				            textStyle={loadingButton.loadingButtonText} />
					</ScrollView>
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1}
});