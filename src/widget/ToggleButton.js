

import React, { Component } from 'react';
import {
  TouchableHighlight,
  View,
  Image,
  Text,
  StyleSheet
} from 'react-native';

import Button from './Button'


export default class ToggleButton extends Component{


	constructor(props) {
	  super(props);
	
	  this.state = {selected:this.props.selected === undefined ? false : this.props.selected};
	}

	_onPress=()=>{
		console.log(this.state);
		let selected = !this.state.selected;
		this.setState({selected});
		this.props.onChange && this.props.onChange(selected);
	}

	isSelected(){
		return this.state.selected;
	}

	render(){
		let {icon,iconSelected,iconStyle,...otherProps} = this.props;
 		let img = <Image source={this.state.selected ? iconSelected : icon} style={iconStyle} />

		return <TouchableHighlight 
			underlayColor="#ececec"
			onPress={this._onPress} 
			{...otherProps}>
				<View style={styles.container}>{img}<Text style={[styles.text,this.props.textStyle]}>{this.props.text}</Text></View>
			</TouchableHighlight>
	}

}


const styles = StyleSheet.create({
  container:{
   	flexDirection:'row'
  },
   text:{
    color:'#333'
  },

});