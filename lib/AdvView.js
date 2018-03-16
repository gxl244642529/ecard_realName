import React, { Component } from 'react';
var { requireNativeComponent } = require('react-native');
// requireNativeComponent 自动把这个组件提供给 "RCTMapManager"
var RCTAdvView = requireNativeComponent('RCTAdvView', null);



export default class AdvView extends Component {
  static propTypes = {
    /**
     * 指定模块id
     *
     * @type {[type]}
     */
     id: React.PropTypes.string.isRequired
  };


  render() {

    return <RCTAdvView style={this.props.style} id={this.props.id} />;
  }
}
