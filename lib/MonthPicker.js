import React, { Component,PropTypes } from 'react';
import {
  TouchableOpacity
} from 'react-native';
const DatePickerModule = require('react-native').NativeModules.DatePickerModule;

export default class MonthPicker extends Component{

	static propTypes = {
		/**
		 * 提示
		 * @type {[type]}
		 */
	  placeHolder: React.PropTypes.string,
	  /**
	   * 月份：格式yyyy-MM
	   * @type {[type]}
	   */
	  value:React.PropTypes.string,  
	}

	constructor(props) {
	  super(props);
	  this.state = {};
	}

	onPress=()=>{
		let self = this;
		 DatePickerModule.select(this.props.value,this.props.placeHolder,"month",(date)=>{
		 	self.props.onChange && self.props.onChange(date);
		 });
	}

	render(){
		return (
			<TouchableOpacity style={this.props.style} onPress={this.onPress}>{this.props.children}</TouchableOpacity>
		);
	}

}