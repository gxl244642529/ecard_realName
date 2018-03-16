
import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  TextInput,
  View,
  Image,
  TouchableOpacity,
  Dimensions,
  TouchableHighlight,
  Api,
  A,
  Account,
  ActivityIndicator,
  CommonStyle,
  PixelRatio,
  LocalData,
  Log,
  ScrollView
} from '../../lib/Common';

import {
  TitleBar,
} from '../Global'

import StateListView from '../widget/StateListView'
import StandardStyle from '../lib/StandardStyle'
import {IC_Bus} from './icons/Icons'
import {formatFee,format15Time} from '../lib/StringUtil'

export default class QrBusRecord extends Component{
	constructor(props){
		super(props);
		this.state={};
	}
	_renderRow=(rowData)=>{
		let h4=[StandardStyle.h4,styles.gray];
      console.log(rowData.busLine.replace(/\b(0+)/gi,""));
      let busLine = rowData.busLine.replace(/\b(0+)/gi,"");
    	return (
			<View style={styles.list}>
							<View style={styles.leftList}>
								<IC_Bus/>
								<View>
									<Text style={h4}>公交扫码</Text>
									<Text style={h4}>路线 {busLine}</Text>
								</View>
							</View>
							<View style={styles.rightList}>
								<Text style={[h4,styles.lightGray]}>{format15Time(rowData.time)}</Text>
								<Text style={[h4,styles.red]}>- {formatFee(rowData.fee)}元</Text>
							</View>
						</View>
		);
	}

	render(){
			return (
				<View style={CommonStyle.container}>
					<TitleBar title="乘车记录" />
					<View style={styles.listList}>
					</View>
					<StateListView renderRow={this._renderRow} api="qr_trans/bus" style={styles.container} />
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1},
	listList:{

	},
	list:{
		paddingLeft:20,
		paddingRight:20,

		flexDirection:'row',
		justifyContent:'space-between',

		borderBottomWidth:1,
		borderBottomColor:'#d7d7d7',
		paddingBottom:10,
		paddingTop:10,
	},
	leftList:{
		flexDirection:'row',
		alignItems:'center'
	},
	rightList:{
		alignItems:'flex-end'
	},

	//功能样式

	gray:{
		color:'#595757'
	},
	lightGray:{
		color:'#b3b3b4'
	},
	red:{
		color:'#e8464c'
	}
});
