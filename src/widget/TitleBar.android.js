
import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Image,
  TouchableOpacity,
  Dimensions,
  PixelRatio,
  A,
  Api
} from '../../lib/Common';

import {BackIcon} from './Icons'

const screenWidth = Dimensions.get('window').width;

export default class TitleBar extends Component{

  constructor(props) {
    super(props);
  }

  _onBack=()=>{
  	if(this.props.onBack){
  		this.props.onBack();
  		return;
  	}

  	Api.goBack();
  }


	render(){
		return (
			<View style={styles.titleBar}>
		        {this.props.isNoleft?<Text></Text>:<TouchableOpacity
	              onPress={this._onBack}
	              style={styles.backButton}
	            >
	           <BackIcon  style={styles.backImage}  />
	          </TouchableOpacity>}
		        <Text numberOfLines={1} style={styles.titleText}>{this.props.title}</Text>
		        <View>{this.props.renderRight&&this.props.renderRight()}</View>
		        <View style={styles.titleLine} />
			</View>
		);
	}
}


const styles = StyleSheet.create({
	titleLine:{
		height:1/PixelRatio.get(),
         width:screenWidth,
         position:'absolute',
         left:0,
         top:44,
         backgroundColor:'#dadbdb'
    },
	backImage:{
		width:9,height:19
	},
	backButton:{
		height:45,width:40,justifyContent:'center',alignItems:'center'
	},
	titleText:{
		// height:45,
		marginTop:12,
		color:'#595757',
		fontSize:15,
		textAlign:'center',
		position:'absolute',
		left:100,
		right:100,
    // backgroundColor:'red'
	},
	titleBar:{
		height:45,
    justifyContent:'space-between',
    flexDirection:'row',backgroundColor:'#FEFEFE'
	}
});
