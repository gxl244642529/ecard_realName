
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
  ScrollView
} from '../../lib/Common';

import {
  TitleBar,
} from '../Global'




export default class QrRequestSuccess extends Component{
	constructor(props){
		super(props);
		this.state={};
	}
	render(){
			return (
				<View style={CommonStyle.container}>
					<TitleBar title="申请成功" />
					<ScrollView style={styles.container}>
	
					</ScrollView>
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1}
});