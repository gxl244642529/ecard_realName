
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
  ScrollView,
  
} from '../../lib/Common';

import SearchView from './SearchView'

import {
  TitleBar,
} from '../Global'


export default class NetpotView extends Component{
	constructor(props){
		super(props);
    let data = JSON.parse(props.params.json);
    //type
    this.state={data};
	}


	render(){
			return (
				<View style={CommonStyle.container}>
					<TitleBar title="网点查询" renderRight={this._renderRight} />
          <SearchView {...this.state.data} />
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{
    flex:1,

  },

 
});
