

import React, { Component, } from 'react';
import {
  View,
  Image
} from '../../lib/Common';

export default class QrImage extends Component {

  render(){
    return(
      <View>
        <Image  style={this.props.imageStyle} source={{uri:this.props.uri}}/>
      </View>
    );
  }

}
