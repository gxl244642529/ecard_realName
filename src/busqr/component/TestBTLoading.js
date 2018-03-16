import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  CommonStyle,
  Dimensions,
  A
} from '../../../lib/Common';


import {
    IC_BlueTooth,IC_BlueTooth2,IC_BlueTooth3,IC_BlueTooth4,
    Lod1,Lod2,Lod3,Lod0
} from '../icons/BTIcons'
import {
    IC_Omit,
    IC_Xmark,
    IC_Apply,
    IC_Draw,
    IC_DrawActive,
    IC_WithdrawGray
} from '../icons/Icons'

const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT = Dimensions.get('window').height;

export const LodC = (ComposedComponent) => class extends Component {
    
    shouldComponentUpdate(nextProps, nextState) {
    return false;
  }
    render() {
        return <ComposedComponent />;
    }
};


var Lod0C = LodC(Lod0);
var Lod1C = LodC(Lod1);
var Lod2C = LodC(Lod2);
var Lod3C = LodC(Lod3);


export default class TestBTLoading extends Component{
    constructor(props){
        super(props);
        this.state={index:2,arr:[<Lod0 />,<Lod1 />,<Lod2 />,<Lod3 />]};
        this._timer=null;
        
    }
    componentDidMount(){
        this.countTime();
    }
    componentWillUnmount() {
        this._timer && clearInterval(this._timer);
    }
    countTime(){
        //A.alert("here");
        this._timer=setInterval(()=>{
            this.setState({index:this.state.index+1});
        },500);
  }
    /*componentWillUnmount() {
        clearInterval(this.timer);//FIXME:无法实现卸载时清除计时器
   }*/
   
  
   /* _counteTimer=()=>{
        let index=1;
        this.timer = setInterval(()=> {
            if(index==4){
                index=0
            }else{
                index++
            }
            this.setState({index})
        },1000);
    }*/
    _renderIcon=()=>{
      /*  switch(this.state.index%4){
            case 0:
                return <Lod0 />;
                break
            case 1:
                return <Lod1 />;
                break
            case 2:
                return <Lod2 />;
                break
            case 3:
                return <Lod3 />;
                break
        }*/
        return this.state.arr[this.state.index%4];
    }


/*
 <View>
                    <View style={{position:'absolute',width:30,height:30,opacity:this.state.index%4==0 ? 1:0}}>
                    <Lod0 />
                   </View>
                    <View style={{position:'absolute',width:30,height:30,opacity:this.state.index%4==1 ? 1:0}}>
                     <Lod1 />
                   </View>
                    <View style={{position:'absolute',width:30,height:30,opacity:this.state.index%4==2 ? 1:0}}>
                    <Lod2 />
                   </View>
                    <View style={{position:'absolute',width:30,height:30,opacity:this.state.index%4==3 ? 1:0}}>
                   <Lod3 />
                   </View>
                   </View>
*/

    render(){
        return <View style={styles.container}>
            <View style={styles.black}></View>
            <View style={styles.box}>
                <View style={styles.row}>
                    <IC_BlueTooth />
                  
                    {this._renderIcon()}
                   
                   
                    
                </View>
                <View style={styles.mt10}/>
                <Text>蓝牙连接中……</Text>
                <Text>{parseInt(this.state.index/2)}s</Text>
            </View>
        </View>
    }
}

const styles=StyleSheet.create({
    container:{
        width:SCREEN_WIDTH,
        height:SCREEN_HEIGHT,
        alignItems:'center',
        justifyContent:'center'
    },
    black:{
        width:SCREEN_WIDTH,
        height:SCREEN_HEIGHT,
        backgroundColor:'#000',
        opacity:0.5,

        position:'absolute',
        top:0,
        left:0
    },
    box:{
        width:120,
        height:120,
        borderRadius:5,
        justifyContent:'center',
        alignItems:'center',

        backgroundColor:'#fff',
        marginBottom:40
    },

    //功能样式
    mt10:{
        marginTop:10
    },
    row:{
        flexDirection:'row'
    }
});