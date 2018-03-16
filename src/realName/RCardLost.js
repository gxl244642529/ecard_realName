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
  ScrollView
} from '../../lib/Common'
import {FormItem,FormImage} from '../../lib/TemplateForm'
import LoadingButton from '../widget/LoadingButton'
import ImagePicker from '../../lib/ImagePicker'
import {formatDate} from '../lib/StringUtil'
// import CheckBox from 'react-native-checkbox'
import CheckBox from '../../lib/checkbox'
import FormInput from './StandardFormInput'
import FormSelect from './StandardFormSelect'
import {MsgView} from './realNameUtils'
import {LIAN_ID,LIAN_NAME} from './CommonData'
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
import MD5 from 'crypto-js/md5'
import StandardStyle from '../lib/StandardStyle'
import WebUtil from '../../lib/WebUtil'
const picBg = require('./images/nopic.png');
import {BANKID_VALUES,BANKID_LABELS} from './CommonData'
export default class RCardLost extends Component{
  constructor(props){
    super(props);
    this.state={checked:true,isChange:true,update:false,isBankJian:false};
    this.form = new Form();

  }
  componentDidMount(){
    let data = {cardId:this.props.params.id};
    Api.api({
      api:"rcard/preLost",
      data:data,
      success:(result)=>{
        console.log(result);
        this.setState(result);
        this.setState({tmpbank:result.bankCard});
        this.setState({tmpbankName:result.bankName});
        if (result.bankId===4) {
          this.setState({isBankJian:true});
        }
      }
    });
  }
  onSubmit=()=>{
    let cardId = this.props.params.id;
    // if(!this.state.pwd){
    //   A.alert("请输入登录密码");
    //   return;
    // }
    if (!this.state.checked) {
      A.alert("请认真阅读并同意e通卡挂失须知");
      return;
    }

    let data = {cardId:this.props.params.id,pwd:  MD5(this.state.pwd).toString(),update:this.state.update,
    bankName:this.state.bankName};
    A.confirm("本操作不可撤销，挂失以后该卡将不能继续使用，确认挂失?",(index)=>{
      if(index==0){

        this.form.submit(this,{
          api:"rcard/lost",
          data:data,
          success:(result)=>{
              A.toast("挂失成功");
              Api.replace("/realName/rCard");

            // Api.returnTo("realName/rCardList");
          }
        });
      }else{
        return;
      }
    });
  }
  _change=()=>{
    // this.setState({isChange:false,update:true});
    //  this.setState({isChange:false,update:true});
  }
  _url=()=>{
    console.log("链接到指定位置");
    WebUtil.open(Api.imageUrl + '/index.php/sys_agree_detail/content/reallost',"");
  }
  // <FormInput
  //     label="开户支行"
  //     ref="bankAddr"
  //     value={this.state.bankAddr}
  //     placeholder="示例:福建省厦门市"
  //     onChange={(bankAddr)=>{this.setState({bankAddr})}}
  //     hasBottom={true}
  //    />
  _banckCheck=(bankName)=>{
    console.log("bankName="+bankName);
    if (bankName==="建设银行") {
      console.log("选择建行");
      this.setState({isBankJian:true,bankName:bankName});
    }else {
      this.setState({isBankJian:false,bankName:bankName});
    }
  }
  _update=(update)=>{

    console.log(update);

    if(update){
      //非勾选则银行名称保持前面
      this.setState({bankName:BANKID_LABELS[this.state.bankId-1]})
      this.setState({bankCard:""})
    }else {
      //勾选则卡号为原卡号、银行名称为原名称
      this.setState({bankCard:this.state.tmpbank,bankName:this.state.tmpbankName});
      /**勾选则银行名称为原名称,卡号为原卡号*/
    }

  }

  render(){
    let self = this.state
    return <View style={[CommonStyle.container,container.container]}>

      <TitleBar title="挂失"/>
      <ScrollView>

      <MsgView tip="卡       号" msg={self.cardId} msgTo={self.cardIdExt}/>
      <MsgView tip="开通挂失时间" msg={self.createDate?formatDate(self.createDate):null} />
      <MsgView tip="姓       名" msg={self.bindName} />
      <MsgView tip="证件号码" msg={self.bindIdCard} />
        <FormInput
          label="登录密码"
          value={this.state.pwd}
          ref="pwd"
          secureTextEntry={true}
          placeholder="请输入登录密码"
          hasBottom={true}
          onChange={(pwd)=>{this.setState({pwd})}} />
        <View style={{flexDirection:'row',backgroundColor:'#f0f0f0'}}>
          <CheckBox
            checkboxStyle={{width:20,height:20}}
            containerStyle ={{marginLeft:10,marginTop:10,marginRight:10}}
            label='与身份认证时的银行卡信息一致'
            labelStyle={[StandardStyle.h3,StandardStyle.fontBlack]}
            labelLines={2}
            checked={!this.state.update}
            onChange={(update) => {this.setState({update:!this.state.update,});this._update(update)}}
          />

        </View>
        <FormInput
          label="姓      名"
          value={this.state.name}
          editable={false}
          hasBottom={true}
          hasHead={true}
          />


          {this.state.update?    <View>
              <FormSelect
                label="银行名称"
                placeholder="请选择银行名称"
                ref="bankId"
                values={BANKID_VALUES}
                labels={BANKID_LABELS}
                value={this.state.bankId}
              	onChange={(index,bankId,bankName)=>{this._banckCheck(bankName);this.setState({bankId})}}
                hasBottom={true} />

          {this.state.isBankJian&&  <FormSelect
            	label="分行名称"
            	placeholder="请选择分行名称"
            	ref="bankNo"
            	values={LIAN_ID}
            	labels={LIAN_NAME}
            	value={this.state.bankNo}
            	onChange={(index,bankNo)=>{this.setState({bankNo:bankNo})}}
              hasBottom={true}/>}
             <FormInput
               label="银行卡号"
               placeholder={"请输入姓名对应的银行卡号"}
               ref="bankCard"
               value={this.state.bankCard}
               onChange={(bankCard)=>{this.setState({bankCard})}}
               hasBottom={true}/>
              </View>:
          <View>
          <FormInput
            label="银行名称"
            value={this.state.tmpbankName}
            editable={false}
            hasBottom={true}
            hasHead={true}
            />
            <FormInput
              label="银行卡号"
              value={this.state.tmpbank}
              editable={false}
              hasBottom={true}/>
          </View> }


        <CheckBox
          checkboxStyle={{width:20,height:20}}
          containerStyle ={{marginLeft:10,marginTop:10}}
          labelBe='同意'
          label='《挂失须知》'
          labelBeStyle={[StandardStyle.h4,StandardStyle.fontBlack]}
          labelStyle={[StandardStyle.h4,StandardStyle.fontBlack,{color:'#e8464c'}]}
          checked={this.state.checked}
          urlPress={this._url}
          onChange={(checked) => {this.setState({checked:!this.state.checked});console.log(checked)}}
        />


          <LoadingButton
            loading={this.state.submiting}
            text="确认挂失"
            onPress={this.onSubmit}
            disabled={this.state.submiting}
            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
            textStyle={loadingButton.loadingButtonText} />
          <View style={{marginBottom:200}}/>
    </ScrollView>
  </View>
  }
}
const styles=StyleSheet.create({
  top:{
    //flex:1,
    height:160,
    backgroundColor:'#43b0b1',
    alignItems:'center',
    justifyContent:'center',
    marginBottom:10,
  },
  item:{
    marginLeft:10,
    marginRight:10,
    marginTop:5,
    borderRadius:5,
  },
  main:{
    padding:15,
    flexDirection:"row",
    justifyContent:"space-between",
    borderBottomWidth:1,
    borderBottomColor:'#d7d7d7',
    backgroundColor:'#fff'
  },
  changButStyle:{
    position:'absolute',
    top:15,right:15,
    backgroundColor:'#e8464c',
    width:50,
    height:30,
    alignItems:'center',
    justifyContent:'center',
    borderRadius:5,
  }
})
