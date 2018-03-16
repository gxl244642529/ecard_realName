
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
  TouchableAll,
  Platform,
  RefreshControl,
} from '../../lib/Common';
import {
  Button,
} from '../Global'
import Swiper from 'react-native-swiper';
let ScreenModule = require('react-native').NativeModules.ScreenModule;

import{
    pageButton,
}from '../GlobalStyle'
import WebUtil from '../../lib/WebUtil'
import HeaderTitleBar from './HeaderTitleBar'
import Notifier from '../../lib/Notifier'

import Navigator from '../lib/Navigator'

import {onRequireLoginPress} from '../../lib/LoginUtil'
import PersonHead from '../personal/PersonHead'
import NfcUtil from '../../lib/NfcUtil'

import AdvView from '../widget/AdvView'
import {
  AsyncStorage
} from 'react-native';

import {refreshColors} from '../widget/StateListView'

/**
 *
 *  _test=()=>{
    Api.push("/realName/overTrans");
    // Api.push("/realName/realLead");
  }
  _rCard=()=>{
    Api.push("/realName/rCardList");
    // Api.push("/realName/rCard");
  }
 */

const image_index= require('./images/indexbg.jpg');

const main1 = require('./images/main1.png');
const main2 = require('./images/main2.png');
const main3 = require('./images/main3.png');
const main4 = require('./images/main4.png');



const width = Dimensions.get('window').width ;
const height = Dimensions.get('window').height - (Platform.OS=='android' ? 20 : 0) ;
const CENTER_GROUP_HEIGHT = 80;

const KEY_ISFIRST="key_isFirstLogin8";
const QR_MAIN1 = "/busqr/qrReturnUrl/0"
const QR_MAIN2 = "/busqr/qrReturnUrl/1"

const RenderGridOne=(data)=>{
  let img = Api.imageUrl + data.img;
  return (
    <TouchableAll onPress={()=>{WebUtil.open(data.url,data.title)}} style={styles.grid}>
      <View>
        <Image resizeMode="stretch" source={{uri:img}} style={styles.gridImg} />
        <Text style={styles.gTitle}>{data.title}</Text>
        <Text style={styles.gSubLabel}>{data.subtitle}</Text>
      </View>
    </TouchableAll>
  );
}

const RenderGrid=(data,index)=>{
  return (
    <View style={styles.gridContainer} key={"fg"+index}>
      {RenderGridOne(data[0])}
      <View style={{paddingLeft:VSPACING/2,paddingRight:VSPACING/2}}/>
      {RenderGridOne(data[1])}
    </View>
  );
}

const RenderSingle=(data,index)=>{
  return (
    <TouchableAll activeOpacity={0.8} key={"fs"+index} onPress={()=>{WebUtil.open(data.url,data.title)}}>
      <Image source={{uri:Api.imageUrl + data.img}} style={styles.single}>
        <View style={styles.singleBottom}>
          <Text style={styles.sMainLabel}>{data.title}</Text>
          <View style={styles.sbt}>
            <Text style={styles.sSubTip}>好货推荐</Text>
            <Text style={styles.sSubLabel}> | </Text>
            <Text style={styles.sSubLabel}>{data.subtitle}</Text>
          </View>
        </View>
      </Image>
    </TouchableAll>

  );
}


class FlowGrid extends Component{

   constructor(props){
    super(props);
    var arr = [];
    var c = Math.floor(props.children.length / 2);
    var j = 0;
    for(var i=0; i < c ; ++i){
      arr.push([props.children[j],props.children[j+1]]);
      j = j+2;
    }

    this.state={children:arr};
  }
  componentWillReceiveProps(nextProps){
    var arr = [];
    var c = Math.floor(nextProps.children.length / 2);
    var j = 0;
    for(var i=0; i < c ; ++i){
      arr.push([nextProps.children[j],nextProps.children[j+1]]);
      j = j+2;
    }
    this.setState({children:arr});
  }


  shouldComponentUpdate(nextProps, nextState) {
    return true;
  }


  render(){
    let icon = Api.imageUrl+this.props.icon;
    return (
      <View style={styles.flow}>
        <View style={[styles.flowTitle,{backgroundColor:this.props.bg}]}>
          <View style={styles.bian}/>
          <Text style={styles.mainTitle}>{this.props.title}</Text>
          <Text style={styles.subTitle}>{this.props.subtitle}</Text>
        </View>
        <View style={styles.titleLeft}>
          <Image style={styles.gridIcon} resizeMode="contain" source={{uri:icon}} />
          <View style={styles.titleLine}></View>
        </View>
        {this.state.children.map(RenderGrid)}
      </View>
    );
  }
}



class FlowSingle extends Component{


  shouldComponentUpdate(nextProps, nextState) {
    return nextProps.bg!=this.props.bg ||
          nextProps.title!=this.props.title ||
          nextProps.subtitle!=this.props.subtitle ||
          nextProps.children !== this.props.children;
  }


  render(){
    let icon  =Api.imageUrl+this.props.icon;
    return (
      <View style={styles.flow}>
          <View style={[styles.flowTitle,{backgroundColor:this.props.bg}]}>
            <View style={styles.bian}/>
            <Text style={styles.mainTitle}>{this.props.title}</Text>
            <Text style={styles.subTitle}>{this.props.subtitle}</Text>
          </View>
          <View style={styles.titleLeft}>
            <Image style={styles.gridIcon} resizeMode="contain" source={{uri:icon}} />
            <View style={styles.titleLine}></View>
          </View>
          {this.props.children.map(RenderSingle)}
        </View>
    );
  }
}


class Header extends Component{


  shouldComponentUpdate(nextProps, nextState) {
    return nextProps.title !== this.props.title || nextState.account != this.state.account;
  }

  constructor(props){
    super(props);
    var str = "";
    var week = new Date().getDay();
    if (week == 0) {
            str = "周日";
    } else if (week == 1) {
            str = "周一";
    } else if (week == 2) {
            str = "周二";
    } else if (week == 3) {
            str = "周三";
    } else if (week == 4) {
            str = "周四";
    } else if (week == 5) {
            str = "周五";
    } else if (week == 6) {
            str = "周六";
    }
    this.state={date:new Date().format("yyyy.MM.dd ")+str,account:Account.user};
    Notifier.addObserver("loginSuccess",this._refresh);
    Notifier.addObserver("logoutSuccess",this._refresh);
  }

  componentWillUnmount() {
     Notifier.removeObserver("loginSuccess",this._refresh);
      Notifier.removeObserver("logoutSuccess",this._refresh);
  }

  _refresh=()=>{
    let {account} = Account.user || {};
    this.setState({account})
  }
  /**
   * 个人中心
   * @return {[type]} [description]
   */
  _person=()=>{
    Api.push('/personal/center');
  }

  render(){
    return (
      <View style={{alignItems:'center'}}>
       <Image  source={image_index} style={styles.header} resizeMode="stretch">
        <View style={styles.headerContainer}>
          <PersonHead onPress={this._person} style={styles.headerImgContainer} />
          <TouchableOpacity onPress={this._person} activeOpacity={0.8} >
            <Image
              source={this.state.account ? require('./images/leve_login.png') : require('./images/leve.png') }
              resizeMode="contain"
              style={styles.levelImage}>
            </Image>
          </TouchableOpacity>
        </View>
        <Text style={styles.date}>{this.state.date}</Text>
        <View style={styles.headerLabelContainer}>
          <Text style={styles.headerLabel}>{this.props.title}</Text>
        </View>
      </Image>
    </View>
    );
  }
}

class MainButton extends Component{

  constructor(props){
    super(props);
    if(props.requireLogin){
      this._onPress = onRequireLoginPress(this._onPress);
    }

  }

  shouldComponentUpdate(nextProps, nextState) {
    return false;
  }

  _onPress=()=>{
    if( typeof this.props.url==='string' ){
      Api.push(this.props.url);
    }else{
      this.props.url();
    }

  }


  render(){
    return (
      <TouchableOpacity onPress={this._onPress} >
        <Image source={this.props.source} resizeMode="contain" style={styles.mainButton} />
      </TouchableOpacity>
    );
  }
}


class SubButton extends Component{

  constructor(props){
    super(props);
    this.state={};
    if(props.requireLogin){
      this._onPress = onRequireLoginPress(this._onPress);
    }
  }

  shouldComponentUpdate(nextProps, nextState) {
    return true;
  }
  _onPress=()=>{
    Api.push(this.props.url);
  }

  render(){
    return (
      <TouchableOpacity onPress={this._onPress} >
        <View style={sbutton.container}>
          <Image source={this.props.source} style={sbutton.icon} resizeMode="contain" />
          <Text style={sbutton.label}>{this.props.title}</Text>
        </View>
      </TouchableOpacity>
    );
  }
}

const sbutton = StyleSheet.create({
  container:{width:width/4.5,height:CENTER_GROUP_HEIGHT, justifyContent:'center', alignItems:'center' },
  label:{marginTop:5,
    // color:'#5A7BAA',
    color:'#796a56',
    fontSize:12},
  // icon:{width:28,height:28},
  icon:{width:30,height:30},
});


class Introduce extends Component{
  constructor(props) {
      super(props);
  }

  _gotoRequest=()=>{
      Api.replace("/");
  }
  renderImg=()=>{
   let imageViews=[];
  }
  _goMian=()=>{
    Api.returnTo("/");
  }


  render(){
      return (
        <Swiper  loop={false}  >
          <Image style={introStyle.backgroundImage} source={require("./images/guide_1.png")}/>
          <Image style={introStyle.backgroundImage} source={require("./images/guide_2.png")}/>
          <Image style={introStyle.backgroundImage} source={require("./images/guide_3.png")}/>
          <Image style={introStyle.backgroundImage} source={require("./images/guide_4.png")}>
          <View style={introStyle.btnBox}>
              <Button
                    text="立即体验" style={[introStyle.button,introStyle.btn,introStyle.btngo]}
                    onPress={this._gotoRequest}
                    textStyle={pageButton.buttonText}
              />
          </View>

          </Image>
        </Swiper>

      );
  }
}

const introStyle = StyleSheet.create({
  contentContainer: {
       width: width*2,
       height: height,
     },
     backgroundImage: {
       width: width,
       height: height,
     },
     btnBox:{
       position:"absolute",
       bottom:80,
       flexDirection:'row',
       paddingLeft:10,//+10
       paddingRight:10,//+10
     },
     btn:{
         flex:1,
     },
     button:{
       flexDirection:'row',
       margin:10,
       height:35,

       borderColor:'#43b0b1',
       //borderRadius:5,
       borderWidth:1,
       justifyContent:'center',
       alignItems:'center',

     },
     btngo:{
       backgroundColor:'#43b0b1',
     },
     buttonText:{
       fontSize:16,
       color:'#43b0b1'
     },
});

//87/320
export default class _Home extends Component{
  constructor(props){
    super(props);
    this.state={opacity:0,title:"",jump:null,isRefreshing:true,isFirst:Api.isFirst,light:30,};
    this.onStudent = onRequireLoginPress(this.onStudent);
    // if(Platform.OS=='ios'){
       Notifier.addObserver('componentDidFocus',this.componentDidFocus);
    // }

  }

  componentWillUnmount() {
    Notifier.removeObserver('componentDidFocus',this.componentDidFocus);

  }


  componentDidFocus=(name,route)=>{
  /*  if(this.refs.ADV){
      this.refs.ADV.setAutoplay(route.root===true);
    }*/
    if(Platform.OS=='ios'){
      if(route.root===true){
        Navigator.setWhiteStyle();
          // ScreenModule.setValue(this.state.light);
      }else{
        Navigator.setBlackStyle();
      }
    }

    /*
    if (route.location) {
      if (route.location.pathname===QR_MAIN1) {
        ScreenModule.setValue(100);
      }
    }
    ScreenModule.getValue((value)=>{
      console.log("value="+value);
      if (value!=100) {
        this.setState({light:value});
        console.log(this.state.light);
      }
      else if (route.location&&route.location.pathname!=QR_MAIN1&&value==100 ||route.root===true) {
        ScreenModule.setValue(this.state.light);
      }
    })
    */
  }
  componentWillMount(){
    /*let value = AsyncStorage.getItem(KEY_ISFIRST);
    if(value!=null){
      this.setState({isFirst:false});
      console.log("isFirst");
      console.log(value);
    }else{
      this.setState({isFirst:true});
    }*/





  }

  componentDidMount() {
    /*LocalData.getObject(KEY_ISFIRST).then((result)=>{
        console.log("result==="+result);
        this.setState({isFirst:false});
        //Navigator.onStartup();
      },(result)=>{
        this.setState({isFirst:true});
        //this._onRefresh();
      });*/

    Navigator.onStartup();
    this._onRefresh();
    this.getBindCard();

  }
  getBindCard=()=>{
  Api.api({
    api:'newRcard/getBindingCard',
    success:(result)=>{
      // console.log(result);
      this.setState({cardId:result});
    }
  })
  }
  setStorage=()=>{
    /*let record={isFirst:'not'};
    LocalData.putObject(KEY_ISFIRST,record);*/
    this.setState({isFirst:false});
     Navigator.setFirstRead();
  }
  _renderIntroduce=()=>{
    return <Swiper  loop={false}  >
          <Image style={introStyle.backgroundImage} source={require("./images/guide_1.png")}/>
          <Image style={introStyle.backgroundImage} source={require("./images/guide_2.png")}/>
          <Image style={introStyle.backgroundImage} source={require("./images/guide_3.png")}/>
          <Image style={introStyle.backgroundImage} source={require("./images/guide_4.png")}>
            <View style={introStyle.btnBox}>
                <Button
                     text="立即体验" style={[introStyle.button,introStyle.btn,introStyle.btngo]}
                     onPress={this.setStorage}
                     textStyle={pageButton.buttonText}
                />
            </View>

          </Image>
        </Swiper>
  }

  _loadComplete=()=>{
    this.setState({isRefreshing:false});
  }

  _onRefresh=()=>{
    let data = {version:Api.version};
    console.log(  Api.version);
    Api.detail(this,{
      api:'m_config/config2700',
      data:data,
      success:this._loadComplete,
      error:this._loadComplete,
      message:this._loadComplete
    });
  }


  onScroll=(e)=>{
    let y = e.nativeEvent.contentOffset.y;
    if(y > 60){
      y = 60;
    }
    this.setState({opacity:y});
  }
  _onStart=()=>{

  }

  _onEnd=()=>{

  }

  onStudent=()=>{

    if(Platform.OS=='ios'){
      Api.push('/discard/main');
    }else{
      Api.push('discard');
    }

  }

  _renderGrid=(data,index)=>{
    if(data.type == 1){
      return <FlowSingle {...data} key={"grid"+index} />
    }else{
      return <FlowGrid {...data} key={"grid"+index}/>
    }
  }
  _renderSubButton=(data,index)=>{

    return <SubButton
            requireLogin={data.login}
            key={"sub"+index}
            title={data.title}
            url={data.url}
            source={{uri:Api.imageUrl+data.icon} } />
  }

  _renderJump=(jump)=>{
    if(!jump)return null;
    return (
      <View style={styles.floatDiv}>
        <View style={{backgroundColor:'transparent'}}>
          <TouchableOpacity onPress={()=>{Api.push(jump.url)}}>
            <Image
              style={{width:jump.width,height:jump.height,backgroundColor:'transparent'}}
              resizeMode="stretch"
              source={{uri:Api.imageUrl + jump.img}} />
          </TouchableOpacity>
          <TouchableOpacity
            style={{position:'absolute',top:10,right:10,padding:5,}}
            onPress={()=>{this.setState({jump:null})}}>
            <Image
              source={require("./images/main_close.png")}
              resizeMode="contain"
              style={{width:20,height:20,opacity:0.8,}} />
          </TouchableOpacity>
        </View>


      </View>
    );

  }
  _goCheck=()=>{
    this.setState({cardId:null})
    Api.push('/realName/rCard');

    //如果有nfc则跳到nfc验证页面
    // if(Platform.OS=='android'){
    //   NfcUtil.isAvailable((result)=>{
    //     if(result){
    //     Api.push("/realName/nfcVertify/"+this.state.cardId);
    //     }else {
    //       Api.push("/realName/questionVertify/"+this.state.cardId);
    //     }
    //   });
    // }

  }
  _cancelCheck=()=>{
    this.setState({cardId:null})
  }
  renderCardJump=(jump)=>{
    //如果jump不出现则卡验证弹窗出现
    console.log(jump)
    if(jump) return null;
    if(!this.state.cardId)return null;
    return(
      <View style={styles.floatDiv}>
        <View style={{width:width*0.8333,height:width*0.4166,backgroundColor:'#fff',borderRadius:10,alignItems:'center'}}>

          <View style={{flex:1,justifyContent:'center',alignItems:'center'}}><Text style={{fontSize:15}}>您有一张待开通挂失服务的卡!</Text></View>
          <View style={{width:300,height:1/PixelRatio.get(),backgroundColor:'#d7d7d7',}}></View>
          <View style={{flexDirection:'row',justifyContent:'space-between',height:60,bottom:0,width:300}}>
            <TouchableOpacity style={{flex:1,justifyContent:'center',alignItems:'center'}} onPress={this._cancelCheck}>
              <Text style={{fontSize:18,color:'#000'}}>取消</Text>
            </TouchableOpacity>
            <TouchableOpacity style={{flex:1,borderBottomRightRadius:10,justifyContent:'center',alignItems:'center',backgroundColor:'#e8464c'}} onPress={this._goCheck}>
              <Text style={{fontSize:18,color:'#fff'}}>去开通</Text>
            </TouchableOpacity>
          </View>
          <View style={{position:'absolute', width:1/PixelRatio.get(),height:60,backgroundColor:'#d7d7d7',left:150,bottom:0}}></View>
        </View>
      </View>

    )

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
  //<SubButton title="电子发票"  requireLogin={true}  url="/ticket" source={require('./images/ebill.png')} />
// <SubButton title="商城"     url="/shopUser/main" source={require('./images/ic_main_shop.png')} />
//  <SubButton title="众城理财"  requireLogin={true}  url="/zhongcheng" source={require('./images/zhong.png')} />

/**
<SubButton
  title="惠民理财"
  url="fund"
  source={require('./images/sub1.png')} />
<SubButton
  title="绿色出行"
  requireLogin={true}
  url="/greentravel"
  source={require('./images/sub_gt.png')}/>
<SubButton title="电子发票"  requireLogin={true}  url="/ticket" source={require('./images/ebill.png')} />
<SubButton title="保险" url="safe" source={require('./images/sub3.png')} />
<SubButton title="平安之家" requireLogin={true} url="pingan"
  source={require('./images/sub4.png')} />
 <SubButton title="公交" url="bus" source={require('./images/sub2.png')} />

<SubButton title="联盟商家" url="business" source={require('./images/sub5.png')} />
<SubButton title="网点查询" url="/netpot" source={require('./images/sub6.png')} />
<SubButton title="常见问题" url="question" source={require('./images/question.png')} />
<SubButton title="拾卡不昧" url="pickCard" source={require('./images/sub8.png')} />
<SubButton title="新闻"     url="news" source={require('./images/news.png')} />
*/

  render(){
      const refresh = this.renderRefresh();
      return (this.state.isFirst?this._renderIntroduce():
        <View style={[CommonStyle.container,styles.bg]}>
          <View style={styles.main}>
            <ScrollView
              automaticallyAdjustContentInsets={false}
              alwaysBounceVertical={false}
              refreshControl={refresh}
              onScrollBeginDrag={this._onStart}
              scrollEventThrottle={16}
              onScroll={this.onScroll}
              style={styles.container}>
              <Header title={this.state.title} />
              <View style={styles.imageGroup}>
                <MainButton requireLogin={false} url="/recharge" source={main1} />
                <MainButton requireLogin={false} url="selling" source={main2} />
                <MainButton requireLogin={true} url="/myecardlist" source={main3} />
                <MainButton requireLogin={false} url={this.onStudent} source={main4} />
              </View>

              <ScrollView horizontal={true} showsHorizontalScrollIndicator={false} style={styles.centerGroup}>
              {this.state.pages&&this.state.pages.map(this._renderSubButton)}
	            </ScrollView>
              <View style={styles.adv}>
                <AdvView ref="ADV" width={IMAGE_WIDTH} height={ADV_HEIGHT} data={this.state.advs} />
              </View>
              {this.state.grids && this.state.grids.map(this._renderGrid)}

            </ScrollView>
          </View>
          <HeaderTitleBar value={this.state.opacity} msg={this.state.msg}/>
          {this._renderJump(this.state.jump)}
          {this.state.cardId&&this.renderCardJump(this.state.jump)}
        </View>
      );
  }
}



const HEADER_SIZE = width / 320 * 87;

const SPACING = 6;
const VSPACING = 6;
const MAIN_BUTTON_CONTAINER_HEIGHT = width * 86 / 320;
//521 346
// width = 720 * 356 / 521
const IMAGE_WIDTH = width - SPACING*2;
const IMAGE_HEIGHT = IMAGE_WIDTH * 356 / 470;
const MAIN_BUTTON_SIZE =  width * 55 / 320;

const ADV_HEIGHT = width * 122 / 310;

const GRID_WIDTH = (width - VSPACING - SPACING*2)/2 ;
const GRID_HEIGHT=  GRID_WIDTH* 103/154;

const FLOW_TITLE_HEIGHT = width * 44 / 310;

const SINGLE_HEIGHT = (width - SPACING*2) * 177 / 310;


const styles = StyleSheet.create({
  main:{position:'absolute',width:width,height:height},
  floatDiv:{ position:'absolute',justifyContent:'center',alignItems:'center',zIndex:10000, width:width,height:height, backgroundColor:'rgba(0,0,0,0.6)'  },

  date:{fontSize:19,textAlign:'center',color:'#fff',backgroundColor:'transparent'},   //日期
  headerLabelContainer:{width:width, height:40, },//头部的文字容器
  headerLabel:{color:'#fff', marginTop:3, width:width,textAlign:'center',backgroundColor:'transparent',fontSize:12},//头部文字

  adv:{paddingLeft:SPACING,paddingTop:VSPACING,paddingBottom:VSPACING},
  centerGroup:{backgroundColor:'#EAEAEA',height:CENTER_GROUP_HEIGHT,flexDirection:'row'},
  imageGroup:{paddingLeft:15,paddingRight:15,height:MAIN_BUTTON_CONTAINER_HEIGHT,flexDirection:'row',justifyContent:'space-around',alignItems:'center'},
  topPadding:{height:20},
  bg:{backgroundColor:'#888888'},
  container:{flex:1,backgroundColor:'#fff',marginTop:Platform.OS=='android'?0:20},

  //头部
  // headerContainer:{flex:1, alignItems:'center',justifyContent:'center'},
  // 289 * 121
  levelImage:{width:width/1.875,height:width/5.682,marginTop:70,marginLeft:-10,justifyContent:'center',alignItems:'center'},
  headerContainer:{flex:1, alignItems:'center',justifyContent:'space-around',flexDirection:'row'},
  headerImgContainer:{marginTop:78,marginRight:-30,zIndex:1000},
  header:{backgroundColor:'#ccc',height:IMAGE_HEIGHT , width:width, justifyContent:'center',alignItems:'center', },//,width:IMAGE_WIDTH,marginTop:VSPACING,marginLeft:SPACING},
               //图片上面一条
  mainButton:{width:MAIN_BUTTON_SIZE,height:MAIN_BUTTON_SIZE },
  buttonGroup:{height:100,flexDirection:'row'},


  //下面title
  titleLine:{ top:0,right:0,position:'absolute', width:1/PixelRatio.get(),height:FLOW_TITLE_HEIGHT,backgroundColor:'#fff'},
  flow:{paddingLeft:VSPACING,paddingRight:VSPACING,paddingBottom:VSPACING,},
  flowTitle:{height:FLOW_TITLE_HEIGHT, marginBottom:VSPACING, justifyContent:'center',alignItems:'center'},
  titleLeft:{ justifyContent:'center', alignItems:'center',position:'absolute',top:0,left:0,height:FLOW_TITLE_HEIGHT,width:FLOW_TITLE_HEIGHT,marginLeft:VSPACING},
  mainTitle:{color:'#fff',fontSize:18},
  subTitle:{color:'#fff',fontSize:11,marginTop:2},
  //两个的
  grid:{flex:1},
  gridImg:{height:GRID_HEIGHT,backgroundColor:'#ccc'},
  gridContainer:{flexDirection:'row'},
  gMargin:{marginLeft:VSPACING},
  gSubLabel:{fontSize:11,color:'#888888'},
  gTitle:{fontSize:14},
  gridIcon:{width:23,height:23},

  //一个的
  single:{height:SINGLE_HEIGHT, marginBottom:VSPACING, justifyContent:'flex-end', backgroundColor:'#ccc'},
  singleBottom:{padding:5,backgroundColor:'rgba(0,0,0,0.3)'},
  sbt:{flexDirection:'row',marginTop:2},
  sMainLabel:{fontSize:15,color:'#fff'},
  sSubLabel:{fontSize:11,color:'#fff'},
  sSubTip:{fontSize:11,color:'#f29600'},
  bian:{height:6,width:width,position:'absolute',bottom:0,backgroundColor:'#fff',opacity:0.1},

});
