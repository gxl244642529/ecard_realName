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
import {BackIcon} from '../widget/Icons'
import Navigator from '../lib/Navigator'
const BACK = require('./images/icon_3Left.png');
// const BELL_H =<BackIcon/>


//0-60 = 0.1-0.9
//比例为0.8 / 60,在0的时候为最小0.1
const RATE = 0.8 / 60;

export default class PcHearderTitleBar extends Component{


  constructor(props){
    super(props);
    this.state={value:0,back:BACK,bgColor:'transparent',op:0.1,isChange:false };
  }


  shouldComponentUpdate(nextProps, nextState) {
    return nextProps.value!==undefined && nextProps.value !== this.state.value;
  }
  // componentDidMount(){
  //   Api.api({
  //     api:'mc/count',
  //     success:(result)=>{
  //       console.log("result"+result);
  //       this.setState({msg:result});
  //     }
  //
  //   });
  // }


  componentWillReceiveProps(nextProps) {
    let {value} = nextProps;
    let bell = null;
    if(value > 30){
      //变成黑色
      Navigator.setBlackStyle();
      console.log("正在改变")

      this.setState({isChange:true});
    }else{
      Navigator.setWhiteStyle();
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

// 0.1 + RATE * this.state.value
  render(){
    return (
      <View style={styles.headerTitle}>
          {this.state.isChange?<Image style={{ position:'absolute', width:width,height:60,
          zIndex:0}} source={require('./images/headBg.png')}/>
          :<View style={[{ position:'absolute', width:width,height:60, zIndex:0,  },{backgroundColor:this.state.bgColor,opacity: this.state.op }]}>
          </View>}
          <View style={{position:'absolute',
            width:width,height:60,
            zIndex:1, paddingTop:20,
            flexDirection:'row',
            justifyContent:'space-between'}}>
            <TouchableOpacity onPress={this._back} style={{width:40,height:40,justifyContent:'center',alignItems:'center'}}>
              <Image style={{width:9,height:19}} source={ this.state.back } />

            </TouchableOpacity>
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

const styles=StyleSheet.create({
	headerTitle:{position:'absolute', zIndex:100, width:width, height:60},
});
