import React,{Component} from 'react'

import TitleBar from '../widget/TitleBar'
import CryptoJS from 'crypto-js'
import{
  View,
  Text,
  Image,
  StyleSheet,
  Api,
  CommonStyle,
  Account,
  Dimensions
}from '../../lib/Common'

//import VipCard from './VipCard'
import {VipCard} from './ShopUserUtils'
import QrImage from '../widget/QrImage'
import QrView from '../lib/QrView'
import {QR_MEMBER} from '../qr/QrEnums'


const url = 'http://192.168.1.241/index.php/api/qr/index/';
const uri = '';

const SCREEN_WIDTH = Dimensions.get('window').width;
const CARD_WIDTH=SCREEN_WIDTH*0.7;

export default class MyVipcard extends Component{
  constructor(props){
    super(props);
    this.state={};

  }
  componentDidMount(){
    console.log(Account.user);
    let data ={code:this.props.params.id}
    Api.api({
      api:"member/detail",
      data:data,
      success:(result)=>{
        let code = result.code;
        var str = CryptoJS.enc.Utf8.parse(JSON.stringify({code:code,type:QR_MEMBER}));
        var base64 = CryptoJS.enc.Base64.stringify(str);
        this.setState({...result,...{content:base64}});

      }
    });

  }

  // _deal=(code)=>{
  //   console.log("code="+code);
  //   let dataJson = JSON.stringify(code);
  //   let dataBase = new Buffer(dataJson,"utf8").toString("base64");
  //
  //
  // }



  render(){
    return <View style={[CommonStyle.container,{backgroundColor:'#f0f0f0'}]}>
      <TitleBar title="会员卡详情"/>
      <View style={{alignItems:'center',marginTop:20}}>
        <VipCard
              disabled={true}
              rowData={this.state}/>
        {/*<VipCard title={this.state.shpName} cardid={this.state.code} endtime={this.state.endTime} style={styles.card}/>*/}
        <View style={{width:200,height:200,marginTop:20}}>
          <QrView  content={this.state.content} style={{width:200,height:200}} />
        </View>
        <View style={styles.tip}><Text style={styles.tiptext}>温馨提示：该二维码由商户扫描识别</Text></View>
      </View>
    </View>
  }
}

const styles=StyleSheet.create({
  card:{
    width:235,
    height:155,
    paddingTop:55,
    paddingLeft:15,
  },
  tip:{
    marginTop:10,
  },
  tiptext:{

  }
})
