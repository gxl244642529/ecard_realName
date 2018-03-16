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
  TouchableAll
} from '../../../lib/Common';

import Navigator from '../../lib/Navigator'
import {BackIcon} from '../icons/Icons'
// const BELL_H = require('./images/bell_h.png');


//0-60 = 0.1-0.9
//比例为0.8 / 60,在0的时候为最小0.1
const RATE = 0.8 / 60;
const BUSMAIN = "/busqr/qrReturnUrl/0"
const PERSONAL  = "/personal/center"

export default class HeaderTitleBar extends Component{


  constructor(props){
    super(props);
    this.state={value:0,isChange:false};
  }
  // componentDidMount(){
  //   this.loadCent();
  // }
  // loadCent=()=>{
  //   Api.api({
  //     api:'mc/count',
  //     success:(result)=>{
  //       console.log("result"+result);
  //       this.setState({msg:result});
  //     }
  //
  //   });
  // }


  shouldComponentUpdate(nextProps, nextState) {
    return nextProps.value!==undefined && nextProps.value !== this.state.value;

  }


  componentWillReceiveProps(nextProps) {
    let {value} = nextProps;
    let bell = null;
    if(value > 30){
      //变成黑色
      // bell = BELL_H;
      this.setState({isChange:true});
    }else{
      // bell = BELL;
      this.setState({isChange:false});
    }

    this.setState({value,bell});
  }


  // _msgCenter=()=>{
  // 	Api.push('/msgenter');
  // }
  //
  // _qr=()=>{
  // 	Api.push('/busqr/main');
  // }
  _back=()=>{
    let arr = Api.getRoutes();
    for(let i= arr.length-1; i >=0; --i){
      let vo = arr[i];
      if(vo.location===undefined){
        break;
      }
      if (vo.location.pathname.indexOf(BUSMAIN)>=0) {
        console.log(vo.location.pathname);
         Api.returnTo(vo.location.pathname);
           return true;
      }
      if(vo.location.pathname==PERSONAL){
          console.log("PERSONAL="+PERSONAL);
        Api.returnTo(PERSONAL);
        return true;
      }
    }
    console.log("返回主页");
    Api.returnTo("/");
    return true;
  }

  _gotoQrtrans=()=>{
    // Api.push('/busqr/trans');
    Api.push('/busqr/qrOutFundFirStep');
  }

  render(){
    console.log(this.state.value);
    console.log(0.1 + RATE * this.state.value);
    return (
      <View style={styles.headerTitle}>
          <View style={{ position:'absolute', backgroundColor:'transparent', width:width,height:TITLE_HEIGHT, zIndex:0,  opacity:  0.1 }}>
          </View>

          <View style={{position:'absolute',
            width:width,height:TITLE_HEIGHT,
            zIndex:1,
            flexDirection:'row',
            justifyContent:'space-between'}}>
            <TouchableAll onPress={this._back} style={{width:40,height:65,justifyContent:'center',alignItems:'center',backgroundColor:'transparent'}}>
              <BackIcon/>


            </TouchableAll>
            <Text style={{backgroundColor:'transparent',color:'#fff',justifyContent:'center',alignItems:'center',marginTop:25,marginLeft:20}}>余额管理</Text>
            <TouchableAll onPress={this._gotoQrtrans} style={{width:80,height:65, justifyContent:'center',alignItems:'center',backgroundColor:'transparent'}}>
              <Text style={{color:'#fff'}}>关闭服务</Text>
            </TouchableAll>
          </View>
      </View>
    );
  }
}
let width = Dimensions.get('window').width ;

const TITLE_HEIGHT = 65;

const styles=StyleSheet.create({
	headerTitle:{position:'absolute', zIndex:100, width:width, height:TITLE_HEIGHT},
});
