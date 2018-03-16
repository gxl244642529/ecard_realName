
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
  Api
} from '../../lib/Common';

import TitleBar from '../widget/TitleBar'
import TabHost from '../widget/TabHost'
import Ecshop from './Ecshop'
import PCenter from './PCenter'



const COMPS = [Ecshop,PCenter];


export default class ShopUserMain extends Component{

  constructor(props) {
    super(props);
  }


  render(){
    return (
      <View style={styles.container}>

        <TabHost buttons={BUTTONS} comps={COMPS} />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container:{
    flex:1,
    backgroundColor:'#f0f0f0'
  },
  button:{

  },
  iconStyle:{
    width:24,
    height:20
  },
  textStyle:{
    fontSize:12,
  },
  textStyleSelected:{
    fontSize:12,
    color:'#43B0B1'
  }


});

const ICON1 = require('./images/shop1.png');
const ICON2 = require('./images/personal1.png');
// const ICON3 = require('./images/managebar.png');
// const ICON4 = require('./images/settingbar.png');

const ICON1_S = require('./images/shop1se.png');
const ICON2_S = require('./images/personal1se.png');
// const ICON3_S = require('./images/managebarse.png');
// const ICON4_S = require('./images/settingbarse.png');




const BUTTONS = [
  {text:'商城',iconPadding:5,textStyle:styles.textStyle,textStyleSelected:styles.textStyleSelected,icon:ICON1,iconSelected:ICON1_S,iconStyle:styles.iconStyle},
  {text:'我的',iconPadding:5,textStyle:styles.textStyle,textStyleSelected:styles.textStyleSelected,icon:ICON2,iconSelected:ICON2_S,iconStyle:styles.iconStyle},
  // {text:'管理',iconPadding:5,textStyle:styles.textStyle,textStyleSelected:styles.textStyleSelected,icon:ICON3,iconSelected:ICON3_S,iconStyle:styles.iconStyle},
  // {text:'设置',iconPadding:5,textStyle:styles.textStyle,textStyleSelected:styles.textStyleSelected,icon:ICON4,iconSelected:ICON4_S,iconStyle:styles.iconStyle}
];
