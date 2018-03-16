import React, { Component } from 'react';
import {
  StyleSheet,
  View
} from '../../lib/Common';

import Button from './Button'

export default class ButtonGroup extends Component{

  constructor(props) {
    super(props);
    this.state={selectedIndex:this.props.selectedIndex!==undefined ? this.props.selectedIndex : 0}
  }


  shouldComponentUpdate(nextProps, nextState) {
    return nextProps.selectedIndex !== this.state.selectedIndex || nextState.selectedIndex !== this.state.selectedIndex;
  }

  onPress=(index)=>{
  	if(index != this.state.selectedIndex){
  		this.setState({selectedIndex:index});
  		this.props.onChange && this.props.onChange(index);
  	}
  }

  render(){
    return (
      <View style={[styles.container,this.props.style]}>
      {this.props.buttons.map((data,index)=>{
      	let selected = this.state.selectedIndex == index ;
      	let icon =  selected && data.iconSelected ? data.iconSelected : data.icon;
      	let textStyle = selected && data.textStyleSelected ? data.textStyleSelected : data.textStyle;
        let buttonStyle = selected && data.buttonStyleSelected ? data.buttonStyleSelected : data.buttonStyle;

      	return <Button text={data.text}
      			onPress={()=>this.onPress(index)}
	      		icon={icon}
	      		iconPadding={data.iconPadding}
	      		iconStyle={data.iconStyle}
	      		textStyle={textStyle}
	      		style={[styles.button, buttonStyle]}
	      		key={"button"+index} />
      })}
      </View>
    );

  }
}


const styles = StyleSheet.create({
	container:{
		flexDirection:'row',
	},
	button:{
		flex:1
	}
});
