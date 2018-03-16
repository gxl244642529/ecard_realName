import React, { Component,PropTypes } from 'react';
import {
  TouchableOpacity,
  Text,
  Image,
  View,
  StyleSheet
} from '../../lib/Common';

import ButtonGroup from './ButtonGroup'


export default class RadioButtonGroup extends Component{



	static propTypes = {
		styleConfig:PropTypes.object.isRequired,
	  	value:PropTypes.any,
	  	values:PropTypes.array.isRequired,
	  	labels:PropTypes.array.isRequired,
	}

	constructor(props) {
	  super(props);
	  //根据values计算
	  let values = props.values;
	  let labels = props.labels;
	  var styles = [];
	  for(let i=0 ,c = values.length; i < c ;++i){
	  	styles.push({...props.styleConfig, ... { text: labels[i]}});
	  }
	  this.state={buttons:styles};
	  
	}



	_getSelectedIndex(){
		 let index = -1;
		  //扫描
		  for(let i =0 , c= this.props.values.length ; i < c; ++i){
		  	if(this.props.value === this.props.values[i]){
		  		index = i;
		  		break;
		  	}
		  }
		  return index;
	}

	_onChange=(index)=>{
		console.log(this.props);
		this.props.onChage && this.props.onChage(index,this.props.values[index]);
	}


	render(){
		//整合
		
		return <ButtonGroup 
			onChange={this._onChange}
			selectedIndex={this._getSelectedIndex()} 
			buttons={this.state.buttons} 
			style={this.props.style} />
	}
}