

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
  Log,
  Form,
  LocalData,

} from '../../lib/Common';

import {Modal} from 'react-native'

import SearchBar from '../widget/SearchBar'
import StateListView from '../widget/StateListView'
import SearchHis from './SearchHis'
import SearchView from './SearchView'


const NetpotKey = "NetpotKey1";

export default class NetpotSearch extends Component{

	constructor(props) {
		super(props);
		this.state={data:{keyword:""},showList:false,his:[]};
		//搜索记录中宣勋
		LocalData.getObject(NetpotKey).then((data)=>{
			this.setState(data);
		},()=>{

		});
	}

	shouldComponentUpdate(nextProps, nextState) {
		return nextProps.show !== this.props.show
			|| nextState.showList !== this.state.showList
			|| nextState.data !== this.state.data
			|| nextState.his !== this.state.his;
	}

	onSearch=(keyword)=>{
		//改变data,并且展示出列表
		//这个时候才出来
		let data = {keyword};
		this.setState({data:data,showList:true});
	}

	/**
	 * 改变text的时候
	 * 
	 * @param  {[type]} text [description]
	 * @return {[type]}      [description]
	 */
	onChange=(text)=>{
		//展示快捷列表
		
	}


	onSuccess=(data)=>{
		if(data.page){
			if(data.page.isFirst()){
				if(data.page.isEmpty()){
					return;
				}
				//记录一下
				if(this.state.his.indexOf(this.state.data.keyword)<0){
					let his = [ this.state.data.keyword ];
					his = his.concat(this.state.his);
					if(his.length > 10){
						his.length = 10;
					}
					this.setState({his});
					LocalData.putObject(NetpotKey,{his});
				}

			}

		}
	}

	onCancel=()=>{
		this.setState({showList:false});
		this.props.onCancel();
	}
	onRequestClose=()=>{
		this.setState({showList:false})
	}
	render(){
		
		return (
			<View style={{position:'absolute',left:0,right:0,top:0,bottom:0,backgroundColor:'#fff'}}>
				<SearchBar 
					onChange={this.onChange}
					style={styles.searchBar}
					value={this.state.data.keyword} 
					placeholder="请输入商家名" 
					onSearch={this.onSearch} 
					onCancel={this.onCancel} />
				<View style={styles.container}>
					{this.state.showList ? 
						<SearchView cancelLoad={false} onSuccess={this.onSuccess} type={-1} {...this.state.data} /> :
						<SearchHis items={this.state.his} onSearch={this.onSearch} />}
					
				</View>
				
				
			</View>
		);
	}
}

const styles = StyleSheet.create({
	container:{flex:1},
	item:{padding:15,borderBottomWidth:1/PixelRatio.get(),borderBottomColor:'#ccc'},
	searchBar:{backgroundColor:'#eb613b'},
	list:{
		flex:1
	}
	
});

/*
<TouchableOpacity style={styles.leftButtonSelected}>
		            		<Text style={styles.textSelected}>优惠卡</Text>
		            	</TouchableOpacity>
*/

