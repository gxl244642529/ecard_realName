import React, { Component } from 'react';
import {
  Animated,
  TouchableWithoutFeedback
} from 'react-native'
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
  Account,
  Push,
  CommonStyle,
  PixelRatio
} from '../../lib/Common';

import StandardStyle from '../lib/StandardStyle'
import {IC_XMark} from './Icons'



const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT = Dimensions.get('window').height;




class CodeCell extends Component{
  render(){
    return <View style={[styles.codecell,StandardStyle.center]}>
      <Text style={styles.codeCellText}>{this.props.hasDot && '·'}</Text>
    </View>
  }
}

class NumCell extends Component{
  render(){
    return <TouchableOpacity
    disabled={this.props.disabled}
    onPress={this.props.onPress}
    style={[styles.numcell,StandardStyle.center,StandardStyle.row,this.props.center && styles.bothBorder]}>
      <Text style={[styles.numcellText,this.props.disabled && styles.gray]}>{this.props.index}</Text>
    </TouchableOpacity>
  }
}

export default class PayBox extends Component{
    constructor(props){
        super(props);
        this.state={
          str:'',
          fadeAnim: new Animated.Value(0), //设置初始值
        }
    }
    _onClean=()=>{
      this.setState({str:''});
    }
    componentDidMount(){
      Animated.timing(
        this.state.fadeAnim,//初始值
            {toValue: SCREEN_HEIGHT/1.6,
              duration: 150,
          }//结束值
      ).start();//开始
    }
    _hasDot=(index)=>{
        let result=this.state.str.substr(index-1,index);
        console.log(this.state.str);
        if(result)
            return true
        else
            return false
    }
    _addDot=(index)=>{
        let str=this.state.str;
        str=str+index;
        this.setState({str});
        if(str.length==6){
            console.log("yes");
            //this.props.setState({hasPayBox:false});
            this.props.onSuccess(str);
        }
    }
    _subtract=()=>{
        let str=this.state.str;
        let length=str.length;
        str=str.substr(0,length-1);
        this.setState({str});
    }
    render(){
      let locked=this.props.locked;
        return <View style={styles.container}>
        <Animated.View style={[styles.paybox,{height:this.state.fadeAnim}]}>
        <View style={[StandardStyle.row,styles.paytitle]}>
          <TouchableOpacity style={StandardStyle.center} onPress={this.props.onCancel}>
          <View style={styles.p5}><IC_XMark/></View>
          </TouchableOpacity>
          <View style={[StandardStyle.center,StandardStyle.fixed,styles.titletext]}>
            <Text style={StandardStyle.h3}>请输入支付密码</Text>
          </View>

      </View>
        <View style={styles.lineView}/>
      <View style={[StandardStyle.row,styles.codelist]}>
        <CodeCell hasDot={this._hasDot(1)}/>
        <CodeCell hasDot={this._hasDot(2)}/>
        <CodeCell hasDot={this._hasDot(3)}/>
        <CodeCell hasDot={this._hasDot(4)}/>
        <CodeCell hasDot={this._hasDot(5)}/>
        <CodeCell hasDot={this._hasDot(6)}/>
      </View>
      <View>

      {this.state.locked && <View style={styles.pl10}><Text style={styles.red}>今日密码输入次数已达上限，请明日再试!</Text></View>}
      {this.props.onForget && !this.state.locked && <TouchableOpacity style={styles.forget} onPress={this.props.onForget}>
        <Text style={styles.forgetText}>忘记密码？</Text>
      </TouchableOpacity>}
    </View>

      <View>
        <View style={styles.numlist}>
          <NumCell index={1} onPress={()=>this._addDot(1)} disabled={locked}/>
          <NumCell index={2} onPress={()=>this._addDot(2)} disabled={locked} center={true}/>
          <NumCell index={3} onPress={()=>this._addDot(3)} disabled={locked}/>
        </View>
        <View style={styles.numlist}>
          <NumCell index={4} onPress={()=>this._addDot(4)} disabled={locked}/>
          <NumCell index={5} onPress={()=>this._addDot(5)} disabled={locked} center={true}/>
          <NumCell index={6} onPress={()=>this._addDot(6)} disabled={locked}/>
        </View>
        <View style={styles.numlist}>
          <NumCell index={7} onPress={()=>this._addDot(7)} disabled={locked}/>
          <NumCell index={8} onPress={()=>this._addDot(8)} disabled={locked} center={true}/>
          <NumCell index={9} onPress={()=>this._addDot(9)} disabled={locked}/>
        </View>
        <View style={styles.numlist}>
          <View style={[styles.numcell,styles.numcellBlack]}></View>
          <NumCell index={0} onPress={()=>this._addDot(1)} center={true} disabled={locked}/>
          <TouchableOpacity
            disabled={locked}
            style={[styles.numcell,styles.numcellBlack,StandardStyle.center]} onPress={this._subtract}>
            <Text style={[styles.numcellText,StandardStyle.fontWhite]}>×</Text>
          </TouchableOpacity>
        </View>
      </View>
   </Animated.View>
   <View style={styles.blackBg}></View>
   </View>
    }
}

const styles=StyleSheet.create({
  container:{
    position:'absolute',
    left:0,
    bottom:0,
  },
  blackBg:{
    position:'absolute',
    bottom:0,
    left:0,

    width:SCREEN_WIDTH,
    height:SCREEN_HEIGHT,

    backgroundColor:'#000',
    zIndex:200,
    opacity:0.5

  },
    //仿支付宝弹出框
  paybox:{
    backgroundColor:'#fff',
    width:SCREEN_WIDTH,
    position:'absolute',
    bottom:0,
    left:0,
    zIndex:300,
    justifyContent:'space-between',
    opacity:1
  },

  paytitle:{
    padding:10,
    // borderBottomWidth:1,
    // borderBottomColor:'#d7d7d7',

    //borderTopColor:'#d7d7d7',
    //borderTopWidth:1,
  },

  codelist:{
    margin:10,
    //marginTop:0,
    marginBottom:0,
    borderWidth:1,
    borderColor:'#999999',
    borderRadius:5
  },
  codecell:{
    flex:1,
    borderRightWidth:1,
    borderRightColor:'#d7d7d7',
    //padding:10,
    //paddingTop:15,
    //paddingBottom:15,
    height:SCREEN_HEIGHT/12
  },
  codeCellText:{
      fontSize:35,
      fontWeight:'bold'
  },
  forget:{
      flexDirection:'row',
      justifyContent:'flex-end',
      paddingRight:10
  },
  forgetText:{
      color:'#3c6ea6'
  },
  numlist:{
    //width:SCREEN_WIDTH,
    flexDirection:'row',
    flexWrap:'wrap',
    borderTopWidth:1,
    borderTopColor:'#d7d7d7'
  },
  numcell:{
    width:SCREEN_WIDTH/3,
    height:60,
    //borderWidth:1,
    //borderColor:'#d7d7d7'
  },
  numcellBlack:{
      backgroundColor:'#d7d7d7'
  },
  numcellText:{
    fontSize:22,
    //fontWeight:'bold'
  },
  bothBorder:{
    borderLeftWidth:1,
    borderLeftColor:'#d7d7d7',
    borderRightWidth:1,
    borderRightColor:'#d7d7d7'
  },

  p5:{
    padding:5,
    paddingTop:0,
    paddingBottom:0
  },
  pl10:{
    paddingLeft:10
  },
  //分割线
  lineView:{
		height:1/PixelRatio.get(),
		backgroundColor:'#d7d7d7',
	},
  red:{
    color:'#e8464c'
  },
  gray:{
    color:'#d7d7d7'
  }
})
