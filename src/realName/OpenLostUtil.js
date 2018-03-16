
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
DatePicker,
Account,
ActivityIndicator,
CommonStyle,
PixelRatio,
ScrollView,
Form,
Notifier,
Platform,
} from '../../lib/Common';


export class Jump extends Component{
  constructor(props) {
    super(props);
    this.state={};
  }
  render(){
    return(
      <TouchableOpacity style={{padding:12}} onPress={this.props.skip}>
          <Text>跳过</Text>
      </TouchableOpacity>
    );
  }
}
