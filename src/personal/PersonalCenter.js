
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
  // Account,
  ActivityIndicator,
  CommonStyle,
  PixelRatio,
  LocalData,
  Log,
  ScrollView,
  Notifier,
  Platform
} from '../../lib/Common';



import {InteractionManager} from 'react-native'
import {onCheckLoginPress,onRequireLoginPress} from '../../lib/LoginUtil'
// const SCREEN_WIDTH = Dimensions.get('window').width;
// const SCREEN_HEIGHT= Dimensions.get('window').height;
import {SCREEN_WIDTH,SCREEN_HEIGHT} from '../widget/Screen'
import PersonHead from './PersonHead'
import {
  TitleBar,
} from '../Global'
import StandardStyle from '../lib/StandardStyle'
import R from '../lib/R'
import PcHearderTitleBar from './PcHearderTitleBar'
import {logout} from '../../lib/LoginUtil'
import Account from '../../lib/network/Account'


const enterOvertrans=()=>{
  Api.push('/realName/overTrans');
}

const NotRealName = ()=>{
  return (
    <TouchableOpacity onPress={enterOvertrans} style={styles.RealNameTextNo}>
      <Image
        source={require('./images/icon_2.png')}
        resizeMode="contain"
        style={styles.accIcon}/>
      <Text style={[StandardStyle.h4,StandardStyle.fontWhite,]}>去认证</Text>
    </TouchableOpacity>
  );
};

const RealName = ()=>{
  return (
    <TouchableOpacity onPress={enterOvertrans} style={styles.RealNameText}>
      <Image
        resizeMode="contain"
        source={require('./images/icon_1.png')}
        style={styles.accIcon}/>
      <Text style={[StandardStyle.h4,StandardStyle.fontWhite,]}>已认证</Text>
    </TouchableOpacity>
  );
}



class SubButton extends Component{

  constructor(props){
    super(props);
    this._onPress = onRequireLoginPress(this._onPress);
  }

  shouldComponentUpdate(nextProps, nextState) {

    return false;
  }

  _onPress=()=>{
    Api.push(this.props.url);
  }


  render(){
    return (
      <TouchableOpacity style={styles.mainList} onPress={this._onPress}>
        <View  style={styles.mainListText}>
           <Image source={this.props.source} style={subbutton.icon}/>
           <Text style={[StandardStyle.h4,StandardStyle.fontBlack,]}>{this.props.label}</Text>
        </View>
        <Image source={require('./images/icon_3.png')} style={subbutton.ricon}/>
      </TouchableOpacity>
    );
  }
}

const subbutton=StyleSheet.create({
  icon:{ width:22, height:22, marginLeft:20, marginRight:10, },
  ricon:{ width:10, height:14, marginRight:20, },
});


/**
 * 根据实名化显示不同状态
 */

export default class PersonalCenter extends Component{
  constructor(props){
    super(props);
    this.state = {};
    this._person = onCheckLoginPress(this._person);
    Notifier.addObserver("loginSuccess",this._refresh);
    Notifier.addObserver("logoutSuccess",this._refresh);
  }

  componentDidMount() {
    this._refresh();
  }

  componentWillUnmount() {
      Notifier.removeObserver("loginSuccess",this._refresh);
      Notifier.removeObserver("logoutSuccess",this._refresh);
  }

  _loadIsReal=(valid)=>{
     InteractionManager.runAfterInteractions(()=>{
        this.setState({valid});
     });
  }


  _refreshImpl=()=>{
    if(Account.user){
      this.setState({opacity:0,account:Account.user.account,valid:null});
       Api.api({
        api:'real/is',
        success:this._loadIsReal
      });
    }else{
     this.setState({opacity:0,valid:null,account:null});
    }
  }

  _refresh=()=>{
    InteractionManager.runAfterInteractions(this._refreshImpl);

  }

  _pcmsg=()=>{
    Api.push("/realName/pCMessage");
  }
  _qrPay=()=>{
    // Api.push("/busqr/qrRequest");
    // Api.push("/busqr/out_fund");
    // Api.push("/busqr/forget_pass")
    Api.push("/busqr/qrReturnUrl/0");//扫码支付

  }
  _qrAcc=()=>{
    Api.push("/busqr/qrReturnUrl/1");//扫码支付
  }



  _exit=()=>{
    logout();
  }
  _login=()=>{
    onRequireLoginPress(()=>{

    })
  }
  // <TouchableOpacity style={{padding:10}} onPress={this._login}><Text>登录</Text></TouchableOpacity>
  // <TouchableOpacity style={{padding:10}} onPress={this._realName}><Text>去实名化</Text></TouchableOpacity>
  // <TouchableOpacity style={{padding:10}} onPress={this._realCard}><Text>我的实名绑卡</Text></TouchableOpacity>
  //  <TouchableOpacity style={{padding:10}} onPress={this._pcmsg}><Text>我的消息中心</Text></TouchableOpacity>
  //  <TouchableOpacity style={{padding:10}} onPress={this._qrPay}><Text>扫码支付</Text></TouchableOpacity>
  //  <TouchableOpacity style={{padding:10}} onPress={this._exit}><Text>退出</Text></TouchableOpacity>
_renderRight=()=>{
  return(
     <TouchableOpacity style={{flexDirection:'row',
    height:45,
    width:50,
    justifyContent:'center',
    alignItems:'center'}}>
      <Image source={require('./images/SetUp.png')}
      style={{ width:20, height:20, marginRight:20}}/>
     </TouchableOpacity>
  );
}
onScroll=(e)=>{
  let y = e.nativeEvent.contentOffset.y;
  if(y > 40){
    y = 40;
  }
  this.setState({opacity:y});
}


_person=()=>{
  Api.push('personInfo');
}

_renderReal=(state)=>{

  if(state.account==null)return null;
  if(state.valid===true){
    return <RealName />;
  }else if(state.valid === false){
    return <NotRealName />
  }else{
    return null;
  }

}
//------扫码存放---
 /**
  *
  * <SubButton source={require('./images/ic_my_qr_pay.png')} label="扫码支付" url="/busqr/qrReturnUrl/0" />
    <SubButton source={require('./images/ic_my_qr.png')} label="扫码余额管理" url="/busqr/qrReturnUrl/1" />
  * @return {[type]} [description]
  */
  render(){
      return (
        <View style={[CommonStyle.container,styles.bg]}>
          <View style={{position:'absolute',width:SCREEN_WIDTH,height:SCREEN_HEIGHT}}>
          <ScrollView style={styles.container} onScroll={this.onScroll}>
             <Image
              source={require('./images/backgroundImg.png')}
              style={styles.headImg}>
                <PersonHead onPress={this._person} style={styles.person} />
                {this._renderReal(this.state)}
                <Text style={styles.account}>{this.state.account}</Text>
             </Image>
            <View style={styles.main}>
              <SubButton source={require('./images/ic_my_ecard.png')} label="我的e通卡" url="/myecardlist" />
              <SubButton source={require('./images/icon_4.png')} label="购卡订单" url="myorder" />
              <SubButton source={require('./images/icon_5.png')} label="充值记录" url="myrecharge" />
              <SubButton source={require('./images/icon_6.png')} label="挂失服务" url="/realName/rCard" />
              <SubButton source={require('./images/icon_7.png')} label="认证信息" url="/realName/overTrans" />
              {/*<SubButton source={require('./images/ic_my_qr_pay.png')} label="扫码支付" url="/busqr/qrReturnUrl/0" />*/}
              {/*<SubButton source={require('./images/ic_my_qr.png')} label="扫码余额管理" url="/busqr/qrReturnUrl/1" />*/}

              <SubButton source={require('./images/icon_8.png')} label="保单" url="mysafe" />
              <SubButton source={require('./images/icon_9.png')} label="收藏" url="mycollection" />

              {/*<SubButton source={require('./images/icon_9.png')} label="未实名提交（测试）" url="/realName/noRealMessage" />*/}
            </View>

          </ScrollView>
          </View>
          <PcHearderTitleBar value={this.state.opacity}/>
        </View>
      );
  }
}


const styles = StyleSheet.create({
  headImg:{ alignItems:'center', justifyContent:'center' , width:SCREEN_WIDTH, paddingTop:65, height:250, },

  accIcon:{ width:20, height:20, marginRight:5, },
  account:{backgroundColor:'transparent',fontSize:R.fontSize.h4,color:R.color.white,lineHeight:R.lineHeight.h4},
  container:{flex:1,marginTop:Platform.OS=='android'?0:20, backgroundColor:'#fff'},
  My:{
    flex:1,
    height:45,
    alignItems:'center',
    zIndex:9,
    flexDirection:"row",
    justifyContent:"space-between",
  },
 bg:{backgroundColor:'#888888'},
  top:{
    position:"absolute",
    top:0,

  },

  person:{
    marginTop:10,
  },

  RealNameTextNo:{
    marginTop:10,
    width:110,
    height:25,
    backgroundColor:"rgba(255,255,255,0.1)",
    alignItems:'center',
    justifyContent:'center',
    flexDirection:"row",
    marginBottom:5,
    borderRadius:15,
  },


  RealNameText:{
    marginTop:10,
    width:110,
    height:25,
    backgroundColor:"#e8464c",
    alignItems:'center',
    justifyContent:'center',
    flexDirection:"row",
    marginBottom:5,
    borderRadius:15,
  },

  main:{
    marginTop:10,
    marginBottom:100,

  },

  mainList:{
    flex:1,
    height:50,
    borderBottomWidth:1,
    borderBottomColor:'#d7d7d7',
    alignItems:'center',
    justifyContent:'center',
    flexDirection:"row",
    justifyContent:"space-between",
  },

  mainListText:{
    flexDirection:"row",
    alignItems:'center',
    justifyContent:'center',
  },

});
