
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
  Account,
  ActivityIndicator,
  CommonStyle,
  PixelRatio,
  LocalData,
  Log,
  ScrollView,
  Platform,
  Notifier
} from '../../lib/Common';

import TitleBar from '../widget/TitleBar'
import AdvView from '../widget/AdvView'
import Button from '../widget/Button'

import PayUtil from '../../lib/PayUtil'
import NfcUtil from '../../lib/NfcUtil'
import OrderStatus from '../lib/OrderStatus'
import {renderLoading,renderError} from '../widget/StateListView'

import {InteractionManager} from 'react-native'
const SCREEN_WIDTH = Dimensions.get('window').width;

import {
radioButton,
loadingButton,
loadingButtonReal,
loadingButtonRealDisabled
} from '../GlobalStyle'
import LoadingButton from '../widget/LoadingButton'
import {onBack} from './QrUtils'
const URL="/busqr/main"
const ACC = "/busqr/account"


export default class QrChargeResult extends Component{
	constructor(props){
		super(props);
    //loading
		this.state={data:{id:this.props.params.orderId},error:null};
    this.retry = 0;
	}
  componentWillMount() {
    Notifier.addObserver("androidBack",this.gotoSuccess);

  }

  componentWillUnmount() {
      Notifier.removeObserver("androidBack",this.gotoSuccess);
  }


  componentDidMount() {
    this.load();

  }
  gotoSuccess=()=>{
    // Api.go(-2);
    // Api.returnTo("/busqr/account");
    Notifier.notifyObservers("updateAcc");
    let arr = Api.getRoutes();
    for(let i= arr.length-1; i >=0; --i){
      let vo = arr[i];
      if(vo.location===undefined){
        break;
      }
      if(vo.location.pathname==URL){
        Api.returnTo(URL);
        return true;
      }
    }
    Api.returnTo("/busqr/account");
    return true;



  }

  _gotoRefund=()=>{
    Api.push("/busqr/qrChargeRefund");
  }

  /**
   * 加载订单信息
   * @return {[type]} [description]
   */
  load=()=>{
    Api.api({
      api:"qr_trans/clientNotify",
      crypt:3,
      data:this.state.data,
      error:this._error,
      message:this._message,
      success:this._success
    });
  }

  /**
   *
   * @return {[type]} [description]
   */
  addRetry=()=>{
    ++this.retry;
    if(this.retry >= 2){
      return false;
    }
    return true;
  }

   /**
   * 或者客户端错误
   * @param  {[type]}  error          [description]
   * @param  {Boolean} isNetworkError [description]
   * @return {[type]}                 [description]
   */
  _error=(error,isNetworkError)=>{
    this.setState({error,isNetworkError});
    return true;
  }

  /**
   *  服务器端返回信息
   * @param  {[type]} error [description]
   * @return {[type]}       [description]
   */
  _message=(error)=>{
    if(error=='io'){
      //this.setState({error,true});
      //重新发一次请求
      this.checkRetry();

    }else{
      A.alert(error,()=>{
        Api.goBack();
      });
      //this.setState({error,false});
    }
    return true;
  }

  checkRetry=()=>{
     if(this.addRetry()){
        this.load();
      }else{
         this.setState({order:{status:OrderStatus.OrderStatus_SubmitError}})
      }
  }


  renderData=(order)=>{
    InteractionManager.runAfterInteractions(()=>{
      this.setState({order});
    });
  }

  _success=(order)=>{
    console.log(order.status);
    if(order.status==OrderStatus.OrderStatus_Submiting){
      this.checkRetry();
    }else if(order.status == OrderStatus.OrderStatus_Success){
      //这里成功了，
      this.renderData(order);
    }else if(order.status == OrderStatus.OrderStatus_SubmitError){
      //失败
      this.renderData(order);
    }else{
      A.alert("非法订单状态");
    }
  }


  reloadWithStatus=()=>{
    this.setState({order:undefined,error:null});
    this.load();
  }


  _refundSuccess=()=>{
    Api.push('/recharge/refundSuccess')
  }


  doRefund=()=>{
    Api.api({
      api:'recharge/setRefund',
      crypt:3,
      data:{data:this.props.params.orderId},
      success:this._refundSuccess
    });
  }


  renderSuccess=()=>{
    return <View style={styles.page}>
      <View style={styles.topView} >
          <Image source={require('./images/paysuc.png')} style={styles.paysucImage}/>
          <Text style={styles.text}>支付成功！</Text>
        </View>
        <View style={styles.but}>
          <LoadingButton
              loading={this.state.submiting}
              text="完成"
              onPress={this.gotoSuccess}
              disabled={this.state.submiting}
              style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
              textStyle={loadingButton.loadingButtonText} />
        </View>
    </View>
  }

  renderError=(order)=>{
    return (
       <View style={styles.page}>
      <View style={styles.topView} >
          <Image source={require('./images/nomoney.png')} style={styles.paysucImage}/>
          <View style={styles.mtop10}/>
          <Text style={styles.font14}><Text style={styles.red}>充值失败！</Text>刚刚服务器开了个小差，</Text>
          <Text style={styles.font14}>您可以点击下方按钮进行退款</Text>
        </View>
        <View style={styles.but}>
          <LoadingButton
              loading={this.state.submiting}
              text="退款"
              onPress={this._gotoRefund}
              disabled={this.state.submiting}
              style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
              textStyle={loadingButton.loadingButtonText} />
        </View>
    </View>
    );
  }


  renderStatus=(order)=>{
    if(order.status == OrderStatus.OrderStatus_Success){
      //这里成功了，
      return this.renderSuccess(order);
    }else if(order.status == OrderStatus.OrderStatus_SubmitError){
      //失败
      return this.renderError(order);
    }
  }

  onNfc=()=>{
    NfcUtil.recharge(this.state.order);
  }

  onRecharge=()=>{
    Api.push('/netpot/view/'+JSON.stringify({type:4}));
  }



   _renderContent(){
    if(this.state.order!==undefined){
      return this.renderStatus(this.state.order);
    }else if(this.state.error){
      return renderError(this.state.error,this.state.isNetworkError,this);
    }else{
      return renderLoading();
    }
  }


  /*renderNfc=()=>{
    return (
      <TouchableHighlight underlayColor="#ccc" onPress={this.onNfc} style={{flex:1}}>
          <View style={{flex:1,alignItems:'center',justifyContent:'center'}}>
            <Text>1.手机NFC在线卟噔</Text>
            <Image source={require('./images/ic_exam1.png')} resizeMode="contain" style={{marginTop:5,width:'60%'}}/>
            <Text style={{marginTop:5}}>将卡片直接贴在手机背面</Text>
            <Text>完成信息写入</Text>
          </View>
        </TouchableHighlight>
    );
  }*/

	render(){
			return (
				<View style={CommonStyle.container}>
          <TitleBar title="充值结果"  onBack={this.gotoSuccess}/>
          {this._renderContent()}
				</View>
			);
	}
}


const screenWidth = Dimensions.get('window').width;

const styles = StyleSheet.create({
  container:{flex:1},
  refundButton:{backgroundColor:'#E8464C',borderRadius:0,margin:10,paddingTop:15,paddingBottom:15},
  page:{
    flex:1
  },
  topView:{
    alignItems:'center',
    flex:0.75,
    justifyContent:'center',

  },
  paysucImage:{
    height:120,
    width:120,
    resizeMode:'contain'
  },
  text:{
    color:'#009285',
    fontSize:16,
    marginTop:10
  },
  but:{
    flex:0.25,
  },

  //功能样式
  mtop10:{
    marginTop:10
  },
  font14:{
    fontSize:14,
    lineHeight:21,
    color:'#595757',
  },
  red:{
    color:'#e8464c'
  },
  green:{
    color:'#009285'
  }
});
