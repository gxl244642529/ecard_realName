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
		CommonStyle,
		Api,
		A
		} from '../../lib/Common'

import TitleBar from '../widget/TitleBar'


const SCREEN_WIDTH = Dimensions.get('window').width;

export default class MyCouponDetails extends Component{

	constructor(props) {
   		super(props);
			this.state={};
	}
	componentDidMount(){

		let data = {code:this.props.params.id}
		Api.api({
			api:"coupon/detail",
			data:data,
			success:(result)=>{
				this.setState(result);
			}
		});
	}

	_use=()=>{
		let data = {code:this.props.params.id}
		A.confirm("是否确认使用?",(index)=>{
			if(index==0){
				Api.api({
					api:"coupon/use",
					data:data,
					success:(result)=>{
						// this.setState(result);
						A.toast("优惠使用成功");
						Api.goBack();
					}
				});
			}
		});

	}


	render(){
		let result = this.state;
    	return(
    		<ScrollView style={CommonStyle.container}>
    			<TitleBar style={{ color:'#804e21'}} title="我的优惠券详情" />
				<View style={{paddingLeft:5,paddingRight:5}}>
				<Image source={require("./images/coupontitle.png")} style={{alignItems:'center',marginLeft:5,width:SCREEN_WIDTH-20,resizeMode:'contain',height:200,marginTop:15,
					}}>
						<Text style={[styles.newFont,{fontSize:16,paddingBottom:15,marginTop:30}]}>{result.title}</Text>
						<Text style={styles.newFont}>到期时间:{result.endTime}</Text>
						<Text style={styles.newFont}>所属商家：{result.shpName}</Text>
					</Image>
					<View style={styles.newContent}>
						<Text style={{fontSize:16,marginBottom:5,color:'#070000'}}>使用说明</Text>
						<Text style={{color:'#353131',padding:5}}>请勿自行点击使用按钮，该操作由商家点击确认</Text>
						<Text style={{fontSize:16,marginBottom:5,color:'#070000'}}>优惠内容</Text>

						<Text style={{color:'#353131',padding:5}}>{result.desc}</Text>
					</View>
				</View>
    			{/*旧渲染方式<View style={styles.MyCouponDetailsMain}>
    				 <View style={styles.MyCouponDetails_one}>
    				 	<Text>标题:</Text>
    				 	<Text style={{ color:'#eb614b', marginLeft:5, }}>{result.title}</Text>
    				 </View>
				</View>

				<View style={styles.MyCouponDetailsMain}>
    				 <View style={styles.MyCouponDetails_one}>
    				 	<Text style={{ flex:1, }}>截止日期:</Text>

    				 	<View style={{ flexDirection:'row', }}>
    				 		<Text style={{ color:'#eb614b', marginLeft:5, }}>{result.endTime} </Text>

    				 	</View>
    				 </View>
				</View>

				<View style={styles.MyCouponDetailsMain}>
    				 <View style={styles.MyCouponDetails_one}>
    				 	<Text>优惠券来源:</Text>
    				 	<Text style={{ color:'#eb614b', marginLeft:5, }}>{result.shpName}</Text>
    				 </View>
				</View>

				<View style={styles.MyCouponDetailsMain}>
    				 <View style={[styles.MyCouponDetails_one,{flexDirection:'row'}]}>
    				 	<Text>优惠内容:</Text>
    				 	<Text style={{ color:'#eb614b', marginLeft:5,flexWrap:'wrap' }}>{result.desc}</Text>
    				 </View>
				</View>
				*/}


				{result.state===0 &&<View style={{ alignItems:'center', marginTop:20, }}>
					<TouchableOpacity style={styles.mainBottom} onPress={this._use}>
						<Image style={{ width:15, height:15, marginRight:5, }} source={require('./images/hook.png')}/>
						<Text style={{ color:'#fff', }}>立即使用</Text>
					</TouchableOpacity>
				</View>}


			</ScrollView>
    	)
  	}
}

const styles = StyleSheet.create({
	container:{
		backgroundColor:"#fef3cc",
		flex:1,
	},

	MyCouponDetailsMain:{
		padding:15,
		backgroundColor:'#fff',
		marginTop:10,
	},

	MyCouponDetails_one:{
		flexDirection:'row',
		justifyContent: 'space-between',

	},

	MyCouponDetails_two:{
		flexDirection:'row',
		marginTop:5,

	},

	MyCouponDetails_three:{
		flexDirection:'row',
		marginTop:5,
	},

	mainBottom:{
		flexDirection:'row',
		alignItems:'center',
		backgroundColor:'#3fb0b1',
		width:200,
		height:40,
		borderRadius:5,
		justifyContent:'center',
	},
	newImg:{
		//这里根据手机屏幕宽，样式可能会有适应性问题
		width:SCREEN_WIDTH-10,
		height:200,
		justifyContent:'center',
		alignItems:'center',
		resizeMode:"contain"
	},
	newFont:{
		color:'#fff',
		marginTop:5,
		backgroundColor:'transparent'
	},
	newContent:{
		paddingLeft:5,
		paddingRight:5,
		paddingTop:10,
		paddingBottom:20,
		backgroundColor:'#fff',
		borderRadius:5,
		marginTop:5,
		//height:100
	}
});
