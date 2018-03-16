
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
DatePicker,
Account,
ActivityIndicator,
CommonStyle,
PixelRatio,
ScrollView,
Form,
Notifier,
Platform,
} from '../../lib/Common';
import WebUtil from '../../lib/WebUtil'
import {WebView}  from 'react-native'
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;
export default class ConfirmAlert extends Component{
  constructor(props) {
    super(props)
  }
  _onNavigationStateChange=(navState)=>{
    // if(Platform.OS=='android' && navState.url && navState.url.endsWith('pdf')){
      if(navState.url!=this.url){
        this.url = navState.url;
        let url = navState.url;
      }

  }
  render(){
    return(
      <View style={styles.confirmView}>
          <View style={styles.confirmViewAb}>
              <View style={styles.titleView}>
                <Image source={require('./images/title_left.png')} style={{width:35,height:18}} resizeMode="contain"/>
                <Text style={styles.titletext}>真实性承诺函</Text>
                <Image source={require('./images/title_right.png')} style={{width:35,height:18}} resizeMode="contain"/>
              </View>
              <ScrollView style={{flex:1}}>
              <WebView
                ref={(webview)=>{this.webview=webview}}
                style={styles.webviewstyle}
                source={{uri:Api.imageUrl+'/index.php/sys_agree_detail/content/confirm',method: 'GET'}}
                javaScriptEnabled={true}
                domStorageEnabled={true}
                onNavigationStateChange={this._onNavigationStateChange}
              />

              </ScrollView>
              <View style={styles.groupButView}>
                <TouchableOpacity style={styles.cancelbut} onPress={this.props.noagree}>
                  <Text style={styles.textsize}>取消</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.agreeBut} onPress={this.props.agree}>
                  <Text style={[styles.textsize,{color:'#fff'}]}>同意</Text>
                </TouchableOpacity>
              </View>
          </View>
        </View>

    );
  }


}


const styles=StyleSheet.create({
   confirmView:{position:'absolute',zIndex:100,width:SCREEN_WIDTH,height:SCREEN_HEIGHT,alignItems:'center',backgroundColor:'rgba(0,0,0,0.6)'},
   confirmViewAb:{position:'absolute',zIndex:2,top:50,left:20,width:SCREEN_WIDTH-40,height:SCREEN_WIDTH*1.45,backgroundColor:'#fff',borderRadius:10,alignItems:'center'},
   titleView:{flexDirection:'row',alignItems:'center',padding:15},
   titletext:{fontSize:20,color:'#000',paddingLeft:10,paddingRight:10},
   webviewstyle:{backgroundColor:'transparent',width:SCREEN_WIDTH-70,height:800,padding:5,flex:1,alignItems:'center'},
   groupButView:{flexDirection:'row',justifyContent:'space-between',height:60,bottom:0,width:SCREEN_WIDTH-40,borderTopWidth:1,borderColor:'#ccc'},
   cancelbut:{flex:1,justifyContent:'center',alignItems:'center'},
   agreeBut:{flex:1,justifyContent:'center',alignItems:'center',borderLeftWidth:1,borderColor:'#ccc',backgroundColor:'#ff5454',borderBottomRightRadius:10},
   textsize:{fontSize:18,color:'#000'},
})
