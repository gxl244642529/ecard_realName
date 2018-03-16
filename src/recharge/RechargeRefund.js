
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
} from '../../lib/Common';

import TitleBar from '../widget/TitleBar'
import Button from '../widget/Button'




export default class RechargeRefund extends Component{
	constructor(props){
		super(props);
	}

  onBack=()=>{
    Api.go(-2);
  }


	render(){
		return (
			<View style={CommonStyle.container}>
        <TitleBar title="退款成功" onBack={this.onBack} />
        <View style={{flex:1}}>
        <Image 
          source={require('./images/ic_refund_success.png')} 
          style={{width:'80%',marginLeft:'10%'}} 
          resizeMode="contain"/>
        <Button onPress={this.onBack} text="返回" style={styles.refundButton} textStyle={{color:'#fff'}} />
        </View>
      </View>
		);
	}
}

const SELECTED_COLOR = '#EE7700';


const screenWidth = Dimensions.get('window').width;
const styles = StyleSheet.create({
  refundButton:{backgroundColor:'#E8464C',borderRadius:0,margin:10,paddingTop:15,paddingBottom:15},
});
