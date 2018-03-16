
import React, { Component } from 'react';
import {
  StyleSheet,
  View,
} from '../../lib/Common';
import ButtonGroup from './ButtonGroup'


import TabContainer from '../../lib/TabContainer'


export default class TabHost extends Component{

	static propTypes={
	  groupStyle:React.PropTypes.object,			//组的样式
	}

	
	constructor(props) {
	  super(props);
	  this.state={
	  	selectedIndex:this.props.selectedIndex!==undefined ? this.props.selectedIndex : 0,
	  }
	}

	/*
	shouldComponentUpdate(nextProps, nextState) {
		return nextProps.selectedIndex!==undefined && nextProps.selectedIndex !== this.state.selectedIndex;
	}*/

	componentWillReceiveProps(nextProps) {
	  if(nextProps.selectedIndex!==undefined && nextProps.selectedIndex !== this.state.selectedIndex){
	  	this.setState({selectedIndex:nextProps.selectedIndex});
	  }
	}


	onChange=(index)=>{
    	this.setState({selectedIndex:index});
  	}


	render(){
		return (
			<View style={styles.container}>
	         <TabContainer 
	          	comps={this.props.comps} 
	          	selectedIndex={this.state.selectedIndex} 
	          	compProps={this.props.compProps} />
	          <ButtonGroup 
	          	buttonStyle={this.props.buttonStyle} 
	          	onChange={this.onChange} 
	          	buttons={this.props.buttons} />
	        </View>
        );
	}
}




const styles = StyleSheet.create({
   container:{
	flex:1
  },

});
