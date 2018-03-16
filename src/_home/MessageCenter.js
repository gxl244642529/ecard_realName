
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
  Account,
  ActivityIndicator,
  CommonStyle,
  PixelRatio,
  LocalData,
  Log,
  ScrollView,
} from '../../lib/Common';
import StateListView from '../widget/StateListView'
import Notifier from '../../lib/Notifier'
import {openMessage} from '../../lib/PushUtil'
import {SCREEN_WIDTH} from '../widget/Screen'
import {renderEmpty} from '../widget/ListUtil'
import {
  TitleBar,
} from '../Global'

//实名化
const REAL_VERIFY_OK = 101;//app实名审核通过
const REAL_VERIFY_ERROR = 102;//app实名审核失败

const LOST_FUND_ERROR = 103;//挂失打款失败
const LOST_FUND_OK = 104;	//挂失打款成功
const LOST_FUND_ERROR_LOCK = 105;//挂失打款失败，余额不足
const LOST_FUNDING = 106;//挂失打款成功

const REAL_SINGLE_PAY_SUCCESS = 107;//银行卡打款成功
const  REAL_SINGLE_PAY_ERROR  = 108;//银行卡打款失败
//实名化二期
const  DB_ORDER_SUCCESS  = 109;//补登验证绑卡成功
const DB_ORDER_CANEL = 110; //补登验证绑卡订单取消
const DB_ORDER_ERROR = 111; //卟噔绑卡失败

//公交扫码
const BUS_OK = 201;				//上车成功
const OUT_FUND_OK = 202;			//出金成功
const OUT_FUND_ERROR = 203;		//出金失败

//卟噔充值成功（卟噔模块）
const BD_RECHARGE_SUCCESS = 120;//卟噔成功

//电子发票

const TICKET_SUCCESS = 302;//开票成功
const TICKET_RED_SUCCEED = 303;//红冲成功
const TICKET_ERROR = 304;//开票失败

//app实名
const APP_Y = require('./images/app_y.png');
const APP_N = require('./images/app_n.png');
//公交
const BUS_Y = require('./images/bus_y.png');
const BUS_N = require('./images/bus_n.png');
//电子发票
const BILL_Y = require('./images/ebill_y.png');

const BILL_N = require('./images/ebill_n.png');
//卟噔
const BUDENG_Y = require('./images/budeng_y.png');
const BUDENG_N = require('./images/budeng_n.png');
//其他
const CARD_Y = require('./images/card_y.png');
const CARD_N = require('./images/card_n.png');

// const SCREEN_WIDTH = Dimensions.get('window').width;
// const SCREEN_HEIGHT= Dimensions.get('window').height;




export default class MessageCenter extends Component{
	constructor(props){
		super(props);
		this.state={};
	}

  onRead=(rowData)=>{
    let data = {id:rowData.id};
    Api.api({
      api:"mc/setReaded",
      data:data,
      success:(result)=>{
        console.log(result);
      }
    });
  }

  setRead=(rowData)=>{
    if(!rowData.readed){
       rowData.readed=true;
      this.refs.LIST.updateData(rowData);
    }
    setTimeout(()=>{
      this.onRead(rowData);
      openMessage(rowData);
    },0);

  }
  renderImage=(imageSouce)=>{
    return(
      <Image style={styles.msgImage}
      source={imageSouce}/>
    );
  }
  imageItem=(rowData)=>{
    let componentImage;
    if (rowData.type===REAL_VERIFY_OK||rowData.type===REAL_VERIFY_ERROR
      ||rowData.type===LOST_FUND_ERROR ||rowData.type===LOST_FUND_OK ||
        rowData.type===LOST_FUND_ERROR_LOCK||rowData.type===REAL_SINGLE_PAY_SUCCESS ||
      rowData.type===REAL_SINGLE_PAY_ERROR ||rowData.type===DB_ORDER_SUCCESS||rowData.type===DB_ORDER_CANEL||
      rowData.type===DB_ORDER_ERROR||rowData.type===LOST_FUNDING) {
      componentImage=!rowData.readed?this.renderImage(APP_Y):this.renderImage(APP_N)
    }else if (rowData.type===BUS_OK||rowData.type===OUT_FUND_OK||rowData.type===OUT_FUND_ERROR) {
      componentImage=!rowData.readed?this.renderImage(BUS_Y):this.renderImage(BUS_N)
    }else if (rowData.type===TICKET_SUCCESS||rowData.type===TICKET_RED_SUCCEED||rowData.type===TICKET_ERROR) {
      componentImage=!rowData.readed?this.renderImage(BILL_Y):this.renderImage(BILL_N)
    }else if (rowData.type===BD_RECHARGE_SUCCESS) {
      componentImage=!rowData.readed?this.renderImage(BUDENG_Y):this.renderImage(BUDENG_N)
    }else{
      componentImage=!rowData.readed?this.renderImage(CARD_Y):this.renderImage(CARD_N)
    }
    return(
      componentImage
    );
  }

  renderRow=(rowData)=>{
    return <View>
        <TouchableOpacity style={styles.msgView} onPress={()=>{this.setRead(rowData)}}>

          {this.imageItem(rowData)}
          <View style={styles.allText}>
            <View style={styles.titleAndTime}>
              <Text style={{ fontSize:16,}}>{rowData.title}</Text>
              <Text style={styles.timeText}>{rowData.time.substring(0,10)}</Text>
            </View>
            <Text style={styles.contentText}>{rowData.content}</Text>
          </View>
        </TouchableOpacity>
        <View style={styles.line}/>

      </View>
  }


	render(){
			return (
				<View style={CommonStyle.container}>
					<TitleBar title="消息中心" />
          <StateListView
            renderEmpty={renderEmpty}
           ref="LIST"
           api="mc/list"
           style={styles.container}
           renderRow={(rowData)=> this.renderRow(rowData)}
           />
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1},
  msgView:{
    flexDirection:'row',
    backgroundColor:'#fff',
  },
  msgImage:{
    width:60,
    height:60,
    resizeMode:'contain',
    margin:10
  },
  msg:{
    fontSize:16,
    color:'#595757',
    paddingBottom:10
  },

  line:{
    height:1/PixelRatio.get(),
    backgroundColor:'#d7d7d7',
    marginLeft:70,
  },
  allText:{
    justifyContent:'space-around',
    flex:1
  },
  titleAndTime:{
    justifyContent:'space-between',
    flexDirection:'row',
    marginTop:10
  },
  timeText:{
    marginRight:10,
    color:'#595757',
  },
  contentText:{
    marginBottom:10,
    fontSize:16,
    color:'#595757',
  }
});
