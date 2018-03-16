import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  Image,
  Dimensions,
  Linking,
  TouchableHighlight,
  Api,
  A,
  ActivityIndicator,
  ScrollView,
  RefreshControl,
  CommonStyle,
  Platform,
  Account
} from '../../lib/Common';
import {WebView}  from 'react-native'
// import {TitleBar} from '../Global'
import TitleBarWebView from '../widget/TitleBarWebView'
import Web from 'react-native-webview2';
import WebUtil from '../../lib/WebUtil'

const screenWidth = Dimensions.get('window').width;
const screenHeight = Dimensions.get('window').height;

//const url = "https://egis-cssp-dmzstg1.pingan.com.cn/m/insurance_unlogin/client.html#financing/fuying5thirdparty/index"
// const url_release = "http://www.cczcc.net/index.php/pingan/index/";
export const MAIN_URL = "http://www.cczcc.net/index.php/";
export const URL_DEBUG = "test_ticket/index?userid=";//测试地址
export const URL_RELEASE = "ticket/index?userid=";//正式地址
// const url_debug = "http://www.cczcc.net/index.php/test_ticket/index?userid=";   //测试地址
// const url_debug = "http://www.cczcc.net/index.php/ticket/index?userid=";   //正式地址


export default class Ticketmain extends Component{


  constructor(props) {
     super(props);
     let uri="";
     if(Api.imageUrl.startsWith("http://218.5.80.17")){
       uri = MAIN_URL+URL_RELEASE;
     }else {
       uri = MAIN_URL+URL_DEBUG;
     }
     let state = {canBack:false,renderBack:false,uri:uri + Account.user.hash,title:'电子发票',ispdf:true};
     this.state=state;
     this.url = state.url;
     console.log(Account.user.hash);
     console.log(this.state.uri)
   }
   componentDidMount(){
     this.setState({ispdf:true})
   }

    onMessage =(e)=>{
        // console.log('-----------------------onMessage');
        // console.log(e.nativeEvent.data);
        this.props.onMessage && this.props.onMessage(e);
        this.evalReturn(e.nativeEvent.data);
    }


    setHeight(h) {
        this.setState({
            height: h
        });
    }

    // only support on android
    evalJs =(js)=>{
        // console.log('-----------------------evalJs');
        // console.log(js);
        this.go(`javascript: ${js};` + Math.random());
    }

    evalReturn=(r)=>{
        // console.log('--------------------evalReturn');
        // console.log(r);
        if (this.props.evalReturn && r.indexOf(this.state.nativeJsId) == -1) { //外部有传递evalReturn 并且要执行的hash非内嵌的方法
            this.props.evalReturn(r); //在外部执行hash
        } else {
            eval(r);
        }
    }

    go =(uri)=>{
        this.setState({source: {
            uri: uri
        }});
    }

    goForward=()=>{
        this.newPageId();
        this.webview.goForward();
    }

    goBack = ()=>{
        this.newPageId();
        this.webview.goBack();
    }

    stopLoading=()=>{
        this.webview.stopLoading();
    }

    reload =()=>{
        this.newPageId();
        this.webview.reload();
    }

    newPageId =()=>{
        this.setState({
            pageId: Math.random()
        });
    }

  //  console.log(navState.url);
  //  this.curUrl = navState.url;
  _onNavigationStateChange=(navState)=>{
    // if(Platform.OS=='android' && navState.url && navState.url.endsWith('pdf')){
      if(navState.url!=this.url){
        this.url = navState.url;
        let url = navState.url;
      }
        this.setState({ canBack: navState.canGoBack,renderBack:navState.canGoBack,ispdf:true});
      if (navState.url.indexOf('pdf')>0&&this.state.ispdf) {
         this.setState({ispdf:false});
         WebUtil.open(navState.url,"下载电子发票");
        return;
      }

  }

  _onMessage=()=>{

  }

  getHost=()=>{
    let url = this.state.uri;
    if(url.startsWith('http')){
      return url.substring(7,url.indexOf('/',8))
    }

    return url.substring(8,url.indexOf('/',9));
  }

  _onShouldStartLoadWithRequest=(event)=>{
    console.log(event);
    return true
  }


  onBack=()=>{
    console.log(this.state.canBack);

    // if(this.whiteUrl.indexOf(this.state.uri) >= 0){
    //
    //     Api.goBack();
    //     return;
    // }
    // let host = this.getHost();
    // console.log(host);
    // if(this.whiteList.indexOf(host) >= 0){
    //   this.evalJs('javascript:window.back();');
    //   return;
    // }


    if (this.state.canBack) {
        this.goBack();
     } else {
         //提示不能返回上一页面了
        //  this.toast.show('再点击就退出啦', DURATION.LENGTH_SHORT);
        Api.goBack();
     }
  }


 render() {
   return (
     <View style={CommonStyle.container}>
       <TitleBarWebView
         title={this.state.title?this.state.title:this.props.title} renderBack={this.state.renderBack} onBack={this.onBack}/>
       <WebView
         ref={(webview)=>{this.webview=webview}}
         style={{backgroundColor:'#fff',flex:1}}
         source={this.state.source ? this.state.source : {uri:this.state.uri,method: 'GET'}}
         javaScriptEnabled={true}
         domStorageEnabled={true}
         onNavigationStateChange={this._onNavigationStateChange}
       />
     </View>
   );
 }

}

const styles = StyleSheet.create({
  text: {
        fontSize: 20,
        color: '#333',
        marginLeft: 10
    },
});
