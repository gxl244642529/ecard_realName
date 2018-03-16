
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

import Navigator,{PAY_ALIPAY,PAY_WEIXIN,PAY_ETONGKA,PAY_CMB} from '../lib/Navigator'
import PayUtil from '../../lib/PayUtil'
import Notifier from '../../lib/Notifier'

//新增
import StandardStyle from '../lib/StandardStyle'
import {IC_Weixin,IC_DrawInCircle,IC_SingleDraw,IC_DrawCircle} from './icons/Icons'

import {
  radioButton,
  loadingButton,
  loadingButtonRealDisabled
} from '../GlobalStyle'

import {
  LoadingButton
} from '../Global'
import QrUtils from './QrUtils'
const SCREEN_WIDTH = Dimensions.get('window').width;

class MoneyButton extends Component{
	constructor(props){
		super(props);
		this.state={}
	}
	render(){
		return <TouchableOpacity
		onPress={this.props.onPress}
		style={[styles.moneybtn,this.props.selected && styles.moneybtn_selected]}>
			<Text style={[StandardStyle.h3,StandardStyle.fontGray]}>{this.props.text}</Text>
			{this.props.selected && <View style={styles.triangle}/>}
			{this.props.selected && <View style={styles.singledraw}><IC_SingleDraw/></View>}
		</TouchableOpacity>
	}
}

class   PayTypeItem extends Component{
  constructor(props){
    super(props);
    this.state={}
  }
  render(){
    return <View><TouchableOpacity style={[StandardStyle.row,styles.payway]} onPress={this.props.onPress}>
      <View style={[StandardStyle.row,styles.acenter]}>
        <Image style={styles.payIconSty} source={this.props.payicon}/>
        <View style={styles.ml10}/>
        <Text>{this.props.payText}</Text>
      </View>
      {this.props.isSelect? <IC_DrawInCircle/>: <IC_DrawCircle/>}

    </TouchableOpacity>
    <View style={styles.lineView}/>
    </View>
  }
}

// const FEE_ARRAY = [1,10,100,200];
const FEE_ARRAY=[20,50,100,200];
const PAY_TYPES = [PAY_WEIXIN,PAY_ETONGKA,PAY_CMB];

export default class QrChage extends Component{
	constructor(props){
		super(props);
  	this.state={index:0,type:PAY_WEIXIN,weixin:true,balance:QrUtils.balance,acc:Account.user.qrAccount};
	}

  componentDidMount(){
    Api.detail(this,{
      api:'qr_acc/balance',
      success:this._getBalance
    });
    LocalData.getObject("index").then((result)=>{
      console.log("result==="+result);
        this.setState({index:result});

      },()=>{

      });
    Navigator.setIndex(0);

  }
  _getBalance=(result)=>{
    this.setState({balance:result.balance});
  }

	componentWillMount() {
		this.setState({submiting:false});
		Navigator.create("qr_trans",PAY_TYPES);
	}

	componentWillUnmount() {
		Navigator.destroy();
	}

	//支付流程
	 _onPayResult=(flag,data)=>{
	 	 this.setState({submiting:false});
     		console.log("flag="+flag);
	 	 switch(flag){
	 	 case PayUtil.ClinetPaySuccess:
	 	 	Api.push('/busqr/charge_result/'+data.orderId);
	 	 	return;
     case PayUtil.PayCancel:
       //A.alert('取消支付');
       return;
     case PayUtil.GetOrderInfoError:
        A.alert('支付错误');
        return;

	 	 }
	 	// Navigator.setCallback(this._onPayResult);
	  }

	onSubmit=()=>{
		if(this.state.index==0){
			A.alert("请选择充值金额！");
			return;
		}
    LocalData.putObject("index",this.state.index+"");
		// let arr=[2000,5000,10000,20000];
  //  let arr=[100,5000,10000,20000];
		let fee=FEE_ARRAY[this.state.index-1] * 100;
		this.setState({submiting:true});
		Navigator.call("qr_trans/submit",{fee:fee,type:this.state.type},this._onPayResult);
	}

	changeIndex=(index)=>{
		this.setState({index});
	}

	isSelected=(index)=>{
        return index==this.state.index;
    }
    _paySet=(type,index)=>{
         Navigator.setIndex(index);
        switch (type) {
          case PAY_WEIXIN:
            this.setState({weixin:true,etongka:false,cmb:false,type:type});
            break;
          case PAY_ETONGKA:
            this.setState({etongka:true,weixin:false,cmb:false,type:type});
            break;
          case PAY_CMB:
            this.setState({cmb:true,weixin:false,etongka:false,type:type});
            break;
          default:
        }

    }

	render(){
		let textProps={
			style:[StandardStyle.h3,StandardStyle.fontGray]
		}
			return (
				<View style={StandardStyle.container}>
					<TitleBar title="充值" />
					<View style={styles.msg}>
						<Text {...textProps}>当前账户:{this.state.acc}</Text>
						<Text {...textProps}>余额：{this.state.balance}</Text>
					</View>
					<View style={styles.ml10}>
							<Text {...textProps}>充值金额</Text>
					</View>
					<View style={styles.btnlist}>
						<View style={StandardStyle.row}>
							<MoneyButton text={FEE_ARRAY[0]+"元"} selected={this.isSelected(1)} onPress={()=>this.changeIndex(1)}/>
							<MoneyButton text={FEE_ARRAY[1]+"元"} selected={this.isSelected(2)} onPress={()=>this.changeIndex(2)}/>
						</View>
						<View style={StandardStyle.row}>
							<MoneyButton text={FEE_ARRAY[2]+"元"} selected={this.isSelected(3)} onPress={()=>this.changeIndex(3)}/>
							<MoneyButton text={FEE_ARRAY[3]+"元"} selected={this.isSelected(4)} onPress={()=>this.changeIndex(4)}/>
						</View>
					</View>
					<View style={styles.ml10}>
							<Text {...textProps}>支付方式</Text>
					</View>
			    <PayTypeItem
            payText="微信支付"
            payicon={require('./images/_pay_wx.png')}
            isSelect={this.state.weixin}
            onPress={()=>this._paySet(PAY_WEIXIN,0)}/>
          <PayTypeItem
            payText="e通卡账户支付"
            payicon={require('./images/ecard_pay.png')}
            isSelect={this.state.etongka}
            onPress={()=>this._paySet(PAY_ETONGKA,1)}/>
          <PayTypeItem
            payText="一网通银行卡支付"
            payicon={require('./images/cmb_pay.png')}
            isSelect={this.state.cmb}
            onPress={()=>this._paySet(PAY_CMB,2)}/>

					<View style={styles.bottomBtn}>
					 <LoadingButton
							loading={this.state.submiting}
							text="提交"
							onPress={this.onSubmit}
							disabled={this.state.submiting}
							style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
							textStyle={loadingButton.loadingButtonText} />
					</View>
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1},
	btnlist:{
		marginLeft:5,//+5
		marginRight:5,//+5
	},
	msg:{
		marginLeft:10,
		marginTop:10,
	},
	//moneybtn
	moneybtn:{
		marginTop:10,
		flex:1,
		borderRadius:5,
		borderWidth:1,
		borderColor:'#d7d7d7',
		margin:5,
		padding:10,
		paddingTop:15,
		paddingBottom:15,
		justifyContent:'center',
		alignItems:'center'
	},
	moneybtn_selected:{
		borderColor:'#e8464c',
	},
	payway:{
		justifyContent:'space-between',
		alignItems:'center',
    padding:15
	},
	bottomBtn:{
		position:'absolute',

		bottom:10,
		left:10,

		width:SCREEN_WIDTH-20
	},

	triangle:{
		position:'absolute',
		bottom:0,
		right:0,

		width:0,
		height:0,
		// borderRadius:15,
		borderTopWidth:30,
		borderTopColor:'transparent',

		borderRightWidth:30,
		borderRightColor:'red',


	},

	singledraw:{
		position:'absolute',
		bottom:0,
		right:0,
    backgroundColor:'transparent'
	},
	//功能样式
	ml10:{
		marginLeft:10
	},
	acenter:{
		alignItems:'center'
	},
  payIconSty:{
    width:35,
    height:35,
    resizeMode:'contain'
  },
  lineView:{
    height:1/PixelRatio.get(),
    backgroundColor:'#d7d7d7'
  }


});
