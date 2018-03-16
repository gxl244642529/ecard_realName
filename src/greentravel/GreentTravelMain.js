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
  Account,
  Button
} from '../../lib/Common';
import {WebView}  from 'react-native'
// import {TitleBar} from '../Global'
import TitleBarWebView from '../widget/TitleBarWebView'

const screenWidth = Dimensions.get('window').width;
const screenHeight = Dimensions.get('window').height;

//const url = "https://egis-cssp-dmzstg1.pingan.com.cn/m/insurance_unlogin/client.html#financing/fuying5thirdparty/index"
// const url_release = "http://www.cczcc.net/index.php/pingan/index/";
const url_debug = "/index.php/g_travel?userid=";
const url_person="/index.php/g_person";

const ShareModule = Platform.OS=='android' ? require('react-native').NativeModules.ECardShareModule : require('react-native').NativeModules.ShareModule; ;
//const ShareModule = require('react-native').NativeModules.ShareModule;

export default class GreenTravelMain extends Component{


  constructor(props) {
     super(props);
     let state = {canBack:false,renderBack:false,uri: Api.imageUrl + url_debug + Account.user.hash,title:'绿色出行',};
     this.state=state;
     this.url = state.url;
     
     //console.log(Account.user.hash);
     console.log(this.state.uri)
   }
   componentDidMount(){
        console.log("didmount");
        //this.setSomeTest();
        /*ShareModule.wxShare({
                title:'分享標題',
                content:'分享描述',
                url:'鏈接',
                type:1,      //朋友圈

        },(ret)=>{
            if(ret===0){
                //分享成功
            }
    })*/
      
       this.initJavaScript();
   }
    /*evalReturn(r) {
        eval(r);
    }*/
    /*onMessage =(e)=>{
        // console.log('-----------------------onMessage');
        // console.log(e.nativeEvent.data);
        this.props.onMessage && this.props.onMessage(e);
        this.evalReturn(e.nativeEvent.data);
    }*/
    onLoad=()=>{
        //console.log("ll");
        this.initJavaScript();
    }
    onLoadEnd=()=>{
        console.log("loadEnd");
    }
    initJavaScript =()=>{
        //this.webview.evalJs(`alert('ok');`);
        //this.webview.evalJs(`test();`);
    }

    setText=(title)=>{
        console.log("setText");
        this.setState({title:title});
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

    go =(uri)=>{//因为后退后再前进，source已被更改，需要先清空并设置延时防止同步执行
        this.setState({source:null});
        setTimeout(()=>{
            this.setState({source:{
                uri:uri
            }});
        },50);
        
    }
    goPerson=()=>{
        //this.setState({uri:url_person});
        this.go(Api.imageUrl + url_person);
        //Api.push("/greentravelperson");
    }
    goRule=()=>{
        this.go(url_withdraw_rule);
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
    renderRight=()=>{
        let arr=this.state.uri.split("?");
        console.log(arr);
        /*if(arr[0]=="http://110.80.22.108:8887/index.php/g_main"){
            return <TouchableOpacity onPress={this.goPerson} style={styles.person}><Text style={styles.rightText}>我的红包</Text></TouchableOpacity>
        }*/
        if(this.state.title=="绿色出行"){
            return <TouchableOpacity onPress={this.goPerson} style={styles.person}><Text style={styles.rightText}>我的红包</Text></TouchableOpacity>
        }
        return null 
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

  }

  setSomeTest=()=>{
      console.log("sometest");
  }

  /*_onMessage=()=>{

  }*/

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
    
    //console.log(this.state.canBack);
    if (this.state.canBack) {
        this.goBack();
     } else {
         //提示不能返回上一页面了
        //  this.toast.show('再点击就退出啦', DURATION.LENGTH_SHORT);
        Api.goBack();
     }
  }
  
  onShareFriend=(event)=>{
    let json = JSON.parse(event.nativeEvent.data);
    console.log(json);

    ShareModule.wxShare({
        title:json.FR_TITLE,
        content:json.FR_CONTENT,
        url:json.FR_URL,
        type:1,      //朋友圈
    },(ret)=>{
        if(ret===0){//分享成功
            const script = `shareSuccess()`;
            if(this.webview){
                this.webview.postMessage("success");
            }
        }
    })
  }

 render() {
   return (
     <View style={CommonStyle.container}>
       <TitleBarWebView
       renderRight={this.renderRight}
         title={this.state.title?this.state.title:this.props.title} renderBack={this.state.renderBack} onBack={this.onBack}/>
       <WebView
         ref={(webview)=>{this.webview=webview}}
         style={{backgroundColor:'#fff',flexDirection:'column',flex:1}}
         source={this.state.source ? this.state.source : {uri:this.state.uri,method: 'GET'}}
         javaScriptEnabled={true}
         onLoad={this.onLoad}
         onLoadEnd={this.onLoadEnd}
         domStorageEnabled={true}
         onNavigationStateChange={this._onNavigationStateChange}
         onMessage={this.onShareFriend}
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
    person:{
        marginRight:10,
        padding:10,
        paddingTop:15,
        paddingBottom:0,
        
    },
    rightText:{
        color:'#333'
    }
});
