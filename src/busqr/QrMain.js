
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
import {InteractionManager} from 'react-native'
import {
  TitleBar,
} from '../Global'
let ScreenModule = require('react-native').NativeModules.ScreenModule;
import {DeviceEventEmitter} from 'react-native'

import QrView from '../lib/QrView'
import QrUtils from './QrUtils'
import Button from '../widget/Button'
import PayBox from '../widget/PayBox'

//新增
import StandardStyle from '../lib/StandardStyle'
import StateListView from '../widget/StateListView'
const SCREEN_WIDTH = Dimensions.get('window').width;
import {IC_Reflash,IC_Bus2,IC_VerticalOmit} from './icons/Icons'
import {renderError,renderLoading} from '../widget/StateListView'
import Menu from './component/Menu'
import {checkedPwd} from './QrUtils'
import {refreshColors} from '../widget/StateListView'
const SCREEN_HEIGHT = Dimensions.get('window').height;
const SUCCESS = 0; //成功
const NOREQUEST = 204;  //该用户未开通，再调一次激活
const NOACTIVE = 205;  //当前设备未激活,再调一次激活
const NOMONEY = 206;  //账户余额不足，展示余额不足页面，提示用户去充值
const ACCNOACTIVE = 208;  //弹出框框提示账户已冻结，点击确定以后跳出页面
const OTHER = OTHER; //其他错误，获取二维码失败，请点击重试
//测试

const renderBusLine=(rowData,index)=>{
  return (
    <View style={styles.listItem} key={"road"+index}>
        <Text style={[StandardStyle.h4,styles.gray]}>{rowData.name}路</Text>
        <Text style={[StandardStyle.h4,styles.gray]}>{rowData.line}</Text>
    </View>
  );
}

class QrBottom extends Component{

  constructor(props){
    super(props);
    this.state={};
  }

  shouldComponentUpdate(nextProps, nextState) {
    return this.state.list!==nextState.list;
  }

  componentDidMount() {

    Api.detail(this,{
      api:'qr_info/support',
      name:'list',
    });
  }

  render(){
    return (
      <View style={{height:100}}>
        <View style={styles.listHead}>
          <IC_Bus2 />
          <Text style={[StandardStyle.h4,styles.gray]}>厦门已开通的路线</Text>
        </View>
        <ScrollView style={{flex:1}}>
           {this.state.list && this.state.list.map(renderBusLine)}
        </ScrollView>
      </View>
    );
  }
}

let QrC  = QrUtils.BusQrView;
const QR_MAIN1 = "/busqr/qrReturnUrl/0"
const QR_MAIN2 = "/busqr/qrReturnUrl/1"
export default class QrMain extends Component{
	constructor(props){
		super(props);
		this.state={hasMenu:false,hasPayBox:false,qr:null};
    Notifier.addObserver("updateAcc",this._getQr);
    Notifier.addObserver('componentDidFocus',this.componentDidFocus);
    this.isLive =true;
	}


  componentDidFocus=(name,route)=>{

    if (route.location) {
      if (route.location.pathname!=QR_MAIN1 && route.location.pathname!=QR_MAIN2) {
        this._viewWillDispear();
      }else{
        this._viewWillAppear();
      }
    }

  }


  shouldComponentUpdate(nextProps, nextState) {
    return nextState.hasMenu!==this.state.hasMenu ||
          nextState.hasPayBox !== this.state.hasPayBox ||
          nextState.qr !== this.state.qr;
  }
  componentWillUpdate(){

  }

  componentDidMount(){


     ScreenModule.getValue(this._saveValue);
     //禁止截屏
     if(Platform.OS=='android'){
       console.log("正在禁止截屏")
           QrUtils.enableCapture(false); //如果是android
     }
      this.subscription = DeviceEventEmitter.addListener("viewWillAppear", this._viewWillAppear);
      this.viewWillDispear = DeviceEventEmitter.addListener("viewWillDispear", this._viewWillDispear);

     let needsPwd = Account.user.needsPwd;
     if(!needsPwd){
        Api.detail(this,{api:"qr_info/support",success:this._onApiSuccess});
        this._getQr();
        //return renderLoading();
     }else{
        InteractionManager.runAfterInteractions(()=>{
            this.setState({hasPayBox:true});
        });
     }
  }

  _viewWillAppear=()=>{
    console.log('viewWillAppear');
    this._getQr();
    //获取值
    ScreenModule.getValue(this._saveValue);
  }

  _viewWillDispear=()=>{

    //恢复
    if(this.screenValue !== undefined){
      ScreenModule.setValue(this.screenValue);
      this.screenValue = undefined;
    }

  }

  _saveValue=(value)=>{
    if(this.screenValue !== value){
        this.screenValue = value;
        if(this.screenValue!=100){
           ScreenModule.setValue(100);
        }
      }
  }



  componentWillUnmount() {
    if(Platform.OS=='android'){
       console.log("正在禁止截屏")
           QrUtils.enableCapture(true); 
     }
   
     Notifier.removeObserver('componentDidFocus',this.componentDidFocus);
    this._viewWillDispear();

    if(this.subscription){
      this.subscription.remove();
      this.subscription = null;
    }

    if(this.viewWillDispear){
      this.viewWillDispear.remove();
      this.viewWillDispear = null;
    }

    Notifier.removeObserver("updateAcc",this._getQr);
    this.isLive = false;
    if (this.timer) {
      clearInterval(this.timer);//FIXME:无法实现卸载时清除计时器
      this.timer=null;
    }

   }


  _getQr=()=>{
    if(!this.isGetQr){
      this.isGetQr=true;
      QrUtils.getQr(this._onGetQr);
    }
  }

  _onApiSuccess=(result)=>{
    this.setState({list:result});
  }

  _checkOk=()=>{
    this.setState({hasPayBox:false});
    this._getQr();
  }


  _checkError=(result)=>{
    console.log("result="+result);
    let str = "密码错误，你还有"+(result-100)+"次机会";
    A.alert(str,(r)=>{

    });
    this.setState({hasPayBox:true});
    this.refs.PAYBOX._onClean();
    return;
  }


  _checkPwd=(str)=>{
    console.log("验证密码输入是否正确");
    let data = {pwd:str};

    checkedPwd(data,this._checkError,this._checkOk);
  }

  _onCancelPayBox=()=>{
    Api.goBack();
  }

  _onGetQr=(qr)=>{

    this.isGetQr = false;

    if(!this.isLive){
      return;
    }

    if(!qr){
      return;
    }



    InteractionManager.runAfterInteractions(()=>{
       this.setState({qr});
    });
    console.log(qr.exp);
    if (qr.ret===SUCCESS) {
      if (this.timer) {
        clearInterval(this.timer);//FIXME:无法实现卸载时清除计时器
        this.timer=null;
      }
      this._setConutDown(qr.exp)
    }

  }
  _onSetMenu=()=>{
    this.setState({hasMenu:!this.state.hasMenu});
  }
  _renderRight=()=>{
    return <TouchableOpacity style={styles.titleIcon} onPress={()=>this._onSetMenu()}><IC_VerticalOmit/></TouchableOpacity>
  }
  _gotocharge=()=>{
    Api.push("/busqr/chage");
  }
  _setConutDown=(qrtime)=>{
    console.log("qrtime="+qrtime);
    this.setState({count:qrtime});
    this.timer = setInterval(()=> {
      this.setState({
          count: this.state.count - 1,
      });
      if (this.state.count === 0) {
        console.log("倒计时完成");
          clearInterval(this.timer);
          //过期刷新
          this._getQr();
      }
    }, 1000);

  }
  renderQr(){
    let qr = this.state.qr;
    console.log("qr="+qr);
    if(qr.ret===SUCCESS){

      return <View style={styles.msg}>
        <Text style={[StandardStyle.h3,styles.gray]}>请将二维码靠近公交扫码器</Text>
        <View style={styles.mTop10}/>


          <QrC content={qr.qrcode} style={styles.qr} />
        <TouchableOpacity onPress={this._getQr} style={{width:SCREEN_WIDTH}}>
        <View style={[StandardStyle.row,StandardStyle.center]}>
          <View style={styles.p10}><IC_Reflash /></View>
          <Text style={[StandardStyle.h3,styles.gray]}>每分钟自动更新</Text>
        </View>
        <View style={StandardStyle.center}>
          <Text style={[StandardStyle.h3,styles.gray,]}>连续使用请刷新二维码</Text>
        </View>
        </TouchableOpacity>
      </View>;
    }else if (qr.ret===NOREQUEST || qr.ret===NOACTIVE) {

        // console.log(  "active="+QrUtils.activate());
        //  A.alert("二维码付款功能未开通！请先开通",()=>{
        //       Api.replace("/busqr/qrRequest");
        // });
        return (
           <TouchableOpacity style={styles.nomoney} onPress={this._getQr}>
                <Image source={require("./images/nomoney.png")} style={styles.graphic}/>
                <Text style={[StandardStyle.h3,StandardStyle.fontGray]}>获取二维码失败</Text>
                <Text style={[StandardStyle.h3,StandardStyle.fontGray]}>请点击重试!</Text>
            </TouchableOpacity>
        )

    }else if (qr.ret===NOMONEY) {
      return  <TouchableOpacity style={styles.nomoney} onPress={this._gotocharge}>
                <Image source={require("./images/nomoney.png")} style={styles.graphic}/>
                <Text style={[StandardStyle.h3,StandardStyle.fontGray]}>抱歉，您的付款余额不足，</Text>
                <Text style={[StandardStyle.h3,StandardStyle.fontGray]}>无法生成支付码，点击进入充值！</Text>
            </TouchableOpacity>
    }else if (qr.ret===ACCNOACTIVE) {
      A.alert("二维码付款功能已关闭,无法生成二维码",()=>{
          Api.replace("/realName/_main");
      });
    }else if (qr.ret===undefined) {
      return renderLoading();
    }
    else {
      return (
         <TouchableOpacity style={styles.nomoney} onPress={this._getQr}>
              <Image source={require("./images/nomoney.png")} style={styles.graphic}/>
              <Text style={[StandardStyle.h3,StandardStyle.fontGray]}>获取二维码失败</Text>
              <Text style={[StandardStyle.h3,StandardStyle.fontGray]}>请点击重试!</Text>
          </TouchableOpacity>
      )
    }

  }


	render(){
			return (
				<View style={CommonStyle.container}>
					<TitleBar title="二维码付款" renderRight={this._renderRight} />
          <View style={styles.container}>
            {this.state.qr&&this.renderQr()}
          </View>
          <QrBottom />

          {this.state.hasMenu && <Menu onPress={this._onSetMenu}/>}
          {this.state.hasPayBox &&
              <PayBox
                ref="PAYBOX"
                onCancel={this._onCancelPayBox}
                onSuccess={this._checkPwd}
                onForget={()=>Api.push("/busqr/forget_pass")}
              />}
				</View>
			);
	}
}





const styles = StyleSheet.create({
	container:{
    flex:1,

  },
  msg:{
    marginTop:20,
    alignItems:'center'
  },
  titleIcon:{
    padding:10
  },
  qr:{
    marginTop:0,
    marginBottom:0,

    width:SCREEN_WIDTH*0.8,
    height:SCREEN_WIDTH*0.8,

    backgroundColor:'#0099cc'
  },
  listHead:{
    paddingLeft:20,
    paddingTop:10,
    paddingBottom:10,
    backgroundColor:'#dadbdb',
    flexDirection:'row',
    alignItems:'center'
  },
  listItem:{
    flexDirection:'row',
    justifyContent:'space-between',
    paddingLeft:20,
    paddingRight:40,
    paddingTop:10,
    paddingBottom:10,
    backgroundColor:'transparent'

    /*borderBottomColor:'#d7d7d7',
    borderBottomWidth:1,
    zIndex:0*/
  },
  borderBottom:{

  },
  test:{
    zIndex:100,
  },
  //功能样式
  p10:{
    padding:10
  },
  gray:{
    color:'#595757'
  },
  op5:{
    opacity:0.5
  },
  row:{
    flexDirection:'row'
  },
  graphic:{
      width:SCREEN_WIDTH*0.35,
      height:SCREEN_WIDTH*0.35,

      marginBottom:20
  },
  nomoney:{
    alignItems:'center',
    justifyContent:'center',
    marginTop:40,
    alignItems:'center',
    height:SCREEN_WIDTH*0.8,
    marginBottom:40
  },
  mTop10:{
    marginTop:20
  }
});
