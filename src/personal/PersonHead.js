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
  // Account,
  ActivityIndicator,
  CommonStyle,
  PixelRatio,
  LocalData,
  Log,
  ScrollView,
  Notifier,
  DeviceEventEmitter,
  TouchableAll
} from '../../lib/Common';
import {onRequireLoginPress} from '../../lib/LoginUtil'
import {SCREEN_WIDTH,SCREEN_HEIGHT} from '../widget/Screen'

import {logout} from '../../lib/LoginUtil'
import Account from '../../lib/network/Account'
import {headChanged} from '../lib/Notes'

//device
//
const DEFAULT_IMAGE = require('./images/personWomen.png');

export default class PersonHead extends Component{

  constructor(props){
    super(props);
    let url = Account.user ? Account.user.head : null;
    this.state={url};
  }

  componentDidMount() {
    Notifier.addObserver("loginSuccess",this._refresh);
    Notifier.addObserver("logoutSuccess",this._refresh);
    Notifier.addObserver(headChanged,this._refresh);

  }

  _refresh=()=>{
    let url = Account.user ? Account.user.head : null;
    this.setState({url});
  }

  componentWillUnmount() {
    Notifier.removeObserver("loginSuccess",this._refresh);
    Notifier.removeObserver("logoutSuccess",this._refresh);
    Notifier.removeObserver(headChanged,this._refresh);
  }

  headChanged=(url)=>{
    this.setState({url});
  }

  render(){
    return (
      <TouchableAll onPress={this.props.onPress} style={this.props.style}>
       <Image source={DEFAULT_IMAGE} style={ styles.header }>
        {this.state.url ? <Image source={{uri:this.state.url}} style={styles.image} /> : null}
       </Image>
      </TouchableAll>
    );
  }

}



const HEADER_SIZE = SCREEN_WIDTH  / 320 * 87;
const IMAGE_SIZE = HEADER_SIZE * 0.8;
const styles = StyleSheet.create({
  image:{width:IMAGE_SIZE,height:IMAGE_SIZE,borderRadius:IMAGE_SIZE/2 },
  header:{width:HEADER_SIZE,height:HEADER_SIZE,justifyContent:'center',alignItems:'center'},
});
