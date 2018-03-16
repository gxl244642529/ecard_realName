
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
import FormSelect from '../widget/StandardFormSelect'
import Notifier from '../../lib/Notifier'
// import {VerifyCode,checkPhone} from '../widget/VerifyCodeUtil'
import {VerifyCode} from './component/VerifyCodeUtil'
import QrUtils from './QrUtils'
import {makePhoneCall} from '../lib/CommonUtil'
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
const URL = "/personal/center"



import {BANKID_VALUES,BANKID_LABELS} from '../realName/CommonData'


export default class QrOutFund extends Component{
	constructor(props) {
	    super(props);
	    this.state={isBankJian:false};
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
    componentWillMount() {
      Notifier.addObserver("androidBack",this.onBack);

    }

    componentWillUnmount() {
        Notifier.removeObserver("androidBack",this.onBack);
    }
    onBack=()=>{

        let arr = Api.getRoutes();
        for(let i= arr.length-1; i >=0; --i){
          let vo = arr[i];
          if(vo.location===undefined){
            break;
          }
          if(vo.location.pathname==URL){
            Api.returnTo(URL);
            return true;
          }
        }
        Api.returnTo("/");
          return true;

    }
	  onSuccess=(result)=>{
	    console.log(result+"sdFdsgf");
      Api.returnTo("/busqr/qrAccStatus/3");
	  }
	  onSubmit=()=>{
      let data = {code:this.state.code,verifyId:this.state.verifyId};
      A.confirm("请确认信息是否填写正确，提交申请以后公交扫码服务将不能使用，请确认是否提交信息？",
      (index)=>{
          if(index==0){
            this.form.submit(this,{
              api:'qr_fund/submit',
              crypt:0,
              data:data,
              success:(result)=>{
                console.log(result);
                QrUtils.disableToken();
                QrUtils.inactiveDevice();
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
    _call=()=>{
        makePhoneCall("0592968870");
    }


	render(){
			return (
				<View style={StandardStyle.container}>
					<TitleBar title="出金申请"  onBack={this.onBack}/>
					<ScrollView style={styles.container}>
						<View style={styles.mt10}>
						      <WithDrawProcess step={2} status={0} />
						</View>
            <FormInput
              label="姓        名"
              value={this.state.name}
              editable={false}
              hasHead={true}
              hasBottom={true}/>

            <FormInput
              label="银行卡号"
              placeholder={"请输入姓名对应的借记卡号"}
              ref="bankCard"
              onChange={(bankCard)=>{this.setState({bankCard})}}
              />
            <VerifyCode
              apiName="qr_fund/verify"
              phoneValue={this.state.phone}
              codeValue={this.state.code}
              codeChange={(code)=>{this.setState({code})}}
              setVerify={this._setVerify}
              editable={false}
            />
            <View style={styles.viewStyle}/>
            <View>
              <Text style={{padding:15,color:'#aaa'}} onPress={this._call}>温馨提示：如有疑问请联系客服968870</Text>
            </View>
            <View style={styles.butSty}>
						 <LoadingButton
				            loading={this.state.submiting}
				            text="申      请"
				            onPress={this.onSubmit}
				            disabled={this.state.submiting}
				            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
				            textStyle={loadingButton.loadingButtonText} />
            </View>
                <View style={{marginBottom:200}}></View>
					</ScrollView>
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
    marginTop:10,
  }
});
