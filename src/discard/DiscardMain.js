

import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  TextInput,
  View,
  Image,
  TouchableOpacity,
  Dimensions,
  TouchableHighlight,
  Api,
  A,
  Account,
  ActivityIndicator,
  CommonStyle,
  PixelRatio
} from '../../lib/Common';

import {
  TitleBar,
} from '../Global'

import TabContainer from '../../lib/TabContainer'

import ButtonGroup from '../widget/ButtonGroup'

import DiscardQuery from './DiscardQuery'
import ExamQuery from '../exam/ExamQuery'

const screenWidth = Dimensions.get('window').width;



export default class DiscardMain extends Component{

	constructor(props) {
		super(props);
	    this.state = {index:0};
	}

	

	_onBack(){
		//直接退出
		Api.goBack();
	}

	_onChange=(index)=>{
		this.setState({index:index});
	}

	render(){
		let comp = this.state.index==0 ? <DiscardQuery /> : <ExamQuery />
		return (
			<View style={CommonStyle.container}>
				<View style={styles.titleBar}>
			        <TouchableOpacity
		              onPress={this._onBack}
		              style={styles.backButton}
		            >
		              <Image source={require('../images/back.png')} style={styles.backImage} />
		            </TouchableOpacity>
		            <ButtonGroup onChange={this._onChange} style={styles.buttonGroup} selectedIndex={0} buttons={BUTTONS}  />
		     		
		     		<View style={styles.backButton}/>
			        <View style={styles.titleLine} />
				</View>
				
				<TabContainer comps = {COMPS} selectedIndex={this.state.index} />
			</View>
		);
	}

}


const COMPS = [DiscardQuery,ExamQuery];

const styles = StyleSheet.create({
	buttonGroup:{height:35,width:160,alignSelf:'center'},
	text:{
		fontSize:12,
		color:'#ee6060',
		textAlign:'center'
	},
	textSelected:{
		fontSize:12,
		color:'#FFFFFF',
		textAlign:'center'
	},
	leftButton:{
		borderColor:'#ee6060',
		borderWidth:1,
		borderTopLeftRadius:7,
		borderTopRightRadius:0,
		borderBottomRightRadius:0,
		borderBottomLeftRadius:7,
		backgroundColor:'#FFFFFF',
	},
	leftButtonSelected:{
		borderColor:'#ee6060',
		borderTopLeftRadius:7,
		borderBottomLeftRadius:7,
		backgroundColor:'#ee6060',
	},
	rightButton:{
		borderWidth:1,
		borderColor:'#ee6060',
		borderTopLeftRadius:0,
		borderBottomLeftRadius:0,
		borderBottomRightRadius:7,
		borderTopRightRadius:7,
		backgroundColor:'#FFFFFF'
	},
	rightButtonSelected:{
		borderColor:'#ee6060',
		borderTopRightRadius:7,
		borderBottomRightRadius:7,
		backgroundColor:'#ee6060'
	},
	titleLine:{
		height:1/PixelRatio.get(),
         width:screenWidth,
         position:'absolute',
         left:0,
         top:64,
         backgroundColor:'#ccc'
    },
	backImage:{
		width:9,
		height:19
	},
	backButton:{
		height:45,
		width:40,
		justifyContent:'center',
		alignItems:'center'
	},
	titleText:{
		height:45,
		flexDirection:'row',
		alignItems:'center',
		justifyContent:'center',
		marginLeft:(screenWidth-90)/2-80
	},
	titleBar:{
		paddingTop:20,
		height:65,
		backgroundColor:'#FFFFFF',
		flexDirection:'row',
		justifyContent:'space-between'
	}
});

const BUTTONS = [
	{text:"优惠卡",textStyle:styles.text,textStyleSelected:styles.textSelected,buttonStyle:styles.leftButton,buttonStyleSelected:styles.leftButtonSelected},
  	{text:"年审",textStyle:styles.text,textStyleSelected:styles.textSelected,buttonStyle:styles.rightButton,buttonStyleSelected:styles.rightButtonSelected},
];





