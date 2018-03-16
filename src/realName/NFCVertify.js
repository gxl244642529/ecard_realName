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
  Notifier
} from '../../lib/Common';

import TitleBar from '../widget/TitleBar'
import {IC_CircleDot,IC_HollowCircle} from './component/Icons'
import LoadingButton from '../widget/LoadingButton'
import {Animated,Easing} from 'react-native'

import NfcUtil from '../../lib/NfcUtil'

import {
  radioButton,
  loadingButton,
  loadingButtonReal,
  loadingButtonRealDisabled,
  container
} from '../GlobalStyle'
import {FROM_REAL,FROM_REAL_CARD} from './CommonData'

export default class NFCVertify extends Component{
    constructor(props){
        super(props);
       let json=props.params.json;
        this.state={
            data:JSON.parse(json),
            index:0,
            animat:true,
            animatedValue:new Animated.Value(0),
        };
        this._timer=null;
    }
    componentDidMount(){
       Notifier.addObserver("nfcTag",this.onNfc);
       this.willmount&&this.animate();
      //  this.onSubmit();
    }
    componentWillMount(){
      this.willmount = true;
    }
    onReadCard=(cardId)=>{
      let nfc = true;
      // this.setState({cardId,nfc});
      let data = {cardId:cardId};
      // let ycardId = this.props.params.id;///
      let ycardId = this.state.data.cardId;
      // console.log(ycardId+" "+this.state.data.fromto);
      if(ycardId!=cardId){

        A.alert("请贴原卡");
        return;
      }
      //提交
      Api.api({
        api:"rcard/nfcVerify",
        crypt:0,
        data:data,
        waitingMessage:'请稍等...',
        success:this.onSuccess,
      });
    }
    _goPage=()=>{
      if(this.state.data.fromto==FROM_REAL){
        Api.returnTo('/');
        return true;
      }else if (this.state.data.fromto==FROM_REAL_CARD) {
        Api.returnTo('/realName/rCard');
        return true;
      }
    }
    onSuccess=(result)=>{
        A.alert("开通挂失成功");
        // Api.returnTo("/realName/rCardList");
        this._goPage();

    }

    onNfc=()=>{
      console.log("onNfc");
      NfcUtil.readCard(this.onReadCard);
      return true;
    }

    componentWillUnmount() {
      Notifier.removeObserver("nfcTag",this.onNfc);
      this.willmount = false;
      // this._timer && clearInterval(this._timer);
    }
    animate () {
      this.state.animatedValue.setValue(0)
      Animated.timing(
        this.state.animatedValue,
        {
          toValue: 1,
          duration: 2000,
          easing: Easing.linear
        }
      ).start(() =>{this.willmount&&this.animate()} )
    }

    // countTime=()=>{
    //     this._timer=setInterval(()=>{
    //         this.setState({index:this.state.index+1});
    //     },400);
    // }
    onChangeIndex=(index)=>{
        this.setState({index});
    }
    // onSubmit=()=>{//测试按钮：修改动画状态
    //     // this.setState({animat:!this.state.animat})
    //     if(this.state.animat){
    //         this.countTime();
    //     }else{
    //         clearInterval(this._timer);
    //         this.setState({index:0})
    //     }
    // }
    isSelected=(index)=>{
        return index==this.state.index;
    }
    _renderImg(marginLeft){
        return     <View style={[styles.imgContainer,styles.center]}>
              <View style={{top:100,zIndex:-1000,}}>
                <Animated.View
                   style={{
                   marginLeft,
                  }} >
                  <Image source={require('./images/anicard.png')} style={{width:90,height:60,resizeMode:'contain',}} />
                 </Animated.View>
              </View>
              <Image source={require('./images/aniphone1.png')} style={styles.img}>
                 {this.state.index%3==1 && <Image source={require('./images/nfc_wave1.png')} style={styles.img_wave1}/>}
                 {this.state.index%3==2 && <Image source={require('./images/nfc_wave2.png')} style={styles.img_wave2}/>}
             </Image>

            </View>
    }
    _renderRight=()=>{
      return(
        <TouchableOpacity style={styles.lost} onPress={this.gotoOtherVertify}>
          <Text style={{color:'red'}}>其他方式></Text>
        </TouchableOpacity>
      );
    }
    gotoOtherVertify=()=>{
          // Api.push("/realName/questionVertify/"+this.props.params.id);///
          this.willmount = false;
          Notifier.removeObserver("nfcTag",this.onNfc);
          let data ={cardId:this.state.data.cardId,fromto:this.state.data.fromto};
          Api.push("/realName/questionVertify/"+JSON.stringify(data));
    }
    _onBack=()=>{
      let arr = Api.getRoutes();
      for(let i= arr.length-1; i >=0; --i){
        let vo = arr[i];
        if(vo.root){
          Api.returnTo('/');
          return true;
        }
        if(vo.location===undefined){
          break;
        }
        if(vo.location.pathname=='/realName/rCard'){
          Api.returnTo('/realName/rCard');
          return true;
        }
      }
    }
    render(){
      const marginLeft = this.state.animatedValue.interpolate({
       inputRange: [0, 1],
       outputRange: [300, 80]
     })
        return <View style={CommonStyle.container}>
            <TitleBar title="NFC贴卡验证"  renderRight={this._renderRight} onBack={this._onBack}/>
            <View style={styles.topMsg}>
                <Text style={styles.topMsgText}>请将e通卡贴在手机背后不要移动</Text>
            </View>
              {this._renderImg(marginLeft)}
            <View style={styles.textContainer}>
                <Text style={styles.font16}>温馨提示</Text>
                <Text style={styles.font14}>使用时必须开启手机NFC功能</Text>
                <Text style={styles.font14}>未完成时请勿中途退出</Text>
            </View>

        </View>
    }
}

const styles=StyleSheet.create({
    topMsg:{
        backgroundColor:'#ededee',
        paddingTop:25,
        paddingBottom:25,

        alignItems:'center',
        justifyContent:'center',
    },
    topMsgText:{
        color:'#595757',
        fontSize:14,
        lineHeight:21
    },
    imgContainer:{
        marginTop:20,
        marginBottom:40,

    },
    img:{
        width:150,
        height:172,
        resizeMode:'contain'
    },
    img_wave1:{
        width:9,
        height:9,
        position:'absolute',
        top:35,
        left:89,
        resizeMode:'contain'
    },
    img_wave2:{
        width:16,
        height:16,
        position:'absolute',
        top:28,
        left:89,
    },
    textContainer:{
        paddingLeft:20
    },
    btn:{
        marginTop:30
    },
    //功能样式
    center:{
        alignItems:'center',
        justifyContent:'center',
    },
    font14:{
        color:'#595757',
        fontSize:14,
        lineHeight:21
    },
    font16:{
        color:'#595757',
        fontSize:16,
        lineHeight:24
    },
    lost:{
      flexDirection:'row',
      height:45,
      width:80,
      justifyContent:'center',
      alignItems:'center'
    },
});
