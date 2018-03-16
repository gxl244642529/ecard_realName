

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
  Platform,
} from '../../lib/Common';

import {renderLoading,renderError} from './StateListView'


export default class StatusPage extends Component{
	constructor(props){
		super(props);
    this.state={status:undefined,error:null};
	}

  /**
   * [description]
   * @param  {[type]}  error          [description]
   * @param  {Boolean} isNetworkError [description]
   * @return {[type]}                 [description]
   */
  _error=(error,isNetworkError)=>{
    this.setState({error,isNetworkError});
    return true;
  }

  /**
   * [description]
   * @param  {[type]} error [description]
   * @return {[type]}       [description]
   */
  _message=(error)=>{
    this.setState({error,false});
    return true;
  }

	componentDidMount() {
	//开始支付,创建订单
		this.load();
	}

  reloadWithStatus=()=>{
    this.setState({status:undefined,error:null});
    this.load();
  }

  _success=(data)=>{
    this.props.onSuccess && this.props.onSuccess(data);
  }

  load=()=>{
    Api.detail(this,{
      api:this.props.api,
      crypt:this.props.crypt || 0,
      data:this.props.data,
      error:this._error,
      message:this._message,
      success:this._success
    });
  }

  _renderContent(){
    if(this.state.status!==undefined){
      return this.props.renderStatus(this.state);
    }else if(this.state.error){
      if(this.props.renderError){
        return this.props.renderError(this.state.error,this.state.isNetworkError,this);
      }
      return renderError(this.state.error,this.state.isNetworkError,this);
    }else{
      return renderLoading();
    }
  }

	render(){
		return (
			<View style={styles.container}>
      {this._renderContent()}
			</View>
		);
	}
}



const styles = StyleSheet.create({
  container:{flex:1},
  
});

