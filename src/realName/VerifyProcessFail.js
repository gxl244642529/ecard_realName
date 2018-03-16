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

	_angin=()=>{
		Api.replace("/realName/messageSubmit");
	}
	_onBack=()=>{
		let arr = Api.getRoutes();
		for(let i= arr.length-1; i >=0; --i){
			let vo = arr[i];
			if(vo.location===undefined){
				break;
			}
			if(vo.location.pathname=='/personal/center'){
				Api.returnTo('/personal/center');
				return true;
			}
			if(vo.location.pathname=="/msgenter"){
				 Api.returnTo("/msgenter");
				return true;
			}
		}
		Api.returnTo("/");
		return true;
	}



	render(){
    	return(
    		<View style={CommonStyle.container}>
				<TitleBar
					 title="审核" onBack={this._onBack}/>



				 <View style={styles.top}>
						 <Image source={require('./images/img3.png')} style={{ width:300, height:65, }}/>
				 </View>

				 <View style={styles.line}/>

				<View style={{flex:0.65,justifyContent:'center',alignItems:'center'}}>
						<Image source={require('./images/checkFail.png')} style={{		width:150,
								height:150,
								resizeMode:'contain'}}></Image>
						<Text style={[StandardStyle.h4,StandardStyle.fontBlack,{paddingTop:20}]}>
								您提交的实名资料审核失败！
						</Text>
						<Text style={[StandardStyle.h4,StandardStyle.fontBlack,{paddingLeft:10,paddingRight:10}]}>

							原因:{this.props.data}
						</Text>

				</View>



				 <TouchableOpacity style={styles.bottom} onPress={this._angin}>
					 <Text style={[StandardStyle.h4,StandardStyle.fontWhite,]}>重新提交资料</Text>
				 </TouchableOpacity>

			</View>
    	)
  	}
}

const styles = StyleSheet.create({
	top:{
		justifyContent:'center',
		alignItems:'center',
		flex:0.2,
		marginTop:10,
		marginBottom:10,

	},
	main:{
		padding:10,
		marginTop:20,
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
