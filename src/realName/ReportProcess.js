import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  CommonStyle
} from '../../lib/Common'

import {
    IC_Omit,
    IC_Xmark,
    IC_Bank,
    IC_BankActive,
    IC_Draw,
    IC_DrawActive,
} from './RPIcons'

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
            <View style={styles.itemTitle}>
                <View style={[styles.round,roundStyle]}>{this.props.renderIcon}</View>
                <Text style={[styles.itemTitleText,this.props.active && styles.itemTitleTextActive]}>{this.props.title}</Text>
            </View>
            <View style={styles.itemContent}>
             <Text style={styles.itemContentText}>{this.props.msg}</Text>
             <Text style={styles.itemContentText}>{this.props.msgto}</Text>
            </View>
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
            }
            if(index==3){
                return <IC_DrawActive/>
            }else{
                return <IC_Omit/>
            }

        }else if(this._isActive(index)){
            return <IC_DrawActive/>
        }else{
            if(index==2){
                return <IC_Bank/>
            }else{
                return <IC_Draw/>
            }
        }
    }
    render(){
        return <View style={styles.container}>
            <Items title={(this._isActive(1) && !this._isCurrentStep(1))?"挂失处理完毕":"挂失处理中"}
            active={this._isActive(1)}
            msg={ this.props.lost}
            renderIcon={
                this._renderIcon(1)

            }
            />

            <View style={[styles.line,this._isActive(2) && styles.lineActive]} />

            <Items title={(this._isActive(2) && !this._isCurrentStep(2))?"打款处理完毕":"打款中"}
            active={this._isActive(2)}
            msg={ this.props.name}
            msgto={ this.props.card}
            renderIcon={
                this._renderIcon(2)
            }
            />

            <View style={[styles.line,this._isActive(3) && styles.lineActive]} />

            <Items title={(!this._isActive(3) && !this._isCurrentStep(3))?"打款结果":(this.props.status==1 && this._isCurrentStep(3))?"打款失败"+this.props.failReason :"打款成功"}
            active={this._isActive(3)}
            msg={ this.props.msg}
            renderIcon={
                this._renderIcon(3)
            }
            />
        </View>
    }
}
const styles=StyleSheet.create({
    container:{
        // paddingLeft:20,
        // paddingRight:20,
        // margin
        padding:20
    },
    row:{
        flexDirection:'row'
    },
    item:{

        //alignItems:'center',
        //justifyContent:'center'
    },
    itemTitle:{
        flexDirection:'row',
        alignItems:'center',
    },
    itemTitleText:{
        fontSize:16,
        color:'#9d9d9e'
    },
    itemTitleTextActive:{
        color:'#e8464c'
    },
    itemContentText:{
        color:'#595757',
        paddingTop:2,

    },

    round:{
        width:40,
        height:40,
        borderRadius:20,
        //backgroundColor:'#d7d7d7',

        justifyContent:'center',
        alignItems:'center',

        marginRight:5,//距文字右侧间距

    },
    roundDeActive:{
        borderColor:'#d7d7d7',
        borderWidth:2
    },
    roundActive:{//激活状态
        backgroundColor:'#e8464c',
    },
    line:{
        borderLeftWidth:4,
        borderLeftColor:'#d7d7d7',
        width:4,
        height:60,
        marginLeft:18,
    },
    lineActive:{
        borderLeftColor:'#e8464c',
    },
    //绝对布局
    itemContent:{
        position:'absolute',
        top:40,
        left:40,


        paddingRight:40//因绝对定位，防止右侧溢出
    }
})
