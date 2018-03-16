import React, { Component } from 'react';
import {
  View,
  ActivityIndicator,
  StyleSheet,
  CommonStyle,
  Account,
  Api,
  A,
  Text,
  TouchableOpacity
} from '../../lib/Common'
// import {login} from '../lib/LoginUtil'
import Notifier from '../../lib/Notifier'

import {BANK_CARD_VERIFY_ERROR_MSG,BANK_CARD_VERIFY_TO_MANY_ERROR_MSG} from './RealI18N'

const  SAVE_INFO = 0; //已经保存信息，跳转银行卡填写页面
const  SAVE_BANK_CARD = 1;// 保存银行卡，跳转审核页面
const  VERIFY_OK = 2; //审核通过，跳转银行卡验证界面   ////审核自动通过后，开始打款、进入打款流程，页面还是在审核信息页面
const  VERIFY_ERROR = 3; //审核失败，重新提交信息页
const  BANK_SEND_FAIL = 4; //银行卡没有发成功，银行卡信息填写错误，从信息提交页面开始
const  BANK_SEND_SUCCESS = 5; // 银行卡发成功，跳转到银行卡验证页面
const  BANK_CARD_VERIFY_ERROR = 6; //银行卡验证失败，再次跳转到银行信息填写页面
const BANK_CARD_VERIFY_ERROR_TO_MUCH = 7;
const BANK_CARD_FUNDING = 8;//银行卡打款中,跳转审核页面。。
const   FINISH = 88; // 已经提交(实名化完成)，跳转实名信息页面。。
import RealInfo from './RealInfo'
import RealLead from './RealLead'
import BankMessage from './BankMessage'
import MessageSubmit from './MessageSubmit'
import VerifyProcess from './VerifyProcess'
import VerifyProcessFail from './VerifyProcessFail'
import BankCheck from './BankCheck'
import NewMessageSubmit from './NewMessageSubmit'
import {
  TitleBar,
} from '../Global'


export default class OverTrans extends Component{

  constructor(props){
    super(props);
    this.state={netError:false,isTitle:true};
  }

  componentDidMount() {
    Notifier.addObserver("verify",this._loadRecent);
    this._loadRecent();
  }

  componentWillUnmount() {
     Notifier.removeObserver("verify",this._loadRecent);
  }



  _loadRecent=()=>{

    // Api.api({
    //   api:"real/info",
    //   crypt:3,
    //   waitingMessage:"",
    //   success:(result)=>{
    //     this.setState({netError:false,isTitle:false});
    //     console.log(result);
    //         if(!result){
    //           // Api.replace("/realName/realLead");
    //           this.setState({flag:null});
    //         }else {
    //           this.setState(result);
    //
    //         }
    //   }	,
    //   error:(error)=>{
  	// 			// A.alert("网络错误无");
    //       this.setState({netError:true});
    //
  	// 		}
    // });
    Api.api({
      api:"newRcard/realResult",
      crypt:3,
      waitingMessage:"",
      timeoutMs:60000,    //超时设置60秒
      success:(result)=>{
        this.setState({netError:false,isTitle:false});
        console.log(result);
            if(!result){
              // Api.replace("/realName/realLead");
              this.setState({flag:null});
            }else {
              this.setState(result);

            }
      }	,
      error:(error)=>{
          // A.alert("网络错误无");
          this.setState({netError:true});

        }
    });
  }
  // <View style={[CommonStyle.container,styles.content]}>
  //   <ActivityIndicator />
  // </View>
  // {data.isReal===BANK_CARD_VERIFY_ERROR_TO_MUCH && <BankMessage error={BANK_CARD_VERIFY_TO_MANY_ERROR_MSG} />}
        // {data.isReal===BANK_CARD_VERIFY_ERROR && <BankMessage error={BANK_CARD_VERIFY_ERROR_MSG} />}
  render(){
    let data = this.state;
    // console.log(data);
      // {data.flag===SAVE_INFO &&  <BankMessage />}
      {data.flag===BANK_CARD_VERIFY_ERROR && <BankMessage error={BANK_CARD_VERIFY_ERROR_MSG} />}
      {data.flag===BANK_CARD_VERIFY_ERROR_TO_MUCH && <BankMessage error={BANK_CARD_VERIFY_TO_MANY_ERROR_MSG} />}
      return (
        <View style={[CommonStyle.container,styles.content]}>
          {data.isTitle && <TitleBar title="认证信息"/>}
          {data.realStatus===null &&  <RealLead />}
          {data.realStatus===SAVE_INFO &&  <RealLead />}
          {data.realStatus===SAVE_BANK_CARD &&  <VerifyProcess />}
          {data.realStatus===VERIFY_OK &&  <VerifyProcess />}
          {data.realStatus===VERIFY_ERROR &&  <VerifyProcessFail  data={data.verifyError}/>}
          {data.realStatus===BANK_SEND_FAIL &&  <NewMessageSubmit errorMsg={data.errorMsg&&data.errorMsg}/>}
          {data.realStatus===BANK_SEND_SUCCESS &&  <VerifyProcess />}
          {data.realStatus===FINISH &&  <RealInfo />}
          {data.realStatus===BANK_CARD_VERIFY_ERROR &&  <VerifyProcess />}
          {data.realStatus===BANK_CARD_VERIFY_ERROR_TO_MUCH && <VerifyProcess /> }
          {data.realStatus===BANK_CARD_FUNDING && <VerifyProcess />}
          {data.netError &&	<View style={{flex:1}}>

                <TouchableOpacity style={styles.centering}
            			onPress={this._loadRecent}>
            			<Text style={{fontSize:15}}>网络错误</Text>
            			<Text style={{fontSize:13,marginTop:5}}>点击重试</Text>
            		</TouchableOpacity>
            </View>}

        </View>
      );

    }
}

const styles = StyleSheet.create({
  // content:{justifyContent:'center',alignItems:'center'}
  centering: {
    flex:1,
    alignItems: 'center',
    justifyContent: 'center'
  },
});
