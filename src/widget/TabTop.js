
import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  TouchableOpacity,
  Image,
} from '../../lib/Common';

//import ButtonGroup from './ButtonGroup'
import ButtongroupTop from './ButtongroupTop'


import TabContainer from '../../lib/TabContainer'

export default class TabTop extends Component{

	constructor(props) {
	  super(props);

	  this.state={
	  	selectedIndex:this.props.selectedIndex!==undefined ? this.props.selectedIndex : 0,

	  }
	}


	onChange=(index)=>{
    	this.setState({selectedIndex:index});
  	}

  _onBack=()=>{
  	if(this.props.onBack){
  		this.props.onBack();
  		return;
  	}
  	this.props.goBack();
  }

	render(){
		return (
			<View style={styles.container}>
            <ButtongroupTop buttonStyle={this.props.buttonStyle} onChange={this.onChange} buttons={this.props.buttons} />
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
	flex:1,
  backgroundColor:"#FFF4CC"
  },
 
  backButton:{
		height:45,width:40,justifyContent:'center',alignItems:'center'
	},
});
