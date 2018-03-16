import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  Image,
  TouchableOpacity,
  A,
  Api
} from '../../lib/Common';

import Button from './Button'

export default class ButtongroupTop extends Component{

  constructor(props) {
    super(props);
    this.state={selectedIndex:this.props.selectedIndex!==undefined ? this.props.selectedIndex : 0}
  }

  onChange=(index)=>{
  	if(index != this.state.selectedIndex){
  		this.setState({selectedIndex:index});
  		this.props.onChange && this.props.onChange(index);
  	}
  }

  _onBack=()=>{
  	if(this.props.onBack){
  		this.props.onBack();
  		return;
  	}
  	this.props.goBack();
  }
  _onAdd=()=>{
    if(this.state.selectedIndex<2){
      Api.push('/shop/addGoods/-1');
    }else{
      Api.push('/shop/addclassify');
    }
  }

  render(){
  	let buttonStyle = this.props.buttonStyle;
    return (
      <View style={[styles.container,this.props.style]}>
      <TouchableOpacity
          onPress={this._onBack}
          style={styles.backButton}
        >
        <Image source={require('../images/back.png')} style={styles.backImage} />
      </TouchableOpacity>
      {this.props.buttons.map((data,index)=>{
      	let selected = this.state.selectedIndex == index ;
      	let icon =  selected && data.iconSelected ? data.iconSelected : data.icon;
      	let textStyle = selected && data.textStyleSelected ? data.textStyleSelected : data.textStyle;
      	return <Button text={data.text}
      			onPress={()=>this.onChange(index)}
	      		/*icon={icon}
	      		iconPadding={data.iconPadding}
	      		iconStyle={data.iconStyle}*/
	      		textStyle={textStyle}
	      		style={[styles.button,buttonStyle]}
	      		key={"button"+index} />
      })}
      <TouchableOpacity
          onPress={this._onAdd}
          style={styles.backButton}
        >
        <Image source={require('../images/add.png')} style={styles.addImage} />
      </TouchableOpacity>
      </View>
    );

  }
}


const styles = StyleSheet.create({
	container:{
		flexDirection:'row',
		height:50,
    backgroundColor:'#FFFDEA',

	},
	button:{
		flex:1
	},
  backButton:{
		height:45,width:40,justifyContent:'center',alignItems:'center'
	},
  backImage:{
		width:9,height:19
	},
  addImage:{
    width:19,height:19
  },
  addImage:{
    width:19,height:19
  }
});
