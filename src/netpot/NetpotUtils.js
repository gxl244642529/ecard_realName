import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  Image,
  TouchableHighlight,
  Api,
  PixelRatio
} from '../../lib/Common';

export const IC_SERVICE = require('./images/ic_service.png');
export const IC_CON = require('./images/ic_con.png');
export const IC_PURCH = require('./images/ic_purch.png');
export const IC_CHARGE = require('./images/ic_charge.png');
export const IC_RECHARGE = require('./images/ic_recharge.png');
export const formatDistance=(distance)=>{
	return (distance / 1000).toFixed(1) + "km";
}

export const getIcon=(type)=>{
	switch(type){
		case 0:
			return IC_CON;
		case 1:
			return IC_CHARGE;
		case 2:
			return IC_PURCH;
		case 3:
			return IC_SERVICE;
    case 4:
      return IC_RECHARGE;
	}
}
export const NetpotItem = (props)=>{
  return (
    <TouchableHighlight onPress={()=>{Api.push('/netpot/view/'+JSON.stringify({type:props.type}))}} underlayColor="#efefef">
      <View style={NetpotStyles.netpot}>
        <Image source={props.icon} style={NetpotStyles.netpotIcon} />
        <View style={NetpotStyles.netpotContainer}>
          <Text style={NetpotStyles.netpotMainTitle}>{props.title}</Text>
          <Text style={NetpotStyles.netpotSubTitle}>{props.subtitle}</Text>
        </View>
        <Image style={NetpotStyles.arrow} source={require('../img/ic_arrow.png')} />
      </View>
    </TouchableHighlight>
  );
}

export const NetpotStyles=StyleSheet.create({

  line:{height:1/PixelRatio.get(),backgroundColor:'#efefef',marginLeft:15},
	 netpotIcon:{width:24,height:30},
	  netpot:{ padding:20, flexDirection:'row',alignItems:'center',justifyContent:'center' },
	  netpotContainer:{flex:1,marginLeft:10},
	  netpotMainTitle:{fontSize:16},
	  netpotSubTitle:{fontSize:11,color:'#555555',marginTop:5},
	  distance:{fontSize:11,marginTop:3},
	  arrow:{width:7,height:12,marginRight:5,marginLeft:5},
});