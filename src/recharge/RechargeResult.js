
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
  PixelRatio,
  LocalData,
  Log,
  ScrollView,
  Platform,
} from '../../lib/Common';

import TitleBar from '../widget/TitleBar'
import Button from '../widget/Button'




export default class RechargeResult extends Component{
	constructor(props){
		super(props);
		this.state={cardId:null};
	}

  _onSelectECard=(cardId)=>{
    this.setState({cardId});
  }


	render(){
			return (
				
			);
	}
}

const SELECTED_COLOR = '#EE7700';


const screenWidth = Dimensions.get('window').width;
const styles = StyleSheet.create({
 
});
