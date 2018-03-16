import React,{Component} from 'react'

import{
  View,
  Text,
  Image,
  StyleSheet,
  Api,
  A,
  CommonStyle,
  TextInput,
  DatePicker,
  TouchableOpacity,
  Form,
  Dimensions,
  ScrollView
} from '../../lib/Common'
import {FormInput,FormItem,FormSelect,FormImage} from '../../lib/TemplateForm'
import LoadingButton from '../widget/LoadingButton'
import ImagePicker from '../../lib/ImagePicker'
import {
	radioButton,
	loadingButton,
	// loadingButtonDisabled
  loadingButtonReal,
  loadingButtonRealDisabled
} from '../GlobalStyle'
import {container}from '../GlobalStyle'
import {
  TitleBar,
} from '../Global'
import StandardStyle from '../lib/StandardStyle'
const SORT_VALUES = [0,1];
const SORT_LABELS = ['身份证','户口薄'];
const picBg = require('./images/nopic.png');
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;
// import CheckBox from 'react-native-checkbox'
import CheckBox from '../../lib/checkbox'
import WebUtil from '../../lib/WebUtil'

export default class RealLead extends Component{
  constructor(props){
    super(props);
    this.state={checked:true};
    this.form = new Form();

  }
  _submit=()=>{
    if (!this.state.checked) {
      A.alert("请认真阅读并同意e通卡APP用户账号实名认证协议");
      return;
    }
    // Api.replace("/realName/messageSubmit");
      Api.replace("/realName/newMessageSubmit");
  }
  _url=()=>{
    console.log("链接到指定位置");
    WebUtil.open(Api.imageUrl + '/index.php/sys_agree_detail/content/realverify',"");
  }



  render(){
    return <View style={CommonStyle.container}>

      <TitleBar title="身份认证"/>
      <ScrollView>
        <View style={styles.Top}>
          <Image source={require('./images/background.png')} style={styles.backgroundStyle}/>
          <TouchableOpacity style={styles.CharacterImage} onPress={this._submit}>
            <Image source={require('./images/head.png')} style={styles.headImage}/>
          </TouchableOpacity>
        </View>

        <View style={styles.solid}></View>
        <View>
          <Image source={require('./images/background.png')} style={{ width:SCREEN_WIDTH, height:120,}}/>

          <View style={styles.Recharge_text}>
              <Text style={styles.contruStyle}>身份认证说明:</Text>
              <Text style={[StandardStyle.h5,StandardStyle.fontWhite]}>1.身份认证时需要核实您的有效身份信息，认证通过后即可享受挂失服务。</Text>
              <Text style={[StandardStyle.h5,StandardStyle.fontWhite]}>2.身份认证时需要验证您的银行卡信息，卡片挂失后的余额将返回至您绑定的银行卡。</Text>

          </View>
      </View>
      <CheckBox
        checkboxStyle={styles.checkboxStyle}
        containerStyle ={styles.containerStyle}
        labelBe='同意'
        label='《身份认证协议》'
        labelBeStyle={[StandardStyle.h4,StandardStyle.fontBlack]}
        labelStyle={[StandardStyle.h4,StandardStyle.fontBlack,{color:'#e8464c'}]}
        checked={this.state.checked}
        urlPress={this._url}
        onChange={(checked) => {this.setState({checked:!this.state.checked});console.log(checked)}}
      />

        <LoadingButton
          loading={this.state.submiting}
          text="立即认证"
          onPress={this._submit}
          disabled={this.state.submiting}
          style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
          textStyle={loadingButton.loadingButtonText} />

    </ScrollView>
  </View>
  }
}
const styles=StyleSheet.create({
  Top:{
    justifyContent:'center',
    alignItems:'center',
  },
  CharacterImage:{
    position:"absolute",
    backgroundColor:'transparent',
    top:SCREEN_HEIGHT*0.25/2-40,
    left:SCREEN_WIDTH/2-40,
    right:SCREEN_WIDTH/2-40,
  },
  solid:{
    backgroundColor:"#2e6b85",
    padding:3,

  },
  Recharge_text:{
    position:"absolute",
    left:15,
    // top:8,
    // padding:5,
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
      // bottom:-100,

    },
    headImage:{
       width:80,
       height:80,
    },
    backgroundStyle:{
       width:SCREEN_WIDTH,
       height:SCREEN_HEIGHT*0.25,
    },
    containerStyle:{
      paddingLeft:15,
      paddingTop:30,
      paddingBottom:30
    },
    checkboxStyle:{
      width:15,
      height:15,
      padding:10
    }

})
