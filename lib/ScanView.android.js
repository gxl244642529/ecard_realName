import React, { Component } from 'react';
var { requireNativeComponent,StyleSheet } = require('react-native');
var RCTScanView = requireNativeComponent('RCTScanView', null);


export default class ScanView extends Component {
 

  onBarCodeRead=(result)=>{
    let event = result.nativeEvent;
    this.props.onBarCodeRead && this.props.onBarCodeRead(event.code);
  }

  render() {
    return <RCTScanView 
      onChange={this.onBarCodeRead} 
      style={[styles.container,this.props.style]} />;
  }
}

const styles = StyleSheet.create({
  container:{flex:1}
});
