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
import WebUtil from '../../lib/WebUtil'
import {
  radioButton,
  loadingButton,
  loadingButtonReal,
  loadingButtonRealDisabled,
  container
} from '../GlobalStyle'
import {FROM_REAL,FROM_REAL_CARD} from './CommonData'
import ConfirmAlert from './ConfirmAlert'
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

export default class LetterAgreeVerify extends Component{
    constructor(props){
        super(props);
        this.state={
            index:0,
            alertConfirm:true,
        }
    }
    componentDidMount(){
    }

    onChangeIndex=(index)=>{
        this.setState({index});
    }

    _letterVerify=()=>{
          this.setState({alertConfirm:true});
      //     let data = {cardId:this.props.id}
      // A.confirm("请确认您已认真阅读信用承诺书并同意",(index)=>{
      //   if(index==0){
      //     Api.api({
      //       api:"rcard/agreeVerify",
      //       data:data,
      //       waitingMessage:'请稍等...',
      //       success:(result)=>{
      //         A.alert("绑卡成功");
      //         // Api.returnTo("/realName/rCardList");
      //         Api.returnTo("/realName/rCard");
      //       }
      //     });
      //   }else{
      //     return;
      //   }
      // })

    }
    onSubmit=()=>{

      if (this.state.index==0) {
        A.alert("请选择验证绑卡方式");
      }else if(this.state.index==1){
        //信用承诺书
        this._letterVerify();

      }
    }
    isSelected=(index)=>{
        return index==this.state.index;
    }

    _goUrl=()=>{
        WebUtil.open(Api.imageUrl + '/index.php/sys_agree_detail/content/realLetter',"");
    }
    _noagree=()=>{
      this.setState({alertConfirm:false});
      Api.goBack();
    }
    _agree=()=>{
      let data = {cardId:this.props.params.id,isAgree:this.state.alertConfirm};
      Api.api({
        api:"rcard/agreeVerify",
        data:data,
        waitingMessage:'请稍等...',
        success:(result)=>{
          if(result){
            A.alert("开通挂失成功");
            Api.returnTo("/realName/rCard");
          }else {
            A.alert("开通挂失失败");
          }
        }
      });
    }
    // <View>
    // <View style={styles.topMsg}>
    //     <Text style={styles.topMsgText}>为确保实名的安全性，请您选择验证绑卡方式</Text>
    // </View>
    // <View style={styles.center}>
    //
    //   <Option text="真实性承诺函验证" renderImg={<Image source={require('./images/letter.png')} style={styles.img2}/>}
    //   onPress={()=>this.onChangeIndex(1)}
    //   selected={this.isSelected(1)}
    //   />
    //
    // </View>
    // <View style={styles.btn}>
    // <LoadingButton
    //     loading={this.state.submiting}
    //     text="确认绑定"
    //     onPress={this.onSubmit}
    //     disabled={this.state.submiting}
    //     style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
    //     textStyle={loadingButton.loadingButtonText} />
    // </View>
    // </View>
    render(){
        return <View style={CommonStyle.container}>
            <TitleBar title="真实性承诺函" />

            {this.state.alertConfirm&&<ConfirmAlert noagree={this._noagree} agree={this._agree}/>}
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
