import React,{Component,PropTypes} from 'react'
import {TouchableOpacity,TextInput,View,Text,Image,StyleSheet,Select,ImagePicker,
  Dimensions,PixelRatio}
from  '../../lib/Common'
import {findGetIndex} from '../../lib/ArrayUtil'
import SwitchView from './SwitchView'
// import {lineView} from './realNameUtils'


export class FormSwitch extends Component{

   static propTypes = {
    onChange:PropTypes.func,
    value:PropTypes.bool, //默认值
    label:PropTypes.string.isRequired,    //label
  }

  render(){
    return (
      <View>
       <View style={[styles.formItem,styles.switch,this.props.style]}>
          <Text style={styles.labels}>{this.props.label}</Text>
          <SwitchView onChange={this.props.onValueChange} switchTrue={this.props.value}/>
        </View>
        {this.props.hasBottom&&<View style={styles.viewStyle}/>}
      </View>
    );
  }
}
const styles = StyleSheet.create({
  formItem:{
    backgroundColor:'#ffffff',
    height:60,
    paddingRight:10,
    paddingLeft:15,
    alignItems:'center',
    flexDirection:'row',
    // borderTopWidth:1,
    // borderTopColor:'#d7d7d7',
  },
  labels:{
    color:'#595757',
    fontSize:16,
    lineHeight:16,
    // marginRight:10,
  },
  switch:{justifyContent:'space-between'},

  //分割线
  viewStyle:{
		height:1/PixelRatio.get(),
		backgroundColor:'#d7d7d7',
		marginLeft:5,
	},
})
