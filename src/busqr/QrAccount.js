
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
  RefreshControl
} from '../../lib/Common';



//修改
import TitleBar from './component/AccountTitleBar'
import Notifier from '../../lib/Notifier'

import StandardStyle from '../lib/StandardStyle'
import ListButton from '../widget/ListButton'
import {IC_Recharge,IC_Withdraw,IC_Weixin,IC_Help,IC_Detail,IC_charge,IC_RechargeGray} from './icons/Icons'
import {refreshColors} from '../widget/StateListView'
import QrUtils from './QrUtils'
import WebUtil from '../../lib/WebUtil'
const SCREEN_WIDTH = Dimensions.get('window').width;const SCREEN_HEIGHT = Dimensions.get('window').height
const BUSMAIN = "/busqr/main"
const PERSONAL  = "/personal/center"


export default class QrAccount extends Component{
	constructor(props){
		super(props);
    QrUtils.acc = Account.user.qrAccount;
		this.state={acc:Account.user.qrAccount,isRefreshing:true};
    Notifier.addObserver("qrChargeOk",this._refresh);
    Notifier.addObserver("updateAcc",this._refresh);
	}


  componentWillUnmount() {
    Notifier.removeObserver("qrChargeOk",this._refresh);
    Notifier.removeObserver("updateAcc",this._refresh);
  }

  componentDidMount(){
    this._refresh();
  }


  _getBalance=(data)=>{
    this._loadComplete();
    QrUtils.balance = data.balance;
  }
  _loadComplete=()=>{
    this.setState({isRefreshing:false});
  }

 _refresh=()=>{
    Api.detail(this,{
      api:'qr_acc/balance',
      success:this._getBalance,
      error:this._loadComplete,
      message:this._loadComplete,
    });
  }

  _gotoRecharge=()=>{
    Api.push('/busqr/chage');
  }
  _gotoQrtrans=()=>{
    // Api.push('/busqr/trans');
    Api.push('/busqr/qrOutFundFirStep');
  }

  _renderRight=()=>{
    return <TouchableOpacity style={styles.p10} onPress={this._gotoQrtrans}>
      <Text style={styles.f15}>关闭服务</Text>
    </TouchableOpacity>
  }
  _help=()=>{
    WebUtil.open(Api.imageUrl + '/index.php/sys_agree_detail/content/qrhelp',"公交扫码使用说明");
  }

  _onRefresh=()=>{
    this._refresh();
  }
  renderRefresh=()=>{
    return (
      <RefreshControl
          refreshing={this.state.isRefreshing}
          onRefresh={this._onRefresh}
          colors={refreshColors}
          progressBackgroundColor="#ffffff"
        />
    );
  }
	render(){
      let textProps={
        style:[StandardStyle.fontWhite,StandardStyle.h3,{backgroundColor:"transparent"}]
      }
      const refresh = this.renderRefresh();
			return (
				<View style={CommonStyle.container}>
					<TitleBar title="扫码账号" renderRight={this._renderRight} />
          <ScrollView
            refreshControl={refresh}
            onScrollBeginDrag={this._onStart}
            scrollEventThrottle={16}
            onScroll={this.onScroll}
            style={styles.container}>
            <Image source={require('./images/bus_bg.png')} style={styles.bg} resizeMode="contain">
                <View style={[StandardStyle.center,StandardStyle.fixed,styles.pBttom10]}>
                    <Text {...textProps}>{this.state.acc && this.state.acc}</Text>
                    <View style={{justifyContent:'space-between',flexDirection:'row'}}>
                      <Text {...textProps}>余额（元）</Text>
                    </View>
                    <Text style={[StandardStyle.fontWhite,styles.font24]}>{this.state.balance}</Text>
                    <TouchableOpacity style={styles.helpView} onPress={this._help}><IC_Help/></TouchableOpacity>
                </View>
                <View style={styles.msgbox_bottom}>
                </View>
                <View style={styles.bottomTextView}>
                  <Text style={[StandardStyle.fontWhite,StandardStyle.h3,styles.msgbox_bottomText]}>截止：{this.state.time}</Text>
                </View>

            </Image>
            {/*<View style={styles.msgbox}>
                <View>
                    <Text {...textProps}>{this.state.acc && this.state.acc}</Text>
                    <View style={{justifyContent:'space-between',flexDirection:'row'}}>
                      <Text {...textProps}>账户余额（元）</Text>
                      <TouchableOpacity onPress={this._help}><IC_Help/></TouchableOpacity>
                    </View>
                    <Text style={[StandardStyle.fontWhite,StandardStyle.h1]}>{this.state.balance}</Text>
                </View>
                <View style={styles.msgbox_bottom}>

                  <Text style={[StandardStyle.fontWhite,StandardStyle.h3,styles.msgbox_bottomText]}>截止：{this.state.time}</Text>
                </View>
            </View>*/}
            <View style={styles.btnlist}>
              <ListButton text="充值" Icon={<IC_RechargeGray/>} url="/busqr/chage"/>
              <ListButton text="明细" Icon={<IC_Detail/>} url="/busqr/trans" hasBottom={true}/>


            </View>
          </ScrollView>
				</View>
			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1},
  bg:{
    width:SCREEN_WIDTH,
    height:SCREEN_WIDTH/1.42,
  },
  msgbox:{
    backgroundColor:'#e8464c',
    height:SCREEN_HEIGHT/3.5,
    padding:20,
    paddingBottom:0,
    justifyContent:'space-between'
  },
  msgbox_bottom:{
    //backgroundColor:'gray',
    opacity:0.1,
    width:SCREEN_WIDTH,
    height:30,
    position:'absolute',
    bottom:0,
    left:0,
    backgroundColor:'#000',
    padding:5
  },
  msgbox_bottomText:{
    opacity:1,
    backgroundColor:'transparent'
  },
  bottomTextView:{
    position:'absolute',
    bottom:5,
    left:5,
    zIndex:9
  },
  btnlist:{
    marginTop:0
  },
  helpView:{
    position:'absolute',
    top:SCREEN_HEIGHT/7,
    right:30,
    padding:10,
    backgroundColor:'transparent'
  },

  //功能性样式
  p10:{
    padding:10,
  },
  f15:{
    fontSize:15,
    color:'#595757',
  },
  font24:{
    fontSize:50,
    lineHeight:60,
    color:'#fff',
    backgroundColor:'transparent'
  },
  pBttom10:{
    paddingBottom:0
  }
});
