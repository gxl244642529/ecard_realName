import React, { Component } from 'react';
import {
  View,
  ActivityIndicator,
  StyleSheet,
  CommonStyle,
  Account,
  Api,
  A,
  Text,
  Image,
  PixelRatio,
  Dimensions
} from '../../lib/Common'
// import {login} from '../lib/LoginUtil'
import Notifier from '../../lib/Notifier'
import {
  TitleBar,
} from '../Global'



export default class PCMessage extends Component{

  constructor(props){
    super(props);
    this.state={};
  }

  componentDidMount() {
    // Notifier.addObserver("verify",this._loadRecent);
    // this._loadRecent();
  }

  componentWillUnmount() {
    //  Notifier.removeObserver("verify",this._loadRecent);
  }



  _loadRecent=()=>{
    Api.api({
      api:"rcard/list",
      crypt:0,
      waitingMessage:"",
      success:(result)=>{
        this.setState({result});
      }
    });
  }
  msgItem(){

  }
  // <View style={[CommonStyle.container,styles.content]}>
  //   <ActivityIndicator />
  // </View>
  render(){
    let data = this.state;
      return (
        <View style={[CommonStyle.container,styles.content]}>
        <TitleBar title="我的消息中心"/>

          <View>
            <View style={styles.msgView}>
              <Image style={styles.msgImage} source={require('./images/app_y.png')}/>
              <View style={styles.msgViewText}>
                <View style={styles.textRound}>
                  <Text style={styles.typeText}>通知消息</Text>
                  <Text style={styles.msg}>您的实名信息已经审核通过</Text>
                </View>
                <Text style={styles.time}>2017-03-21</Text>
              </View>
            </View>

          </View>
        </View>

      );

    }
}

const styles = StyleSheet.create({
  // content:{justifyContent:'center',alignItems:'center'}
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
  msgViewText:{
    flexDirection:'row',
    justifyContent:'space-between'
  },
  textRound:{
    justifyContent:'space-around'
  },
  typeText:{
    fontSize:16,
    paddingTop:10
  },
  msg:{
    fontSize:16,
    color:'#595757',
    paddingBottom:10
  },
  time:{
    color:'#595757',
    paddingTop:15,
    // marginRight:10,
  },
  line:{
    height:1/PixelRatio.get(),
    backgroundColor:'#d7d7d7',
    marginLeft:70,
  }
});
