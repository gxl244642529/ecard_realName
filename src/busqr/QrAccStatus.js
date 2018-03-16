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
  LocalData,
  Log,
  ScrollView,
  PixelRatio
} from '../../lib/Common';



//修改
import{
  TitleBar,
  Button,
  LoadingButton
} from '../Global'
import{
    pageButton,
    loadingButton,
    loadingButtonRealDisabled
}from '../GlobalStyle'

import QrUtils from './QrUtils'

import StandardStyle from '../lib/StandardStyle'
import WithDrawProcess from './component/WithDrawProcess'
import {renderError,renderLoading} from '../widget/StateListView'
import Notifier from '../../lib/Notifier'
import {makePhoneCall} from '../lib/CommonUtil'

const SCREEN_WIDTH = Dimensions.get('window').width;
const URL = "/personal/center"
const MAIN = "/realName/_main"

class Page extends Component{
    constructor(props){
        super(props)
    }

    componentWillMount() {
    }

    render(){
        return(
            <View style={styles.content}>
                <Image source={require("./images/banner.png")} style={styles.headImg}/>
                <View style={StandardStyle.center}>
                    {this.props.renderImg}
                    <Text style={[StandardStyle.h3,StandardStyle.fontGray]}>"您已成功开通公交扫码支付功能，"</Text>
                    <Text style={[StandardStyle.h3,StandardStyle.fontGray]}>您可以现在前去充值！</Text>
                </View>
                <View style={styles.btnBox}>
                    <Button
                        text="重新开通" style={[loadingButton.loadingButtonReal,styles.btn]}
                        onPress={this._gotoPage}
                        textStyle={pageButton.buttonText}
                        url
                    />
                </View>
            </View>
        )
    }
}
class OutFundMessage extends Component{
  constructor(props){
    super(props);
    this.state={};

  }
  reoutFund=()=>{
    Api.push("/busqr/out_fund");
  }
  _callPhone=()=>{
    makePhoneCall("0592968870");
  }
  render(){
    return(
      <View style={styles.mt10}>
        <View style={{paddingBottom:30}}>
         <WithDrawProcess step={3} status={1} />
        </View>
         <View style={styles.viewLine}/>
         <Text style={[StandardStyle.h3,StandardStyle.fontGray,{padding:20}]}>出金失败!</Text>

         <Text style={[StandardStyle.h3,StandardStyle.fontGray,{paddingLeft:20}]}>
          原因:{this.props.reason}
          {this.props.hasCall &&<Text onPress={this._callPhone} >如有疑问可拨打客服热线968870></Text>}
        </Text>

         {this.props.canre&&<View style={styles.bottomBtn}>
            <LoadingButton
               loading={this.state.submiting}
               text="重新填写信息"
               onPress={this.reoutFund}
               disabled={this.state.submiting}
               style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
               textStyle={loadingButton.loadingButtonText} />
           </View>}
      </View>
    );
  }
}

class RequestResult extends Component{
  constructor(props){
    super(props);
    this.state={};
  }
  componentDidMount(){
      QrUtils.activate((ret)=>{
      this.setState({ret:ret});
    });
  }
  _succGoCharge=()=>{
    Api.returnTo("/busqr/chage");
  }
  _succComplet=()=>{
    Api.returnTo("/");
  }
  rerequest=()=>{
    QrUtils.activate((ret)=>{
      console.log(ret);
      this.setState({ret:ret});
    });
  }
  _renderSuccess=()=>{
      return <View style={styles.content}>
              <Image source={require("./images/banner.png")} style={styles.headImg}/>
              <View style={StandardStyle.center}>
                  <Image source={require("./images/open_success.png")} style={styles.graphic}/>
                  <Text style={[StandardStyle.h3,StandardStyle.fontGray]}>您已成功开通公交扫码支付功能，</Text>
                  <Text style={[StandardStyle.h3,StandardStyle.fontGray]}>您可以现在前去充值！</Text>
              </View>
              <View style={styles.btnBox}>
              <Button
                  text="立即充值"
                  style={[pageButton.button,styles.btn]}
                  onPress={this._succGoCharge}
                  textStyle={pageButton.buttonText}
              />
              <Button
                   text="完成" style={[pageButton.button,styles.btn]}
                   onPress={this._succComplet}
                   textStyle={pageButton.buttonText}
              />
              </View>
          </View>
  }
  _renderRequestError=()=>{
    return <View style={styles.content}>
            <Image source={require("./images/banner.png")} style={styles.headImg}/>
            <View style={StandardStyle.center}>
                <Image source={require("./images/account_closed.png")} style={styles.graphic}/>
                <Text style={[StandardStyle.h3,StandardStyle.fontGray]}>二维码付款开通失败</Text>

            </View>

            <View style={styles.btnBox}>
            <Button
                text="刷新"
                style={[pageButton.button,styles.btn]}
                onPress={this.rerequest}
                textStyle={pageButton.buttonText}
            />
            </View>

        </View>
  }
  renderContent=()=>{
    if (this.state.ret==0) {
      return this._renderSuccess()
    }else if (this.state.ret==undefined) {
      return renderLoading()
    }else if (this.state.ret!=0) {
      return this._renderRequestError()
    }
  }
  render(){
    return(
      <View style={CommonStyle.container}>

       {this.renderContent()}
      </View>
    )
  }
}

export default class QrAccStatus extends Component{
    constructor(props){
        super(props);
        this.state={};
    }
    componentWillMount() {
      Notifier.addObserver("androidBack",this.onBack);

    }

    componentWillUnmount() {
        Notifier.removeObserver("androidBack",this.onBack);
    }
    _gotoActive=()=>{
      Api.returnTo("/busqr/acc_active");
    }
    _renderAccClosed=()=>{
        return <View style={styles.content}>
                <Image source={require("./images/banner.png")} style={styles.headImg}/>
                <View style={StandardStyle.center}>
                    <Image source={require("./images/account_closed.png")} style={styles.graphic}/>
                    <Text style={[StandardStyle.h3,StandardStyle.fontGray]}>二维码付款服务已关闭</Text>

                </View>
                <View style={styles.btnBox}>
                <Button
                     text="重新开通" style={[pageButton.button,styles.btn]}
                     onPress={this._gotoActive}
                     textStyle={pageButton.buttonText}
                     url
                />
                </View>
            </View>
    }
    _renderNoMoney=()=>{
        return <View style={styles.content}>
                <Image source={require("./images/banner.png")} style={styles.headImg}/>
                <View style={StandardStyle.center}>
                    <Image source={require("./images/nomoney.png")} style={styles.graphic}/>
                    <Text style={[StandardStyle.h3,StandardStyle.fontGray]}>抱歉，您的付款余额不足，</Text>
                    <Text style={[StandardStyle.h3,StandardStyle.fontGray]}>无法生成支付码，请先进行充值！</Text>
                </View>
                <View style={styles.btnBox}>
                <Button
                     text="立即充值" style={[pageButton.button,styles.btn]}
                     onPress={this._gotoPage}
                     textStyle={pageButton.buttonText}
                     url
                />
                </View>
            </View>
    }

    _renderOutFund=()=>{
      return(
        <View style={styles.mt10}>
          <View style={{paddingBottom:30}}>
           <WithDrawProcess step={3} status={0} />
          </View>
           <View style={styles.viewLine}/>
          <Text style={[StandardStyle.h3,StandardStyle.fontGray,styles.text]}>正在出金中，出金过程需要5-7天，请耐心等待！</Text>
        </View>
      );
    }
    _renderOutFundFail=()=>{
      return(

        <OutFundMessage reason="您输入的银行信息有误，请重新提交银行信息!" canre={true}/>
      );
    }
    confirm=()=>{
      Api.api({
        api:'qr_fund/confirm',
        success:(result)=>{
          console.log(result);
          Api.goBack();
        }
      })
    }
    _renderOutFundAcc=()=>{
      return(
        <View>
          <OutFundMessage reason="您的余额为0元，出金失败，扫码服务已关闭" canre={false} hasCall={false}/>
          <View style={styles.bottomBtn}>
             <LoadingButton
                loading={this.state.submiting}
                text="确定"
                onPress={this.confirm}
                disabled={this.state.submiting}
                style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
                textStyle={loadingButton.loadingButtonText} />
            </View>
        </View>
      );
    }
    _renderOutFundAccFu=(clearAmt)=>{
      return(
        <View>
          <OutFundMessage
            reason={"您的余额已透支"+(clearAmt/100).toFixed(2)+"元,出金失败，请及时补交透支金额，扫码服务已关闭，"}
            canre={false}
            hasCall={true}/>
            <View style={styles.bottomBtn}>
             <LoadingButton
                loading={this.state.submiting}
                text="确定"
                onPress={this.confirm}
                disabled={this.state.submiting}
                style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
                textStyle={loadingButton.loadingButtonText} />
            </View>
        </View>
      );
    }
    _gotoPage=()=>{

    }
    onBack=()=>{

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
          if(vo.location.pathname=="/msgenter"){
             Api.returnTo("/msgenter");
            return true;
          }
        }
        Api.returnTo("/");
        return true;

    }

    render(){
        return <View style={StandardStyle.container}>
            <TitleBar title="二维码付款"  onBack={this.onBack}/>
            {this.props.params.status=='0' && <RequestResult/>}
            {this.props.params.status=='1' && this._renderNoMoney()}
            {this.props.params.status=='2' && this._renderAccClosed()}
            {this.props.params.status=='3' && this._renderOutFund()}
            {this.props.params.status=='4' && this._renderOutFundFail()}
            {this.props.params.status=='5' && this._renderOutFundAcc()}
            {this.props.params.status=='6' && this._renderOutFundAccFu(this.props.clearAmt)}
        </View>
    }
}
const styles=StyleSheet.create({
    content:{
        flex:1,
        justifyContent:'space-between',
        alignItems:'center',
        paddingBottom:40
    },
    headImg:{
        width:SCREEN_WIDTH,
        height:SCREEN_WIDTH*0.25
    },
    graphic:{
        width:SCREEN_WIDTH*0.35,
        height:SCREEN_WIDTH*0.35,
        marginBottom:20
    },
    btnBox:{
        flexDirection:'row',
        paddingLeft:10,//+10
        paddingRight:10,//+10
    },
    btn:{
        flex:1,
    },
    //功能样式
    mt10:{
      marginTop:30,
      marginBottom:30
    },
    viewLine:{
      height:1/PixelRatio.get(),
      backgroundColor:'#d7d7d7',
    },
    text:{
      // marginTop:20,
      // marginRight:10,
      // marginLeft:10
      padding:20
    },
    bottomBtn:{
      marginTop:20,
    }
})
