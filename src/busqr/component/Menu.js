import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  Image,
  TouchableOpacity,
  Dimensions,
  A,
  Api
} from '../../../lib/Common';
import WebUtil from '../../../lib/WebUtil'
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT = Dimensions.get('window').height;
import {IC_Account,IC_BusRecord,IC_State,IC_Setting,IC_TopTriangle} from '../icons/Icons'

class TinyList extends Component{
    constructor(props){
        super(props);
        this.state={}
    }
    _gotoUrl=(url)=>{
      if (this.props._url) {
          WebUtil.open(Api.imageUrl + '/index.php/sys_agree_detail/content/qrhelp',"公交扫码使用说明");
      }else {
        Api.push(url);
      }

    }
    // _url=()=>{
    //   console.log("链接到指定位置");
    //   WebUtil.open(Api.imageUrl + '/index.php/sys_agree_detail/content/qrrequest',"");
    // }
    render(){

        return <TouchableOpacity
        onPress={()=>this._gotoUrl(this.props.url)}
        style={[styles.menuListItem,!this.props.noBottom && styles.menuListItem_hasBottom ]}>
            {this.props.renderIcon}
            <View style={styles.pr10}/>
            <Text style={styles.gray}>{this.props.text}</Text>
        </TouchableOpacity>
    }
}

export default class Menu extends Component{
    constructor(props){
        super(props);
    }

    render(){
        return <View style={styles.container}>
            <View  style={styles.black_bg}>
            <TouchableOpacity onPress={this.props.onPress} style={styles.fixed}/>
            </View>
            <View style={styles.triangle}><IC_TopTriangle/></View>
            <View style={styles.menulist}>
                <TinyList text="余额管理" url="/busqr/account" renderIcon={<IC_Account/>}/>
                <TinyList text="乘车记录" url="/busqr/bus_record" renderIcon={<IC_BusRecord/>}/>
                <TinyList text="密码设置" url="/busqr/setting" renderIcon={<IC_Setting/>}/>
                <TinyList text="使用指南"_url={true}  renderIcon={<IC_State/>}/>


            </View>
        </View>
    }
}

const styles=StyleSheet.create({
    container:{
        position:'absolute',
        top:0,
        right:0,
        //zIndex:200
        //alignItems:'flex-end'
    },
    black_bg:{
        position:'absolute',
        width:SCREEN_WIDTH,
        height:SCREEN_HEIGHT,
        top:0,
        right:0,
        backgroundColor:'#000',
        opacity:0.5,
        //zIndex:100
    },
    menuListItem:{
        padding:10,
        paddingLeft:10,
        paddingRight:20,

        flexDirection:'row',
        justifyContent:'space-between',
        alignItems:'center',
    },
    menuListItem_hasBottom:{
        borderBottomWidth:1,
        borderBottomColor:'#d7d7d7',
    },
    menulist:{
        /*position:'absolute',
        top:45,
        right:0,
        width:SCREEN_WIDTH/6,
        height:120,*/
        marginTop:45,
        padding:5,
        //zIndex:200,
        backgroundColor:'#fff'
    },
    triangle:{
        position:'absolute',
        top:34,
        right:10,
        backgroundColor:'transparent'
        //zIndex:199,
    },
    //功能样式
    p5:{
        padding:5,
    },
    fixed:{
        flex:1
    },
    pr10:{
        marginRight:5
    },
    gray:{
        color:'#595757'
    }
})
