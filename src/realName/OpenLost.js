
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
import {BackAndroid} from 'react-native'
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;
import WebUtil from '../../lib/WebUtil'
import {skip,renderHeadPng,renderHeadMsg} from './OpenLostConfirm'
// import  {BANK_CARD_FUNDING ,FINISH} from './RealI18N'
import {FROM_REAL,FROM_REAL_CARD,BANK_CARD_FUNDING ,FINISH} from './CommonData'
import {Jump} from './OpenLostUtil'



const picPlaceHolder = require('../images/s_ic_add_diy.png');
const TYPE = [{name:"普通卡",type:0},{name:"优惠卡",type:1}];

class   Option extends Component{
  constructor(props){
    super(props);
    this.state={}
  }
  render(){
    return <View><TouchableOpacity style={[StandardStyle.row,{alignItems:'center',padding:25,marginLeft:25}]} onPress={this.props.onPress}>

      {this.props.keyindex==this.props.onClickindex? <Image source={require('./images/radiose.png')} style={{width:25,height:25,}} resizeMode="contain"/>:
      <Image source={require('./images/radionose.png')} style={{width:25,height:25}} resizeMode="contain"/>}
      <Text style={{paddingLeft:10,fontSize:16}}>{this.props.payText}</Text>

    </TouchableOpacity>
    <View style={styles.lineView}/>
    </View>
  }
}

export default class OpenLost extends Component{

constructor(props) {
  super(props);
  // let hasNfc;
  // if(Platform.OS=='android'){
  //   NfcUtil.isAvailable((result)=>{
  //     console.log(result);
  //     hasNfc = result;
  //   });
  // }
  let data = JSON.parse(props.params.json)
  // console.log(data)
  this.state={checked:false,hasNfc:false,iscommon:true,index:0,data:data};
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
isNfcjump=()=>{
  if(this.state.hasNfc){
    // Api.push("/realName/nfcVertify/"+this.state.cardId);
    let data = {cardId:this.state.cardId,fromto:this.state.data.fromto};
    Api.push("/realName/nfcVertify/"+JSON.stringify(data));
  }else {
    // Api.push("/realName/questionVertify/"+this.state.cardId);
    let data = {cardId:this.state.cardId,fromto:this.state.data.fromto};
    Api.push("/realName/questionVertify/"+JSON.stringify(data));
  }
}
_realProcess=(result)=>{
  //tru实际绑卡成功，提示实名绑卡成功
  //false 绑卡信息提交成功，（1、实名成功==》卡验证界面  2、实名中=》审核页面）
  if(result&&this.state.data.isReal==FINISH){
    A.alert("身份认证成功，开通挂失成功！");
    Api.push('/realName/realInfo');
  }else if (!result&&this.state.data.isReal==FINISH) {
    //验证界面
    this.isNfcjump();
  }else if (!result&&this.state.data.isReal==BANK_CARD_FUNDING) {
    Api.push('/realName/verifyProcess');
  }
}

_realCardProcess=(result)=>{
  if(result){
    A.alert("开通挂失成功！");
    Api.push('/realName/rCard');
  }else {
    this.isNfcjump();
  }
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

onSubmit=()=>{
  if(!this.state.checked){
    A.alert("请认真阅读并同意开通挂失服务须知");
    return;
  }
  let data = {isNfc:this.state.nfc,isAgree:false};
  this.form.submit(this,{
    api:'newRcard/submitCardInfo',
    data:data,
    success:this.onSuccess
  });
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
  skip(this.state.data.isReal);
}
_renderRight=()=>{
  return <Jump skip={this._skip}/>
}
_changeIndex=(row,index)=>{
  console.log(row,index)
  this.setState({index:index});
}
_onBack=()=>{
  if(this.state.data.fromto==FROM_REAL){
    this._skip();
  }else {
    Api.goBack();
  }
  return true;
}

render(){
  console.log(this.state.hasNfc)
  let placeholder = this.state.hasNfc?"请输入e通卡卡号或贴卡读取":"请输入e通卡卡号"
  return (
    <View style={[CommonStyle.container,{backgroundColor:'#fff'}]}>
        <TitleBar title={"开通挂失"} isNoleft={this.state.data.fromto==FROM_REAL&&true} renderRight={this.state.data.fromto==0&&this._renderRight} onBack={this._onBack}/>
        <ScrollView>
        {renderHeadPng()}
        <View style={styles.solid}></View>
        {renderHeadMsg()}

          {this.state.hasNfc&&<View style={styles.imageView}>
            <Image source={require('./images/NFC.png')} style={styles.nfc}/>
          </View>}
          <View style={{flexDirection:"row"}}>
          {TYPE&&TYPE.map((row,index)=>{
              return(
                <Option
                  key={index}
                  payText={row.name}
                  onClickindex={this.state.index}
                  keyindex={index}
                  onPress={()=>this._changeIndex(row,index)}/>
              );
          })}
          </View>
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
            {this.state.index==1&&<FormInput
              require={true}
              label="证件号码"
              ref="idCard"
              value={this.state.idCard}
              placeholder={"请输入卡号对应的证件号码"}
              onChange={(idCard)=>{this.setState({idCard:idCard})}}
              inputStyle={styles.idCardInput}
              hasBottom={true}
              hasHead={true} />}
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
   idCardInput:{
     marginLeft:5,
     height:60,
     width:SCREEN_WIDTH*0.8,
     fontSize:16,
   }

})
