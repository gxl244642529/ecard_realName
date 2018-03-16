

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
  Log,
  Form,
  LocalData,

} from '../../lib/Common';

import {Modal} from 'react-native'



class HisItem extends Component{

	onSearch=()=>{
		this.props.onSearch(this.props.keyword);
	}

	render(){
		return (
			<TouchableOpacity onPress={this.onSearch} style={styles.searchItem}>
				<Text style={styles.searchText}>{this.props.keyword}</Text>
			</TouchableOpacity>
		);
	}
}


export default class SearchHis extends Component{

	constructor(props) {
		super(props);
		this.state={};
	}

	renderItem=(row,index)=>{
		return (
			<View key={'row'+index}>
				<HisItem keyword={row} onSearch={this.props.onSearch} />
				<View style={styles.line}></View>
			</View>
		);
		
	}

	render(){

		return (
			<View style={this.props.style}>
				<View style={styles.div}>
					<Text style={styles.titleText}>搜索历史</Text>
				</View>
				{this.props.items.map(this.renderItem)}
			</View>
		);
	}

}

const styles = StyleSheet.create({
	container:{flex:1},
	titleText:{color:'#676768'},
	div:{height:35,backgroundColor:'#EEEEEE',paddingLeft:16,paddingTop:14},
	container:{flex:1},
	line:{backgroundColor:'#EEEEEE',height:1/PixelRatio.get(),marginLeft:16},
	searchText:{fontSize:15},
	searchItem:{paddingLeft:16,height:45 , justifyContent:'center'},
});

/*
<TouchableOpacity style={styles.leftButtonSelected}>
		            		<Text style={styles.textSelected}>优惠卡</Text>
		            	</TouchableOpacity>
*/

