

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

import SearchBar from '../widget/SearchBar'
import StateListView from '../widget/StateListView'


const FORM_KEY = "schoolSelect";

export default class SchoolSelect extends Component{

	constructor(props) {
		super(props);
		this.state={data:{name:''}};
		LocalData.getObject(FORM_KEY).then((result)=>{
	    	Log.info(result);
	    	this.setState(result);
	    },()=>{

	    });
	}

	componentWillReceiveProps(nextProps) {
		LocalData.getObject(FORM_KEY).then((result)=>{
	    	Log.info(result);
	    	this.setState(result);
	    },()=>{

	    });
	}

	onSearch=(name)=>{
		LocalData.putObject(FORM_KEY,{name});
		this.setState({data:{name}});
	}

	onSelect=(data)=>{
		this.props.onChange && this.props.onChange(data);
	}

	renderRow=(data)=>{
		return (
			<TouchableOpacity onPress={()=>{this.onSelect(data)}} style={styles.item}>
				<Text>{data.name}</Text>
			</TouchableOpacity>
		);

	}

	render(){

		return (
			<Modal visible={this.props.show} animationType="fade">
				<SearchBar 
					style={styles.searchBar}
					value={this.state.name} 
					placeholder="请输入学校关键字" 
					onSearch={this.onSearch} 
					onCancel={this.props.onCancel} />
				<StateListView 
					renderRow={this.renderRow} 
					cancelLoad={true} 
					api="school/list" 
					data={this.state.data} 
					style={styles.list} />
			</Modal>
		);
	}

}

const styles = StyleSheet.create({
	item:{padding:15,borderBottomWidth:1/PixelRatio.get(),borderBottomColor:'#ccc'},
	searchBar:{backgroundColor:'#eb613b'},
	list:{
		flex:1
	}
	
});

/*
<TouchableOpacity style={styles.leftButtonSelected}>
		            		<Text style={styles.textSelected}>优惠卡</Text>
		            	</TouchableOpacity>
*/

