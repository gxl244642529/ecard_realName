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
		CommonStyle
		} from '../../lib/Common'

import TitleBar from '../widget/TitleBar'
import StateListView  from '../widget/StateListView'



export default class HistoryCoupon extends Component{

	constructor(props) {
   		super(props);
			this.state={loading:true,hasData:false};
	}
	_renderItem=()=>{
		return	<TouchableOpacity style={styles.MyCouponMain}>
				 <View style={styles.MyCouponMain_one}>
					<Text>实付金额</Text>
					<Text style={{ color:'#eb614b', marginLeft:5, }}>满35元</Text>
				 </View>

				 <View style={styles.MyCouponMain_two}>
					<Text style={{ color:'#b5b6b6', }}>有效期至</Text>
					<Text style={{ color:'#b5b6b6', marginLeft:5, }}>2016-9-22</Text>
					<Text style={{ color:'#b5b6b6', marginLeft:5, }}>11:39</Text>
				 </View>

				 <View style={styles.MyCouponMain_three}>
					<Text style={{ color:'#b5b6b6', }}>所属店家:</Text>
					<Text style={{ color:'#3fb0b1', marginLeft:5,}}>莲花麦当劳</Text>
				 </View>
			</TouchableOpacity>
	}

	render(){
    	return(
    		<View style={CommonStyle.container}>
    			<TitleBar style={{ color:'#804e21'}} title="历史优惠券" />

					<StateListView
						ref="list"

						api="coupon/list"

						style={  (this.state.loading !== this.state.hasData) ? styles.container : styles.listEmpty}
						renderRow={this._renderItem} />


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


});
