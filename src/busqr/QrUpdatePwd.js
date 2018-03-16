
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
import PayBox from '../widget/PayBox'
const imageHolder = require('../images/s_ic_add_diy.png');

import {
  radioButton,
  loadingButton,

  loadingButtonRealDisabled
} from '../GlobalStyle'



//新增
import FadeFormInput from '../widget/FadeFormInput'
import StandardStyle from '../lib/StandardStyle'



export default class QrUpdatePwd extends Component{
	constructor(props) {
	    super(props);
	    this.state={hasPayBox:false};
	    this.form = new Form();
	  }
	  onSuccess=(result)=>{
	    console.log(result);
        Api.go(-2);
	  }
	  onSubmit=()=>{
      let self = this.state;
      if (!self.oldPwd) {
        A.alert("请输入原密码");
        return;
      }
      if (!self.newPwd) {
        A.alert("请输入新密码");
        return;
      }
      if (!self.newPwdre) {
        A.alert("请输入确认密码");
        return;
      }
      if (self.newPwdre!=self.newPwd) {
        A.alert("两次输入的密码不一致");
        return;
      }
      let data = {oldPwd:self.oldPwd,newPwd:self.newPwd};
      Api.api({
        api:'qr_pwd/update',
        data:data,
        success:this.onSuccess
      });

	    // this.form.submit(this,{
	    //   api:'qr_pwd/update',
	    //   crypt:0,
	    //   success:this.onSuccess
	    // });
	  }

    _callPayBox=(currentInput)=>{
      if(currentInput=='oldPwd'){
        this.setState({oldPwd:''});
      }else if(currentInput=='newPwd'){
        this.setState({newPwd:''});
      }else{
        this.setState({rePwd:''});
      }
      this.setState({currentInput,hasPayBox:true});
    }
    _onSuccess=(str)=>{
      if(this.state.currentInput=='oldPwd'){
        this.setState({oldPwd:str});
      }else if(this.state.currentInput=='newPwd'){
        this.setState({newPwd:str});
      }else{
          this.setState({newPwdre:str});
      }
      this.setState({hasPayBox:false});
    }

	render(){
			return (
				<View style={StandardStyle.container}>
					<TitleBar title="修改密码" />
					<ScrollView style={styles.container}>


            <FadeFormInput
                label="原密码:"
                placeHoder="请输入原密码"
                onPress={()=>this._callPayBox('oldPwd')} value={this.state.oldPwd}

                hasBottom={true}/>
            <FadeFormInput
                label="新密码:"
                onPress={()=>this._callPayBox('newPwd')} value={this.state.newPwd}
                placeHoder="请输入新密码"
                hasBottom={true}/>
            <FadeFormInput
                label="确认新密码:"
                placeHoder="请输入确认密码"
                onPress={()=>this._callPayBox('newPwdre')} value={this.state.newPwdre} hasBottom={true}
                hasBottom={true}/>

						 <LoadingButton
  	            loading={this.state.submiting}
  	            text="确认修改"
  	            onPress={this.onSubmit}
  	            disabled={this.state.submiting}
  	            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
  	            textStyle={loadingButton.loadingButtonText} />
					</ScrollView>
          {this.state.hasPayBox &&
            <PayBox onCancel={this._onCancelPayBox} onSuccess={(str)=>this._onSuccess(str)}
            onCancel={()=>{this.setState({hasPayBox:false})}}/>}
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1}
});
