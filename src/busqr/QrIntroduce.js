
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
  Form
} from '../../lib/Common';
import Swiper from 'react-native-swiper';

import {
  TitleBar,
  Button,
  LoadingButton
} from '../Global'
import{
    pageButton,
    loadingButton,
    loadingButtonRealDisabled
}from '../GlobalStyle'
import {SCREEN_WIDTH,SCREEN_HEIGHT} from '../widget/Screen'



export default class  QrIntroduce extends Component{
	constructor(props) {
	    super(props);

	  }
    _gotoRequest=()=>{
      Api.replace("/busqr/qrRequest");
    }
    // <ScrollView
    //  contentContainerStyle={styles.contentContainer}
    //  bounces={false}
    //  pagingEnabled={true}
    //  horizontal={true}>
    //
    // <Image style={styles.backgroundImage} source={require("./images/guide_2.png")}/>
    //
    // <TouchableOpacity onPress={this._gotoRequest}>
    // <Image style={styles.backgroundImage} source={require("./images/guide_3.png")}/>
    // </TouchableOpacity>
    //
    // </ScrollView>

  renderImg=()=>{
   var imageViews=[];
  }
  _goMian=()=>{
    Api.returnTo("/");
  }


	render(){
			return (
        <Swiper   loop={false}  >
          <Image style={styles.backgroundImage} source={require("./images/guide_2.png")}/>
          <Image style={styles.backgroundImage} source={require("./images/guide_3.png")}>
            <View style={styles.btnBox}>
                <Button
                    text="返回首页"
                    style={[styles.button,styles.btn]}
                    onPress={this._goMian}
                    textStyle={styles.buttonText}
                />
                <Button
                     text="立即开通" style={[styles.button,styles.btn,styles.btngo]}
                     onPress={this._gotoRequest}
                     textStyle={pageButton.buttonText}
                />
            </View>

          </Image>
        </Swiper>

			);
	}
}


const styles = StyleSheet.create({
  contentContainer: {
       width: SCREEN_WIDTH*2,
       height: SCREEN_HEIGHT,
     },
     backgroundImage: {
       width: SCREEN_WIDTH,
       height: SCREEN_HEIGHT,
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
