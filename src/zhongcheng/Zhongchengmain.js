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

const screenWidth = Dimensions.get('window').width;
const screenHeight = Dimensions.get('window').height;

//const url = "https://egis-cssp-dmzstg1.pingan.com.cn/m/insurance_unlogin/client.html#financing/fuying5thirdparty/index"
// const url_release = "http://www.cczcc.net/index.php/pingan/index/";
const url_debug = "https://xiamen.allcitygo.com";


export default class Zhongchengmain extends Component{


  constructor(props) {
     super(props);
     let state = {canBack:false,renderBack:false,uri:url_debug ,title:'众城理财'};
     this.state=state;
     this.url = state.url;
     console.log(Account.user.hash);
     console.log(this.state.uri)
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
        let name = url.substring(url.lastIndexOf('/')+1);
        // url = "http://www.cczcc.net/pingan/" + name + ".htm";
        // this.evalJs("location.href='"+url+"'" );
      }

    // }

    // if (!navState.loading) {
      let title = navState.title.startsWith('http')?'':navState.title;
      this.setState({ title:title,canBack: navState.canGoBack,renderBack:navState.canGoBack});
    // }

    // if(!navState.loading){
    //   let title = navState.title.startsWith('http')?'':navState.title;
    //   console.log(title+" "+navState.canGoBack);
    //   this.setState({
    //       title:title,canBack: navState.canGoBack,renderBack:navState.canGoBack,source:null
    //   });
    // }
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
