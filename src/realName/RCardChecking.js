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
		PixelRatio
		} from '../../lib/Common';

import TitleBar from '../widget/TitleBar'
import StandardStyle from '../lib/StandardStyle'
import {lineView,isReal} from './realNameUtils'
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT = Dimensions.get('window').height;
/*获取屏幕的值*/

export default class RCardChecking extends Component{


	constructor(props) {
			super(props);
	}
	_back=()=>{
		Api.goBack();
	}
	_renderRight=()=>{
		return (
				<TouchableOpacity style={styles.lost} onPress={this._onPress}>
					<Text style={{color:'red'}}>其他方式></Text>
				</TouchableOpacity>
		);
	}
	_onPress=()=>{
		isReal((result)=>{
			if(result){
				Api.push('/realName/letterAgreeVerify/'+this.props.params.id);
			}else {
				let data = {cardId:this.props.params.id,fromto:1};
				Api.push('/realName/questionVertify/'+JSON.stringify(data));
			}
		});
	}


	render(){
    	return(
			<View style={[CommonStyle.container,{backgroundColor:"#fff"}]}>
			 <TitleBar
				 title="实名绑卡详情" renderRight={this._renderRight}/>


				 <View style={styles.top}>
						 <Image source={require('./images/checking.png')} style={styles.processImage}/>
				 </View>
				 	<View style={styles.line}/>

				 <View style={{flex:0.65,justifyContent:'center',alignItems:'center'}}>
						 <Image source={require('./images/checking_icon.png')} style={{		width:150,
								 height:150,
								 resizeMode:'contain'}}></Image>
						 <Text style={[StandardStyle.h4,StandardStyle.fontGray,{paddingTop:20,paddingLeft:20,paddingRight:20}]}>
								您已创建卟噔订单，请尽快到卟噔机上完成贴卡验证！

						 </Text>


				 </View>

				 <TouchableOpacity style={styles.bottom} onPress={this._back}>
					 <Text style={[StandardStyle.h4,StandardStyle.fontWhite,]}>返回</Text>
				 </TouchableOpacity>


	 </View>
    	)
  	}
}

const styles = StyleSheet.create({
	top:{
		justifyContent:'center',
		alignItems:'center',
		marginTop:10,
		marginBottom:10,
		flex:0.2
},
main:{
	padding:20,
	marginTop:30,
	justifyContent:'center',
	alignItems:'center',
	// backgroundColor:"#ededee",
},
bottom:{
	alignItems:'center',
	alignSelf:"center",
	width:SCREEN_WIDTH*0.9,
	padding:10,
	backgroundColor:"#e8464c",
	marginTop:60,
	position:"relative",
	bottom:30,

},
line:{
	height:1/PixelRatio.get(),
	backgroundColor:"#d7d7d7",
},
processImage:{
	//  width:300,
	width:SCREEN_WIDTH*0.9,
	 height:65,
	 resizeMode:'contain',
},
lost:{
	flexDirection:'row',
	height:45,
	width:80,
	justifyContent:'center',
	alignItems:'center'
},


});
