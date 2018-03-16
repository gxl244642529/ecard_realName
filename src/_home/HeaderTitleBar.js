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
  TouchableAll,
  Platform
} from '../../lib/Common';

import Navigator from '../lib/Navigator'
import {onRequireLoginPress} from '../../lib/LoginUtil'

import NoticeIcon from './NoticeIcon'

const BELL = require('./images/bell.png');
const BELL_H = require('./images/bell_h.png');

const QR = require('./images/qrcode.png');
const QR_H = require('./images/qr_h.png');
//0-60 = 0.1-0.9
//比例为0.8 / 60,在0的时候为最小0.1
const RATE = 0.8 / 60;
const WHITE = "#fff";
const BLACK = "#000";

export default class HeaderTitleBar extends Component{


  constructor(props){
    super(props);
    this.state={value:0,bell:BELL,qr:QR,qrtext:WHITE};
    this._qr = onRequireLoginPress(this._qr);
  }


  shouldComponentUpdate(nextProps, nextState) {
    return nextProps.value!==undefined && nextProps.value !== this.props.value;
  }


  componentDidMount() {
    this.refs.BG.setNativeProps({
      style:{
        backgroundColor:'rgba(255,255,255,'+(0.1 + RATE * this.props.value)+')'
      }
    });
  }


  componentWillReceiveProps(nextProps) {
    let {value} = nextProps;
    let bell = null;
    let qr = null;
    let qrtext = null;
    if(value > 50){
      //变成黑色
      if(Platform.OS=='ios'){
        Navigator.setBlackStyle();
      }

      bell = BELL_H;
      qr = QR_H;
      qrtext = BLACK;
    }else{
      if(Platform.OS=='ios'){Navigator.setWhiteStyle();}
      bell = BELL;
     qr = QR;
     qrtext = WHITE;
    }

    this.setState({bell,qr,qrtext});

    this.refs.BG.setNativeProps({
      style:{
        backgroundColor:'rgba(255,255,255,'+(0.1 + RATE * this.props.value)+')'
      }
    });
  }


  _qr=()=>{
  	Api.push('/busqr/qrReturnUrl/0');
  }

  _renderQr=()=>{
    return null;
  }

   /* <TouchableAll onPress={this._qr} style={styles.qrcontainer}>
         <Image style={styles.qricon} source={this.state.qr} />
          <Text style={[styles.qrtext,{color:this.state.qrtext}]}>扫码</Text>
        </TouchableAll>*/
        
  _renderQr=()=>{
    return null;
  }
  
  //------扫码保存


  render(){
  //  console.log(this.state.value);
   // console.log(0.1 + RATE * this.state.value);
    return (
      <View ref="BG" style={styles.iconContainer}>
        <NoticeIcon bell={this.state.bell} />
        {this._renderQr()}
      </View>
    );
  }
}
let width = Dimensions.get('window').width ;

const PADDING_TOP = Platform.OS=='ios' ? 20 : 0;
const HEIGHT = Platform.OS=='ios' ? 65 :45;


const styles=StyleSheet.create({
  qrcontainer:{width:70,height:40, justifyContent:'center',alignItems:'center',flexDirection:'row'},
  qricon:{width:20,height:20},
  iconContainer:{height:HEIGHT,paddingTop:PADDING_TOP,flexDirection:'row',justifyContent:'space-between'},
  qrtext:{paddingLeft:5},
});
