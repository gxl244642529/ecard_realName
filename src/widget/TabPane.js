
import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  Animated,
  Dimensions
} from '../../lib/Common';
import ButtonGroup from './ButtonGroup'

import TabContainer from '../../lib/TabContainer'


export default class TabPane extends Component{


  static propTypes={
    groupStyle:React.PropTypes.any,			//组的样式
  }

	constructor(props) {
	  super(props);
	  let index = this.props.selectedIndex!==undefined ? this.props.selectedIndex : 0;
	  this.state={
	  	selectedIndex:index,
	  }
	}


	onChange=(index)=>{
    	this.setState({selectedIndex:index});
    	this.props.onChange && this.props.onChange(index);
  	}

	render(){
		return (
			<View style={[styles.container,this.props.style]}>
			  <ButtonGroup
			  	buttonStyle={this.props.buttonStyle}
			  	style={this.props.groupStyle}
			  	onChange={this.onChange}
			  	buttons={this.props.buttons} />
			  <TabContainer 
	          	comps={this.props.comps} 
	          	selectedIndex={this.state.selectedIndex} 
	          	compProps={this.props.compProps} />

	        </View>
        );
	}
}



const styles = StyleSheet.create({
  container:{
	flex:1
  },

  comp:{
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
  },
  hide:{
    overflow: 'hidden',
    opacity: 0,
  },

});
