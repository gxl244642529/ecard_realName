
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
  Button,
  LoadingButton
} from '../Global'

import {FormSelect,FormSwitch,FormItem} from '../../lib/TemplateForm'
const imageHolder = require('../images/s_ic_add_diy.png');

import StandardStyle from '../lib/StandardStyle'
import FormInput from '../widget/StandardFormInput'
const SCREEN_WIDTH = Dimensions.get('window').width;


import {
  pageButton,
  radioButton,
  loadingButton,
  loadingButtonRealDisabled
} from '../GlobalStyle'


import {VerifyCode} from './component/VerifyCodeUtil'
import QrUtils from './QrUtils'


export default class QrAccActive extends Component{
	constructor(props) {
	    super(props);
	    this.state={};
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
    _active=()=>{
      QrUtils.activate((ret)=>{
        console.log("ret="+ret)
          if (ret!=0) {

            A.confirm("重新开通失败，请再次提交信息",
            (index)=>{
                if(index==0){
                  return;
                }
            });
          }
      });
    }
	  onSuccess=(result)=>{
	    console.log(result);
      QrUtils.clear();
      QrUtils.disableToken();
      this._active();
      Api.replace("/busqr/main");
	  }
	  onSubmit=()=>{
      console.log(this.form);
      let self = this.state;
      let data = {code:self.code,verifyId:self.verifyId};
	    this.form.submit(this,{
	      api:'qr_acc/active',
	      crypt:0,
        data:data,
	      success:this.onSuccess
	    });
      // QrUtils.activate((ret)=>{
      //   // this.setState({ret:ret});
      // });
	  }
    rerequest=()=>{
      QrUtils.activate((ret)=>{
        console.log(ret);
        // this.setState({ret:ret});
        this.onSubmit();
        if (ret==0) {
          Api.replace("/busqr/main");
        }
      });
    }
    renderAcctiveError(){
      return <View style={styles.content}>

              <View style={StandardStyle.center}>
                  <Image source={require("./images/account_closed.png")} style={styles.graphic}/>
                  <Text style={[StandardStyle.h3,StandardStyle.fontGray,{paddingTop:20,paddingBottom:20}]}>二维码付款重新开通失败</Text>

              </View>

              <View style={styles.btnBox}>
              <Button
                  text="刷新"
                  style={[pageButton.button,styles.btn]}
                  onPress={this.rerequest}
                  textStyle={pageButton.buttonText}
              />
              </View>

          </View>
    }

	render(){
			return (
				<View style={StandardStyle.container}>
					<TitleBar title="账户重新开通" />
					<ScrollView style={styles.container}>
            <VerifyCode
              headLine={true}
              apiName="qr_acc/activeVerify"
              phoneValue={this.state.phone}
              codeValue={this.state.code}
              codeChange={(code)=>{this.setState({code})}}
              setVerify={this._setVerify}
              editable={false}
              codeHasBottom={true}
            />

						 <LoadingButton
		            loading={this.state.submiting}
		            text="确定重新开通"
		            onPress={this.onSubmit}
		            disabled={this.state.submiting}
		            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
		            textStyle={loadingButton.loadingButtonText} />

					</ScrollView>
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1},
  content:{
      flex:1,
      justifyContent:'space-between',
      alignItems:'center',
      paddingBottom:40
  },
  headImg:{
      width:SCREEN_WIDTH,
      height:SCREEN_WIDTH*0.25
  },
  graphic:{
      width:SCREEN_WIDTH*0.35,
      height:SCREEN_WIDTH*0.35,
      // marginBottom:20
      marginTop:100,
  },
  btnBox:{
      flexDirection:'row',
      paddingLeft:10,//+10
      paddingRight:10,//+10
      marginTop:50
  },
  btn:{
      flex:1,
  },
});
