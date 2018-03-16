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
import {FormInput,FormItem,FormSelect,FormImage} from '../../lib/TemplateForm'
import LoadingButton from '../widget/LoadingButton'
import ImagePicker from '../../lib/ImagePicker'
import {formatDate} from '../lib/StringUtil'
import StandardStyle from '../lib/StandardStyle'
import ReportProcess  from './ReportProcess'
import {lineView,MsgView} from './realNameUtils'
import {makePhoneCall} from '../lib/CommonUtil'

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
const picBg = require('./images/nopic.png');
const NORMAL = 0; //正常
const LOST  = 1;  //报失中
const FUNDING  = 2; //打款中
const FUND_SUCCESS  = 3; //打款成功
const FUND_ERROR = 4;//打款失败
const RESULT_LACKOFMONEY = 99//核销余额不足

const TIP_1 = "正在对您的卡进行挂失处理，请耐心等待"
// const TIP_2 = "通过清算截止"
const TIP_2 = "挂失处理完毕,"
const TIP_3 = "卡内余额为"
const TIP_4 = "元"




export default class RCardDetail extends Component{
  constructor(props){
    super(props);
    this.state={};
    this.form = new Form();

  }
  componentDidMount(){
    let data = {cardId:this.props.params.id};
    // Api.api({
    //   api:"rcard/detail",
    //   data:data,
    //   success:(result)=>{
    //     console.log(result);
    //     this.setState(result);
    //   }
    // });
    Api.detail(this,{api:"rcard/detail",data:data});

  }
  componentWillUnmount(){

  }
  _submit=()=>{
    Api.push("/realName/rCardLost/"+this.props.params.id);

  }

  onSubmit=()=>{
    // console.log("重新报失");
    Api.push("/realName/rCardLost/"+this.props.params.id);
  }
  renderProgress(){
    return(
      this.state.lostStatus===NORMAL&&  <View style={styles.butStyle}>
          <LoadingButton
            loading={this.state.submiting}
            text="挂失"
            onPress={this._submit}
            disabled={this.state.submiting}
            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
            textStyle={loadingButton.loadingButtonText} />
        </View>
    );
  }
  relost(){
    return<LoadingButton
            loading={this.state.submiting}
            text="重新挂失"
            onPress={this.onSubmit}
            disabled={this.state.submiting}
            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
            textStyle={loadingButton.loadingButtonText} />
  }
  callPhone=()=>{
    makePhoneCall("0592968870");
  }

  render(){
    let self = this.state
    let lost = TIP_2+TIP_3+(this.state.clearAmt*0.01).toFixed(2)+TIP_4;
    let name = "姓名:"+self.name;
    let card = "收款银行卡号: "+self.bankCard;
    let failReason = ",原因:"+self.clearError;
    return <View style={[CommonStyle.container,container.container]}>

      <TitleBar title="挂失服务详情" onBack={()=>Api.goBack()} />
      <ScrollView>
        <MsgView tip="卡号" msg={self.cardId} />
        <MsgView tip="开通挂失时间" msg={self.createDate?formatDate(self.createDate):null} />
        <MsgView tip="姓名" msg={self.bindName} />
        <MsgView tip="证件号码" msg={self.bindIdCard} />

          {this.renderProgress()}

          {this.state.lostStatus===LOST&&  <ReportProcess step={1} lost={TIP_1}
          status={0} /> }
          {this.state.lostStatus===FUNDING&&  <ReportProcess step={2}
          name={name} card={card} lost={lost}
          status={0}/>}
          {this.state.lostStatus===FUND_SUCCESS&&  <ReportProcess step={3}
          name={name} card={card} lost={lost}
          status={0}/>}
          {this.state.lostStatus===RESULT_LACKOFMONEY&&  <View>
            <ReportProcess step={3}
            name={name} card={card} lost={lost}
            status={1} failReason={failReason}/>

            </View>}
          {this.state.lostStatus===FUND_ERROR&&  <View>
            <ReportProcess step={3}
            name={name} card={card} lost={lost}
            status={1} failReason={failReason}/>

            {this.relost()}
            </View>}
          {this.state.lostStatus!=FUND_ERROR&& this.state.lostStatus!=NORMAL&&
            this.state.lostStatus!=RESULT_LACKOFMONEY&& this.state.lostStatus&& <TouchableOpacity style={styles.phoneStyle}
          onPress={this.callPhone}>
              <Text style={{fontSize:16,color:'#fff'}}>客服电话</Text>
            </TouchableOpacity>}

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
    padding:20,
    flexDirection:"row",
    justifyContent:"space-between",
    // borderBottomWidth:1,
    // borderBottomColor:'#d7d7d7',
    // backgroundColor:'#fff'
  },
  butStyle:{
    // bottom:-300
    marginTop:250
  },
  phoneStyle:{
    height:40,
    margin:20,
    backgroundColor:'#e8464c',
    justifyContent:'center',
    alignItems:'center'
  }

})
