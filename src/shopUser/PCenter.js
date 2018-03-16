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
		A
		// Account
		} from '../../lib/Common';
import {DeviceEventEmitter} from 'react-native'
import TitleBar from '../widget/TitleBar'
import {logout} from '../../lib/LoginUtil'
import Button from '../widget/Button'
import {makePhoneCall} from '../lib/CommonUtil'
import Account from '../../lib/network/Account'
import {onRequireLoginPress} from '../../lib/LoginUtil'

const SCREEN_WIDTH = Dimensions.get('window').width;
/*获取圆的值*/


export default class PCenter extends Component{

	constructor(props) {
   		super(props);
   		let {account} = Account.user || {};
   		this.state={account};
		//console.log(Account.user);
		this._myCollection = onRequireLoginPress(this._myCollection);
		this._myVip = onRequireLoginPress(this._myVip);
		this._myCoupon = onRequireLoginPress(this._myCoupon);
		
	}


	_onLoginSuccess=()=>{
		let {account} = Account.user || {};
		this.setState({account});
	}

	componentDidMount(){
    	this.subscription = DeviceEventEmitter.addListener('loginSuccess', this._onLoginSuccess);
	};

	onLoginSuccess=(data)=>{
			this.setState(data);
	}

	componentWillUnmount(){
	    this.subscription.remove();
	    this.subscription = null;
	}
	_myCollection=()=>{
		Api.push("/shopUser/collectionMain");
	}
	_myVip=()=>{
		Api.push("/shopUser/myVipcard");
	}
	_myCoupon=()=>{
		Api.push("/shopUser/myCoupon");
	}
	_callPhone=()=>{
		makePhoneCall("0592968870");
	}
	onLogout=()=>{
		A.confirm('确定要退出吗?',(index)=>{
			if(index==0){
				logout();
				this.setState({account:null});
			}
		});
	}

	onUserInfo=()=>{
		
	}


	render(){
			// console.log(Account.account);
    	return(
    		<View style={CommonStyle.container}>
    			<TitleBar title="个人中心"/>
						<ScrollView>
					 	<TouchableOpacity onPress={this.onUserInfo} style={styles.top}>
							<View>
									<Image  style={styles.imageStyle} source={require("./images/head.png")}/>
							</View>
							<View style={{ justifyContent:"center", flex:0.6,marginLeft:10}}>
								{this.state.account&&<View><Text style={{ color:'#fff',fontSize:15}}>	用户名	</Text>
								<Text style={{ color:'#fff', marginTop:5, fontSize:15, }}>	{this.state.account}	</Text>

								</View>}

							</View>


						</TouchableOpacity>

    				<TouchableOpacity style={[styles.formItem,]} onPress={this._myCoupon}>
	    				<Image style={{ width:15,height:15, marginLeft:10, marginRight:20,resizeMode:'contain' }} source={require('./images/benefit.png')} />
			        <Text style={{  color:'#a99268', }}>我的优惠券</Text>
		        </TouchableOpacity>
						<TouchableOpacity style={[styles.formItem,]} onPress={this._myVip}>
							<Image style={{ width:15,height:15, marginLeft:10, marginRight:20, }} source={require('./images/card.png')} />
							<Text style={{  color:'#a99268', }}>我的会员卡</Text>
						</TouchableOpacity>

		        {/*<View style={[styles.formItem,]}>
    				<Image style={{ width:15,height:20, marginLeft:10, marginRight:20, }} source={require('./images/address.png')} />
		            <Text style={{  color:'#a99268', }}>收货地址</Text>
		        </View>*/}

		        <TouchableOpacity style={[styles.formItem,]} onPress={this._myCollection}>
	    				<Image style={{ width:15,height:20, marginLeft:10, marginRight:20,resizeMode:'contain' }} source={require('./images/myCollection.png')} />
			        <Text style={{  color:'#a99268', }}>我的收藏</Text>
		        </TouchableOpacity>

		        <TouchableOpacity style={{ flexDirection:'row',justifyContent:'space-between', marginTop:5, backgroundColor:'#fff',}} onPress={this._callPhone}>
	    				<View style={styles.formItem}>
		    						<Image style={{ width:15,height:20, marginLeft:10, marginRight:20, resizeMode:'contain'}} source={require('./images/phone.png')} />
				            <Text style={{  color:'#a99268', }}>联系客服</Text>
			        </View>

	    				<TouchableOpacity style={{ flexDirection:'row', alignItems:'center',	}} onPress={this._callPhone}>
	    					<Text style={{ color:'#a99268', paddingRight:10, }} >968870</Text>
	    					<Text style={{ color:'#ea5442', paddingRight:10,}}>	>	</Text>
	    				</TouchableOpacity>
    			</TouchableOpacity>
					{this.state.account&&<Button text="退出登录" onPress={this.onLogout} style={styles.btn} textStyle={styles.btnText}/>}

					</ScrollView>
			</View>
    	)
  	}
}

const styles = StyleSheet.create({
	container:{
		backgroundColor:"#F0F0F0",
		flex:1,
	},

	top:{
		backgroundColor:"#43B0B1",
		padding:10,
		marginTop:2,
		flexDirection:'row',
		justifyContent:'space-between',
		alignItems:'center',
		marginTop:5,
	},
	round:{
		backgroundColor:"#fff",
		width:SCREEN_WIDTH/3.6,
		height:SCREEN_WIDTH/3.6,
		borderRadius:SCREEN_WIDTH/7.2,
	},
	formItem:{
    marginTop:5,
    backgroundColor:"#fff",
    height:40,
    paddingRight:30,
    paddingLeft:10,
    alignItems:'center',
    flexDirection:'row'
  },
	btn:{
		backgroundColor:'#3eb0b1',
		borderRadius:5,
		marginTop:40,
		marginBottom:10,
		marginLeft:50,
		marginRight:50,
	},
	btnText:{
		color:'#ffffff'
	},
	imageStyle:{
		width:70,
		height:70,
		borderRadius:35,
		// resizeMode:'contain'
	}

});
