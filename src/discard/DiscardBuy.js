

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


import PayUtil from '../../lib/PayUtil'
import ButtonGroup from '../widget/ButtonGroup'
import LoadingButton from '../widget/LoadingButton'

import ToggleButton from '../widget/ToggleButton'
import Drawable from '../widget/Drawable'
import StateList from '../widget/StateList'
import AddressPicker,{getDetailAddress} from '../widget/AddressPicker'
import {loadingButton,loadingButtonDisabled} from '../GlobalStyle'
import {arrayFind} from '../lib/ArrayUtil'


import {TYPE_OLD,TYPE_WORK,TYPE_HERO} from './DiscardUtil'

import Select from '../../lib/Select'

import Navigator,{PAY_ALIPAY} from '../lib/Navigator'




function getThumb(data) {
  switch (data.type) {
  case TYPE_OLD:
    return require('./images/pic_old.png');
  case TYPE_WORK:
    return require('./images/pic_old.png');
  case TYPE_HERO:
    return require('./images/pic_hero.png');
  default:

    return require('./images/pic_student.png');
  }
}

  /**
   * 是否补办
   * @return
   */
  function isReissued(data){
    
    return data.savType != 0;
    
  }
  function isStudentCard(data) {
    let type = data.type;
    if (type == 125000 || type == 126000 || type == 127000) {
      return false;
    }
    return true;
  }


  function getTransFee(){
    return 1000;
  }

/**
 * 卡的价格
 * @return
 */
function getCardPrice(data){
  if(isStudentCard(data) || isReissued(data)){
    return 1500;
  }
  return 0;
  
}

function getFormatTotalPrice(data) {
  let recharge = data.recharge;
  return  ( getCardPrice(data)  +(recharge ? 3000 :0)+ getTransFee(data) )/100;
}


function formatPrice(value){
  var str = value.toString();
  var index = str.indexOf('.');
  if( index  < 0 ){
      return str + ".00";
  }else if(index == str.length - 2){
      return str + "0";
  }else if(index == str.length - 3){
      return str;
  }else{
      return str.substring(0,index+3);
  }
}

export default class DiscardBuy extends Component{

	constructor(props) {
		super(props);
   
	  this.state = Object.assign({recharge:false,address:null},JSON.parse( this.props.params.json ));
     console.log(this.state );
	}

  componentWillUnmount() {
    PayUtil.destroy();
  }


  _onGetPayInfo=(flag,result)=>{
      A.alert("支付成功,我们会在7-10个工作日内制卡，请耐心等待发货",()=>{
        Api.goBack();
      });
  }

  _onPayResult=(flag,data)=>{
    if(flag==PayUtil.ClinetPaySuccess){
      //这里去获取支付信息
      PayUtil.getPayInfo(this._onGetPayInfo);
    }
  }

  _submit=()=>{
    let address = this.state.address;
    let state = this.state;
    this.setState({submiting:true});
    Api.api({
      api:"book/submit",
      data:{
        phone:address.phone,
        name:address.name,
        address:getDetailAddress(address),
        cardId:state.cardId,
        idCard:state.idCard,
        savType:state.savType,
        gdId:0,
        type:state.type,
        recharge:state.recharge === true,
      },
      success:(result)=>{
        this.setState({submiting:false});
        Navigator.pay(result.order_id,result.fee,"book",[PAY_ALIPAY],this._onPayResult);
      },
      error:(error,isNetworkError)=>{
        this.setState({submiting:false});
        return false;
      }
    });
  }

	render(){
    let disabled = this.state.submiting || this.state.address===null;
		return (
			<View style={CommonStyle.container}>
				<TitleBar title="购买卡" />
				<ScrollView style={styles.scrollView}>
          <AddressPicker onChange={(address)=>{this.setState({address})}} />
          <View 
            style={styles.cardPart}>
            <Image 
              style={styles.cardImage} 
              source={getThumb(this.state)} />
            <View>
              <Text style={styles.card}>标准卡</Text>
              <View style={styles.feeContainer}>
                <Text style={styles.feeLabel}>工本费:</Text>
                <Text style={styles.fee}>¥{formatPrice(getCardPrice(this.state)/100)}</Text>
              </View>
            </View>
          </View>
          <View style={styles.postFee}>
            <Text style={{fontSize:13,color:'#555'}}>邮费</Text>
            <Text style={{fontSize:13,color:'#F00'}}>¥ 10.00</Text>
          </View>

          {isStudentCard(this.state) && <ToggleButton 
            textStyle={styles.rechargeLabel}
            icon={require('../images/_ic_check_box_unchecked.png')} 
            iconSelected={require('../images/_ic_check_box_checked.png')}
            text="预充值30元" 
            selected={this.state.recharge}
            ref="recharge"
            onChange={(recharge)=>{this.setState({recharge})}}
            style={styles.recharge} />}
        </ScrollView>
        <View style={styles.bottomBar}>
          <Text>总计:</Text>
          <Text style={styles.bottomFee}>¥{formatPrice(getFormatTotalPrice(this.state))}</Text>
          <LoadingButton 
            text="购买" 
            onPress={this._submit}
            loading={this.state.submiting}
            disabled={disabled}
            style={disabled ? buyButtonDisabled : buyButton} 
            textStyle={loadingButton.loadingButtonText} />
        </View>
			</View>
		);
	}

}

const buyButton = [loadingButton.loadingButton,{width:100,margin:0}];
const buyButtonDisabled = loadingButtonDisabled.concat([{width:100,margin:0}]);





const styles = StyleSheet.create({
  bottomFee:{fontSize:20,color:'#f00',marginRight:10},
  cardPart:{backgroundColor:'#FFF',padding:7,marginTop:7,flexDirection:'row'},
  card:{fontSize:17},
  feeContainer:{flexDirection:'row', marginTop:10, alignItems:'flex-end'},
  cardImage:{width:100,height:64,marginRight:10},
  feeLabel:{fontSize:12},
  fee:{fontSize:17,color:'#F00'},
	ecardText:{backgroundColor:'#FFFFFF',padding:5,height:35,fontSize:15},
	scrollView:{
    padding:5,
    flex:1,
    backgroundColor:'#F2F2F2'
  },
  recharge:{alignItems:'center',justifyContent:'flex-start', height:35,padding:5,backgroundColor:'#FFFFFF',flexDirection:'row',marginTop:7,},
  rechargeLabel:{marginLeft:5},
  bottomBar:{
    padding:10,
    flexDirection:'row',
    justifyContent:'flex-end',
    alignItems:'flex-end'
  },
  postFee:{
    alignItems:'center',
    marginTop:7,
    backgroundColor:'#FFFFFF',
    padding:5,
    height:35,
    justifyContent:'space-between',
    flexDirection:'row'
  }

});



