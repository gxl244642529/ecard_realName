
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
  Form,
} from '../../lib/Common';

import {
  LoadingButton
} from '../Global'



import {FormSelect,FormSwitch,FormItem} from '../../lib/TemplateForm'
const imageHolder = require('../images/s_ic_add_diy.png');

import {
  radioButton,
  loadingButton,
  loadingButtonReal,
  loadingButtonRealDisabled
} from '../GlobalStyle'

//修改

import {
  TitleBar,
} from '../Global'
import StandardStyle from '../lib/StandardStyle'
import FormInput from '../widget/StandardFormInput'
import VerifyCode from './component/VerifyCodeUtil'
import {SCREEN_WIDTH} from '../widget/Screen'

var  CodeTime = 60;

export default class QrRequestVerify extends Component{
	constructor(props) {
	    super(props);
    let json=props.params.json;
    this.state={
			verifyText:"再次发送("+CodeTime+")",
   		 count:CodeTime,
    	disabled:false,
      data:JSON.parse(json),
		};
    this.form = new Form();
  }
  componentDidMount(){
    // this.verifyPress();//原来调用0510
    // console.log(this.state.data.verifyId);
    this.setVerify(this.state.data.verifyId);
    this._countTimer();
    this.setState({disabled:true});
  }

	componentWillUnmount() {
    clearInterval(this.timer);//FIXME:无法实现卸载时清除计时器
   }


  onSuccess=(result)=>{
    console.log(result);
  }
	setVerify=(verifyId)=>{
    console.log(verifyId);
    this.setState({verifyId:verifyId});
  }

  verifyPress=()=>{
      this.setState({disabled:true});
      let data = {phone:this.state.data.phone};
      Api.api({
        api:'qr_acc/submitVerify',
        data:data,
        success:(result)=>{
          this.setVerify(result.toString());
          this._countTimer();
        },message:(error)=>{
					this.setState({disabled:false});
				}
      });
  }

  onSubmit=()=>{
    let self = this.state.data;
    let data = {name:self.name,
      idCard:self.idCard,phone:self.phone,
      verifyId:this.state.verifyId,
      pwd:self.pwd}
    this.form.submit(this,{
      api:"qr_acc/submit",
      crypt:0,
      data:data,
      success:(qrAccount)=>{
        Account.append({qrAccount});
        Api.replace('/busqr/qrAccStatus/0');
      }
    });
  }

  _countTimer=()=>{
    this.timer = setInterval(()=> {

      this.setState({
          count: this.state.count - 1,
          verifyText:"再次发送("+this.state.count+")",
          disabled:true,
      });
      if (this.state.count === 0) {
          clearInterval(this.timer);
          this.setState({count:CodeTime});
          this.setState({verifyText:"重新发送",disabled:false,});

      }
    }, 1000);
  }
	render(){
			return (
				<View style={StandardStyle.container}>
					<TitleBar title="开户短信验证" />
					<ScrollView style={styles.container}>
            <View style={{justifyContent:'center',alignItems:'center'}}>
              <View style={{flexDirection:'row',padding:20}}>
                <Text style={{color:'#595757',fontSize:16}}>我们已经发送了</Text>
                <Text style={{color:'#595757',fontSize:16,color:'#e8464c'}}>验证码</Text>
                  <Text style={{color:'#595757',fontSize:16}}>到你的手机</Text>
              </View>

              <Text style={{fontSize:22,paddingBottom:10}}>{this.state.data.phone}</Text>
            </View>
 						<View>
            <FormInput
              label="验证码:"
              placeholder={"请输入验证码"}
              ref="code"
              value = {this.state.codeValue}
              inputStyle={styles.inputStyle}
              onChange={(codeValue)=>this.setState({codeValue})}
              />
              <View style={styles.viewStyle}/>
            <TouchableOpacity style={styles.verifyStyle} disabled={this.state.disabled}
              onPress={this.verifyPress}>
              <Text>{this.state.verifyText}</Text>
            </TouchableOpacity>
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

 verifyStyle:{
     position:"absolute",
    //  top:20,
     right:20,
    //  height:40,
    // height:60,
    padding:20,
    // backgroundColor:"red"
   },
   viewStyle:{
 		height:1/PixelRatio.get(),
 		backgroundColor:'#d7d7d7',
 		marginLeft:10,
    marginRight:10
 	},
  butSty:{
    marginTop:20
  },
  inputStyle:{
    marginLeft:5,
    height:60,
    width:SCREEN_WIDTH*0.6,
    fontSize:16,
  },
});
