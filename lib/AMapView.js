import React, { Component } from 'react';
import {View} from 'react-native'
var { requireNativeComponent } = require('react-native');
// requireNativeComponent 自动把这个组件提供给 "RCTMapManager"
var RCTAMap = requireNativeComponent('RCTAMap', null);



export default class AMapView extends Component {
  static propTypes = {
     zoom: React.PropTypes.number,      //缩放
    // mapKey:React.PropTypes.string.isRequired,  //key
    // onLoaded : React.PropTypes.func, //加载完毕
     showsUserLocation:React.PropTypes.bool,
     /**
     * 中心位置{lat:,lng:}
     */
     center: React.PropTypes.object,
     userTrackingMode:React.PropTypes.string,

     /**
      * 回调，定位,返回的时间为{lat:,lng:} , status : 'success':'error'
      */
     onGeoLocation:React.PropTypes.func,

     /**
     * 回调，返回移动之后的地图中心位置 {lat:,lng:}
     */
     onMoveEnd:React.PropTypes.func,
  };


  onMoveEnd=(e)=>{
    let event = e.nativeEvent;
    this.props.onMoveEnd({
      lat:event.lat,
      lng:event.lng
    });
  }

  onGeoLocation=(e)=>{
    let event = e.nativeEvent;
    this.props.onGeoLocation({
        lat:event.lat,
        lng:event.lng
    });
  }

  render() {
    let {onMoveEnd,onGeoLocation,...props} = {...this.props};
    if(onMoveEnd){
      props.onMoveEnd = this.onMoveEnd;
    }
    if(onGeoLocation){
      props.onGeoLocation = this.onGeoLocation;
    }

    return <RCTAMap {...props} />;
  }
}