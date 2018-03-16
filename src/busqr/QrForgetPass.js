
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

// import {FormInput,FormSelect,FormSwitch,FormItem} from '../../lib/TemplateForm'
import FormInput from '../widget/StandardFormInput'

const imageHolder = require('../images/s_ic_add_diy.png');

import {
  radioButton,
  loadingButton,
  loadingButtonReal,
  loadingButtonRealDisabled
} from '../GlobalStyle'





export default class QrForgetPass extends Component{
	constructor(props) {
	    super(props);
	    this.state={};
	    this.form = new Form();
	  }

	  onSuccess=(result)=>{
	    console.log(result);
      Api.returnTo("/busqr/reset_pwd");
	  }
	  onSubmit=()=>{
	    this.form.submit(this,{
	      api:'qr_pwd/resetRequest',
	      crypt:0,
	      success:this.onSuccess
	    });
	  }
    _setVerify=(verifyId)=>{
      console.log(verifyId);
      this.setState({verifyId:verifyId});
    }

	render(){
			return (
				<View style={CommonStyle.container}>
					<TitleBar title="忘记密码" />
					<ScrollView style={styles.container}>
            <View style={{marginTop:20}}>
    					<FormInput
    						label="证件号码"
    						value={this.state.idCard}
    						placeholder="请输入开户证件号码"
    						ref="idCard"
    						onChange={(idCard)=>{this.setState({idCard})}} />
            </View>


            <View style={styles.butSty}>
						 <LoadingButton
	            loading={this.state.submiting}
	            text="下一步"
	            onPress={this.onSubmit}
	            disabled={this.state.submiting}
	            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
	            textStyle={loadingButton.loadingButtonText} />
            </View>
					</ScrollView>
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1},
  butSty:{
    marginTop:20
  }
});
