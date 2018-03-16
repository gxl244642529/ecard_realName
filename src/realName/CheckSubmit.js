import React,{Component} from 'react'

import{
  View,
  Text,
  Image,
  StyleSheet,
  Api,
  A,
  CommonStyle,
  TextInput,
  DatePicker,
  TouchableOpacity,
  Form,
  Dimensions,
  ScrollView,
  PixelRatio,
} from '../../lib/Common'
import {FormInput,FormImage} from '../../lib/TemplateForm'
import FormSelect from './StandardFormSelect'
import FormItem from './StandardFormItem'
import DatePickerItem from './DatePickerItem'
import LoadingButton from '../widget/LoadingButton'
import ImagePicker from '../../lib/ImagePicker'
import {lineView} from './realNameUtils'
import   CryptoJS from 'crypto-js'
import {
	radioButton,
	loadingButton,
  loadingButtonReal,
  loadingButtonRealDisabled,
  container
} from '../GlobalStyle'
import {
  TitleBar,
} from '../Global'
import StandardStyle from '../lib/StandardStyle'
import {VerifyCode,checkPhone} from './VerifyCodeUtil'
const SORT_VALUES = [0,1];
const SORT_LABELS = ['身份证','户口薄'];
const DEFAULT = 0;
const picBg = require('./images/nopic.png');
const img1Default = require('./images/CharacterImage3.png');
const img2Default = require('./images/plus1.png');
var  CodeTime = 60;
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;

export default class CheckSubmit extends Component{
  constructor(props){
    super(props);
    this.form = new Form();
    let json=props.params.json;
    let now = new Date();


    this.state={
      // imgType:DEFAULT,
      data:JSON.parse(json),
      editable:true,
      validDate:null,
      // validDate:"2017-03-22",
      minDate:now.format("yyyy-MM-dd"),
    }

  }
  componentWillUnmount() {
    clearInterval(this.timer);//FIXME:无法实现卸载时清除计时器
   }
  componentDidMount(){
    if (this.state.data.idType==1) {
      this.setState({imgType:DEFAULT});
    }

    // Api.api({
    //   api:"real/bindPhone",
    //   success:(result)=>{
    //     console.log(result);
    //     if (result) {
    //       this.setState({phone:result,editable:false})
    //     }
    //   }
    // });
  }
  encCrypt=(str)=>{
    var img1 = CryptoJS.enc.Utf8.parse(str);
    var _base64 = CryptoJS.enc.Base64.stringify(img1);
    return _base64;
  }
  _submit=()=>{
    let self = this.state;
    console.log("正在提交");
    // if (!checkPhone(this.state.phone)) {
    //   A.alert("请输入正确的手机号码");
    //   return;
    // }
    // if (!self.code) {
    //   A.alert("请输入验证码");
    //   return;
    // }


    if(!this.state.validDate){
      A.alert("请选择证件有效期!");
      return;
    }

    if (this.state.validDate<this.state.minDate) {
      A.alert("您的证件已过有效期!");
      return;
    }

    if(!this.state.img1){
      A.alert("请上传证件照片");
      return;
    }

    if(!this.state.img2){
      A.alert("请上传证件照片");
      return;
    }

    const submit=()=>{
      if (self.data.idType===1 && !self.imgType) {
          this.setState({imgType:DEFAULT});
        }

        var _base64img1 = this.encCrypt(self.img1);
        var _base64img2 = this.encCrypt(self.img2);

        let data = {name:self.data.name,idType:self.data.idType
          ,idCard:self.data.idCard,validDate:self.validDate,img1:_base64img1,img2:_base64img2,imgType:self.imgType};

          A.confirm("请确保有效证件照片的真实性",(index)=>{
            if(index==0){
              // this.form.submit(this,{
              //   api:"real/submit",
              //   crypt:3,
              //   data:data,
              //   success:(result)=>{
              //     Api.replace('/realName/bankMessage/1');
              //   }
              // });
              // console.log(JSON.stringify(data));

              Api.push('/realName/bankMessage/'+JSON.stringify(data));
            }else{
              return;
            }
          });
    }

    let nowDate = new Date().format("yyyy-MM-dd");
    if(this.state.validDate == nowDate){
      A.confirm("确认您填写的有效期和身份证背面的有效期一致吗?",(index)=>{
          if(index==0){
            submit();
          }
      });
    }else{
      submit();
    }

  }

  _setVerify=(verifyId)=>{
    console.log(verifyId);
    this.setState({verifyId:verifyId});
  }
  /**
  <VerifyCode
    apiName="real/verify"
    phone={this.state.phone}
    phoneRef="phone"
    phoneValue={this.state.phone}
    phoneChange={(phone)=>{this.setState({phone})}}
    codeRef="code"
    codeValue={this.state.code}
    codeChange={(code)=>{this.setState({code})}}
    inputStyle={styles.inputStyle}
    verifyStyle={styles.verifyStyle}
    setVerify={this._setVerify}
    editable={this.state.editable}
  />**/


   render(){

    return <View style={[CommonStyle.container,container.container]}>

      <TitleBar title="信息提交"/>
      <ScrollView>
      <View style={styles.top}>
        <Image source={require('./images/img1.png')} style={styles.topImage} resizeMode="contain"/>
      </View>



      <DatePickerItem
        required={true}
        label="证件有效期至："
        placeholder="请选择证件有效期"
        ref="validDate"
        value={this.state.validDate}
        min={this.state.minDate}
        onChange={(validDate)=>{this.setState({validDate})}}
        hasBottom={true}
        hasHead={true}
        />

        {parseInt(this.state.data.idType)===1 &&
        <FormSelect
          required={true}
          label="证件类型:"
          placeholder={"请选择证件类型"}
          ref="imgType"
          value={this.state.imgType}
          values={SORT_VALUES}
          labels={SORT_LABELS}
          onChange={(index,imgType)=>{this.setState({imgType})}}
          hasBottom={true}
        />
        }

        <View style={styles.Photo}>
          <View style={styles.Photo_text}>
              <Image source={require('./images/star.png')} style={styles.starImage}/>

              {this.state.imgType===0 &&
              <Text style={[StandardStyle.h5,StandardStyle.fontBlack]}>请上传正面和反面身份证照片
                </Text>}
              {this.state.imgType===1 && <Text style={[StandardStyle.h5,StandardStyle.fontBlack]}>请上传户主和本人户口页照片
                </Text>}
              {parseInt(this.state.data.idType)!=1 && <Text style={[StandardStyle.h5,StandardStyle.fontBlack]}>请上传有效证件所对应的正反面照片
                </Text>}
          </View>
       </View>

        <View style={styles.Photo_img}>
          <ImagePicker
            ref="img1"
            disabled={this.state.submiting}
            placeholder="请上传证件照片"
            file={true}
            onChange={(img1)=>this.setState({img1})}
            album={false}
            style={{marginBottom:10}}>
            <Image

              style={styles.photo}
              source={this.state.img1 ?  {uri:this.state.img1} : img1Default}
              />
          </ImagePicker>
          <ImagePicker
            ref="img2"
            disabled={this.state.submiting}
            placeholder="请上传证件照片"
            file={true}
            onChange={(img2)=>this.setState({img2})}
            album={false}
            style={{marginBottom:10}}>
            <Image

              style={styles.photo}
              source={this.state.img2 ?  {uri:this.state.img2} : img2Default}
              />
          </ImagePicker>
        </View>
        <View style={styles.butStyle}>
          <LoadingButton
            loading={this.state.submiting}
            text="下一步"
            onPress={this._submit}
            disabled={this.state.submiting}
            style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
            textStyle={loadingButton.loadingButtonText} />
        </View>
    </ScrollView>
  </View>
  }
}
const styles=StyleSheet.create({
  topImage:{
     width:300,
     height:65,
  },
  top:{
   justifyContent:'center',
   alignItems:'center',
   marginTop:25,
   marginBottom:25,
 },
  item:{
    marginLeft:10,
    marginRight:10,
    marginTop:5,
    borderRadius:5,
  },
  code_item:{
    marginLeft:10,
    marginRight:10,
    marginTop:5,
    borderRadius:5,
    flex:0.6
  },
  verifyStyle:{
    //  position:"absolute",
    //  top:22,
    //  right:20,
     position:"absolute",
     right:8,
     padding:20,
   },
  butStyle:{
    // top:SCREEN_HEIGHT*0.3,
    marginTop:30,
  },
  viewStyle:{
    height:1/PixelRatio.get(),
    backgroundColor:'#d7d7d7',
    // flexDirection:'row'
  },
  photo:{
     alignSelf:'center',
     width:150,
     height:100
  },

Photo_text:{
    padding:15,
    flexDirection:"row",
},

Photo_img:{
    padding:10,
    flexDirection:"row",
    justifyContent:"space-around",
},
inputStyle:{
  marginLeft:5,
  height:60,
  width:SCREEN_WIDTH*0.6,
  fontSize:16,
},
starImage:{
  width:12,
  height:12,
  marginTop:3,
  marginRight:5,
},


})
