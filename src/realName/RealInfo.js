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
import {formatDate} from '../lib/StringUtil'
import {
	radioButton,
	loadingButton,
	loadingButtonDisabled
} from '../GlobalStyle'
import {
  TitleBar,
} from '../Global'
const SORT_VALUES = [1,2,3,4];
const SORT_LABELS = ['身份证','台胞证','港澳居民身份证','外籍护照'];
const picBg = require('./images/nopic.png');
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;
import StandardStyle from '../lib/StandardStyle'
import {lineView} from './realNameUtils'
import Notifier from '../../lib/Notifier'

export default class RealInfo extends Component{
  constructor(props){
    super(props);
    this.state={};
    this.form = new Form();

  }
  componentDidMount(){
    // Api.api({
    //   api:"real/info",
    //   crypt:3,
    //   success:(result)=>{
    //     console.log(result);
    //     this.setState(result);
    //   }
    // });
    Api.detail(this,{api:"real/info",crypt:3,});
    var url =  Api.getRoutes()[Api.routeCount()-1].location.pathname;
    console.log(url)
    Notifier.addObserver("androidBack",this._onBack);
  }
  componentWillMount() {

  }
  componentWillUnmount(){
    Notifier.removeObserver("androidBack",this._onBack);
  }
  _submit=()=>{
    // let self = this.state;
    // console.log(self);
    if(!this.state.amt){
        A.alert("请输入查询到的金额");
        return;
    }
    let amt = parseInt(this.state.amt);
    let data = {amt:amt};
    if(!isNaN(amt)){
      Api.api({
        api:"real/validateBankCard",
        crypt:3,
        data:data,
        success:(result)=>{
          console.log(result);
        }
      });
    }else{
      A.alert("请输入数字");
    }
  }
  messageView(tip,msg,hasBottom){
    return(
      <View>
      <View style={styles.main}>
        <Text style={[StandardStyle.h4,StandardStyle.fontBlack]}>{tip}</Text>
        <Text style={[StandardStyle.h4,StandardStyle.fontBlack]}>{msg}</Text>
      </View>
      {hasBottom&&lineView()}
      </View>
    );
  }
  // <TouchableOpacity style={styles.ToView}>
  //   <Text style={[StandardStyle.h5,StandardStyle.fontWhite,{padding:5}]}>查看实名权益</Text>
  // </TouchableOpacity>
  _onBack=()=>{
    console.log("正在返回")
      let arr = Api.getRoutes();
      for(let i= arr.length-1; i >=0; --i){
        let vo = arr[i];
        if(vo.root){
          Api.returnTo('/');
          return true;
        }
        if(vo.location===undefined){
          break;
        }
        if(vo.location.pathname=='/msgenter'){
          Api.returnTo('/msgenter');
          return true;
        }
        else if(vo.location.pathname=='/'){
          Api.returnTo('/');
          return true;
        }
        else if(vo.location.pathname=="/personal/center"){
           Api.returnTo("/personal/center");
          return true;
        }

      }
      return true;
    }
    // <View style={styles.Real}>
    //   <Text style={[StandardStyle.h4,StandardStyle.fontWhite,]}>我已通过实名认证</Text>
    //
    //
    // </View>
  render(){
    let self = this.state
    return <View style={[CommonStyle.container,{backgroundColor:'#f0f0f0'}]}>

      <TitleBar title="认证信息" onBack={this._onBack}/>
      <ScrollView>
      <View style={styles.Top}>
        <Image source={require('./images/background.png')} style={{ width:SCREEN_WIDTH, height:200, }}/>

        <View style={styles.position}>
            <View style={styles.CharacterImage}>
              <Image source={require('./images/CharacterImage1.png')} style={{ width:80, height:98, }}/>
            </View>


        </View>
      </View>

      {this.messageView("姓        名",self.name,true)}
      {this.messageView("有效证件类型",SORT_LABELS[self.idType-1],true)}
      {this.messageView("证件号码",self.idCard,true)}
      {this.messageView("证件有效期至",self.validDate?formatDate(self.validDate):null,true)}
      {this.messageView("手机号码",self.phone,true)}
    </ScrollView>

  </View>
  }
}
const styles=StyleSheet.create({
  Top:{
   width:SCREEN_WIDTH,
   height:200,
  //  marginTop:45,
  },

 position:{
   position:"absolute",
   top:0,
   marginLeft:SCREEN_WIDTH*0.3,
   width:SCREEN_WIDTH*0.4,
   height:200,
   alignItems:'center',
   justifyContent:'center',

 },

 CharacterImage:{
   width:SCREEN_WIDTH*0.4,
   height:100,
   position:"absolute",
   top:35,
   alignItems:'center',
   justifyContent:'center',
   alignSelf:"center",

 },
 Real:{
   width:SCREEN_WIDTH*0.4,
   height:50,
   position:"absolute",
   top:110,
   justifyContent:'center',
   alignItems:'center',
   alignSelf:"center",
   backgroundColor:'transparent',
   marginTop:10
 },

 ToView:{
   width:SCREEN_WIDTH*0.4,
   backgroundColor:"#518faa",
   justifyContent:'center',
   alignItems:'center',

 },
 main:{
   padding:20,
   flexDirection:"row",
   justifyContent:"space-between",
  //  borderBottomWidth:1,
  //  borderBottomColor:'#d7d7d7',
 },

})
