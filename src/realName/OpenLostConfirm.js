
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
Form,
Notifier,
Platform,
} from '../../lib/Common';
import {BackAndroid} from 'react-native'

import NfcUtil from '../../lib/NfcUtil'
import {FormSelect,FormSwitch,FormItem} from '../../lib/TemplateForm'
import TitleBar from '../widget/TitleBar'
import Select from '../../lib/Select'
import ImagePicker from '../../lib/ImagePicker'
import RadioButtonGroup from '../widget/RadioButtonGroup'
import LoadingButton from '../widget/LoadingButton'
// import CheckBox from 'react-native-checkbox'
import CheckBox from '../../lib/checkbox'
import StandardStyle from '../lib/StandardStyle'
// import FormInput from './StandardFormInput'
import FormInput from '../widget/StandardFormInput'

import ECardSelector from '../lib/ECardSelector'
import {
radioButton,
loadingButton,
loadingButtonReal,
loadingButtonRealDisabled
} from '../GlobalStyle'
import {makePhoneCall} from '../lib/CommonUtil'
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;
import WebUtil from '../../lib/WebUtil'
import {WebView}  from 'react-native'
// import  {BANK_CARD_FUNDING ,FINISH} from './RealI18N'
import {FROM_REAL,FROM_REAL_CARD,BANK_CARD_FUNDING ,FINISH} from './CommonData'
import {Jump} from './OpenLostUtil'
import ConfirmAlert from './ConfirmAlert'
const picPlaceHolder = require('../images/s_ic_add_diy.png');
export function skip(isRealFlag){
  switch (isRealFlag) {
    case BANK_CARD_FUNDING:
      Api.push('/realName/verifyProcess');
      break;
    case FINISH:
      Api.push('/realName/realInfo');
      break;
    default:
  }
}
export function renderHeadPng(){
  return(
    <Image style={{width:SCREEN_WIDTH,height:SCREEN_HEIGHT*0.2,justifyContent:'center',alignItems:'center'}} source={require('./images/background_4.png')}>
      <View>
        <Image source={require('./images/card_4.png')} style={styles.cardImage}/>
      </View>
    </Image>
  );
}
export function renderHeadMsg(){
  return(
    <Image source={require('./images/background.png')} style={styles.backBotom}>
      <View style={{backgroundColor:'transparent',padding:5}}>
          <Text style={styles.contruStyle}>开通挂失说明:</Text>
          <Text style={[StandardStyle.h5,StandardStyle.fontWhite,{paddingRight:5}]}>
          1.一个账号最多可同时开通5张e通卡挂失服务;
          </Text>
          <Text style={[StandardStyle.h5,StandardStyle.fontWhite,{paddingRight:5}]}>2.为了您e通卡的资金安全，请尽快对已绑定的卡片开通挂失服务;</Text>
          <Text style={[StandardStyle.h5,StandardStyle.fontWhite,{paddingRight:5}]}>
          3.成功开通挂失服务且卡状态正常的e通卡可办理挂失;
          </Text>
          <Text style={[StandardStyle.h5,StandardStyle.fontWhite,{paddingRight:5}]}>
          4.若有异常情况，请您拨打客服热线<Text style={[StandardStyle.h5,{color:'#e8464c',paddingLeft:3,paddingTop:10,paddingBottom:10}]} onPress={()=>makePhoneCall("0592968870")}>968870>></Text>
          </Text>
      </View>
    </Image>
  );
}
export default class OpenLostConfirm extends Component{

constructor(props) {
  super(props);
  let data = JSON.parse(props.params.json)
  // console.log(data)
  this.state={checked:false,hasNfc:false,alertConfirm:false,data:data};
  this.form = new Form();
}


onReadCard=(cardId)=>{
  let nfc = true;
  this.setState({cardId,nfc});
}

onNfc=()=>{
  NfcUtil.readCard(this.onReadCard);
  return true;
}

componentDidMount() {
  Notifier.addObserver("nfcTag",this.onNfc);
  Notifier.addObserver('androidBack', this._onBack);
  if(Platform.OS=='android'){
    NfcUtil.isAvailable((result)=>{
      this.setState({hasNfc:result});
    });
  }
}

componentWillUnmount() {
  Notifier.removeObserver("nfcTag",this.onNfc);
  Notifier.removeObserver('androidBack', this._onBack);
}

onSuccess=(result)=>{
  let fromFlag = this.state.data.fromto;
  /***this.props.param.id==0时走实名接口****/
  /***this.props.param.id==1时走实名绑卡接口***/
  switch (fromFlag) {
    case FROM_REAL:
      this._realProcess(result);
      break;
    case FROM_REAL_CARD:
      this._realCardProcess(result);
      break;
    default:

  }
}
_realProcess=(result)=>{
  //tru实际绑卡成功，提示实名绑卡成功
  //false 绑卡信息提交成功，（1、实名成功==》卡验证界面  2、实名中=》审核页面）
  if(result&&this.state.data.isReal==FINISH){
    A.alert("身份认证成功，开通挂失成功！");
    Api.push('/realName/realInfo');
  }else if (!result&&this.state.data.isReal==FINISH) {
      Api.push('/realName/verifyProcess');
  }else if (!result&&this.state.data.isReal==BANK_CARD_FUNDING) {
    Api.push('/realName/verifyProcess');
  }
}
_realCardProcess=(result)=>{
  if(result){
    A.alert("开通挂失成功");
    Api.push('/realName/rCard');
  }else {
    A.alert("开通挂失失败");
  }

}
onSubmit=()=>{
  if(!this.state.checked){
    A.alert("请认真阅读并同意开通挂失服务须知");
    return;
  }
  if(!this.state.cardId){
    A.alert("请选择或输入e通卡卡号");
    return;
  }
  this.setState({alertConfirm:true});
}
_select=()=>{
  console.log("跳转到卡列表");
  this.setState({nfc:false});
}
_url=()=>{
  WebUtil.open(Api.imageUrl + '/index.php/sys_agree_detail/content/realcard',"");
}

onSelectECard=(cardId)=>{
  this.setState({cardId:cardId,nfc:false});
}
_noreal=()=>{
  let data = {cardId:this.state.cardId,nfc:this.state.nfc};
  Api.push("/realName/noRealMessage/"+JSON.stringify(data));
}
_skip=()=>{
  // Api.push('/realName/verifyProcess');
  //实名成功跳转实名信息页
  let isRealFlag = this.state.data.isReal;
  skip(isRealFlag);
}
_renderRight=()=>{
  return <Jump skip={this._skip}/>
}
_noagree=()=>{
  this.setState({alertConfirm:false})
}
_agree=()=>{
  // A.alert("提交卡号信息")
  let data = {isNfc:this.state.nfc,isAgree:this.state.alertConfirm};
  this.form.submit(this,{
    api:'newRcard/submitCardInfo',
    data:data,
    success:this.onSuccess
  });
}
_onNavigationStateChange=(navState)=>{
  // if(Platform.OS=='android' && navState.url && navState.url.endsWith('pdf')){
    if(navState.url!=this.url){
      this.url = navState.url;
      let url = navState.url;
    }

}
renderConfirm=()=>{
  return(
    <View style={styles.confirmView}>
        <View style={styles.confirmViewAb}>
            <View style={styles.titleView}>
              <Image source={require('./images/title_left.png')} style={{width:35,height:18}} resizeMode="contain"/>
              <Text style={styles.titletext}>真实性承诺函</Text>
              <Image source={require('./images/title_right.png')} style={{width:35,height:18}} resizeMode="contain"/>
            </View>
            <ScrollView style={{flex:1}}>
            <WebView
              ref={(webview)=>{this.webview=webview}}
              style={styles.webviewstyle}
              source={{uri:Api.imageUrl+'/index.php/sys_agree_detail/content/confirm',method: 'GET'}}
              javaScriptEnabled={true}
              domStorageEnabled={true}
              onNavigationStateChange={this._onNavigationStateChange}
            />

            </ScrollView>
            <View style={styles.groupButView}>
              <TouchableOpacity style={styles.cancelbut} onPress={this._noagree}>
                <Text style={styles.textsize}>取消</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.agreeBut} onPress={this._agree}>
                <Text style={[styles.textsize,{color:'#fff'}]}>同意</Text>
              </TouchableOpacity>
            </View>
        </View>
      </View>
  );
}
_onBack=()=>{
  if(this.state.data.fromto==0){
    this._skip();
  }else {
    Api.goBack();
  }
  return true;
}
_call=()=>{
  makePhoneCall("0592968870");
}



render(){
  console.log(this.state.hasNfc+this.state.data.fromto)
  let placeholder = this.state.hasNfc?"请输入e通卡卡号或贴卡读取":"请输入e通卡卡号"
  return (
    <View style={[CommonStyle.container,{backgroundColor:'#fff'}]}>
        <TitleBar title={"开通挂失"} isNoleft={this.state.data.fromto==FROM_REAL&&true} renderRight={this.state.data.fromto==FROM_REAL&&this._renderRight} onBack={this._onBack}/>
        <ScrollView>
        {renderHeadPng()}
        <View style={styles.solid}></View>
        {renderHeadMsg()}

          {this.state.hasNfc?<View style={styles.imageView}>
            <Image source={require('./images/NFC.png')} style={styles.nfc}/>
          </View>:<View style={{padding:40}}/>}
          <View>
            <FormInput
              require={true}
              label="卡号"
              ref="cardId"
              value={this.state.cardId}
              placeholder={placeholder}
              onChange={(cardId)=>{this.setState({cardId:cardId,nfc:false})}}
              inputStyle={styles.inputStyle}
              hasBottom={true}
              hasHead={true} />
            <ECardSelector onSelectECard={this.onSelectECard} style={styles.Verification_code} onPress={this._select}>
             <Text style={[StandardStyle.h4,StandardStyle.fontBlack,]}>选择</Text>
             <Image source={require('./images/bottom.png')} style={{ width:12, height:8, marginLeft:5,}}/>
           </ECardSelector>
            </View>
            <CheckBox
              checkboxStyle={styles.checkboxStyle}
              containerStyle ={styles.containerStyle}
              labelBe='同意'
              label='《开通挂失服务须知》'
              labelBeStyle={[StandardStyle.h4,StandardStyle.fontBlack]}
              labelStyle={[StandardStyle.h4,StandardStyle.fontBlack,{color:'#e8464c'}]}
              checked={this.state.checked}
              urlPress={this._url}
              onChange={(checked) => {this.setState({checked:!this.state.checked});console.log(checked)}}
            />

          <LoadingButton
            loading={this.state.submiting}
            text={"提交"}
            onPress={this.onSubmit}
            disabled={this.state.submiting}
            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
            textStyle={loadingButton.loadingButtonText} />
          <View style={{marginBottom:200}}></View>
      </ScrollView>
      {this.state.alertConfirm&&<ConfirmAlert noagree={this._noagree} agree={this._agree}/>}

    </View>
  );
}
}
const styles=StyleSheet.create({
  Top:{
    justifyContent:'center',
    alignItems:'center',
  },
  CharacterImage:{
    position:"absolute",
    // backgroundColor:'transparent',
    top:SCREEN_HEIGHT*0.25/2-30,
    left:SCREEN_WIDTH/2-40,
    right:SCREEN_WIDTH/2-40,
  },
  solid:{
    backgroundColor:"#2e6b85",
    padding:3,

  },
  Recharge_text:{
    position:"absolute",
    left:20,
    // top:8,
    paddingRight:15,
    backgroundColor:'transparent',
  },
  select:{
    padding:20,
    marginTop:20,
    flexDirection:"row",
    },
  bottom:{
      alignItems:'center',
      alignSelf:"center",
      width:SCREEN_WIDTH*0.9,
      padding:10,
      backgroundColor:"#e8464c",
      marginTop:60,
      position:"relative",
      bottom:30,
    },
    contruStyle:{
      color:'#fff',
    },
    butStyle:{
      bottom:-100,

    },
    Verification_code:{
     position:"absolute",
     top:0,
     padding:20,
     right:0,
     flexDirection:"row",
     justifyContent:'center',
     alignItems:'center',
   },
   backgroundStyle:{
      width:SCREEN_WIDTH,
      height:SCREEN_HEIGHT*0.25,
   },
   cardImage:{
      width:80,
      height:60,
      resizeMode:'contain'
   },
   backBotom:{
     width:SCREEN_WIDTH,
     height:120,
     justifyContent:'center',
    // padding:15,
   },
   imageView:{
     padding:40,
     alignItems:'center'
   },
   nfc:{
      width:100,
      height:50,
      resizeMode:'contain'
   },
   checkboxStyle:{
     width:15,
     height:15,
     padding:10
   },
   containerStyle:{
     paddingLeft:15,
     paddingTop:20,
     paddingBottom:10
   },
   inputStyle:{
     marginLeft:5,
     height:60,
     width:SCREEN_WIDTH*0.63,
     fontSize:16,
   },
   confirmView:{position:'absolute',zIndex:100,width:SCREEN_WIDTH,height:SCREEN_HEIGHT,alignItems:'center',backgroundColor:'rgba(0,0,0,0.6)'},
   confirmViewAb:{position:'absolute',zIndex:2,top:50,left:20,width:SCREEN_WIDTH-40,height:SCREEN_WIDTH*1.45,backgroundColor:'#fff',borderRadius:10,alignItems:'center'},
   titleView:{flexDirection:'row',alignItems:'center',padding:15},
   titletext:{fontSize:20,color:'#000',paddingLeft:10,paddingRight:10},
   webviewstyle:{backgroundColor:'transparent',width:SCREEN_WIDTH-70,height:800,padding:5,flex:1,alignItems:'center'},
   groupButView:{flexDirection:'row',justifyContent:'space-between',height:60,bottom:0,width:SCREEN_WIDTH-40,borderTopWidth:1,borderColor:'#ccc'},
   cancelbut:{flex:1,justifyContent:'center',alignItems:'center'},
   agreeBut:{flex:1,justifyContent:'center',alignItems:'center',borderLeftWidth:1,borderColor:'#ccc',backgroundColor:'#ff5454',borderBottomRightRadius:10},
   textsize:{fontSize:18,color:'#000'},
})
