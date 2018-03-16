
import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  Image
} from '../../lib/Common';



const IconLabel = (props)=>{
	return (
		<View style={[props.style,styles.h]}>
           <Image
               style={props.iconStyle}
               source={props.icon}>
           </Image>
           <Text style={props.textStyle}>进度</Text>
       	</View>
	);
};

const styles = StyleSheet.create({
	h:{
		flexDirection:'row'
	},
	icon:{
		height:20,
	    width:20,
	    alignSelf:'center',
	    resizeMode:'contain'
	},
	text:{

	}

});
