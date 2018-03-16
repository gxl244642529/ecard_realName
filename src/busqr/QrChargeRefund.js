
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
  ScrollView
} from '../../lib/Common';

import {
  TitleBar,
} from '../Global'
import {
radioButton,
loadingButton,
loadingButtonReal,
loadingButtonRealDisabled
} from '../GlobalStyle'
import LoadingButton from '../widget/LoadingButton'


import Notifier from '../../lib/Notifier'
import {SCREEN_WIDTH,SCREEN_HEIGHT} from '../widget/Screen'

const RECHARE_SUCCESS = 1;//充值成功
const RECHARE_FAIL = 2;//充值失败
const RECHARE_REFUND = 3//已发送退款请求

export default class QrChargeRefund extends Component{
	constructor(props){
		super(props);
		this.state={};
	}

  onSubmit=()=>{
    Notifier.notifyObservers('qrChargeOk');
    Api.go(-3);
  }

	render(){
        return (
            <View style={[CommonStyle.container,{backgroundColor:'#fff'}]}>
                <TitleBar title="充值结果" renderLeft={false}/>
                <View style={styles.page}>
                    <View style={styles.topView} >
                        <Image source={require('./images/nomoney.png')} style={styles.paysucImage}/>
                        <View style={styles.mtop10}/>
                        <Text style={styles.font14}>已收到您的<Text style={styles.green}>退款请求</Text></Text>
                        <Text style={styles.font14}>系统将在12小时内将金额退回您的账户。</Text>
                        </View>
                        <View style={styles.but}>
                        <LoadingButton
                            loading={this.state.submiting}
                            text="返回"
                            onPress={this.onSubmit}
                            disabled={this.state.submiting}
                            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
                            textStyle={loadingButton.loadingButtonText} />
                        </View>
                    </View>
            </View>
        );
	}
}


const styles = StyleSheet.create({
	container:{flex:1},
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
