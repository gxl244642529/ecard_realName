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
		A,
		RefreshControl,
		ActivityIndicator
		} from '../../lib/Common'

import TitleBar from '../widget/TitleBar'
import StateListView from '../widget/StateListView'
const SCREEN_WIDTH = Dimensions.get('window').width;
import Notifier from '../../lib/Notifier'
import LoadingButton from '../widget/LoadingButton'
import {formatDate} from '../lib/StringUtil'
import RCardNoReal from './RCardNoReal'
import {renderRow,isReal} from './realNameUtils'
import {InteractionManager} from 'react-native'
import {
	radioButton,
	loadingButton,
	loadingButtonReal,
	loadingButtonRealDisabled,
	container
} from '../GlobalStyle'
const NORMAL = 0; //正常
const LOST  = 1;  //报失中
const FUNDING  = 2; //打款中
const FUND_SUCCESS  = 3; //打款成功
const FUND_ERROR = 4;//打款失败
import {FROM_REAL,FROM_REAL_CARD,BANK_CARD_FUNDING ,FINISH} from './CommonData'
export const refreshColors = ['#ff0000','#00ff00','#0000ff','#3ad564'];


export default class RCardList extends Component{

	constructor(props) {
		super(props);
		this.state = {isRefreshing:false};
	}
	componentDidMount(){
		console.log("rcardlist调用")
		this.loadData();

	}
	componentWillMount() {
		var url =  Api.getRoutes()[Api.routeCount()-1].location.pathname;
		Notifier.addObserver("androidBack",this._onBack);
		Notifier.addObserver("rcard/lost",this.loadData);
		Notifier.addObserver("rcard/submit",this.loadData);
		Notifier.addObserver("rcard/submitVerify",this.loadData);
		Notifier.addObserver("rcard/nfcVerify",this.loadData);
		Notifier.addObserver("rcard/createBDOrder",this.loadData);
		Notifier.addObserver("rcard/deletehis",this.loadData);
		Notifier.addObserver("rcard/submitNotNamed",this.loadData);
		Notifier.addObserver("rcard/agreeVerify",this.loadData);
		Notifier.addObserver("newRcard/submitCardInfo",this.loadData)


	}

	componentWillUnmount() {
			Notifier.removeObserver("rcard/submit",this.loadData);
			Notifier.removeObserver("rcard/lost",this.loadData);
			Notifier.removeObserver("rcard/submitVerify",this.loadData);
			Notifier.removeObserver("rcard/nfcVerify",this.loadData);
			Notifier.removeObserver("rcard/createBDOrder",this.loadData);
			Notifier.removeObserver("rcard/deletehis",this.loadData);
			Notifier.removeObserver("rcard/submitNotNamed",this.loadData);
			Notifier.removeObserver("androidBack",this._onBack);
			Notifier.removeObserver("rcard/agreeVerify",this.loadData);
			Notifier.removeObserver("newRcard/submitCardInfo",this.loadData)
	}
	_onBack=()=>{
		// console.log("rcardlist的返回")
		var url =  Api.getRoutes()[Api.routeCount()-1].location.pathname;
		console.log("url="+url);
		if(url==='/realName/rCard' ){
			let arr = Api.getRoutes();
			for(let i= arr.length-1; i >=0; --i){
				let vo = arr[i];
				if(vo.root){
					Api.returnTo('/');
					return true;
				}
				if(vo.location===undefined){
					break;
				}
				if(vo.location.pathname=='/msgenter'){
					Api.returnTo('/msgenter');
					return true;
				}
				else if(vo.location.pathname=='/'){
					Api.returnTo('/');
					return true;
				}
				else if(vo.location.pathname=="/personal/center"){
					 Api.returnTo("/personal/center");
					return true;
				}

			}

		}

	}


	loadData=()=>{

		// Api.api({
		// 		api:"rcard/list",
		// 		success:(result)=>{
		// 			console.log(result);
		// 			if (result) {
		// 				console.log(result);
		// 				// this.setState(result);
		// 				this.setState({list:result});
		// 			}
		// 		},
		// 		message:(error)=>{
		// 			if(error=='valid'){
		// 				//这里没有实名化
		// 				Api.replace("/realName/rCardNoReal");
		// 				// Api.replace("/realName/overTrans");
		// 				return true;
		// 			}
		// 			return false;
		// 		}
		// 	});
			console.log("loadData调用")
			let data = {version:Api.version};
		  Api.detail(this,{api:"rcard/list",data:data,success:(result)=>{
								console.log(result);
								if (result) {
									// this.setState(result);
									this.setState({list:result});
										console.log(this.state.list);
								}
							},
							message:(error)=>{
								if(error=='valid'){
									// this.setState({valid:2})
									//这里没有实名化
									Api.replace("/realName/rCardNoReal");//0724
									// Api.replace("/realName/overTrans");
									return true;
								}
								return false;
							}});
	}
	_onRefresh=()=>{
		this.setState({isRefreshing: true});
		this.loadData();
		this.setState({isRefreshing: false});

	}
	_goBindCard=()=>{
		this._submit();
	}

	refreshList=()=>{
		this.refs.LIST.reloadWithStatus();
	}
	_isReal=()=>{
		let data = {fromto:FROM_REAL_CARD,isReal:FINISH}
		isReal((result)=>{
					if(result){
						Api.push('/realName/openLostConfirm/'+JSON.stringify(data));
					}else {
						Api.push('/realName/openLost/'+JSON.stringify(data))
					}
		});
	}

  _submit=()=>{
		A.confirm("一个账号最多可同时开通5张e通卡挂失服务，确认开通?",(index)=>{
			if(index==0){
				// Api.push("/realName/rCardMessage");
				this._isReal()
			}else{
				return;
			}
		});
  }
	_onPress=()=>{
		Api.push("/realName/rCardLostList");
	}




	_renderRight=()=>{
		return (
				<TouchableOpacity style={styles.lost} onPress={this._onPress}>
					<Text style={{color:'red'}}>挂失记录</Text>
				</TouchableOpacity>
		);
	}
		// <Text style={[styles.tipMsg,{marginTop:5}]}> 赶紧去新增实名绑卡吧！</Text>
 renderEmpty(){
		return (

			<TouchableOpacity style={styles.centering}
				onPress={this._goBindCard}
				renderRight={this.renderRight}
				>
				<Image source={require('./images/nocard.png')} style={styles.nocardImage}/>
				<Text style={styles.tipMsg}>您还没有开通挂失的e通卡</Text>

			</TouchableOpacity>

		);
	}
	render(){


    	return(
    		<View style={[CommonStyle.container,container.container]}>


					<TitleBar style={{ color:'#804e21'}} title="挂失服务" renderRight={this._renderRight}
					onBack={this._onBack}/>
						{!this.state.list&&<View style={styles.content}><ActivityIndicator /></View>}
						{
						 this.state.list && this.state.list.length===0 &&
							this.renderEmpty()
					 }
					 {this.state.list && this.state.list.length>0&&  <ScrollView style={{flex:0.85}}
 							refreshControl={<RefreshControl
 							refreshing={this.state.isRefreshing}
 							onRefresh={this._onRefresh}
 							colors={refreshColors}
 							progressBackgroundColor="#ffffff"
 							/>}
 						>

 							{
 							 this.state.list && this.state.list.map((data,index)=>{
 									 return renderRow(data,index)
 							 })
 						 }
 	          </ScrollView>}

	          { this.state.list &&this.state.list.length<5&& <View style={{flex:0.15}}>
	            <LoadingButton
	              loading={this.state.submiting}
	              text="新增开通挂失e通卡"
	              onPress={this._submit}
	              disabled={this.state.submiting}
	              style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
	              textStyle={loadingButton.loadingButtonText} />
	          </View>}

			</View>
    	)
  	}
}

const styles = StyleSheet.create({
	container:{
		backgroundColor:"#fef3cc",
		flex:1,
	},
	content:{justifyContent:'center',alignItems:'center',flex:1},
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
	lost:{
		flexDirection:'row',
		height:45,
		width:80,
		justifyContent:'center',
		alignItems:'center'
	},
	rowDataStyle:{
		height:100,
		backgroundColor:'#fff',
		marginTop:10,
		justifyContent:'center',
	},
	lostTouStyle:{
		width:80,
		height:40,
		backgroundColor:'#e8464c',
		justifyContent:'center',
		alignItems:'center',
		borderRadius:5
	},
	statusStyle:{
		width:80,
		height:50,
		justifyContent:'center',
		alignItems:'center'
	},
	centering: {
		flex:0.85,
		alignItems: 'center',
		justifyContent: 'center'
	},
	nocardImage:{
		width:150,
		height:150,
		resizeMode:'contain'
	},
	tipMsg:{
		fontSize:15,
		color:'#595757',
		marginTop:15
	}


});
