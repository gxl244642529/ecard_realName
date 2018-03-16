
//标题组件:首页，商户页、商品页的标题，内容为图标+标题文字

import React, { Component, } from 'react';
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  Image
} from '../../lib/Common';

export default class TitleName extends Component{
  constructor(props){
    super(props);
  }
  render(){
    let {icon,iconStyle}=this.props;
    let img= icon && <Image style={[styles.t_ico,iconStyle]} source={icon}/>
    return <View style={styles.titleName}>
      {img}
      <Text style={styles.t_title}>{this.props.title}</Text>
    </View>
    }
  }

const styles = StyleSheet.create({
  titleName:{
      flexDirection:'row',
      alignItems:'flex-end',
      marginTop:10,
      marginBottom:5,
      paddingBottom:5,
      borderBottomWidth:1,
      borderBottomColor:'#ddd',
  },
  t_ico:{
    width:15,
    height:18,
    resizeMode:'contain',
    marginLeft:4,
    marginRight:4,
    marginBottom:2,
  },
  t_title:{
    fontSize:16,
    //color:'#EA5414',
  },
})
