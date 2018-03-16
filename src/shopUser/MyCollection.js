import React,{Component} from 'react';

import {
		View,
		Text,
		StyleSheet,
		Image,
		TouchableOpacity,
		Dimensions,
		ScrollView,
		ImagePicker,
		Switch,
		TextInput,
		Api,
		} from '../../lib/Common'

import TitleBar from '../widget/TitleBar'
class collectionItem extends Component{
  render(){
    return(
      <TouchableOpacity style={{height:50,width:100}}>
      <Text>账单</Text>

      </TouchableOpacity>
    );
  }
}

export default class MyCollection extends Component{

  render(){
    return(
      <View>
      	<TitleBar title="我的收藏"/>

        <TouchableOpacity style={{height:50}}>
        <Text>账单</Text>

        </TouchableOpacity><collectionItem/>
      </View>
    );
  }

}
