import React, { Component,PropTypes } from 'react';
import {
  TouchableOpacity,
  TextInput
} from 'react-native';

const SelectModule = require('react-native').NativeModules.SelectModule;

const systemButtonOpacity = 0.2;
export default class Select extends Component{

	static propTypes = {
	  value:PropTypes.any,
	  values:PropTypes.array.isRequired,
	  labels:PropTypes.array.isRequired,
	}
	

	constructor(props) {
	  super(props);
	  this.state = {selectedIndex:-1};
	}

	componentWillReceiveProps(nextProps) {
		if(nextProps.value !== undefined){
		  let index = -1;
		  //扫描
		  for(let i =0 , c= nextProps.values.length ; i < c; ++i){
		  	if(nextProps.value === nextProps.values[i]){
		  		index = i;
		  		break;
		  	}
		  }
		  this.state = {selectedIndex:index};
		}
	  
	}

	focus(){
		
	}

	getValue(){
		if(this.state.selectedIndex>=0){
			return this.props.values[this.state.selectedIndex];
		}
		
		return null;
	}

	_onPress=()=>{
		SelectModule.select(
			this.props.labels,
			this.state.selectedIndex,
			this.props.title || this.props.placeholder,
			(i)=>{
				let index = parseInt(i);
				let value = this.props.values 
					? this.props.values[index] 
					: null;
				this.setState({selectedIndex:index});
				this.props.onChange 
					&& this.props.onChange(index,value,this.props.labels[index]);
			}
		);
	}

	_computeActiveOpacity() {
	    if (this.props.disabled) {
	      return 1;
	    }
	    return this.props.activeOpacity != null ?
	      this.props.activeOpacity :
	      systemButtonOpacity;
	  }

	render(){

		let activeOpacity = this._computeActiveOpacity();
		let touchableProps = {
	      activeOpacity: activeOpacity,
	    };

	    if (this.props.values.length > 0 && !this.props.disabled) {
	      touchableProps.onPress = this._onPress;
	    }
	   
		return (
			<TouchableOpacity 
				{...touchableProps}
				style={this.props.style}>
			{this.props.children}
			</TouchableOpacity>
		);
	}
}
