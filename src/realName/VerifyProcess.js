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
		PixelRatio,
		Notifier,
		} from '../../lib/Common';

import TitleBar from '../widget/TitleBar'
import StandardStyle from '../lib/StandardStyle'
import {lineView} from './realNameUtils'

const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT = Dimensions.get('window').height;
/*获取屏幕的值*/

export default class VerifyProcess extends Component{


	constructor(props) {
			super(props);
	}
	componentWillMount() {
		Notifier.addObserver("androidBack",this._onBack);

	}

	componentWillUnmount() {
			Notifier.removeObserver("androidBack",this._onBack);
	}
	_back=()=>{
		Api.goBack();
	}
	_onBack=()=>{
		let arr = Api.getRoutes();
		for(let i= arr.length-1; i >=0; --i){
			let vo = arr[i];
			if(vo.location===undefined){
				break;
			}
			if(vo.location.pathname=="/msgenter"){
				Api.returnTo("/msgenter");
				return true;
			}
			if(vo.location.pathname=="/personal/center"){
				 Api.returnTo("/personal/center");
				return true;
			}
		}
		Api.returnTo("/");
		return true;
	}


	render(){
    	return(
			<View style={[CommonStyle.container,{backgroundColor:"#fff"}]}>
			 <TitleBar
				 title="审核" onBack={this._onBack}/>



				 <View style={styles.top}>
						 <Image source={require('./images/img2.png')} style={styles.processImage}/>
				 </View>
				 	<View style={styles.line}/>

				 <View style={{flex:0.65,justifyContent:'center',alignItems:'center'}}>
						 <Image source={require('./images/checkProcess.png')} style={{		width:150,
								 height:150,
								 resizeMode:'contain'}}></Image>
						 <Text style={[StandardStyle.h4,StandardStyle.fontBlack,{paddingTop:20}]}>
									正在对您提交的资料进行审核,

						 </Text>
						 <Text style={[StandardStyle.h4,StandardStyle.fontBlack,{paddingTop:3}]}>
									请耐心等待1-2个工作日。

						 </Text>


				 </View>

				 <TouchableOpacity style={styles.bottom} onPress={this._onBack}>
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
}


});
