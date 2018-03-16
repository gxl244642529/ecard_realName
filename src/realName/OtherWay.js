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
  Platform,
} from '../../lib/Common';

import TitleBar from '../widget/TitleBar'
import {IC_CircleDot,IC_HollowCircle} from './component/Icons'
import LoadingButton from '../widget/LoadingButton'
import NfcUtil from '../../lib/NfcUtil'
import Notifier from '../../lib/Notifier'

import {
  radioButton,
  loadingButton,
  loadingButtonReal,
  loadingButtonRealDisabled,
  container
} from '../GlobalStyle'

class Option extends Component{
    constructor(props){
        super(props);
    }
    render(){
        return <View style={styles.item}>
            <TouchableOpacity style={styles.title} onPress={this.props.onPress}>
                {this.props.selected?<IC_CircleDot/>:<IC_HollowCircle/>}
                <View style={styles.mleft10}/>
                <Text>{this.props.text}</Text>
            </TouchableOpacity>
            {this.props.renderImg}

        </View>
    }
}

export default class OtherWay extends Component{
    constructor(props){
        super(props);
        let json = props.params.json;
        this.state={
            index:0,
            hasNfc:false,
            data:JSON.parse(json),

        }
    }
    componentDidMount(){
      if(Platform.OS=='android'){
        NfcUtil.isAvailable((result)=>{
          this.setState({hasNfc:result});
        });
      }
    }
    componentWillMount() {
      Notifier.addObserver("androidBack",this.onBack);

    }

    componentWillUnmount() {
        Notifier.removeObserver("androidBack",this.onBack);
    }
    onChangeIndex=(index)=>{
        this.setState({index});
    }
    onSuccess=(result)=>{
        if (result==true) {
          A.toast("下单成功");
          this._goPage();
        }else {
          A.alert("创建绑卡验证订单失败")
        }
    }
    _goPage=()=>{
      if(this.state.data.fromto==FROM_REAL){
        Api.returnTo('/');
      }else if (this.state.data.fromto==FROM_REAL_CARD) {
        Api.returnTo('/realName/rCard');
      }
    }
    _submit=()=>{
      // let data = {cardId:this.props.params.id}///
      let data = {cardId:this.state.data.cardId}///
      A.confirm("请在24小时内完成卟噔贴卡验证！",(index)=>{
        if(index==0){
          Api.api({
            api:"rcard/createBDOrder",
            crypt:0,
            data:data,
            waitingMessage:'正在创建订单，请稍等...',
            success:this.onSuccess,
          });
        }else{
          return;
        }
      });

    }
    onSubmit=()=>{

      if (this.state.index==0) {
        A.alert("请选择验证绑卡方式");
      }else if(this.state.index==1){
        //nfc验证
        // Api.push('/realName/nfcVertify/'+this.props.params.id);///
        let data = {cardId:this.state.data.cardId,fromto:this.state.data.fromto};
        Api.push('/realName/nfcVertify/'+JSON.stringify(data));
      }else if (this.state.index==2) {
        //补登下订单
        this._submit();
      }

    }
    isSelected=(index)=>{
        return index==this.state.index;
    }
    onBack=()=>{

      Api.returnTo("/realName/rCard");
      return true;
    }

    render(){
        return <View style={CommonStyle.container}>
            <TitleBar title="绑卡方式选择" onBack={this.onBack}/>
            <View style={styles.topMsg}>
                <Text style={styles.topMsgText}>为确保实名的安全性，请您选择验证绑卡方式</Text>
            </View>
            <View style={styles.center}>
              {this.state.hasNfc&&  <Option text="NFC手机贴卡验证" renderImg={<Image source={require('./images/vertify_nfc.png')} style={styles.img}/>}
                onPress={()=>this.onChangeIndex(1)}
                selected={this.isSelected(1)}
                />}

                <Option text="卟噔机贴卡验证  " renderImg={<Image source={require('./images/vertify_budeng.png')} style={styles.img2}/>}
                onPress={()=>this.onChangeIndex(2)}
                selected={this.isSelected(2)}
                />
            </View>
            <View style={styles.btn}>
            <LoadingButton
                loading={this.state.submiting}
                text="确认绑定"
                onPress={this.onSubmit}
                disabled={this.state.submiting}
                style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
                textStyle={loadingButton.loadingButtonText} />
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
    img:{
        width:87,
        height:100,
    },
    img2:{
        width:90,
        height:90,
    },
    btn:{
         marginTop:30
    },
    item:{
        marginTop:20,
        marginBottom:10,

        justifyContent:'center',
        alignItems:'center'
    },
    title:{
        flexDirection:'row',
        marginBottom:10
    },
    //功能样式
    row:{
        flexDirection:'row'
    },
    font14:{
        color:'#595757',
        fontSize:14,
    },
    mleft10:{
        marginLeft:10
    },
    center:{
        justifyContent:'center',
        alignItems:'center'
    }
});
