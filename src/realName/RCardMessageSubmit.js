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
  Form
} from '../../lib/Common';


import {FormSelect,FormSwitch,FormItem} from '../../lib/TemplateForm'
import TitleBar from '../widget/TitleBar'
import Select from '../../lib/Select'
import ImagePicker from '../../lib/ImagePicker'
import RadioButtonGroup from '../widget/RadioButtonGroup'
import LoadingButton from '../widget/LoadingButton'
import CommonData from  './CommonData'
import FormInput from './StandardFormInput'
import StandardStyle from '../lib/StandardStyle'
// const SORT_VALUES = [1,2,3,4,6,7,8,9,10,11];
// const SORT_LABELS = ['身份证','户口簿','学生证','残疾证','烈属证','劳模证','低保证'
// ,'老干部证','军官证','士兵证','港澳台通行证'];
const MAP = {
  "00":'身份证',
  "01":'户口簿',
  "02":'学生证',
  "03":'残疾证',
  "04":'烈属证',
  "05":'劳模证',
  "06":'低保证',
  "07":'老干部证',
  "08":'军官证',
  "09":'士兵证',
  "10":'港澳台通行证'
}


import {
  radioButton,
  loadingButton,
  loadingButtonReal,
  loadingButtonRealDisabled,
  container
} from '../GlobalStyle'


// const picPlaceHolder = require('../images/s_ic_add_diy.png');
const picPlaceHolder = require('./images/card_photo.png');

const picBg =require('../images/s_ic_add_diy.png');
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;
export default class RCardMessageSubmit extends Component{

  constructor(props) {
    super(props);
    let json=props.params.json;
    this.state={
      data:JSON.parse(json),
    };
    this.form = new Form();
  }
  componentDidMount(){
    let data = {cardId:this.state.data.cardId};
    Api.api({
      api:"rcard/info",
      data:data,
      success:(result)=>{
        // console.log(result);
        console.log(result);
        this.setState({type:result.idType});
      }
    });
  }

  onSuccess=(result)=>{
    A.alert("实名绑卡成功！");
    // Api.replace("/realName/rCardList");
    Api.replace("/realName/rCard");
    console.log(result);
  }

  onSubmit=()=>{
    // let data = {cardId:this.props.params.id,nfc:false,};
    console.log(this.state.data.cardId+"  "+this.state.data.nfc);
    let data ={cardId:this.state.data.cardId,nfc:this.state.data.nfc};
    this.form.submit(this,{
      api:'rcard/submit',
      data:data,
      crypt:0,
      success:this.onSuccess
    });
  }
//   <View style={styles.main}>
//     <View style={styles.main_text}>
//         <Image source={require('./images/star.png')} style={styles.starStyle}/>
//         <Text style={[StandardStyle.h4,StandardStyle.fontGray]}>请上传e通卡卡号面照片</Text>
//     </View>
//
//     <ImagePicker
//       file={true}
//       disabled={this.state.submiting}
//       ref="cardImg"
//       placeholder="请上传e通卡卡面照片"
//       album={false}
//       onChange={(cardImg)=>{this.setState({cardImg})}}
//       style={{marginBottom:10}}
//       >
//       <Image
//
//         style={styles.cardImage}
//         source={this.state.cardImg ?  {uri:this.state.cardImg} : picPlaceHolder}
//         />
//     </ImagePicker>
// </View>

	render(){
		return (
			<View style={[CommonStyle.container,container.container]}>
		    <TitleBar title="实名绑卡填写信息" />
        <ScrollView>
        <FormInput
          label="卡号"
          value={this.state.data.cardId}
          editable={false}
          hasBottom={true} />
        <FormInput
          label="证件类型"
          value={MAP[this.state.type]}
          editable={false}
          hasBottom={true} />
        <FormInput
          ref="bindIdCard"
          label="证件号码"
          value={this.state.bindIdCard}
          placeholder="请输入原卡号对应的证件号码"
          onChange={(bindIdCard)=>{this.setState({bindIdCard})}}
          hasBottom={true}/>


        <View style={styles.butSty}>
          <LoadingButton
            loading={this.state.submiting}
            text="确认绑定"
            onPress={this.onSubmit}
            disabled={this.state.submiting}
            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
            textStyle={loadingButton.loadingButtonText} />
        </View>
       </ScrollView>
			</View>

		);
	}
}


const styles = StyleSheet.create({

  main:{
    padding:50,
    justifyContent:'center',
    alignItems:'center'
  },
  main_text:{
    flexDirection:"row",
    // justifyContent:'center',
    alignItems:'center',
    marginTop:5,
    marginBottom:20,
  },
  butSty:{
    marginTop:30
  },
  starStyle:{
    width:14,
    height:14,
    marginRight:5,
  },
  cardImage:{
    alignSelf:'center',
    width:180,
    height:122,
    marginRight:5,
    marginTop:5
  }
});
