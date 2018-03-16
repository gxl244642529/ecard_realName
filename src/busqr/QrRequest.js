
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
DatePicker,
Account,
ActivityIndicator,
CommonStyle,
PixelRatio,
ScrollView,
Form
} from '../../lib/Common';


// import {FormSelect,FormSwitch,FormItem} from '../../lib/TemplateForm'

import Select from '../../lib/Select'
import ImagePicker from '../../lib/ImagePicker'
import RadioButtonGroup from '../widget/RadioButtonGroup'
import LoadingButton from '../widget/LoadingButton'
import CheckBox from '../../lib/checkbox'
import StandardStyle from '../lib/StandardStyle'
import FormInput from '../widget/StandardFormInput'
import FadeFormInput from '../widget/FadeFormInput'
import {checkPhone} from  '../widget/VerifyCodeUtil'
import PayBox from '../widget/PayBox'
import ECardSelector from '../lib/ECardSelector'
import WebUtil from '../../lib/WebUtil'

import {
radioButton,
loadingButton,
loadingButtonReal,
loadingButtonRealDisabled
} from '../GlobalStyle'
import {
  TitleBar,
} from '../Global'

//标准


export default class QrRequest extends Component{
	constructor(props) {
	    super(props);
	    this.state={checked:true};
	    this.form = new Form();
	  }
    componentDidMount(){
      Api.api({
        api:'qr_acc/realInfo',
        crypt:3,
        success:(result)=>{
          console.log(result);
          if (result) {
              this.setState(result);
              this.setState({nameState:false,idCardState:false,phoneState:false});
          }

        }
      })
    }
	  onSuccess=(result)=>{

	    console.log(result);
	  }
    _url=()=>{
      console.log("链接到指定位置");
      WebUtil.open(Api.imageUrl + '/index.php/sys_agree_detail/content/qrrequest',"公交扫码服务开通协议");
    }
    _callPayBox=(currentInput)=>{
      if(currentInput=='pwd'){
        this.setState({pwd:''});
      }else{
        this.setState({confPwd:''});
      }
      this.setState({currentInput,hasPayBox:true});
    }
    _onCancelPayBox=()=>{
       this.setState({hasPayBox:false});
     }
    _onSuccess=(str)=>{
      if(this.state.currentInput=='pwd'){
        this.setState({pwd:str});
      }else{
          this.setState({confPwd:str});
      }
      this.setState({hasPayBox:false});
    }
	  onSubmit=()=>{
      let self = this.state;
      var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
      var pattern=new RegExp("[`~%!@#^=''?~！@#￥……&——‘”“'？*()（），,。.、]");
      if (!self.pwd) {
        A.alert("请设置支付密码");
        return;
      }
      if (!self.confPwd) {
        A.alert("请输入确认密码");
        return;
      }
      if (self.pwd!=self.confPwd) {
        A.alert("两次输入的密码不一致");
        return;
      }
      if (!self.checked) {
        A.alert("请认真阅读并同意公交扫码服务开通协议");
        return;
      }
      if (!checkPhone(self.phone)) {
        A.alert("请输入正确的手机号码");
        return;
      }
      if(reg.test(self.idCard)|| pattern.test(self.idCard)){
        A.alert("证件号码输入错误");
        return;
      }



      // let sdata = {name:self.name,idCard:self.idCard,phone:self.phone,pwd:self.pwd}  //0510
	    this.form.submit(this,{
	      api:'qr_acc/check',
	      crypt:0,
	      success:(result)=>{
          console.log(result);
            let sdata = {name:self.name,idCard:self.idCard,phone:self.phone,pwd:self.pwd,verifyId:result};
           Api.replace('/busqr/qrRequestVerify/'+JSON.stringify(sdata));
        }
	    });
	  }

	render(){
			return (
				<View style={StandardStyle.container}>
					<TitleBar title="二维码付款" />
					<ScrollView style={styles.container}>
 						<FormInput
 							label="姓       名"
 							value={this.state.name}
 							placeholder="请输入姓名"
 							ref="name"
 							onChange={(name)=>{this.setState({name})}}
							hasBottom={true}
              editable={this.state.nameState}/>
 						<FormInput
 							label="证件号码"
 							value={this.state.idCard}
 							placeholder="请输入证件号码"
 							ref="idCard"
 							onChange={(idCard)=>{this.setState({idCard})}}
							hasBottom={true}
              editable={this.state.idCardState}/>
 						<FormInput
 							label="手       机"
 							value={this.state.phone}
 							placeholder="请输入手机号码"
 							ref="phone"
 							onChange={(phone)=>{this.setState({phone})}}
							hasBottom={true}
              editable={this.state.phoneState}/>
            <FadeFormInput
                label="支付密码:"
                onPress={()=>this._callPayBox('pwd')} value={this.state.pwd}
                placeHoder="请输入支付密码"
                hasBottom={true}/>
            <FadeFormInput
                label="确认密码:"
                onPress={()=>this._callPayBox('confPwd')} value={this.state.confPwd} hasBottom={true}
                placeHoder="请输入确认密码"
                hasBottom={true}/>
            <CheckBox
              checkboxStyle={{width:20,height:20}}
              containerStyle ={{marginLeft:10,marginTop:20}}
              labelBe='同意'
              label='《公交扫码服务开通协议》'
              labelBeStyle={[StandardStyle.h4,StandardStyle.fontBlack]}
              labelStyle={[StandardStyle.h4,StandardStyle.fontBlack,{color:'#e8464c'}]}
              urlPress={this._url}
              checked={this.state.checked}
              onChange={(checked) => {this.setState({checked:!this.state.checked});console.log(checked)}}
            />
            <View style={styles.butStyle}>
						 <LoadingButton
		            loading={this.state.submiting}
		            text="提交"
		            onPress={this.onSubmit}
		            disabled={this.state.submiting}
		            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
		            textStyle={loadingButton.loadingButtonText} />
            </View>
					</ScrollView>
          {this.state.hasPayBox &&
            <PayBox onCancel={this._onCancelPayBox} onSuccess={(str)=>this._onSuccess(str)}

            />}
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1},
  butStyle:{
    marginTop:30,
  }
});
