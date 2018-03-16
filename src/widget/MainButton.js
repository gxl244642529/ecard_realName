
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
  Api
} from '../../lib/Common';

import {connect} from 'react-redux';
import { push } from 'react-router-redux'
import {bindActionCreators} from 'redux'

const screenWidth = Dimensions.get('window').width;

export default class MainButton extends Component{

	constructor(props) {
    super(props);

  }
  _onClickItem(){
    Api.push(this.props.jumpName);
  }

	render(){
		return (
      <TouchableOpacity 
        style={[styles.button,this.props.icon]} 
        nav={this.props.nav} 
        onPress={this._onClickItem.bind(this)}>
        <Image 
          style={styles.style_image}  
          source={this.props.source}/>
      </TouchableOpacity>
		);
	}
}


const iconWidth = screenWidth*0.386*0.8;
const iconHeight = screenWidth*0.350*0.8;

const styles = StyleSheet.create({
  button:{
     flex:1,
     alignItems:'center',
     justifyContent:'center',
     borderRadius:5
  },
  style_image:{
    // borderRadius:50,
    width:iconWidth,
    height:iconHeight,
    resizeMode:'contain',
    alignSelf:'center',
  },
});



