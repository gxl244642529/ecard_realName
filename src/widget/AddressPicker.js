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
  Log
} from '../../lib/Common';


const AddressPickerModule = require('react-native').NativeModules.AddressPickerModule;


export function getAddress(address){
	if(address.qu){
		return address.sheng + address.shi + address.qu;
	}
	return address.sheng + address.shi;
}

export function getDetailAddress(address){
	console.log(address);
	return getAddress(address) + address.jie;
}

import {globalStyles} from '../GlobalStyle'

export default class AddressPicker extends Component{

	constructor(props) {
		super(props);
		this.state={address:null,submiting:true};
	}

	
	/**
		加载默认地址
	*/
	componentDidMount() {
		Api.api({
			api:"address/def",
			success:(result)=>{
				this._onRecvAddress(result);
				this.setState({address:result,submiting:false});

			},
			error:(error,isNetworkError)=>{
				this.setState({address:null,submiting:false});
			}

		});
	}

	_onRecvAddress=(address)=>{
		this.props.onChange && this.props.onChange(address === undefined ? null : address);
	}

	_renderLoading=()=>{
		return <View style={[styles.loading,this.props.style]}>
				<ActivityIndicator
			        animating={true}
			        style={globalStyles.loading}
			    /><Text style={styles.text}>正在加载地址...</Text>
			</View>
	}

	_selectAddress=()=>{
		AddressPickerModule.selectAddress((address)=>{
			this._onRecvAddress(address);
			this.setState({address:address,submiting:false});
		});
	}

	_renderAddress=(address)=>{
		return (
			<TouchableOpacity onPress={this._selectAddress} style={[styles.baseLoading,this.props.style]}>
				<View style={{flexDirection:'row',justifyContent:'space-between'}}>
					<Text style={styles.name}>{address.name}</Text>
					<Text style={styles.phone}>{address.phone}</Text>
				</View>
				<Text style={styles.address}>{getDetailAddress(address)}</Text>
				<Image style={{position:'absolute',right:10,top:39, width:7,height:12}} source={require('../images/_ic_arrow.png')} />
			</TouchableOpacity>
		);
	}

	_renderEmpty=()=>{
		return <TouchableOpacity onPress={this._selectAddress} style={[styles.loading,this.props.style]}>
				<Text style={styles.text}>请选择收货地址</Text>
			</TouchableOpacity>
	}

	_renderError=()=>{
		return <View />
	}
	
	render(){
		if(this.state.submiting){
			return this._renderLoading();
		}else{
			if(this.state.address){
				return this._renderAddress(this.state.address);
			}else{
				return this._renderEmpty();
			}
		}
	}
}




const styles = StyleSheet.create({
	name:{
		fontSize:15,
		color:'#333',
		alignSelf:'flex-start'
	},
	phone:{
		fontSize:15,
		color:'#333',
		marginRight:10,
		alignSelf:'flex-end'
	},
	address:{
		marginTop:10,
		fontSize:13,
		color:'#555',
	},

	text:{
		fontSize:12,
		color:'#555'
	},
	baseLoading:{
		padding:20,
		backgroundColor:'#FFF',
    	height:90,
	},
   	loading:{
    backgroundColor:'#FFF',
    height:90,
    justifyContent:'center',
    alignItems:'center'
  }
});