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
		A,
		PixelRatio,
		CommonStyle

		} from '../../lib/Common'

import TitleBar from '../widget/TitleBar'
import StarButton from '../widget/StarButton'
// import CommonStar from '../widget/CommonStar'

const SCREEN_WIDTH = Dimensions.get('window').width;



export default class Evaluate extends Component{

	constructor(props) {
   		super(props);
			this.state={
				score:null,
				contents:"",
				num:0,
			}
	}
	// componentDidMount(){
	//
	// }
	_onUpdateScore=(isSelect,num)=>{
		console.log(num);
		this.setState({score:num});
		if(num===1){
			this.setState({one:true});
		}else if(num===2){
			this.setState({one:true,two:true});
		}else if(num===3){
			this.setState({one:true,two:true,three:true});
		}else if(num===4){
			this.setState({one:true,two:true,three:true,four:true});
		}else if(num===5){
			this.setState({one:true,two:true,three:true,four:true,five:true});
		}
	}
	_updateCanScore=(isSelect,num)=>{
		console.log("num="+num);
		this.setState({score:num-1});
		if(num===1){
			this.setState({two:false,three:false,four:false,five:false});
		}else if(num===2){
			this.setState({three:false,four:false,five:false});
		}else if(num===3){
			this.setState({four:false,five:false});
		}else if(num===4){
			this.setState({five:false});
		}
	}
	_evaluate=()=>{
		let score = this.state.score;
		console.log(this.state.contents);
		let data ={shpId:parseInt(this.props.params.id),content:this.state.contents,score:score,position:0}


		// if(this.state.score===undefined){
		// 		A.toast("您还没有打分");
		// 		return;
		// 	}
		//
		// 	Api.api({
		// 		waitingMessage:'正在提交...',
		// 		api:"bshop/evaluate",
		// 		data:data,
		// 		success:(result)=>{
		// 			console.log(result);
		// 			A.toast("评价成功");
		// 			Api.goBack();
		// 		}
		// 	});


		if (score!=null && score!=0) {
			Api.api({
				waitingMessage:'正在提交...',
				api:"bshop/evaluate",
				data:data,
				success:(result)=>{
					console.log(result);
					A.toast("评价成功");
					Api.goBack();
				}
			});
		}else if(score===null || score===0){
			A.toast("您还没有给店铺打分");
		}

	}
	updateNum=(i)=>{
		this.setState({num:i})
	}

	_renderHeader=()=>{
		// console.log(this.props.params.title);
		// console.log(this.props.params.thumb);
		// console.log(this.props.params.id);
		return<View style={{flexDirection:'row',justifyContent:'center',marginTop:10,marginLeft:80,marginRight:80}}>
			<Image style={{width:50,height:50,borderRadius:25,}} source={{uri:this.props.params.thumb}}/>
			<Text numberOfLines={1} style={{alignSelf:'center',marginLeft:10,}}>{this.props.params.title}</Text>
		</View>
	}
	_shopScore=()=>{
		return<View style={{flexDirection:'row',justifyContent:'center',marginTop:10}}>
					<View style={styles.titleLine}/>
					<Text style={{color:'#848383'}}>店铺评分</Text>
					<View style={styles.titleLine}/>
				</View>
	}
	_score=()=>{
		return	<View style={{flexDirection:'row',alignSelf:'center',marginTop:10}}>
			   	<StarButton style={styles.starStyle} isSelect={this.state.one} imageStyle={styles.imageStyle} updateScore={this._onUpdateScore} updateCanScore={this._updateCanScore} num={1}/>
					<StarButton style={styles.starStyle} isSelect={this.state.two} imageStyle={styles.imageStyle} updateScore={this._onUpdateScore} updateCanScore={this._updateCanScore} num={2}/>
					<StarButton style={styles.starStyle} isSelect={this.state.three} imageStyle={styles.imageStyle} updateScore={this._onUpdateScore} updateCanScore={this._updateCanScore} num={3}/>
					<StarButton style={styles.starStyle} isSelect={this.state.four} imageStyle={styles.imageStyle} updateScore={this._onUpdateScore} updateCanScore={this._updateCanScore} num={4}/>
					<StarButton style={styles.starStyle} isSelect={this.state.five} imageStyle={styles.imageStyle} updateScore={this._onUpdateScore} updateCanScore={this._updateCanScore} num={5}/>


			</View>

	}

	render(){
    	return(
    		<ScrollView style={[CommonStyle.container,{backgroundColor:'#f0f0f0'}]}>
    			<TitleBar title="发表评论"/>
					{this._renderHeader()}
					{this._shopScore()}
					{this._score()}

					<View style={{flex:1,marginTop:10}}>
						<TextInput
							blurOnSubmit={true}
							returnKeyType ='done'
							style={ styles.Input}
							multiline ={true}
							placeholder="说说哪里满意帮大家选择"
							underlineColorAndroid={'transparent'}
							onChangeText={(contents)=>this.setState({contents})} />
					</View>

					<View style={{ alignItems: 'center', }}>

	    			<TouchableOpacity style={ styles.botton} onPress={this._evaluate}>
	    				<Image style={{ width:15,height:15,marginRight:5, }} source={require('./images/hook.png')} />
	    				<Text style={{ color:'#fff', }}>发表评论</Text>
	    			</TouchableOpacity>
    			</View>



			</ScrollView>
    	)
  	}
}

const styles = StyleSheet.create({
	container:{
		backgroundColor:"#F0F0F0",
		flex:1,
	},

	titleLine:{
		height:0.8/PixelRatio.get(),
		width:SCREEN_WIDTH/2.5,
		marginTop:10,
		backgroundColor:'#E5E5E5'
		},

	Input:{
		height:70,
		backgroundColor:'#fff',
		color:'#b9a695',
		margin:10,
		borderRadius:10,
		paddingLeft:10,
		fontSize:14,

	},


	botton:{
		// flex:1,
		// paddingLeft:20,
		// paddingRight:20,
		width:200,
		height:40,
		borderRadius:5,
		backgroundColor:'#3fb0b1',
		marginTop:40,
		flexDirection:'row',
		alignItems: 'center',
		justifyContent:'center',
	},
	starStyle:{
		width:35,
		height:35,
		marginRight:5,
		alignSelf:'center',

	},
	imageStyle:{
		width:35,
		height:35,
		alignSelf:'center',

	},
});
