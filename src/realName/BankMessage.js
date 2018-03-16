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
import {FormItem,FormImage} from '../../lib/TemplateForm'
import FormSelect from './StandardFormSelect'
import LoadingButton from '../widget/LoadingButton'
import ImagePicker from '../../lib/ImagePicker'
import FormInput from './StandardFormInput'
import StandardStyle from '../lib/StandardStyle'
import {lineView} from './realNameUtils'
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
import {LIAN_ID,LIAN_NAME} from './CommonData'
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;
import {BANKID_VALUES,BANKID_LABELS} from './CommonData'
import {VerifyCode,checkPhone} from './VerifyCodeUtil'
const picBg = require('./images/nopic.png');
import   CryptoJS from 'crypto-js'
import CheckBox from '../../lib/checkbox'
import WebUtil from '../../lib/WebUtil'
const TIME_OUT = 120000;
const MSG_IDCARD = "审核中，预计需要45秒，请您耐心等待或稍后回来。";
const MSG_OTHER = "正在提交...";
const CONFRIM_TEXT = "1、请再次核对您的银行卡号；\n2、请再次确认银行卡持卡人信息与真实姓名一致；\n3、卡片挂失后的余额将返回至您绑定的银行卡。";
export default class BankMessage extends Component{
  constructor(props){
    super(props);

    let state;
    if(props.params&&props.params.json){
       let json=props.params.json;
        state = {isBankJian:false,data:JSON.parse(json),editable:true,checked:false};
    }else {
       state = {isBankJian:false,editable:true,checked:false};
    }
    state.error = ( state.data && (state.data.error == "1" ? null : state.data.error )) || props.error;
    this.state=state;
    this.form = new Form();

  }
  componentDidMount(){
    Api.api({
      api:"real/info",
      crypt:3,
      success:(result)=>{
        console.log(result);
        if (result) {
          this.setState(result);
        }

      }
    });
    Api.api({
      api:"real/bindPhone",
      success:(result)=>{
        console.log(result);
        if (result) {
          this.setState({phone:result,editable:false})
        }
      }
    });
    this.getIsBank();
  }
  getIsBank=()=>{
    let data = {idType:this.state.data.idType,idCard:this.state.data.idCard};
    Api.api({
      api:"newRcard/isHXB",
      data:data,
      success:(result)=>{
        this.setState({isHXB:result});
      }
    });
  }
  componentWillUnmount() {
    clearInterval(this.timer);//FIXME:无法实现卸载时清除计时器
   }
  decCrypt=(str)=>{
    var parsedWordArray = CryptoJS.enc.Base64.parse(str);
    var parsedStr = parsedWordArray.toString(CryptoJS.enc.Utf8);
    return parsedStr;
  }
  loadMsg=()=>{
      console.log(this.state.checked);
      let self = this.state.data;
      let isAgree = self.idType==1?this.state.checked:true;//上传协议是否勾选
      data = {bankCard:this.state.bankCard,bankName:this.state.bankName,name:self.name,phone:this.state.phone,
      idCard:self.idCard,idType:parseInt(self.idType),validDate:self.validDate,imgType:self.imgType,
      verifyId:this.state.verifyId,code:this.state.code,bankId:this.state.bankId,bankNo:this.state.bankNo,isAgree:isAgree};
      // data = {bankCard:this.state.bankCard,bankName:this.state.bankName,name:self.name,phone:this.state.phone,
      // idCard:self.idCard,idType:parseInt(self.idType),validD ate:self.validDate,imgType:self.imgType,
      // verifyId:this.state.verifyId,code:this.state.code,bankId:this.state.bankId,bankNo:this.state.bankNo,img1:self.img1,img2:self.img2};
      return data;
  }
  //是否启用承诺书
  _isAgree(result){
    let flag = result.isAgree;
    // let data = {isReal:result.isReal}
    let isAgreeData = {fromto:0,isReal:result.realStatus};
    if(flag){
      //启用
      Api.push('/realName/openLostConfirm/'+JSON.stringify(isAgreeData))
    }else {
      //禁用
      Api.push('/realName/openLost/'+JSON.stringify(isAgreeData));
    }

  }
  _success=(result)=>{
    //实名失败，提示实名失败不跳转
    //实名成功提示实名成功根据是否启用承诺书跳转对应页面
    //实名中不提示，根据是否启用承诺书跳转对应页面
    let realStatus = result.realStatus;
    switch (realStatus) {
      case 4:
        A.alert("实名失败，请确定实名信息是否填写正确！")
        break;
      case 8:
        //实名中
        this._isAgree(result);
        break;
      case 88:
        // A.alert("实名成功！您可以继续开通实名挂失功能")
        this._isAgree(result);
        break;
      default:

    }

  }
  noErrorSubmit=()=>{
    let self = this.state.data;
    let selfthis = this.state
    let data = this.loadMsg();
    // A.alert("正在调用")
    // Api.push('/realName/openLostConfirm/'+1);
    let img1 = this.decCrypt(this.state.data.img1);
    let img2 = this.decCrypt(this.state.data.img2);
    Api.api({
      api:'newRcard/submitRealInfo',
      data:data,
      timeoutMs:TIME_OUT,
      waitingMessage:(self.idType==1&&selfthis.isHXB==true)?MSG_IDCARD:MSG_OTHER,
      files:{img1:img1,img2:img2},
      crypt:3,
      success:(result)=>{
          this._success(result);
          console.log(result);
      }
    });
  }
  errorSubmit=()=>{
    let data = {bankCard:this.state.bankCard,bankName:this.state.bankName};
      this.form.submit(this,{
          api:"real/saveBank",
          crypt:3,
          timeoutMs:TIME_OUT,    //超时设置30秒
          data:data,
          success:(result)=>{
            //这里并不一定是到实名化，如果有错误，则应该是到重新验证银行卡
            if(this.state.error){
               Api.replace('/realName/bankCheck');
            }else{
               Api.replace('/realName/VerifyProcess');
            }
          }
        });
  }

  _submit=()=>{
    //let data = {bankCard:this.state.bankCard,bankName:this.state.bankName};
    console.log(this.state.code)
    // if(this.state.code&&!this.state.code){
    //   A.alert("请输入验证码");
    //   return;
    // }
    if(!this.state.error&&!this.state.phone){
      A.alert("请输入手机号码");
       return;
    }
    if(!this.state.error&&!this.state.code){
      A.alert("请输入验证码");
       return;
    }
    if(!this.state.bankName){
      A.alert("请选择银行名称");
      return;
    }
    // if(this.state.bankNo&& !this.state.bankNo){
    //   A.alert("请选择分行名称");
    //   return;
    // }
    if(this.state.isBankJian&&!this.state.bankNo){
      A.alert("请选择分行名称");
        return;
    }
    if(!this.state.bankCard){
      A.alert("请输入与姓名一致的借记卡号");
      return;
    }
    if(this.state.isHXB){
      if(this.state.data.idType==1&&!this.state.checked){
        A.alert("请认真阅读并同意《个人电子账户产品服务协议》");
        return;
      }
    }


    A.confirm(CONFRIM_TEXT,(index)=>{
      if(index==0){
        if(!this.state.error){
          this.noErrorSubmit();
        }else{
          this.errorSubmit();
        }

      }
    });

  }
  _banckCheck=(bankId)=>{
    console.log("bankId="+bankId);
    if (bankId===4) {
      console.log("选择建行");
      this.setState({isBankJian:true,bankId:bankId});
    }else {
      this.setState({isBankJian:false,bankId:bankId});
    }
  }
  _setVerify=(verifyId)=>{
    console.log(verifyId);
    this.setState({verifyId:verifyId});
  }
  _url=()=>{
    WebUtil.open(Api.imageUrl + '/index.php/sys_agree_detail/content/open',"");
  }
 //  <View style={styles.main}>
 //   <Text style={[StandardStyle.h4,StandardStyle.fontBlack,]}>验证流程：</Text>
 //   <Text style={[StandardStyle.h5,StandardStyle.fontBlack]}>为确保银行卡为您本人持有，我们会在信息审核通过后向您的银行卡汇入一笔验证金额，请及时查看您的银行卡收款信息，并填写收到的金额进行验证。</Text>
 // </View>

  render(){
    let progressImage = this.state.error ? require('./images/img4.png'):require('./images/img1.png');
    let data = {phone:this.state.phone,idType:this.state.data.idType,idCard:this.state.data.idCard};
    return <View style={[CommonStyle.container,container.container]}>
      <TitleBar title="绑定银行卡"/>

      <ScrollView>
        <View style={styles.top}>
          <Image source={progressImage} style={styles.progressImageSty} resizeMode="contain"/>
        </View>

        {this.state.error&&<Text style={styles.errorText}>{this.state.error}</Text>}
        {!this.state.error&&<VerifyCode
            apiName="newRcard/verify"
            phone={this.state.phone}
            timeoutMs={TIME_OUT}
            sdata={data}
            phoneRef="phone"
            phoneValue={this.state.phone}
            phoneChange={(phone)=>{this.setState({phone})}}
            codeRef="code"
            codeValue={this.state.code}
            codeChange={(code)=>{this.setState({code})}}
            inputStyle={styles.inputStyle}
            verifyStyle={styles.verifyStyle}
            setVerify={this._setVerify}
            editable={this.state.editable}
          />}

        {lineView()}

        <FormInput
          label="真实姓名:"
          value={this.state.data?(this.state.data.name?this.state.data.name:this.state.name):this.state.name}
          hasBottom={true}
          editable={false}/>


        <FormSelect
        	label="银行名称:  "
        	placeholder="请选择银行名称"
        	ref="bankId"
        	values={BANKID_VALUES}
        	labels={BANKID_LABELS}
        	value={this.state.bankId}
        	onChange={(index,bankId,bankName)=>{this._banckCheck(bankId);this.setState({bankName:bankName})}}
          hasBottom={true}/>
        {this.state.isBankJian&&  <FormSelect
          	label="分行名称:"
          	placeholder="请选择分行名称"
          	ref="bankNo"
          	values={LIAN_ID}
          	labels={LIAN_NAME}
          	value={this.state.bankNo}
          	onChange={(index,bankNo)=>{this.setState({bankNo:bankNo})}}
            hasBottom={true}/>}

        <FormInput
          label="银行卡号:"
          placeholder={"请输入与姓名一致的借记卡号"}
          ref="bankCard"
          onChange={(bankCard)=>{this.setState({bankCard})}}
          hasBottom={true}/>
        <View style={styles.tipView}>
          <Text style={styles.tipText}>卡片挂失后的余额将返回至您绑定的银行卡</Text>
        </View>
        {(this.state.data.idType==1&&this.state.isHXB)&&<CheckBox
          checkboxStyle={styles.checkboxStyle}
          containerStyle ={styles.containerStyle}
          labelBe='同意'
          label='《个人电子账户产品服务协议》'
          labelBeStyle={[StandardStyle.h4,StandardStyle.fontBlack]}
          labelStyle={[StandardStyle.h4,StandardStyle.fontBlack,{color:'#0063b5'}]}
          checked={this.state.checked}
          urlPress={this._url}
          onChange={(checked) => {this.setState({checked:!this.state.checked});console.log(checked)}}
        />}

        <View style={styles.butStyle}>
          <LoadingButton
            loading={this.state.submiting}
            text="提交"
            onPress={this._submit}
            disabled={this.state.submiting}
            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
            textStyle={loadingButton.loadingButtonText} />
        </View>

        {(this.state.data.idType==1&&this.state.isHXB)&&<View style={{marginBottom:20,flexDirection:'row',alignItems:'center',justifyContent:'center'}}>
          <Text style={{color:'#ddd'}}>该服务由</Text>
          <Image source={require('./images/huaxia.png')} style={{width:18,height:18,paddingLeft:3,paddingRight:3}} resizeMode="contain"/>
          <Text style={{color:'#ddd'}}>华夏银行提供</Text>
        </View>}
        <View style={{marginBottom:250}}/>
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
  item:{
    marginTop:5,
  },
  main:{
   padding:20,
   marginTop:10,
   justifyContent:'center',
   alignItems:'center',
   backgroundColor:"#ededee",

 },
 name:{
   backgroundColor:'#ffffff',
   height:60,
   paddingRight:10,
   paddingLeft:15,
   alignItems:'center',
   flexDirection:'row',
  //  borderTopWidth:1,
  //  borderTopColor:'#d7d7d7',
  //  borderBottomWidth:1,
  //  borderBottomColor:'#d7d7d7',
   },
   butStyle:{
      marginTop:10,
   },

   realName:{
     color:'#666666',
     marginRight:10,
     fontSize:16
   },
   realNameText:{
     fontSize:16
   },
   progressImageSty:{
     width:300,
     height:65,
   },
   errorText:{
    //  padding:15
    marginLeft:15,
    marginBottom:15
  },
  inputStyle:{
    marginLeft:5,
    height:60,
    width:SCREEN_WIDTH*0.6,
    fontSize:16,
  },
  verifyStyle:{
    //  position:"absolute",
    //  top:22,
    //  right:20,
     position:"absolute",
     right:8,
     padding:20,
   },
   containerStyle:{
     paddingLeft:15,
     paddingTop:10,
     paddingBottom:10
   },
   checkboxStyle:{
     width:15,
     height:15,
     padding:10
   },
   tipView:{
     paddingLeft:15,paddingTop:5,paddingBottom:10
   },
   tipText:{
     fontSize:12,color:'#e8464c',fontWeight:'bold'
   }

})
