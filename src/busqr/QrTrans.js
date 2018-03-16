
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
import {IC_Bus,IC_Detail_Recharge} from './icons/Icons'
import {formatFee,format14Time,format15Time} from '../lib/StringUtil'

export default class QrTrans extends Component{
	constructor(props){
		super(props);
		this.state={};
	}
	/*<View>
				<Text>{row.amt}</Text><Text>{row.time}</Text><Text>{row.type}</Text>
			</View>*/
	_renderRow=(rowData)=>{
		if(rowData.type==1){
			return this._renderBus(rowData);
		}else{
			return this._renderRecharge(rowData);
		}
	}

	_renderBus=(rowData)=>{
		let h4=[StandardStyle.h4,styles.gray];
    console.log(rowData.busLine.replace(/\b(0+)/gi,""));
    let busLine = rowData.busLine.replace(/\b(0+)/gi,"");
		return <View><View style={styles.list}>
				<View style={styles.leftList}>
					<IC_Bus/>
					<View>
						<Text style={h4}>公交扫码</Text>
						<Text style={h4}>路线 {busLine}</Text>
					</View>
				</View>
				<View style={styles.rightList}>
					<Text style={[h4,styles.lightGray]}>{format14Time(rowData.time)}</Text>
					<Text style={[h4,styles.red]}>- {formatFee(rowData.fee)}元</Text>
				</View>
			</View>
      <View style={styles.lineview}/>
      </View>
	}
	_renderRecharge=(rowData)=>{
		let h4=[StandardStyle.h4,styles.gray];
		return <View><View style={styles.list}>
				<View style={styles.leftList}>
					<IC_Detail_Recharge/>
					<View style={StandardStyle.center}>
						<Text style={[h4,styles.lineHeight40]}>充值</Text>
					</View>
				</View>
				<View style={styles.rightList}>
					<Text style={[h4,styles.lightGray]}>{format14Time(rowData.time)}</Text>
					<Text style={[h4,styles.green]}>+{formatFee(rowData.fee)}元</Text>
				</View>
			</View>
      <View style={styles.lineview}/>
      </View>
	}

	render(){
			return (
				<View style={[CommonStyle.container,{backgroundColor:'#fff'}]}>
					<TitleBar title="明细" />

					<StateListView
            renderRow={this._renderRow}
            api="qr_trans/his"
            style={styles.container} />
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1},
	listList:{
		//paddingLeft:20,
		//paddingRight:20
	},
	list:{
		paddingLeft:20,
		paddingRight:20,

		flexDirection:'row',
		justifyContent:'space-between',

		// borderBottomWidth:1,
		// borderBottomColor:'#d7d7d7',
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
  icon:{
    width:25,
    height:25,
    resizeMode:'contain'
  },

	//功能样式
	green:{
		color:'#0e8c7f'
	},
	red:{
		color:'#db474a'
	},
	gray:{
		color:'#595757'
	},
	lightGray:{
		color:'#b3b3b4'
	},
	lineHeight40:{
		lineHeight:20
	},
  lineview:{
    height:1/PixelRatio.get(),
    backgroundColor:'#d7d7d7',
  }
});
