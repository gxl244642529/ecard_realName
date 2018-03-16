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
import {FormItem,FormSelect,FormImage} from '../../lib/TemplateForm'
import FormInput from './StandardFormInput'
import LoadingButton from '../widget/LoadingButton'
import ImagePicker from '../../lib/ImagePicker'
import StandardStyle from '../lib/StandardStyle'
import {lineView} from './realNameUtils'
import {BANK_CARD_VERIFY_ERROR_MSG,BANK_CARD_VERIFY_TO_MANY_ERROR_MSG} from './RealI18N'

import {
	radioButton,
	loadingButton,
  loadingButtonReal,
  loadingButtonRealDisabled,
  container
} from '../GlobalStyle'
import {
  TitleBar,
} from '../Global'
const SORT_VALUES = [0,1];
const SORT_LABELS = ['身份证','户口薄'];
const picBg = require('./images/nopic.png');
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;

function isNumber(input)
{
     var re = /^[0-9]+.?[0-9]*$/;   //判断字符串是否为数字     //判断正整数 /^[1-9]+[0-9]*]*$/
     return re.test(input);
}
export default class BankCheck extends Component{
  constructor(props){
    super(props);
    this.state={};
    this.form = new Form();

  }
  componentDidMount(){
    Api.api({
      api:"real/bankCardInfo",
      crypt:3,
      success:(result)=>{
        console.log(result);
        this.setState(result);
      }
    });
  }
  _submit=()=>{
    // let self = this.state;
    // console.log(self);
    let amt = this.state.amt;
    if(!amt){
        A.alert("请输入查询到的金额");
        return;
    }

    if(!isNumber(amt)){
      A.alert("请输入数字");
      return;
    }

    let data = {amt:parseInt(amt*100)};
    Api.api({
      api:"real/validateBankCard",
      crypt:3,
      data:data,
      success:(result)=>{
        console.log(result);
        if (result === 0) {


          Api.replace("/realName/realInfo");
          A.toast("实名化app成功!");

        }else if(result === 1){
          A.alert("金额验证失败！请重新填写银行信息",()=>{
            let data = {error:BANK_CARD_VERIFY_ERROR_MSG};
              // Api.replace("/realName/bankMessage/"+BANK_CARD_VERIFY_ERROR_MSG);
              Api.replace("realName/bankMessage/"+JSON.stringify(data))
          });
        }else if(result === 2){
          A.alert("金额验证失败！请重新填写银行信息",()=>{
            let data = {error:BANK_CARD_VERIFY_TO_MANY_ERROR_MSG};
              // Api.replace("/realName/bankMessage/"+BANK_CARD_VERIFY_TO_MANY_ERROR_MSG);
                Api.replace("realName/bankMessage/"+JSON.stringify(data))
          });
        }
      }
    });

  }
// <Text style={[StandardStyle.h4,StandardStyle.fontBlack]}>e通卡于{this.state.time}向您卡号为{this.state.bankCard}的银行账户转入小于一笔0.1元人民币。请将查询到的金额填入到下方金额输入框，进行验证！</Text>
  render(){
    return <View style={[CommonStyle.container,container.container]}>

      <TitleBar title="银行卡验证"/>
      <ScrollView>
        <View style={styles.top}>
            <Image source={require('./images/img4.png')} style={styles.processImage}/>
        </View>
        {lineView()}
        <View style={styles.main}>
          <Image source={require('./images/bankface.png')} style={styles.tipImage}/>
          <Text style={[StandardStyle.h3,StandardStyle.fontBlack,{marginTop:10}]}>温馨提示</Text>
          <Text style={[StandardStyle.h4,StandardStyle.fontBlack,{marginTop:10}]}>为确保银行卡为您本人持有，我们会在人工审核通过后向您的银行卡汇入一笔验证金额，请及时查看您的银行卡收款信息，并填写收到的金额进行验证。</Text>

       </View>
        <FormInput
          label="金额(元):"
          placeholder={"请输入查询到的金额(元)"}
          ref="amt"
          onChange={(amt)=>{this.setState({amt})}}
          hasBottom={true}
          hasHead={true}/>
        <View style={styles.butStyle}>
          <LoadingButton
            loading={this.state.submiting}
            text="提交"
            onPress={this._submit}
            disabled={this.state.submiting}
            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
            textStyle={loadingButton.loadingButtonText} />
        </View>
        <View style={{marginBottom:200}}/>
    </ScrollView>
  </View>
  }
}
const styles=StyleSheet.create({
  top:{
   justifyContent:'center',
   alignItems:'center',
   marginTop:25,
   marginBottom:25,
 },
  main:{
    padding:20,
    // marginTop:30,
    marginBottom:20,
    justifyContent:'center',
    alignItems:'center',
    // backgroundColor:"#ededee",
  },
  main_text:{
    marginTop:20,
    padding:15,
    flexDirection:"row",
    justifyContent:"space-between",
    borderTopWidth:1,
    borderTopColor:'#d7d7d7',
    borderBottomWidth:1,
    borderBottomColor:'#d7d7d7',
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
  item:{
    marginLeft:10,
    marginRight:10,
    marginTop:5,
    borderRadius:5,
    borderTopWidth:1,
    borderTopColor:'#d7d7d7',
  },
  butStyle:{
    // bottom:-200,
      marginTop:50,
  },
  processImage:{
    //  width:300,
    width:SCREEN_WIDTH*0.9,
     height:65,
     resizeMode:'contain',
  },
  tipImage:{
    width:50,
    height:50,
    resizeMode:'contain'
  }

})
