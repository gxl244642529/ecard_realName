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
import  QrRequest from './QrRequest'
import QrMain from './QrMain'
import QrAccStatus from './QrAccStatus'
import QrAccount from './QrAccount'
import QrIntroduce from './QrIntroduce'
import {renderError,renderLoading} from '../widget/StateListView'
// import {BANK_CARD_VERIFY_ERROR_MSG,BANK_CARD_VERIFY_TO_MANY_ERROR_MSG} from './RealI18N'

const NOOPEON = 0; //未开户
const NORMAL = 2; //正常
const OUTFUND = 3;//出金中
const FREEZE = 4;//冻结
const OUTFUNDFAIL = 5;//出金失败

const FAIL0 = 0 ;//出金的金额为零
const FAIL1 = 1 ;//出金的金额为负
const FAIL2 = 2 ;//出金资料填写错误


import {
  TitleBar,
} from '../Global'


  export default class QrReturnUrl extends Component{

  constructor(props){
    super(props);
    let returnUrl = this.props.params.status;
    this.state={isTitle:true,error:null,returnUrl:returnUrl};
  }

  shouldComponentUpdate(nextProps, nextState) {
    return nextState.isTitle!=this.state.isTitle ||
        nextState.error != this.state.error ||
        nextState.status != this.state.status;
  }

  componentDidMount() {
    this._loadRecent();
  }


  _loadRecent=()=>{
    Api.detail(this,{
      api:"qr_acc/status",
      success:(result)=>{
         this.setState({...result,...{isTitle:false,error:null}});
      }	,
      error:(error,isNetworkError)=>{
          this.setState({error,isNetworkError});
          return true;
  			}
    });

  }
  reloadWithStatus=()=>{
    this._loadRecent();
  }

  renderContent=(data)=>{
    if(data.error){
      return renderError(data.error,data.isNetworkError,this);
    }else if(data.status!==undefined){
      if(data.status===NOOPEON)return　<QrIntroduce/>;
      if(data.status===NORMAL && data.returnUrl==0)　return <QrMain/>;
      if(data.status===NORMAL && data.returnUrl==1)　return <QrAccount/>;
      if(data.status===OUTFUND )　return <QrAccStatus params={{status:3}}/>;
      if(data.status===OUTFUNDFAIL && data.errorReason===FAIL0)　return <QrAccStatus params={{status:5}}/>;
      if(data.status===OUTFUNDFAIL && data.errorReason===FAIL1)　return <QrAccStatus params={{status:6}} clearAmt={data.clearAmt}/>;
      if(data.status===OUTFUNDFAIL && data.errorReason===FAIL2)　return <QrAccStatus params={{status:4}} />;
      if(data.status===FREEZE )　return <QrAccStatus params={{status:2}}/>;
    }else{
      return renderLoading();
    }
  }

  render(){
    let data = this.state;

      return (
        <View style={[CommonStyle.container,styles.content]}>
         {data.isTitle&&<TitleBar title={"二维码付款"}/>}
         {this.renderContent(this.state)}
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
