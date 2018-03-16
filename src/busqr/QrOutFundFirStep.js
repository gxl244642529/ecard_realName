
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
import PayBox from '../widget/PayBox'

// import {FormInput,FormSelect,FormSwitch,FormItem} from '../../lib/TemplateForm'
import FormInput from '../widget/StandardFormInput'
import FormSelect from '../widget/StandardFormSelect'
// import {VerifyCode,checkPhone} from '../widget/VerifyCodeUtil'
import {VerifyCode} from './component/VerifyCodeUtil'
const imageHolder = require('../images/s_ic_add_diy.png');


import {
  radioButton,
  loadingButton,
  loadingButtonReal,
  loadingButtonRealDisabled
} from '../GlobalStyle'

//新增
import WithDrawProcess from './component/WithDrawProcess'
import StandardStyle from '../lib/StandardStyle'
import {SCREEN_WIDTH} from '../widget/Screen'
import {checkedPwd} from './QrUtils'
import {BANKID_VALUES,BANKID_LABELS} from '../realName/CommonData'

const  OK = 0;
const ERROR = 1;
const EXTEND = 2;


export default class QrOutFund extends Component{
	constructor(props) {
	    super(props);
	    this.state={isBankJian:false,hasPayBox:true};
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
	  onSuccess=(result)=>{
	    console.log(result+"sdFdsgf");
      Api.returnTo("/busqr/qrAccStatus/3");
	  }
	  onSubmit=()=>{
      let data = {code:this.state.code,verifyId:this.state.verifyId};
      A.confirm("请确认信息是否填写正确，提交申请以后账户将被关闭，公交扫码功能将不能使用，是否确认提交信息？",
      (index)=>{
          if(index==0){
            this.form.submit(this,{
              api:'qr_fund/submit',
              crypt:0,
              data:data,
              success:(result)=>{
                console.log(result);
                Api.returnTo("/busqr/qrAccStatus/3");
              }
            });
          }
      });

	  }
    _setVerify=(verifyId)=>{
      console.log(verifyId);
      this.setState({verifyId:verifyId});
    }
    _banckCheck=(bankId)=>{
      console.log("bankId="+bankId);
      if (bankId===3) {
        console.log("选择建行");
        this.setState({isBankJian:true});
      }else {
        this.setState({isBankJian:false});
      }
    }
    _checkError=(result)=>{
      console.log("result="+result);
      let str = "密码错误，你还有"+(result-100)+"次机会";
      A.alert(str,(r)=>{

      });
      this.setState({hasPayBox:true});
      this.refs.PAYBOX._onClean();
      return;
    }
    _checkSucceed=(result)=>{
      Api.returnTo("/busqr/out_fund");
    }
    _checkPwd=(str)=>{
      console.log("验证密码输入是否正确");
      let data = {pwd:str};

      checkedPwd(data,this._checkError,this._checkSucceed);
    }

    _onCancelPayBox=()=>{
      Api.goBack();
    }
    // <FormSelect
    //   label="银行名称"
    //   placeholder="请选择银行名称"
    //   ref="bankId"
    //   values={BANKID_VALUES}
    //   labels={BANKID_LABELS}
    //   value={this.state.bankId}
    //   onChange={(index,bankId)=>{this._banckCheck(bankId)}}
    //   hasBottom={true}
    //   hasHead={true} />
    // {this.state.isBankJian?  <FormSelect
    //     label="联行号"
    //     placeholder="请选择联行号"
    //     ref="bankSub"
    //     values={LIAN_ID}
    //     labels={LIAN_NAME}
    //     value={this.state.bankId}
    //     onChange={(index,bankSub)=>{this.setState({bankSub:bankSub+""})}}
    //     hasBottom={true}/>:  <FormInput
    //         label="开户支行"
    //         ref="bankAddr"
    //         value={this.state.bankAddr}
    //         placeholder="请选择开户行"
    //         onChange={(bankAddr)=>{this.setState({bankAddr})}}
    //         hasBottom={true}
    //        />}


	render(){
			return (
				<View style={StandardStyle.container}>
					<TitleBar title="出金申请" />
					<ScrollView style={styles.container}>
						<View style={styles.mt10}>
						      <WithDrawProcess step={1} status={0} />
						</View>

					</ScrollView>
          {this.state.hasPayBox &&

            <PayBox
              ref="PAYBOX"
              onCancel={this._onCancelPayBox}
              onSuccess={(str)=>this._checkPwd(str)}
              onForget={()=>Api.push("/busqr/forget_pass")}

          />}
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1},

	//功能样式
	mt10:{
	  marginTop:30,
    marginBottom:30
	},
  inputStyle:{
    marginLeft:5,
    height:60,
    width:SCREEN_WIDTH*0.6,
    fontSize:16,
  },
  verifyStyle:{
     position:"absolute",
     top:25,
     right:20,
   },
   //分割线
   viewStyle:{
    height:1/PixelRatio.get(),
    backgroundColor:'#d7d7d7',
    marginLeft:5,
  },
  butSty:{
    marginTop:10
  }
});
