
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




export default class GoodsDetail extends Component{

	render(){
			<View style={CommonStyle.container}>
				<TitleBar title="标题" />
				<ScrollView style={styles.container}>

				</ScrollView>
			</View>
	}
}


const styles = StyleSheet.create({
	container:{flex:1}
});