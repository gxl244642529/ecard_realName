import React, { Component,PropTypes } from 'react';
import {
  TouchableOpacity
} from 'react-native';
const DatePickerModule = require('react-native').NativeModules.DatePickerModule;

export default class DatePicker extends Component{

	static propTypes = {
	  placeHolder: React.PropTypes.string,
	  value:React.PropTypes.string,
	  max:React.PropTypes.string,
	  min:React.PropTypes.string
	}

	constructor(props) {
	  super(props);
	  this.state = {};
	}



	onPress=()=>{
		let self = this;
		let opts = {};
		if(this.props.max){
			opts.max = this.props.max;
		}
		if(this.props.min){
			opts.min = this.props.min;
		}


		 DatePickerModule.select(this.props.value,this.props.placeHolder,"date", opts, (date)=>{
		 	self.props.onChange && self.props.onChange(date);
		 });
	}

	render(){
		return (
			<TouchableOpacity style={this.props.style} onPress={this.onPress}>{this.props.children}</TouchableOpacity>
		);
	}

}