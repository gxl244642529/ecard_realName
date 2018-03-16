import React,{Component} from 'react'

import{
  View,
  Text,
  Image,
  StyleSheet,
  TouchableOpacity,
  Dimensions
}from '../../lib/Common'
const SCREEN_WIDTH = Dimensions.get('window').width;
export default class VipCard extends Component{
  constructor(props){
    super(props);
  }
  render(){
    return(
    <TouchableOpacity onPress={this.props._onPress}>
    <Image source={require('./images/vvip.png')} style={[styles.img,this.props.style]}>
        <Text style={{color:'#fff',backgroundColor:'transparent',marginTop:3,width:(SCREEN_WIDTH-80)*0.5}}>{this.props.title}</Text>
        <View style={{right:10,top:110,position:'absolute'}}>
          <Text style={{fontSize:12,color:'#fff',backgroundColor:'transparent',}}>有效期至</Text>
          <Text style={{fontSize:12,color:'#fff',backgroundColor:'transparent'}}>{this.props.endtime}</Text>

        </View>
    </Image>
    </TouchableOpacity>);
  }
}

const styles=StyleSheet.create({
  img:{
    width:230,
    height:145,
    paddingTop:55,
    paddingLeft:15
  },
})
