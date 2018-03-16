
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
import AdvView from '../widget/AdvView'
import Button from '../widget/Button'

import ECardSelector from '../lib/ECardSelector'
import PayUtil from '../../lib/PayUtil'
import {onCheckLoginPress} from '../../lib/LoginUtil'

const SCREEN_WIDTH = Dimensions.get('window').width;




class RechargeButton extends Component{

  constructor(props){
    super(props);
    this._onPress = onCheckLoginPress(this._onPress);
  }

  shouldComponentUpdate(nextProps, nextState) {
    return nextProps.enabled !== this.props.enabled;
  }

  _onPress=()=>{
    this.props.onSelectValue(this.props.value);
  }

  render(){
    let enabled = this.props.enabled;
    let press= enabled ? { onPress:this._onPress,underlayColor:"#FFFFFFAA" } : null;
    return (
      <TouchableHighlight 
        {...press}
        style={[styles.btnRg,this.props.style,enabled?null:styles.btnRgSel]}>
        <Text style={[styles.btnRgText,enabled?null:styles.btnRgTextSel]}>{this.props.value}元</Text>
      </TouchableHighlight>
    );
  }
} 

const rechargeECard = 'rechargeECard';

export default class RechargeMain extends Component{
	constructor(props){
		super(props);
    LocalData.getValue(rechargeECard).then((cardId)=>{
        this.setState({cardId})
    },()=>{

    })
		this.state={cardId:null};
	}

  componentWillUnmount() {
    PayUtil.destroy();
  }

  _onSelectECard=(cardId)=>{
    this.setState({cardId});
  }

  onPaySuccess=(flag,data)=>{
    if(flag==PayUtil.ClinetPaySuccess){
      Api.push('/recharge/success/' + data.orderId);
    }
  }

  _success=(order)=>{
    LocalData.putObject(rechargeECard,this.state.cardId);
    PayUtil.pay(order.order_id,order.fee,"recharge",[PayUtil.PAY_WEIXIN,PayUtil.PAY_ETONGKA,PayUtil.PAY_CMB],this.onPaySuccess);
  }

  onOther=()=>{
    Api.push('otherRecharge') 
  }

  onPot=()=>{
    Api.push('/netpot/view/'+JSON.stringify({type:4}));
  }

  onHis=()=>{
     Api.push('myrecharge') 
  }

  onSelectValue=(currentValue)=>{
   //开始支付,创建订单
   Api.api({
    api:'recharge/submit',
    crypt:3,
    waitingMessage:'请稍等...',
    data:{fee:currentValue,cardId:this.state.cardId},
    success:this._success
   });
  }


  _renderOther=()=>{
    return (
       <View style={[styles.btnGroup,{position:'absolute'}]}>
          <Button 
          onPress={this.onOther}
          textStyle={styles.btnRgText}
          style={[styles.btnRg,styles.other]} 
          text="为他人补登" />
          <View
          style={[styles.btnRg,styles.other,styles.btnMargin,{padding:10,opacity:0}]} 
          >
            <Text style={styles.btnRgText}>为他人补登</Text>
          </View>
        </View>
    );
  }

	render(){
    let enabled = this.state.cardId != null && this.state.cardId.length>0;
			return (
				<View style={CommonStyle.container}>
          <TitleBar title="卟噔充值" />  
          <View style={styles.container}>
            <AdvView id="4" />
            <View style={styles.txtContainer}>
              <TextInput 
                returnKeyType="done"
                numberOfLines={1}
                underlineColorAndroid={'#fff'}
                value={this.state.cardId}
                onChangeText={this._onSelectECard}
                style={styles.cardId} 
                placeholder="请输入要充值的e通卡号" />
              <ECardSelector 
                onSelectECard={this._onSelectECard} 
                style={styles.ecardSelector}>
                <Image 
                  source={require('./images/ic_select_card.png')} 
                  resizeMode="contain" 
                  style={styles.ecardSelectorIcon} />
              </ECardSelector>
            </View>
            <Image 
              style={styles.imgProgress} 
              resizeMode="contain" 
              source={require('./images/recharge_help_step.png')} />
            <View style={styles.btnGroup}>
              <RechargeButton enabled={enabled} onSelectValue={this.onSelectValue} value={50} />
              <RechargeButton enabled={enabled} onSelectValue={this.onSelectValue} value={100} style={styles.btnMargin} />
            </View>
            <View style={styles.btnGroup}>
              <RechargeButton enabled={enabled} onSelectValue={this.onSelectValue} value={150} />
              <RechargeButton enabled={enabled} onSelectValue={this.onSelectValue} value={200} style={styles.btnMargin} />
            </View>
            <View style={styles.container}>
              {Platform.OS=='android' ? this._renderOther() : null}
              <Image source={require('./images/budeng.png')} style={{alignSelf:'flex-end',height:'100%'}} resizeMode="contain" />
            </View>
          </View>
          <View style={styles.bottom}>
            <Button onPress={this.onPot} style={styles.btnLeft} iconStyle={styles.bottomIcon} icon={require('./images/ic_recharge_budeng_spot.png')} textStyle={styles.bottomText} text="卟噔网点" />
            <View style={styles.btnLine} />
            <Button onPress={this.onHis} style={styles.btnRight} iconStyle={styles.bottomIcon} icon={require('./images/ic_recharge_budeng_record.png')} textStyle={styles.bottomText} text="卟噔记录" />
          </View>
				</View>
			);
	}
}

const SELECTED_COLOR = '#EE7700';


const screenWidth = Dimensions.get('window').width;
const styles = StyleSheet.create({
  container:{flex:1},
  txtContainer:{flexDirection:'row',borderRadius:10,marginTop:10,marginRight:10,marginLeft:10,backgroundColor:'#fff',height:50},
  cardId:{height:50,fontSize:20,padding:5,flex:1,color:'#333333'},
  ecardSelectorIcon:{width:20,height:20},
  ecardSelector:{borderBottomRightRadius:10,alignItems:'center',justifyContent:'center',borderTopRightRadius:10,backgroundColor:'#EE7700',paddingLeft:15,paddingRight:15,height:50},
  imgProgress:{width:SCREEN_WIDTH,height:40,marginTop:10,marginBottom:10},
  

  btnRgText:{color:'#EE7700',fontSize:20},
  btnRgTextSel:{color:'#cccccc'},


  btnRg:{ paddingBottom:15,paddingTop:15, borderRadius:10,backgroundColor:'#fff',flex:1,justifyContent:'center',alignItems:'center'},
  btnRgSel:{backgroundColor:'#BBBBBB55'},
  

  btnGroup:{flexDirection:'row',marginLeft:10,marginRight:10,padding:7},
  btnMargin:{marginLeft:4},
  other:{borderWidth:1,borderColor:SELECTED_COLOR},
  bottom:{flexDirection:'row',padding:10},
  btnLeft:{flexDirection:'row',paddingTop:15,paddingBottom:15,flex:1,borderTopLeftRadius:10,borderBottomLeftRadius:10},
  btnRight:{flexDirection:'row',paddingTop:15,paddingBottom:15,flex:1,borderTopRightRadius:10,borderBottomRightRadius:10},
  btnLine:{backgroundColor:'#ccc',width:1/PixelRatio.get(),marginTop:5,marginBottom:5},
  bottomText:{color:'#333'},
  bottomIcon:{width:20,height:20,marginRight:5},
});
