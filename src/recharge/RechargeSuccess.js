
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



export default class RechargeSuccess extends Component{
	constructor(props){
		super(props);
    //loading
		this.state={data:{id:this.props.params.orderId},error:null};
    this.retry = 0;
	}


  componentDidMount() {
    this.load();
    
  }



  /**
   * 加载订单信息
   * @return {[type]} [description]
   */
  load=()=>{
    Api.api({
      api:"recharge/payResult",
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

  
  renderSuccess=(order)=>{
    return (
      <View style={{flex:1}}>
        <View style={{alignItems:'center',justifyContent:'center'}}>
          <View style={{flexDirection:'row',alignItems:'center',justifyContent:'center',padding:20}}>
            <Image source={require('./images/ic_success.png')}  resizeMode="contain" style={{width:40,height:40}} />
            <Text style={{marginLeft:3,fontSize:16}}>支付成功</Text>
          </View>
          <View style={{flexDirection:'row'}}>
            <Text>卡号:</Text><Text>{order.cardId}</Text>
          </View>
          <View style={{flexDirection:'row',marginTop:5}}>
            <Text>支付金额:</Text><Text>{(order.fee/100).toFixed(2)}</Text>
          </View>
        </View>
        <Text style={{paddingTop:20,color:'#ee6060',textAlign:'center'}}>您可以通过以下方式完成充值金额写入</Text>
        <View style={{flex:1,flexDirection:'row',paddingTop:20,paddingBottom:20}}>
          {Platform.OS=='android' ? this.renderNfc() : null}
          <TouchableHighlight  underlayColor="#ccc" onPress={this.onRecharge} style={{flex:1}}>
            <View style={{flex:1,alignItems:'center',justifyContent:'center'}}>
              <Text>2.线下卟噔机卟噔</Text>
              <Image source={require('./images/ic_exam2.png')} resizeMode="contain" style={{marginTop:5,width:'60%'}}/>
              <Text  style={{marginTop:5}}>到附近的卟噔点卟噔</Text>
              <Text></Text>
            </View>
          </TouchableHighlight>
        </View>
      </View>
    );
  }

  renderError=(order)=>{
    return (
       <View style={{flex:1}}>
        <Image 
          source={require('./images/ic_pay_error.png')} 
          style={{width:'80%',marginLeft:'10%'}} 
          resizeMode="contain"/>
        <Button onPress={this.doRefund} text="退款" style={styles.refundButton} textStyle={{color:'#fff'}} />
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


  renderNfc=()=>{
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
  }

	render(){
			return (
				<View style={CommonStyle.container}>
          <TitleBar title="卟噔结果" />
          {this._renderContent()}
				</View>
			);
	}
}


const screenWidth = Dimensions.get('window').width;

const styles = StyleSheet.create({
  container:{flex:1},
  refundButton:{backgroundColor:'#E8464C',borderRadius:0,margin:10,paddingTop:15,paddingBottom:15},
});
