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
  Form
} from '../../lib/Common';
import StandardStyle from '../lib/StandardStyle'

export default class FadeFormInput extends Component{
    render(){
        return<View>
          {this.props.hasHead&&<View style={styles.viewStyle}/>}
         <TouchableOpacity style={styles.formItem}  onPress={this.props.onPress}>
          <View style={StandardStyle.row}>

            <View style={styles.labelView}>
                <Text style={styles.label}>{this.props.label}</Text>
            </View>
            <View style={styles.labelView}>

                {!this.props.value&&  <Text style={styles.placeholder}>{this.props.placeHoder}</Text>}
            </View>

            <View style={styles.textView}>
                  <Text style={styles.font20}>{this.props.value && '· · · · · ·'}</Text>
            </View>
        </View>

      </TouchableOpacity>
        {this.props.hasBottom&&<View style={styles.viewStyle}/>}
      </View>
    }
}

const styles=StyleSheet.create({
    labelView:{
        justifyContent:'center',
        marginRight:10,
    },
    textView:{
        //backgroundColor:'red',
        //flex:1,

    },
    label:{
        color:'#666666',
        fontSize:16,
        lineHeight:16,
    },
    placeholder:{
      color:'#c7c7c9',
      fontSize:16,
      lineHeight:16,
    },
    text:{
        flex:1,
        fontSize:16,
    },
    formItem:{
        // padding:15,
        // flexDirection:'row',
        // borderTopWidth:1,
        // borderTopColor:'#d7d7d7',
        //flex:1
        backgroundColor:'#ffffff',
        height:60,
        paddingRight:10,
        paddingLeft:15,
        alignItems:'center',
        flexDirection:'row',
    },
    borderBottom:{
        borderBottomWidth:1,
        borderBottomColor:'#d7d7d7'
    },

    font15:{
        fontSize:15
    },
    font20:{
        fontSize:20,
        fontWeight:'bold',
    },
    fontGray:{
        color:'#575757'
    },
    //分割线
    viewStyle:{
      height:1/PixelRatio.get(),
      backgroundColor:'#d7d7d7',
      marginLeft:5,
    },
})
