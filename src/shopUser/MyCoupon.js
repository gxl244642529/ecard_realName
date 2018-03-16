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
		CommonStyle
		} from '../../lib/Common'

import TitleBar from '../widget/TitleBar'
import StateListView from '../widget/StateListView'
const SCREEN_WIDTH = Dimensions.get('window').width;
import Notifier from '../../lib/Notifier'



export default class MyCoupon extends Component{

	constructor(props) {
		super(props);
		this.state={data:{canUse:true}};
	}
	componentWillMount() {
		Notifier.addObserver("coupon/use",this.refreshList);

	}

	componentWillUnmount() {
			Notifier.removeObserver("coupon/use",this.refreshList);
	}

	refreshList=()=>{
		this.refs.list.reloadWithStatus();
	}




	_couponDetail=(rowData)=>{
		console.log("code"+rowData.code);
		Api.push("/shopUser/myCouponDetails/"+rowData.code);
	}

	_renderTitle=(rowData)=>{
		return<View style={{flexDirection:'row',justifyContent:'space-between',backgroundColor:'transparent'}}>
						<View style={{flexDirection:'row'}}>
							<Text style={{color:'#fff'}}>{rowData.title}</Text>
							<Image source={require('./images/counter.png')} style={{width:19,height:19}}/>
						</View>

					</View>
	}
	_renderContent=(rowData)=>{
		return	<View style={{justifyContent:'space-between',flexDirection:'row'}}>
				<View style={{justifyContent:'space-between'}}>
					<View style={{flexDirection:'row',justifyContent:'space-between',paddingRight:20,marginTop:15,marginBottom:15}}>

						<Text style={{fontSize:12,marginTop:10,backgroundColor:'#fff'}}>截止时间:{rowData.endTime}</Text>

					</View>
					<Text style={{fontSize:12,marginBottom:10,backgroundColor:'#fff'}}>所属商家:{rowData.shpName}</Text>

				</View>
					{rowData.state===1&&<Image style={{width:100,height:100}} source={require("./images/false1.png")}/>}
			</View>
	}
	_renderList=(rowData)=>{
		return <TouchableOpacity  onPress={()=>{this._couponDetail(rowData)}}>
			 <Image style={styles.list} source={require('./images/count_re.png')}>
					{this._renderTitle(rowData)}
					{this._renderContent(rowData)}
				</Image>

		</TouchableOpacity>
	}
	_onPress=()=>{
		Api.push("/shopUser/myCouponHistory");
	}
	_renderRight=()=>{
		return (
				<TouchableOpacity style={{flexDirection:'row',height:45,width:45,justifyContent:'center',alignItems:'center'}}
					onPress={this._onPress}>
					<Text style={{color:'red'}}>历史</Text>
				</TouchableOpacity>
		);
	}
	render(){


    	return(
    		<View style={[CommonStyle.container,{backgroundColor:'#f0f0f0'}]}>
    			<TitleBar style={{ color:'#804e21'}} title="我的优惠券" renderRight={this._renderRight} />


					<StateListView
						ref="list"
						api="coupon/list"
						style={{flex:1,marginBottom:10}}
						data={this.state.data}
						renderRow={this._renderList}
					/>

			</View>
    	)
  	}
}

const styles = StyleSheet.create({
	container:{
		backgroundColor:"#fef3cc",
		flex:1,
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


});
