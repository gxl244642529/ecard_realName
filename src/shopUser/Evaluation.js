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
		TextInput
		} from '../../lib/Common'

import TitleBar from '../widget/TitleBar'

import {FormInput,FormSelect,FormSwitch} from '../../lib/TemplateForm'

export default class Evaluation extends Component{

	constructor(props) {
   		super(props);
	}

	render(){
    	return(
    		<ScrollView style={styles.container}>
    			<TitleBar title="查看标题"/>

    			<View style={styles.top}>
    				<Text  style={{ color:'#007964'}}>	全部(480)	</Text>
    				<Text  style={{ color:'#a38b77'}}>	好评(480)	</Text>
    				<Text  style={{ color:'#a38b77'}}>	中评(480)	</Text>
    				<Text  style={{ color:'#a38b77'}}>	差评(480)	</Text>
    			</View>

    			<View style={styles.main}>
    				<View style={styles.mainOne}></View>

    				<View style={styles.mainTwo}>
    					<Image style={{ width:60, height:60, }} source={require('./images/hb.png')} />
    					<View style={styles.Evaluation}>
    						<View style={{ height:60, justifyContent:'space-between', }}>
    							<View style={{ flexDirection:'row', justifyContent:'space-between', }}>
		 							<Text style={{ color:'#804f22', }}>都市食客</Text>
	    							<Text style={{ color:'#a38b77', }}>2016-10-1 12.06</Text>
    							</View>

    							<View style={{ flexDirection:'row', marginTop:10,  }}>
		 							<Text style={{ color:'#a38b77', marginRight:5, }}>打分</Text>
	    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_1.png')} />
	    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2,  }} source={require('./images/star_1.png')} />
	    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2,  }} source={require('./images/star_1.png')} />
	    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2,  }} source={require('./images/star_1.png')} />
	    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2,  }} source={require('./images/star_2.png')} />
    							</View>
    						</View>


    						<View style={styles.EvaluationThree}>
    							<Text style={{ color:'#a99280', fontSize:14, marginTop:5, }}>还会再来还会再来还会再来还会再来</Text>
							</View>

    					</View>
    				</View>

    				<View style={styles.mainThree}>
    					<Text style={{ color:'#eb614b', }}>水煮田鸡</Text>
    					<View style={styles.EvaluationListing}>
    						<View style={{ flexDirection:'row', marginBottom:5,}}>
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_1.png')} />
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_1.png')} />
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_1.png')} />
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_1.png')} />
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_2.png')} />
    						</View>

    						<Text>这家店不错哦，挺好吃的！都可以来尝尝，的确很不错。</Text>
						</View>
    				</View>

    				<View style={styles.mainThree}>
    					<Text style={{ color:'#eb614b', }}>水蒸龙虾</Text>
    					<View style={styles.EvaluationListing}>
    						<View style={{ flexDirection:'row', marginBottom:5,}}>
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_1.png')} />
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_1.png')} />
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_1.png')} />
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_1.png')} />
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_2.png')} />
    						</View>

    						<Text>这家店不错哦，挺好吃的！都可以来尝尝，的确很不错。</Text>
						</View>
    				</View>

    				<View style={styles.mainThree}>
    					<Text style={{ color:'#eb614b', }}>清蒸石斑</Text>
    					<View style={styles.EvaluationListing}>
    						<View style={{ flexDirection:'row', marginBottom:5,}}>
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_1.png')} />
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_1.png')} />
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_1.png')} />
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_1.png')} />
    							<Image style={{ width:14, height:14, marginLeft:2, marginTop:2, }} source={require('./images/star_2.png')} />
    						</View>

    						<Text>这家店不错哦，挺好吃的！都可以来尝尝，的确很不错。</Text>
						</View>
    				</View>


				</View>
			</ScrollView>
    	)
  	}
}

const styles = StyleSheet.create({
	container:{
		backgroundColor:"#fef3cc",
		flex:1,
	},
	top:{
		marginTop:20,
		flexDirection:'row',
		justifyContent:'space-around',
	},
	main:{
		marginTop:20,
		marginLeft:20,
		marginRight:20,
		marginBottom:20,
		backgroundColor:'#fff',
		borderRadius:10,
	},
	mainOne:{
		 padding:10,
		 height:10,
		 flex:1,
		 backgroundColor:'#e8d1ab',
		 borderRadius:'10 10 0 0',
	},


	mainTwo:{
		marginTop:10,
		padding:10,
		flexDirection:'row',
		borderBottomWidth:1,
		borderColor:'#e9e9e9',

	},
	Evaluation:{
		flex:1,
		marginLeft:10,

		flexDirection:'column',
	},
	EvaluationTwo:{
		borderBottomWidth:1,
		borderColor:'#e9e9e9',
		height:60,
	},
	EvaluationThree:{
		flex:1,
		marginTop:6,
		justifyContent:'center',
	},




	mainThree:{
		padding:10,
		flexDirection:'row',
		borderBottomWidth:1,
		borderColor:'#e9e9e9',

	},

	EvaluationListing:{
		marginLeft:10,
		flexDirection:'column',
		flex:1,
		color:'#b09987',
		fontSize:14,
	},
});
