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
} from '../../lib/Common';

import Navigator from '../lib/Navigator'
const BACK = require('./images/icon_3Left.png');
// const BELL_H = require('./images/bell_h.png');


//0-60 = 0.1-0.9
//比例为0.8 / 60,在0的时候为最小0.1
const RATE = 0.8 / 60;

export default class HeaderTitleBar extends Component{


  constructor(props){
    super(props);
    this.state={value:0,back:BACK,isChange:false};
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
    Api.goBack();
  }
  _setting=()=>{
    Api.push('setting');
  }

  render(){

    return (
      <View style={styles.headerTitle}>
          {this.state.isChange?<Image style={{ position:'absolute', width:width,height:TITLE_HEIGHT,
          zIndex:0}} source={require('./images/headBg.png')}/>:<View style={{ position:'absolute', backgroundColor:'transparent', width:width,height:TITLE_HEIGHT, zIndex:0,  opacity:  0.1 }}>
          </View>}

          <View style={{position:'absolute',
            width:width,height:TITLE_HEIGHT,
            zIndex:1,
            flexDirection:'row',
            justifyContent:'space-between'}}>
            <TouchableAll onPress={this._back} style={{width:40,height:40,justifyContent:'center',alignItems:'center'}}>
              <Image style={{width:9,height:19}} source={ this.state.back } />


            </TouchableAll>
            <Text style={{backgroundColor:'transparent',color:'#fff',justifyContent:'center',alignItems:'center',marginTop:10}}>我的</Text>
            <TouchableAll onPress={this._setting} style={{width:40,height:40, justifyContent:'center',alignItems:'center'}}>
              <Image style={{width:20,height:20}} source={require('./images/SetUp.png')} />
            </TouchableAll>
          </View>
      </View>
    );
  }
}
let width = Dimensions.get('window').width ;

const TITLE_HEIGHT = 45;

const styles=StyleSheet.create({
	headerTitle:{position:'absolute', zIndex:100, width:width, height:TITLE_HEIGHT},
});
