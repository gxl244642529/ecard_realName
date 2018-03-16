import React, { Component } from 'react';

import {
  StyleSheet
} from '../../lib/Common';

import {Dimensions} from '../../lib/Common'
const SCREEN_WIDTH = Dimensions.get('window').width;

const StandardStyle=StyleSheet.create({
    container:{
        flex:1,
        backgroundColor:'#fff'
    },
    fixed:{
        flex:1
    },
    h1:{
        fontSize:34,
        lineHeight:50
    },
    h2:{
        fontSize:20,
        lineHeight:30
    },
    h3:{
        fontSize:16,
        lineHeight:24
    },
    h4:{
        fontSize:14,
        lineHeight:22,
    },
    h5:{
        fontSize:12,
        lineHeight:18
    },
    fontWhite:{
        color:'#fff'
    },

    //灰色
    fontGray:{
        color:"#727171",
    },

    //黑色
    fontBlack:{
        color:"#595656",
    },

    //蓝色
    fontBlue:{
        color:"#1263a6",
    },


    row:{
        flexDirection:'row'
    },
    list:{
        marginTop:5,
        marginBottom:5,
        marginLeft:20,
        marginRight:20
    },
    listLayout:{
        flexDirection:'row',
        justifyContent:'space-between'
    },
    center:{
        justifyContent:'center',
        alignItems:'center'
    },
    headerImg:{
        width:SCREEN_WIDTH,
        height:120,
    }
})

module.exports = StandardStyle;
