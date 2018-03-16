
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

const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;
import WebUtil from '../../lib/WebUtil'

const picPlaceHolder = require('../images/s_ic_add_diy.png');

export default class RCardMessage extends Component{

constructor(props) {
  super(props);
  // let hasNfc;
  // if(Platform.OS=='android'){
  //   NfcUtil.isAvailable((result)=>{
  //     console.log(result);
  //     hasNfc = result;
  //   });
  // }
  this.state={checked:true,hasNfc:false};
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
  if(Platform.OS=='android'){
    NfcUtil.isAvailable((result)=>{
      this.setState({hasNfc:result});
    });
  }
}

componentWillUnmount() {
  Notifier.removeObserver("nfcTag",this.onNfc);
}

onSuccess=(result)=>{
  console.log(result);
  // let data = {cardId:this.state.cardId,nfc:this.state.nfc};
  let data = {cardId:result.cardId,nfc:this.state.nfc};
  if (result.named) {
    // Api.push("/realName/rCardMessageSubmit/"+this.state.cardId);
    Api.push("/realName/rCardMessageSubmit/"+JSON.stringify(data));
  }else {
    Api.push("/realName/noRealMessage/"+JSON.stringify(data))
  }
}

onSubmit=()=>{
  if(!this.state.checked){
    A.alert("请认真阅读并同意e通卡APP实名用户账号绑卡须知");
    return;
  }
  let data = {version:Api.version};
  this.form.submit(this,{
    api:'rcard/info',
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

render(){
  console.log(this.state.hasNfc)
  let placeholder = this.state.hasNfc?"请输入e通卡卡号或贴卡读取":"请输入e通卡卡号"
  return (
    <View style={[CommonStyle.container,{backgroundColor:'#fff'}]}>
        <TitleBar title="新增实名绑卡" />
        <ScrollView>
        <Image style={{width:SCREEN_WIDTH,height:SCREEN_HEIGHT*0.25,justifyContent:'center',alignItems:'center'}} source={require('./images/background_4.png')}>
          <View>
            <Image source={require('./images/card_4.png')} style={styles.cardImage}/>
          </View>
        </Image>
        <View style={styles.solid}></View>
        <Image source={require('./images/background.png')} style={styles.backBotom}>
          <View style={{backgroundColor:'transparent',padding:5}}>
              <Text style={styles.contruStyle}>实名绑卡说明:</Text>
              <Text style={[StandardStyle.h5,StandardStyle.fontWhite,{paddingRight:5}]}>1. 一个已实名认证的APP账号最多可同时绑定5张电子钱包e通卡,绑定成功且卡状态正常的e通卡方可办理挂失业务。</Text>
              <Text style={[StandardStyle.h5,StandardStyle.fontWhite,{paddingRight:5}]}>2.为了您e通卡资金的安全，请尽快进行电子钱包卡实名绑定。</Text>
          </View>
        </Image>

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
              label='《实名绑卡须知》'
              labelBeStyle={[StandardStyle.h4,StandardStyle.fontBlack]}
              labelStyle={[StandardStyle.h4,StandardStyle.fontBlack,{color:'#e8464c'}]}
              checked={this.state.checked}
              urlPress={this._url}
              onChange={(checked) => {this.setState({checked:!this.state.checked});console.log(checked)}}
            />

          <LoadingButton
            loading={this.state.submiting}
            text="下一步"
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

})
