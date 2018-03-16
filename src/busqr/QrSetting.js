
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
import StandardStyle from '../lib/StandardStyle'

 import {FormSwitch} from '../widget/StandardFormSwitch'

export default class QrSetting extends Component{
	constructor(props){
		super(props);
		this.state={isNotNeedPwd:!Account.user.needsPwd};
	}
  
  _setPwd=()=>{
    Api.push("/busqr/update_pwd");
  }

  switchChange=(switchTrue)=>{
    console.log(switchTrue);
    this.setState({isNotNeedPwd:switchTrue});
    let data = {need:!switchTrue}
    Account.user = Object.assign({},Account.user,{needsPwd:!switchTrue});
    Account.save();
    Api.api({
       api:"qr_pwd/set",
       data:data,
       success:(result)=>{
         A.toast("设置成功");
       }
     });
  }

  // <SwitchView onChange={this.switchChange} switchTrue={this.state.need}/>

	render(){
			return (
				<View style={CommonStyle.container}>
					<TitleBar title="付款设置" />
					<ScrollView >
            <FormSwitch
              label="免密支付"
              placeholder="是否密码支付?"
              value={this.state.isNotNeedPwd}
              hasBottom={true}
              onValueChange={this.switchChange}
             />
            <Text style={StandardStyle.h3,{padding:15,color:'#666666'}}>开启免密支付后下次使用将不需要输入密码</Text>
            <TouchableOpacity style={styles.setPwdSty} onPress={this._setPwd}>
              <Text style={styles.label}>修改支付密码</Text>
            </TouchableOpacity>

					</ScrollView>
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1},
  setPwdSty:{
    backgroundColor:'#fff',
    justifyContent:'center',
    height:60,
    paddingRight:10,
    paddingLeft:15,
  },
  label:{
    color:'#595757',
    fontSize:16,
    lineHeight:16,
  }

});
