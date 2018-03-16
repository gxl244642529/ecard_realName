import React,{Component} from 'react';

import {
		View,
		Text,
		StyleSheet,
		Image,
		TouchableOpacity,
		Dimensions,
		ScrollView,
		ImagePicker,
		Switch,
		TextInput,
		Api,
		CommonStyle,
		ListView,
		A
		} from '../../lib/Common'

import TitleBar from '../widget/TitleBar'
import StateListView from '../widget/StateListView'
const SCREEN_WIDTH = Dimensions.get('window').width;
import Notifier from '../../lib/Notifier'
import LoadingButton from '../widget/LoadingButton'
import {formatDate} from '../lib/StringUtil'
import StandardStyle from '../lib/StandardStyle'
import {
	radioButton,
	loadingButton,
	loadingButtonRealDisabled
} from '../GlobalStyle'



export default class RCardNoReal extends Component{

	constructor(props) {
		super(props);
		this.state = {
		};
	}

  _submit=()=>{
	   Api.push("/realName/overTrans");
  }
	_onPress=()=>{
		Api.push("/realName/rCardLostList");
	}
	_renderRow=(rowData)=>{
		return <TouchableOpacity  style={{height:50,backgroundColor:'#f0f0f0',}} onPress={()=>{this._couponDetail(rowData)}}>
          <Text>{rowData.cardId} </Text>
					<Text>{rowData.cardIdExt} </Text>
					<Text>{formatDate(rowData.createDate)}</Text>

		</TouchableOpacity>
	}

	_renderRight=()=>{
		return (
				<TouchableOpacity style={{flexDirection:'row',height:45,width:80,justifyContent:'center',alignItems:'center'}}
					onPress={this._onPress}>
					<Text style={{color:'red'}}>挂失记录</Text>
				</TouchableOpacity>
		);
	}
	// <TouchableOpacity style={{flex:0.2}} onPress={this._submit}>
	// 		<Image source={require('./images/images.png')} style={{ width:SCREEN_WIDTH, height:SCREEN_WIDTH*0.3, }}/>
	// </TouchableOpacity>
	render(){


    	return(
    		<View style={[CommonStyle.container,{backgroundColor:'#f0f0f0'}]}>
					<TitleBar style={{ color:'#804e21'}} title="挂失服务"  />



						<View style={{flex:0.65,justifyContent:'center',alignItems:'center'}}>
								<Image source={require('./images/norealapp.png')} style={{		width:150,
										height:150,
										resizeMode:'contain'}}></Image>
								<Text style={[StandardStyle.h4,StandardStyle.fontBlack,{paddingTop:20}]}>
											先认证，后挂失。

								</Text>
								<Text style={[StandardStyle.h4,StandardStyle.fontBlack]}>

									为确保您e通卡的资金安全，请先进行身份认证。
								</Text>

						</View>
						<View style={{flex:0.15}}>
	            <LoadingButton
	              loading={this.state.submiting}
	              text="立即开通挂失服务"
	              onPress={this._submit}
	              disabled={this.state.submiting}
	              style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
	              textStyle={loadingButton.loadingButtonText} />
						</View>


			</View>
    	)
  	}
}

const styles = StyleSheet.create({
	container:{
		backgroundColor:"#fef3cc",
		flex:1,
	},
  listStyle:{
    flex:1,
    marginBottom:10
  },
	container:{flex:1},
	listHasData:{flex:1},
	listEmpty:{height:0},

	MyCouponMain:{
		padding:15,
		backgroundColor:'#fff',
		marginTop:10,
	},

	MyCouponMain_one:{
		flexDirection:'row',

	},

	MyCouponMain_two:{
		flexDirection:'row',
		marginTop:5,

	},

	MyCouponMain_three:{
		flexDirection:'row',
		marginTop:5,
	},
	list:{
		paddingLeft:25,
		paddingTop:12,
		paddingBottom:15,
		paddingRight:15,
		marginLeft:10,
		justifyContent:'space-between',

		width:SCREEN_WIDTH-20,
		height:135,

		marginTop:20,
	},
	butStyle:{
		bottom:-200,
	}


});
