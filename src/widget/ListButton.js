import React, { Component, } from 'react';
import {ART} from 'react-native'
const {Surface, Shape, Group,Path} = ART;

import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  Image,
  Api,
  PixelRatio
} from '../../lib/Common';

const IC_ForwardArrow=()=>{
    let path="M767.707334 519.450692 308.065819 71.476915 256.054235 122.209366 663.627884 519.419993 256.085958 916.629597 308.060703 967.363072Z";
    return <Surface
        width={15}
        height={19}
        >
        <Group scale={0.015}>
            <Shape
                fill='#999999'
                d={path}
            />
        </Group>
      </Surface>
}

export default class ListButton extends Component{
    static propTypes={
        text:React.PropTypes.string.isRequired,//文字
        Icon:React.PropTypes.any.isRequired,//图标组件
        url:React.PropTypes.string,//跳转地址
    }
    constructor(props){
        super(props);
    }
    _GotoUrl=(url)=>{
        Api.push(url);
    }

    render(){
        let event=()=>{};//点击事件
        if(this.props.onPress){
            event=()=>this.props.onPress();
        }else{
            event=()=>this._GotoUrl(this.props.url);
        }
        return <View>
          <View style={styles.lineView}/>
        <TouchableOpacity style={[styles.container]} onPress={event}>
            <View style={styles.group}>
                {this.props.Icon}
                <Text style={styles.text}>{this.props.text}</Text>
            </View>
            <IC_ForwardArrow/>
        </TouchableOpacity>
        {this.props.hasBottom&&    <View style={styles.lineView}/>}
        </View>
    }
}
const styles=StyleSheet.create({
    container:{
        flexDirection:'row',
        justifyContent:'space-between',
        alignItems:'center',
        //marginLeft:20,
        paddingLeft:20,
        //marginRight:20,
        // borderTopWidth:1,
        // borderTopColor:'#d7d7d7',
        padding:10,
        paddingBottom:5,
        height:50
    },
    // borderBottom:{
    //     borderBottomWidth:1,
    //     borderBottomColor:'#d7d7d7'
    // },
    group:{
        flexDirection:'row',
        //padding:5
    },
    text:{
        fontSize:18,
        lineHeight:23,
        color:'#595757'
    },
    lineView:{
      height:1/PixelRatio.get(),
      backgroundColor:'#d7d7d7',
    }
})
