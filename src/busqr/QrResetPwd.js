
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
import FadeFormInput from '../widget/FadeFormInput'
import PayBox from '../widget/PayBox'
const imageHolder = require('../images/s_ic_add_diy.png');
import {VerifyCode} from './component/VerifyCodeUtil'

import {
  radioButton,
  loadingButton,
  loadingButtonReal,
  loadingButtonRealDisabled
} from '../GlobalStyle'





export default class QrResetPwd extends Component{
	constructor(props) {
	    super(props);
	    this.state={hasPayBox:false};
	    this.form = new Form();
	  }
    componentDidMount(){
      Api.api({
        api:"qr_fund/preFund",
        success:(result)=>{
          console.log(result);
          this.setState(result);
        }
      });
    }
    _setVerify=(verifyId)=>{
      console.log(verifyId);
      this.setState({verifyId:verifyId});
    }
    _callPayBox=(currentInput)=>{
      if(currentInput=='newPwd'){
        this.setState({newPwd:''});
      }else{
        this.setState({newPwdre:''});
      }
      this.setState({currentInput,hasPayBox:true});
    }
    _onCancelPayBox=()=>{
      Api.goBack();
    }
    _onSuccess=(str)=>{
      if(this.state.currentInput=='newPwd'){
        this.setState({newPwd:str});
      }else{
          this.setState({newPwdre:str});
      }
      this.setState({hasPayBox:false});
    }
	  onSuccess=(result)=>{
	    console.log(result);
      if (result===true) {
        Api.go(-2);
      }
	  }
	  onSubmit=()=>{
      let self = this.state;
      if (!self.newPwd) {
        A.alert("请输入新密码");
        return;
      }
      if (!self.newPwdre) {
        A.alert("请输入确认密码");
        return;
      }
      if (!self.code) {
        A.alert("请输入验证码");
        return;
      }
      if (self.newPwdre!=self.newPwd) {
        A.alert("两次输入的密码不一致");
        return;
      }
      let data = {newPwd:self.newPwd,verifyId:self.verifyId,code:self.code};
      Api.api({
        api:"qr_pwd/reset",
        crypt:0,
        data:data,
        success:this.onSuccess,
      });
	  }

	render(){
			return (
				<View style={CommonStyle.container}>
					<TitleBar title="重置密码" />
					<ScrollView style={styles.container}>

            <FadeFormInput
                label="新密码"
                onPress={()=>this._callPayBox('newPwd')} value={this.state.newPwd}
                placeHoder="请输入新密码"
                hasBottom={true}/>
            <FadeFormInput
                label="确认新密码"
                onPress={()=>this._callPayBox('newPwdre')} value={this.state.newPwdre}
                placeHoder="请输入确认密码"
              />

            <VerifyCode
              apiName="qr_pwd/verify"
              phoneValue={this.state.phone}
              codeValue={this.state.code}
              codeChange={(code)=>{this.setState({code})}}
              setVerify={this._setVerify}
              editable={false}
            />

						 <LoadingButton
	            loading={this.state.submiting}
	            text="提交"
	            onPress={this.onSubmit}
	            disabled={this.state.submiting}
	            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
	            textStyle={loadingButton.loadingButtonText} />
					</ScrollView>
          {this.state.hasPayBox &&
            <PayBox onCancel={this._onCancelPayBox} onSuccess={(str)=>this._onSuccess(str)}

            />}
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1}
});
