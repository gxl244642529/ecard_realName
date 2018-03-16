
import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Image,
  TouchableOpacity,
  Dimensions,
  PixelRatio,
  A,
  Api,
  Animated,
  CommonStyle
} from '../../lib/Common';

import TitleBar from '../widget/TitleBar'
import TabPane from '../widget/TabPane'

import ShopCollection from './ShopCollection'
import GoodsCollection from './GoodsCollection'


class Comp1 extends Component{
  render(){
    return <View ><Text>Comp1</Text></View>
  }
}
class Comp2 extends Component{

  componentWillMount() {

  }

  componentDidMount() {

  }

  componentWillUnmount() {
    console.log('comp2 will unload');
  }

  render(){
    return <View ><Text>Comp2</Text></View>
  }
}

class Comp3 extends Component{
  render(){
    return <View ><Text>Comp3</Text></View>
  }
}


const COMPS = [ShopCollection,GoodsCollection];


export default class CollectionMain extends Component{

  constructor(props) {
    super(props);
  }


	render(){
		return (
			<View style={[CommonStyle.container,{backgroundColor:'#f0f0f0'}]}>
        <TitleBar title="我的收藏" />
        <TabPane groupStyle={styles.group} buttons={BUTTONS} comps={COMPS} />
			</View>
		);
	}
}

const styles = StyleSheet.create({
	container:{
		flex:1
  },
  group:{
    height:30
  },
  comp:{
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
  },
  hide:{
    overflow: 'hidden',
    opacity: 0,
  },
  button:{
    backgroundColor:'#ffffff',
    height:30
  },
  iconStyle:{
    width:20,
    height:20
  },
  textStyle:{
    fontSize:12,
    color:'#333'
  },
  textStyleSelected:{
    fontSize:12,
    color:'#f00'
  }


});

// const ICON1 = require('./img/ic_insurance_claims_step1.png');
// const ICON2 = require('./img/ic_insurance_claims_step2.png');
// const ICON3 = require('./img/ic_insurance_claims_step3.png');
//
// const ICON1_S = require('./img/ic_insurance_claims_step1_1.png');
// const ICON2_S = require('./img/ic_insurance_claims_step2_2.png');
// const ICON3_S = require('./img/ic_insurance_claims_step3_3.png');




const BUTTONS = [
  {text:'商家收藏',style:styles.button,textStyle:styles.textStyle,textStyleSelected:styles.textStyleSelected},
  {text:'商品收藏',style:styles.button,textStyle:styles.textStyle,textStyleSelected:styles.textStyleSelected},


];
