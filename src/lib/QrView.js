import React, { Component } from 'react';
var { requireNativeComponent } = require('react-native');
var RCTQrView = requireNativeComponent('RCTQrView', null);


export default class QrView extends Component {
  static propTypes = {
    /**
     * 指定内容
     * 
     * @type {[type]}
     */
     content: React.PropTypes.string.isRequired
  };

  
  render() {
    return <RCTQrView style={this.props.style} content={this.props.content} />;
  }
}