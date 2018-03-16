
import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  RefreshControl,
  ListView,
  TouchableOpacity,
  Api,
  ActivityIndicator,
  Log,
  Platform,
  Image,
  A
} from '../../lib/Common';

export const renderEmpty=(component)=>{
	return (
		<TouchableOpacity style={styles.centering}
			onPress={()=>{component.reloadWithStatus();}}>
			<Image 
				source={require('./images/ic_empty.png')} 
				resizeMode="contain" 
				style={{width:100,height:100}} />
			<Text style={{fontSize:13,marginTop:5}}>暂无消息</Text>
		</TouchableOpacity>
	);
}



const styles = StyleSheet.create({
	centering: {
		flex:1,
		alignItems: 'center',
		justifyContent: 'center'
	},
	
});