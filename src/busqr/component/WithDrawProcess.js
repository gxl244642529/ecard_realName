import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  CommonStyle
} from '../../../lib/Common';

import {
    IC_Omit,
    IC_Xmark,
    IC_Apply,
    IC_Draw,
    IC_DrawActive,
    IC_WithdrawGray
} from '../icons/Icons'

class Items extends Component{
    constructor(props){
        super(props);
    }
    render(){
        let roundStyle,IconStyle,titleStyle;
        if(this.props.active){
            roundStyle=styles.roundActive;
        }else{
            roundStyle=styles.roundDeActive;
        }
        return <View style={styles.item}>
                <View style={[styles.round,roundStyle]}>{this.props.renderIcon}</View>
                <Text style={[styles.itemTitleText,this.props.active && styles.itemTitleTextActive]}>{this.props.title}</Text>
        </View>
    }
}

export default class ReportProcess extends Component{
    constructor(props){
        super(props);
    }
    //是否激活状态
    _isActive=(index)=>{
        //A.alert(step);
        return index<=this.props.step;
    }

    //是否当前步骤
    _isCurrentStep=(index)=>{
        return index==this.props.step;
    }

    //渲染合适的图标
    _renderIcon=(index)=>{
        if(this._isCurrentStep(index)){
            if(this.props.status==1){
                return <IC_Xmark />
            }else{
                return <IC_Omit/>
            }

        }else if(this._isActive(index)){
            return <IC_DrawActive/>
        }else{
            if(index==2){
                return <IC_Apply/>
            }else if(index==3){
                return <IC_WithdrawGray />
             }else{
                return <IC_Draw/>
            }
        }
    }
    render(){
        return <View style={styles.container}>
            <Items title="验证"
            active={this._isActive(1)}
            msg={this._isCurrentStep(1) && this.props.msg}
            renderIcon={
                this._renderIcon(1)

            }
            />

            <View style={[styles.line,this._isActive(2) && styles.lineActive]} />

            <Items title="申请"
            active={this._isActive(2)}
            msg={this._isCurrentStep(2) && this.props.msg}
            renderIcon={
                this._renderIcon(2)
            }
            />

            <View style={[styles.line,this._isActive(3) && styles.lineActive]} />

            <Items title="出金"
            active={this._isActive(3)}
            msg={this._isCurrentStep(3) && this.props.msg}
            renderIcon={
                this._renderIcon(3)
            }
            />

            <View style={[styles.line,this._isActive(4) && styles.lineActive]} />

            <Items title={(this.props.status==1 && this._isCurrentStep(4))?"失败" :"完成"}
            active={this._isActive(4)}
            msg={this._isCurrentStep(4) && this.props.msg}
            renderIcon={
                this._renderIcon(4)
            }
            />
        </View>
    }
}
const styles=StyleSheet.create({
    container:{
        paddingLeft:20,
        paddingRight:20,
        flexDirection:'row'
    },
    row:{
        flexDirection:'row'
    },
    item:{

        alignItems:'center',
        justifyContent:'center'
    },
    itemTitle:{
        flexDirection:'row',
        alignItems:'center',
    },
    itemTitleText:{
        /*position:'absolute',
        top:40,
        right:0,*/

        fontSize:14,
        color:'#9d9d9e'
    },
    itemTitleTextActive:{
        color:'#e8464c'
    },
    itemContentText:{
        color:'#595757',
    },

    round:{
        width:40,
        height:40,
        borderRadius:20,
        //backgroundColor:'#d7d7d7',

        justifyContent:'center',
        alignItems:'center',

        //marginRight:5,//距文字右侧间距

    },
    roundDeActive:{
        borderColor:'#d7d7d7',
        borderWidth:2,

        marginBottom:5
    },
    roundActive:{//激活状态
        backgroundColor:'#e8464c',

        marginBottom:5
    },
    line:{
        borderTopWidth:2,
        borderTopColor:'#d7d7d7',
        flex:1,
        //width:40,
        //height:1,
        marginTop:19,
        marginLeft:0
    },
    lineActive:{
        borderTopColor:'#e8464c',
    },
    //绝对布局
    itemContent:{
        position:'absolute',
        top:40,
        left:40,


        paddingRight:40//因绝对定位，防止右侧溢出
    }
})
